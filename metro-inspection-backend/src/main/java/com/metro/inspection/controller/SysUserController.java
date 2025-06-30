package com.metro.inspection.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.metro.inspection.common.ApiResponse;
import com.metro.inspection.entity.SysUser;
import com.metro.inspection.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sys-user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/list")
    public ApiResponse<Page<SysUser>> list(SysUser user, @RequestParam(defaultValue = "1") long current, @RequestParam(defaultValue = "10") long size) {
        return ApiResponse.success(sysUserService.findUserPage(new Page<>(current, size), user));
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
    public ApiResponse<Boolean> add(@RequestBody SysUser sysUser) {
        return ApiResponse.success(sysUserService.save(sysUser));
    }

    @PutMapping
    public ApiResponse<Boolean> update(@RequestBody SysUser user) {
        return ApiResponse.success(sysUserService.updateById(user));
    }

    @DeleteMapping("/{ids}")
    public ApiResponse<Boolean> delete(@PathVariable List<Long> ids) {
        return ApiResponse.success(sysUserService.removeByIds(ids));
    }
} 