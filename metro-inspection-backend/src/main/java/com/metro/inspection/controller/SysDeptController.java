package com.metro.inspection.controller;

import com.metro.inspection.common.ApiResponse;
import com.metro.inspection.entity.SysDept;
import com.metro.inspection.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sys-dept")
public class SysDeptController {
    @Autowired
    private SysDeptService sysDeptService;

    @GetMapping("/tree")
    public ApiResponse<List<SysDept>> getTree(@RequestParam(required = false) String deptName) {
        List<SysDept> deptTree = sysDeptService.getDeptTree(deptName);
        return ApiResponse.success(deptTree);
    }

    @GetMapping("/{id}")
    public ApiResponse<SysDept> get(@PathVariable Long id) {
        SysDept dept = sysDeptService.getById(id);
        if (dept == null) {
            return ApiResponse.error("部门不存在");
        }
        return ApiResponse.success(dept);
    }

    @PostMapping
    public ApiResponse<SysDept> create(@RequestBody SysDept dept) {
        sysDeptService.save(dept);
        return ApiResponse.success(dept);
    }

    @PutMapping
    public ApiResponse<SysDept> update(@RequestBody SysDept dept) {
        sysDeptService.updateById(dept);
        return ApiResponse.success(dept);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        sysDeptService.removeById(id);
        return ApiResponse.success(null);
    }
} 