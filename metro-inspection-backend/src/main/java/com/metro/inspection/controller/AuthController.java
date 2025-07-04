package com.metro.inspection.controller;

import com.metro.inspection.common.ApiResponse;
import com.metro.inspection.dto.LoginRequest;
import com.metro.inspection.dto.LoginResponse;
import com.metro.inspection.dto.RegisterRequest;
import com.metro.inspection.dto.ForgotPasswordRequest;
import com.metro.inspection.dto.ResetPasswordRequest;
import com.metro.inspection.entity.SysUser;
import com.metro.inspection.entity.SysOperLog;
import com.metro.inspection.service.AuthService;
import com.metro.inspection.service.SysUserService;
import com.metro.inspection.service.SysOperLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysOperLogService sysOperLogService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            LoginResponse response = authService.login(loginRequest);
            // 登录成功后写入操作日志
            if (response != null && response.getUserId() != null) {
                SysOperLog log = new SysOperLog();
                log.setUserId(response.getUserId().intValue());
                log.setModule("登录");
                log.setOperType("登录");
                log.setOperDesc("用户登录系统");
                log.setRequestParam("{\"username\":\"" + loginRequest.getUsername() + "\"}");
                log.setOperTime(java.time.LocalDateTime.now());
                log.setIp(request.getRemoteAddr());
                log.setDevice(request.getHeader("User-Agent"));
                sysOperLogService.save(log);
            }
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            authService.register(registerRequest);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        try {
            authService.forgotPassword(forgotPasswordRequest);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            authService.resetPassword(resetPasswordRequest);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader("Authorization") String token) {
        try {
            // 移除Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            authService.logout(token);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ApiResponse<String> refreshToken(@RequestHeader("Authorization") String token) {
        try {
            // 移除Bearer前缀
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            String newToken = authService.refreshToken(token);
            return ApiResponse.success(newToken);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/migrate-passwords")
    public ApiResponse<String> migratePasswords() {
        try {
            // 获取所有用户
            List<SysUser> users = sysUserService.list();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            
            int updatedCount = 0;
            for (SysUser user : users) {
                // 检查密码是否已经加密（BCrypt加密的密码以$2a$开头）
                if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
                    // 根据用户名设置对应的明文密码
                    String plainPassword = getPlainPasswordByUsername(user.getUsername());
                    if (plainPassword != null) {
                        // 加密密码
                        String encryptedPassword = passwordEncoder.encode(plainPassword);
                        user.setPassword(encryptedPassword);
                        sysUserService.updateById(user);
                        updatedCount++;
                        System.out.println("用户 " + user.getUsername() + " 密码已更新为加密版本");
                    }
                }
            }
            
            return ApiResponse.success("成功更新 " + updatedCount + " 个用户的密码为加密版本");
        } catch (Exception e) {
            return ApiResponse.error("更新密码失败：" + e.getMessage());
        }
    }
    
    /**
     * 根据用户名获取对应的明文密码
     */
    private String getPlainPasswordByUsername(String username) {
        switch (username) {
            case "admin":
                return "aaaaaa";
            case "zhangsan":
                return "bbbbbb";
            case "lisi":
                return "cccccc";
            default:
                return null;
        }
    }
} 