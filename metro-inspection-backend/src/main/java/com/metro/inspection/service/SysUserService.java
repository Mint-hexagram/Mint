package com.metro.inspection.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.metro.inspection.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    
    Page<SysUser> findUserPage(Page<SysUser> page, SysUser user);
    
    // 根据用户名查找用户
    SysUser getByUsername(String username);
} 