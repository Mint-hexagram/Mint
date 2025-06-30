package com.metro.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metro.inspection.entity.SysRole;
import com.metro.inspection.mapper.SysRoleMapper;
import com.metro.inspection.service.SysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    // 继承ServiceImpl后，基础的CRUD操作已经由MyBatis-Plus提供
    // 可以在这里添加自定义的业务方法

    @Override
    public IPage<SysRole> findByConditions(String roleName, String permission, String status, Page<SysRole> page) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(roleName)) {
            queryWrapper.like(SysRole::getRoleName, roleName);
        }
        if (StringUtils.hasText(permission)) {
            queryWrapper.like(SysRole::getPermission, permission);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq(SysRole::getStatus, Integer.parseInt(status));
        }
        
        queryWrapper.orderByAsc(SysRole::getSort);
        
        return this.page(page, queryWrapper);
    }
} 