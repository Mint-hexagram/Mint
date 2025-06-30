package com.metro.inspection.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.metro.inspection.entity.SysRole;

public interface SysRoleService extends IService<SysRole> {
    // 根据条件分页查询角色
    IPage<SysRole> findByConditions(String roleName, String permission, String status, Page<SysRole> page);
} 