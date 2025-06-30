package com.metro.inspection.controller;

import com.metro.inspection.entity.SysServiceMonitor;
import com.metro.inspection.service.SysServiceMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sys-service-monitor")
public class SysServiceMonitorController {
    @Autowired
    private SysServiceMonitorService sysServiceMonitorService;

    @GetMapping
    public List<SysServiceMonitor> list() {
        return sysServiceMonitorService.list();
    }

    @GetMapping("/{id}")
    public SysServiceMonitor get(@PathVariable Long id) {
        return sysServiceMonitorService.getById(id);
    }

    @PostMapping
    public SysServiceMonitor create(@RequestBody SysServiceMonitor monitor) {
        sysServiceMonitorService.save(monitor);
        return monitor;
    }

    @PutMapping
    public SysServiceMonitor update(@RequestBody SysServiceMonitor monitor) {
        sysServiceMonitorService.updateById(monitor);
        return monitor;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        sysServiceMonitorService.removeById(id);
    }
} 