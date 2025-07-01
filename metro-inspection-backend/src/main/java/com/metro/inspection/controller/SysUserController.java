package com.metro.inspection.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.metro.inspection.common.ApiResponse;
import com.metro.inspection.entity.SysUser;
import com.metro.inspection.entity.SysUserRole;
import com.metro.inspection.entity.SysRole;
import com.metro.inspection.service.SysUserService;
import com.metro.inspection.service.SysUserRoleService;
import com.metro.inspection.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sys-user")
@Validated
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/list")
    public ApiResponse<IPage<SysUser>> getList(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) Integer status) {
        
        Page<SysUser> page = new Page<>(current, size);
        IPage<SysUser> userPage = sysUserService.findByConditions(username, nickname, phone, deptId, status, page);
        return ApiResponse.success(userPage);
    }

    @GetMapping("/{id}")
    public ApiResponse<SysUser> get(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        if (user == null) {
            return ApiResponse.error("用户不存在");
        }
        return ApiResponse.success(user);
    }

    @PostMapping
    public ApiResponse<SysUser> create(@RequestBody SysUser user) {
        sysUserService.save(user);
        return ApiResponse.success(user);
    }

    @PutMapping
    public ApiResponse<SysUser> update(@RequestBody SysUser user) {
        sysUserService.updateById(user);
        return ApiResponse.success(user);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        sysUserService.removeById(id);
        return ApiResponse.success(null);
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        Integer status = body.get("status");
        SysUser user = new SysUser();
        user.setUserId(id);
        user.setStatus(status);
        sysUserService.updateById(user);
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/reset-password")
    public ApiResponse<Void> resetPassword(@PathVariable Long id) {
        // TODO: 实现重置密码逻辑
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/roles")
    public ApiResponse<Void> assignRoles(@PathVariable Long id, @RequestBody Map<String, List<Long>> body) {
        List<Long> roleIds = body.get("roleIds");
        // TODO: 实现角色分配逻辑
        return ApiResponse.success(null);
    }

    @PostMapping("/init-roles")
    public ApiResponse<String> initRoles() {
        try {
            // 检查是否已存在角色
            List<SysRole> existingRoles = sysRoleService.list();
            if (!existingRoles.isEmpty()) {
                return ApiResponse.success("角色已存在，无需初始化");
            }

            // 创建默认角色
            SysRole adminRole = new SysRole();
            adminRole.setRoleName("admin");
            adminRole.setPermission("sys:*:*");
            adminRole.setRoleSort(1);
            adminRole.setStatus(1);
            adminRole.setCreateTime(LocalDateTime.now());
            adminRole.setRemark("超级管理员");
            sysRoleService.save(adminRole);

            SysRole userRole = new SysRole();
            userRole.setRoleName("user");
            userRole.setPermission("sys:user:view,sys:task:view,sys:defect:view");
            userRole.setRoleSort(2);
            userRole.setStatus(1);
            userRole.setCreateTime(LocalDateTime.now());
            userRole.setRemark("普通用户");
            sysRoleService.save(userRole);

            SysRole operatorRole = new SysRole();
            operatorRole.setRoleName("operator");
            operatorRole.setPermission("sys:user:view,sys:task:*,sys:defect:*");
            operatorRole.setRoleSort(3);
            operatorRole.setStatus(1);
            operatorRole.setCreateTime(LocalDateTime.now());
            operatorRole.setRemark("操作员");
            sysRoleService.save(operatorRole);

            return ApiResponse.success("角色初始化成功：admin(超级管理员)、user(普通用户)、operator(操作员)");
        } catch (Exception e) {
            return ApiResponse.error("角色初始化失败：" + e.getMessage());
        }
    }

    @GetMapping("/init-admin")
    public String initAdminRole() {
        // 1. 查找 admin 用户
        SysUser adminUser = sysUserService.getByUsername("admin");
        if (adminUser == null) {
            return "错误：未能找到用户名为 'admin' 的用户。";
        }

        // 2. 查找 admin 角色
        SysRole adminRole = sysRoleService.lambdaQuery().eq(SysRole::getPermission, "admin").one();
        if (adminRole == null) {
            return "错误：未能找到权限标识为 'admin' 的角色。";
        }

        // 3. 检查是否已存在关联
        boolean exists = sysUserRoleService.lambdaQuery()
                .eq(SysUserRole::getUserId, adminUser.getUserId())
                .eq(SysUserRole::getRoleId, adminRole.getRoleId())
                .exists();

        if (exists) {
            return "成功：'admin' 用户已经拥有 'admin' 角色，无需操作。";
        }

        // 4. 创建并保存关联
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(adminUser.getUserId());
        userRole.setRoleId(adminRole.getRoleId());
        sysUserRoleService.save(userRole);

        return "成功：已为 'admin' 用户分配 'admin' 角色。请重新登录以使权限生效。";
    }
} 