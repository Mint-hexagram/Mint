package com.metro.inspection.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.metro.inspection.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    // 根据条件分页查询用户
    IPage<SysUser> findByConditions(String username, String nickname, String phone, Long deptId, Integer status, Page<SysUser> page);
    
    // 根据用户名查找用户
    SysUser getByUsername(String username);

    // 重置为默认密码
    void resetPassword(Long userId);
    // 重置为指定密码
    void resetPassword(Long userId, String newPassword);
} 