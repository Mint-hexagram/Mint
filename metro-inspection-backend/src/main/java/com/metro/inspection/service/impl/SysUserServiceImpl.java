package com.metro.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metro.inspection.entity.SysUser;
import com.metro.inspection.mapper.SysUserMapper;
import com.metro.inspection.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    // 继承ServiceImpl后，基础的CRUD操作已经由MyBatis-Plus提供
    // 可以在这里添加自定义的业务方法

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean save(SysUser entity) {
        if (StringUtils.hasText(entity.getPassword())) {
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        }
        return super.save(entity);
    }

    @Override
    public SysUser getByUsername(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        return this.getOne(queryWrapper);
    }

    @Override
    public Page<SysUser> findUserPage(Page<SysUser> page, SysUser user) {
        return baseMapper.findUserPage(page, user);
    }
} 