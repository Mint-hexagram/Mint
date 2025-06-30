package com.metro.inspection.controller;

import com.metro.inspection.entity.SysOperLog;
import com.metro.inspection.service.SysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sys-oper-log")
public class SysOperLogController {
    @Autowired
    private SysOperLogService sysOperLogService;

    @GetMapping
    public List<SysOperLog> list() {
        return sysOperLogService.list();
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