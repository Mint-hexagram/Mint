package com.metro.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metro.inspection.entity.SysUserRole;
import com.metro.inspection.entity.SysRole;
import com.metro.inspection.mapper.SysUserRoleMapper;
import com.metro.inspection.service.SysUserRoleService;
import com.metro.inspection.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRoleService sysRoleService;

    @Override
    @Transactional
    public void assignRolesToUser(Long userId, List<Long> roleIds) {
        // 先删除用户现有角色
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        this.remove(wrapper);
        
        // 分配新角色
        if (roleIds != null && !roleIds.isEmpty()) {
            List<SysUserRole> userRoles = roleIds.stream().map(roleId -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                return userRole;
            }).collect(Collectors.toList());
            
            this.saveBatch(userRoles);
        }
    }

    @Override
    public List<Long> getUserRoleIds(Long userId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> userRoles = this.list(wrapper);
        
        return userRoles.stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getUserRoleNames(Long userId) {
        return sysUserRoleMapper.getUserRoleNames(userId);
    }

    @Override
    @Transactional
    public void assignDefaultRoleToUser(Long userId) {
        // 为注册用户分配默认角色（普通用户角色）
        try {
            // 查找user角色
            LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysRole::getRoleName, "user");
            SysRole userRole = sysRoleService.getOne(wrapper);
            
            if (userRole != null) {
                SysUserRole userRoleRelation = new SysUserRole();
                userRoleRelation.setUserId(userId);
                userRoleRelation.setRoleId(userRole.getRoleId());
                this.save(userRoleRelation);
            } else {
                // 如果user角色不存在，使用第一个可用角色
                List<SysRole> roles = sysRoleService.list();
                if (!roles.isEmpty()) {
                    SysUserRole userRoleRelation = new SysUserRole();
                    userRoleRelation.setUserId(userId);
                    userRoleRelation.setRoleId(roles.get(0).getRoleId());
                    this.save(userRoleRelation);
                }
            }
        } catch (Exception e) {
            // 如果分配角色失败，记录日志但不影响用户注册
            System.err.println("为用户分配默认角色失败: " + e.getMessage());
        }
    }

    @Override
    public List<Long> getUserIdsByRoleId(Long roleId) {
        return this.lambdaQuery().eq(SysUserRole::getRoleId, roleId).list()
            .stream().map(SysUserRole::getUserId).toList();
    }

    @Override
    public void assignUsersToRole(Long roleId, List<Long> userIds) {
        // 先删除该角色下所有用户
        this.lambdaUpdate().eq(SysUserRole::getRoleId, roleId).remove();
        // 再批量插入
        if (userIds != null && !userIds.isEmpty()) {
            List<SysUserRole> list = userIds.stream().map(uid -> {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(uid);
                ur.setRoleId(roleId);
                return ur;
            }).toList();
            this.saveBatch(list);
        }
    }
} 