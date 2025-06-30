package com.metro.inspection.controller;

import com.metro.inspection.entity.SysLoginLog;
import com.metro.inspection.service.SysLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sys-login-log")
public class SysLoginLogController {
    @Autowired
    private SysLoginLogService sysLoginLogService;

    @GetMapping
    public List<SysLoginLog> list() {
        return sysLoginLogService.list();
    }

    @GetMapping("/{id}")
    public SysLoginLog get(@PathVariable Long id) {
        return sysLoginLogService.getById(id);
    }

    @PostMapping
    public SysLoginLog create(@RequestBody SysLoginLog log) {
        sysLoginLogService.save(log);
        return log;
    }

    @PutMapping
    public SysLoginLog update(@RequestBody SysLoginLog log) {
        sysLoginLogService.updateById(log);
        return log;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        sysLoginLogService.removeById(id);
    }
} 