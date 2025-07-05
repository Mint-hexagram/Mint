package com.metro.inspection.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.metro.inspection.common.ApiResponse;
import com.metro.inspection.entity.TaskInfo;
import com.metro.inspection.service.TaskInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.springframework.validation.annotation.Validated;
import jakarta.servlet.http.HttpServletResponse;
import com.metro.inspection.common.ExcelExportUtil;
import com.metro.inspection.entity.SysOperLog;
import com.metro.inspection.service.SysOperLogService;
import com.metro.inspection.common.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tasks")
@Validated
public class TaskInfoController {
    @Autowired
    private TaskInfoService taskInfoService;
    @Autowired
    private SysOperLogService sysOperLogService;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping
    public ApiResponse<Map<String, Object>> getTaskPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String taskNo,
            @RequestParam(required = false) String taskName,
            @RequestParam(required = false) String taskType,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        Page<TaskInfo> page = new Page<>(current, size);
        IPage<TaskInfo> taskPage = taskInfoService.findByConditions(
            taskNo, taskName, taskType, priority, status, startDate, endDate, page);
        
        Map<String, Object> result = new HashMap<>();
        result.put("records", taskPage.getRecords());
        result.put("total", taskPage.getTotal());
        return ApiResponse.success(result);
    }

    @GetMapping("/all")
    public ApiResponse<List<TaskInfo>> getAll() {
        List<TaskInfo> tasks = taskInfoService.list();
        return ApiResponse.success(tasks);
    }

    @GetMapping("/{id}")
    public ApiResponse<TaskInfo> getById(@PathVariable Long id) {
        TaskInfo task = taskInfoService.getById(id);
        if (task == null) {
            return ApiResponse.error("任务不存在");
        }
        return ApiResponse.success(task);
    }

    @PostMapping
    public ApiResponse<TaskInfo> create(@Valid @RequestBody TaskInfo taskInfo, HttpServletRequest request) {
        taskInfoService.save(taskInfo);
        // 记录系统操作日志
        String token = request.getHeader("Authorization");
        Long userId = null;
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            userId = jwtUtils.getUserIdFromToken(token);
        }
        SysOperLog log = new SysOperLog();
        log.setUserId(userId != null ? userId.intValue() : null);
        log.setModule("任务管理");
        log.setOperType("新增");
        log.setOperDesc("新增任务：" + taskInfo.getTaskName());
        log.setRequestParam(taskInfo.toString());
        log.setOperTime(LocalDateTime.now());
        log.setIp(request.getRemoteAddr());
        log.setDevice(request.getHeader("User-Agent"));
        sysOperLogService.save(log);
        return ApiResponse.success(taskInfo);
    }

    @PutMapping("/{id}")
    public ApiResponse<TaskInfo> update(@PathVariable Long id, @Valid @RequestBody TaskInfo taskInfo, HttpServletRequest request) {
        taskInfo.setTaskId(id);
        taskInfoService.updateById(taskInfo);
        // 记录系统操作日志
        String token = request.getHeader("Authorization");
        Long userId = null;
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            userId = jwtUtils.getUserIdFromToken(token);
        }
        SysOperLog log = new SysOperLog();
        log.setUserId(userId != null ? userId.intValue() : null);
        log.setModule("任务管理");
        log.setOperType("编辑");
        log.setOperDesc("编辑任务：" + taskInfo.getTaskName() + " (ID:" + id + ")");
        log.setRequestParam(taskInfo.toString());
        log.setOperTime(java.time.LocalDateTime.now());
        log.setIp(request.getRemoteAddr());
        log.setDevice(request.getHeader("User-Agent"));
        sysOperLogService.save(log);
        return ApiResponse.success(taskInfo);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        taskInfoService.removeById(id);
        // 记录系统操作日志
        String token = request.getHeader("Authorization");
        Long userId = null;
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            userId = jwtUtils.getUserIdFromToken(token);
        }
        SysOperLog log = new SysOperLog();
        log.setUserId(userId != null ? userId.intValue() : null);
        log.setModule("任务管理");
        log.setOperType("删除");
        log.setOperDesc("删除任务 (ID:" + id + ")");
        log.setRequestParam("taskId=" + id);
        log.setOperTime(java.time.LocalDateTime.now());
        log.setIp(request.getRemoteAddr());
        log.setDevice(request.getHeader("User-Agent"));
        sysOperLogService.save(log);
        return ApiResponse.success(null);
    }

    @GetMapping("/export")
    public void exportAllTasks(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=tasks.xlsx");
            List<TaskInfo> taskList = taskInfoService.list();
            ExcelExportUtil.exportTaskInfoList(taskList, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            throw new RuntimeException("导出任务信息失败: " + e.getMessage(), e);
        }
    }
} 