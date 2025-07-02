package com.metro.inspection.controller;

import com.metro.inspection.common.ApiResponse;
import com.metro.inspection.entity.SysMenu;
import com.metro.inspection.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("/tree")
    public ApiResponse<List<SysMenu>> getTree(@RequestParam(required = false) String menuName) {
        List<SysMenu> menuTree = sysMenuService.getMenuTree(menuName);
        return ApiResponse.success(menuTree);
    }

    @GetMapping("/{id}")
    public ApiResponse<SysMenu> get(@PathVariable Long id) {
        SysMenu menu = sysMenuService.getById(id);
        if (menu == null) {
            return ApiResponse.error("菜单不存在");
        }
        return ApiResponse.success(menu);
    }

    @PostMapping
    public ApiResponse<SysMenu> create(@RequestBody SysMenu menu) {
        sysMenuService.save(menu);
        return ApiResponse.success(menu);
    }

    @PutMapping
    public ApiResponse<SysMenu> update(@RequestBody SysMenu menu) {
        sysMenuService.updateById(menu);
        return ApiResponse.success(menu);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        sysMenuService.removeById(id);
        return ApiResponse.success(null);
    }
} 