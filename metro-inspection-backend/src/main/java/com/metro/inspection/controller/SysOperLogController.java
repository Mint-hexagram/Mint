package com.metro.inspection.controller;

import com.metro.inspection.entity.SysOperLog;
import com.metro.inspection.service.SysOperLogService;
import com.metro.inspection.common.ApiResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sys-oper-log")
public class SysOperLogController {
    @Autowired
    private SysOperLogService sysOperLogService;

    @GetMapping
    public ApiResponse<IPage<SysOperLog>> list(@RequestParam(defaultValue = "1") int current,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(required = false) String startTime,
                                               @RequestParam(required = false) String endTime,
                                               @RequestParam(required = false) Integer userId) {
        Page<SysOperLog> page = new Page<>(current, size);
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SysOperLog> wrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        if (userId != null) {
            wrapper.eq("user_id", userId);
        }
        if (startTime != null && !startTime.isEmpty()) {
            wrapper.ge("oper_time", startTime);
        }
        if (endTime != null && !endTime.isEmpty()) {
            wrapper.le("oper_time", endTime);
        }
        wrapper.orderByDesc("oper_time");
        IPage<SysOperLog> result = sysOperLogService.page(page, wrapper);
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}")
    public SysOperLog get(@PathVariable Long id) {
        return sysOperLogService.getById(id);
    }

    @PostMapping
    public SysOperLog create(@RequestBody SysOperLog log) {
        sysOperLogService.save(log);
        return log;
    }

    @PutMapping
    public SysOperLog update(@RequestBody SysOperLog log) {
        sysOperLogService.updateById(log);
        return log;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        sysOperLogService.removeById(id);
    }
} 