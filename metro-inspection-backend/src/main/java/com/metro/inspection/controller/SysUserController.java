package com.metro.inspection.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.metro.inspection.common.ApiResponse;
import com.metro.inspection.entity.SysUser;
import com.metro.inspection.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sys-user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

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
        // TODO: 实现重置密码逻辑（未实现，待开发）
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/roles")
    public ApiResponse<Void> assignRoles(@PathVariable Long id, @RequestBody Map<String, List<Long>> body) {
        // TODO: 实现角色分配逻辑（未实现，待开发）
        return ApiResponse.success(null);
    }
} 