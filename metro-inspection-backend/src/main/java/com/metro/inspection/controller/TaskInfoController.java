package com.metro.inspection.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.metro.inspection.common.ApiResponse;
import com.metro.inspection.entity.TaskInfo;
import com.metro.inspection.service.TaskInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Validated
public class TaskInfoController {
    @Autowired
    private TaskInfoService taskInfoService;

    @GetMapping
    public ApiResponse<IPage<TaskInfo>> getTaskPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String taskNo,
            @RequestParam(required = false) String taskName,
            @RequestParam(required = false) String startPoint,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        Page<TaskInfo> page = new Page<>(current, size);
        IPage<TaskInfo> taskPage = taskInfoService.findByConditions(
            taskNo, taskName, startPoint, status, startDate, endDate, page);
        
        return ApiResponse.success(taskPage);
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
    @PreAuthorize("hasAnyAuthority('admin', 'operator')")
    public ApiResponse<TaskInfo> create(@Valid @RequestBody TaskInfo taskInfo) {
        taskInfoService.save(taskInfo);
        return ApiResponse.success(taskInfo);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin', 'operator')")
    public ApiResponse<TaskInfo> update(@PathVariable Long id, @Valid @RequestBody TaskInfo taskInfo) {
        taskInfo.setTaskId(id);
        taskInfoService.updateById(taskInfo);
        return ApiResponse.success(taskInfo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin', 'operator')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        taskInfoService.removeById(id);
        return ApiResponse.success(null);
    }
} 