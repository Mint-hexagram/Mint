package com.metro.inspection.service.impl;

import com.metro.inspection.common.JwtUtils;
import com.metro.inspection.dto.LoginRequest;
import com.metro.inspection.dto.LoginResponse;
import com.metro.inspection.dto.RegisterRequest;
import com.metro.inspection.dto.ForgotPasswordRequest;
import com.metro.inspection.dto.ResetPasswordRequest;
import com.metro.inspection.entity.SysUser;
import com.metro.inspection.service.AuthService;
import com.metro.inspection.service.SysUserService;
import com.metro.inspection.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 根据用户名查找用户
        SysUser user = sysUserService.getByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查用户状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new RuntimeException("用户已被禁用");
        }

        // 验证密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 获取用户真实角色（从数据库查询）
        List<String> roles = sysUserRoleService.getUserRoleNames(user.getUserId());
        
        // 如果没有角色，分配默认角色
        if (roles.isEmpty()) {
            sysUserRoleService.assignDefaultRoleToUser(user.getUserId());
            roles = sysUserRoleService.getUserRoleNames(user.getUserId());
        }
        
        // 转换为Spring Security格式的角色名
        List<String> securityRoles = roles.stream()
                .map(roleName -> "ROLE_" + roleName.toUpperCase())
                .toList();

        // 生成JWT令牌
        String token = jwtUtils.generateToken(user.getUsername(), user.getUserId(), String.join(",", securityRoles));

        // 将令牌存储到Redis，设置过期时间
        redisTemplate.opsForValue().set("token:" + token, user.getUsername(), 24, TimeUnit.HOURS);

        return new LoginResponse(token, user.getUserId(), user.getUsername(), user.getNickname(), securityRoles, List.of());
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        SysUser existingUser = sysUserService.getByUsername(registerRequest.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建新用户
        SysUser user = new SysUser();
        user.setUsername(registerRequest.getUsername());
        user.setNickname(registerRequest.getNickname());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setPhone(registerRequest.getPhone());
        user.setStatus(1); // 正常状态
        user.setCreateTime(LocalDateTime.now());
        user.setRemark("用户注册");

        // 保存用户
        sysUserService.save(user);
        
        // 为新注册用户分配默认角色（普通用户角色）
        sysUserRoleService.assignDefaultRoleToUser(user.getUserId());
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        // 根据用户名查找用户
        SysUser user = sysUserService.getByUsername(forgotPasswordRequest.getUsername());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证手机号
        if (!forgotPasswordRequest.getPhone().equals(user.getPhone())) {
            throw new RuntimeException("手机号与用户名不匹配");
        }

        // 生成6位验证码
        String verificationCode = generateVerificationCode();
        
        // 将验证码存储到Redis，设置5分钟过期
        String redisKey = "reset_pwd:" + user.getUsername();
        redisTemplate.opsForValue().set(redisKey, verificationCode, 5, TimeUnit.MINUTES);

        // 这里应该调用短信服务发送验证码
        // 为了演示，我们直接打印验证码
        System.out.println("验证码: " + verificationCode);
        
        // TODO: 实际项目中应该调用短信服务
        // smsService.sendVerificationCode(user.getPhone(), verificationCode);
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        // 验证密码确认
        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())) {
            throw new RuntimeException("两次输入的密码不一致");
        }

        // 从Redis获取验证码
        String redisKey = "reset_pwd:*"; // 这里需要根据实际情况获取用户名
        String storedCode = redisTemplate.opsForValue().get(redisKey);
        
        if (storedCode == null) {
            throw new RuntimeException("验证码已过期");
        }

        if (!storedCode.equals(resetPasswordRequest.getVerificationCode())) {
            throw new RuntimeException("验证码错误");
        }

        // 更新密码
        // 这里需要根据实际情况获取用户信息
        // SysUser user = sysUserService.getByUsername(username);
        // user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        // sysUserService.updateById(user);

        // 删除Redis中的验证码
        redisTemplate.delete(redisKey);
    }

    @Override
    public void logout(String token) {
        // 从Redis中删除令牌
        redisTemplate.delete("token:" + token);
    }

    @Override
    public String refreshToken(String token) {
        // 验证原令牌
        if (!jwtUtils.validateToken(token)) {
            throw new RuntimeException("令牌无效");
        }

        // 检查令牌是否在Redis中存在
        String username = redisTemplate.opsForValue().get("token:" + token);
        if (username == null) {
            throw new RuntimeException("令牌已失效");
        }

        // 获取用户信息
        SysUser user = sysUserService.getByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 获取用户角色
        List<String> roles = sysUserRoleService.getUserRoleNames(user.getUserId());
        List<String> securityRoles = roles.stream()
                .map(roleName -> "ROLE_" + roleName.toUpperCase())
                .toList();

        // 生成新令牌
        String newToken = jwtUtils.generateToken(user.getUsername(), user.getUserId(), String.join(",", securityRoles));

        // 更新Redis中的令牌
        redisTemplate.delete("token:" + token);
        redisTemplate.opsForValue().set("token:" + newToken, user.getUsername(), 24, TimeUnit.HOURS);

        return newToken;
    }

    /**
     * 生成6位数字验证码
     */
    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}