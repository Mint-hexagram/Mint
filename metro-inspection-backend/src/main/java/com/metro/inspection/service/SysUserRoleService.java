package com.metro.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.metro.inspection.entity.SysUserRole;

import java.util.List;

public interface SysUserRoleService extends IService<SysUserRole> {
    List<String> getUserRoleNames(Long userId);

    void assignDefaultRoleToUser(Long userId);
} 