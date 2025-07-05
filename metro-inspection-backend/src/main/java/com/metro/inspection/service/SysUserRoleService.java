package com.metro.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.metro.inspection.entity.SysUserRole;

import java.util.List;

public interface SysUserRoleService extends IService<SysUserRole> {
    
    /**
     * 为用户分配角色
     */
    void assignRolesToUser(Long userId, List<Long> roleIds);
    
    /**
     * 获取用户的角色ID列表
     */
    List<Long> getUserRoleIds(Long userId);
    
    /**
     * 获取用户的角色名称列表
     */
    List<String> getUserRoleNames(Long userId);
    
    /**
     * 为新注册用户分配默认角色
     */
    void assignDefaultRoleToUser(Long userId);

    /**
     * 获取角色下的用户ID列表
     */
    List<Long> getUserIdsByRoleId(Long roleId);

    /**
     * 为角色分配用户
     */
    void assignUsersToRole(Long roleId, List<Long> userIds);
} 