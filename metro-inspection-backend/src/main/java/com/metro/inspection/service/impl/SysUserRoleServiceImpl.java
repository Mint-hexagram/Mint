package com.metro.inspection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metro.inspection.entity.SysUserRole;
import com.metro.inspection.mapper.SysUserRoleMapper;
import com.metro.inspection.service.SysUserRoleService;
import com.metro.inspection.service.SysRoleService;
import com.metro.inspection.entity.SysRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {
    // This class is intentionally left blank after rollback.

    @Autowired
    private SysRoleService sysRoleService;
    
    @Override
    public List<String> getUserRoleNames(Long userId) {
        return baseMapper.getUserRoleNames(userId);
    }

    @Override
    @Transactional
    public void assignDefaultRoleToUser(Long userId) {
        try {
            LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysRole::getRoleName, "user");
            SysRole userRole = sysRoleService.getOne(wrapper);
            
            if (userRole != null) {
                SysUserRole userRoleRelation = new SysUserRole();
                userRoleRelation.setUserId(userId);
                userRoleRelation.setRoleId(userRole.getRoleId());
                this.save(userRoleRelation);
            }
        } catch (Exception e) {
            System.err.println("为用户分配默认角色失败: " + e.getMessage());
        }
    }
} 