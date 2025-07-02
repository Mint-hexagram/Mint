package com.metro.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metro.inspection.entity.SysUser;
import com.metro.inspection.mapper.SysUserMapper;
import com.metro.inspection.service.SysUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    // 继承ServiceImpl后，基础的CRUD操作已经由MyBatis-Plus提供
    // 可以在这里添加自定义的业务方法

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public IPage<SysUser> findByConditions(String username, String nickname, String phone, Long deptId, Integer status, Page<SysUser> page) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(username)) {
            queryWrapper.like(SysUser::getUsername, username);
        }
        if (StringUtils.hasText(nickname)) {
            queryWrapper.like(SysUser::getNickname, nickname);
        }
        if (StringUtils.hasText(phone)) {
            queryWrapper.like(SysUser::getPhone, phone);
        }
        if (deptId != null) {
            queryWrapper.eq(SysUser::getDeptId, deptId);
        }
        if (status != null) {
            queryWrapper.eq(SysUser::getStatus, status);
        }
        
        queryWrapper.orderByDesc(SysUser::getCreateTime);
        
        return this.page(page, queryWrapper);
    }

    @Override
    public SysUser getByUsername(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        return this.getOne(queryWrapper);
    }

    @Override
    public void resetPassword(Long userId) {
        resetPassword(userId, "123456");
    }

    @Override
    public void resetPassword(Long userId, String newPassword) {
        SysUser user = this.getById(userId);
        if (user == null) return;
        user.setPassword(passwordEncoder.encode(newPassword));
        this.updateById(user);
    }

    @Override
    public boolean save(SysUser user) {
        // 新增用户时处理密码
        if (!StringUtils.hasText(user.getPassword())) {
            user.setPassword(passwordEncoder.encode("123456"));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return super.save(user);
    }

    @Override
    public boolean updateById(SysUser user) {
        if (!StringUtils.hasText(user.getPassword())) {
            // 不更新密码，只更新其他字段
            UpdateWrapper<SysUser> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_id", user.getUserId())
                .set("username", user.getUsername())
                .set("nickname", user.getNickname())
                .set("dept_id", user.getDeptId())
                .set("phone", user.getPhone())
                .set("status", user.getStatus());
            // 可根据需要添加其他字段
            return this.update(null, updateWrapper);
        } else {
            // 更新密码
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return super.updateById(user);
        }
    }
} 