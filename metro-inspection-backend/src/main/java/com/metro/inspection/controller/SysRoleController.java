package com.metro.inspection.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.metro.inspection.common.ApiResponse;
import com.metro.inspection.entity.SysRole;
import com.metro.inspection.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sys-role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/list")
    public ApiResponse<IPage<SysRole>> getList(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) String permission,
            @RequestParam(required = false) String status) {
        
        Page<SysRole> page = new Page<>(current, size);
        IPage<SysRole> rolePage = sysRoleService.findByConditions(roleName, permission, status, page);
        return ApiResponse.success(rolePage);
    }

    @GetMapping("/{id}")
    public ApiResponse<SysRole> get(@PathVariable Long id) {
        SysRole role = sysRoleService.getById(id);
        if (role == null) {
            return ApiResponse.error("角色不存在");
        }
        return ApiResponse.success(role);
    }

    @PostMapping
    public ApiResponse<SysRole> create(@RequestBody SysRole role) {
        sysRoleService.save(role);
        return ApiResponse.success(role);
    }

    @PutMapping
    public ApiResponse<SysRole> update(@RequestBody SysRole role) {
        sysRoleService.updateById(role);
        return ApiResponse.success(role);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        sysRoleService.removeById(id);
        return ApiResponse.success(null);
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String statusStr = body.get("status");
        SysRole role = new SysRole();
        role.setRoleId(id);
        role.setStatus(Integer.parseInt(statusStr));
        sysRoleService.updateById(role);
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/menus")
    public ApiResponse<Void> assignMenus(@PathVariable Long id, @RequestBody Map<String, List<Long>> body) {
        List<Long> menuIds = body.get("menuIds");
        // TODO: 实现菜单分配逻辑
        return ApiResponse.success(null);
    }
} 