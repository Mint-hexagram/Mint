package com.metro.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metro.inspection.entity.TaskInfo;
import com.metro.inspection.mapper.TaskInfoMapper;
import com.metro.inspection.service.TaskInfoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TaskInfoServiceImpl extends ServiceImpl<TaskInfoMapper, TaskInfo> implements TaskInfoService {

    @Override
    public IPage<TaskInfo> findByConditions(
            String taskNo,
            String taskName,
            String taskType,
            String priority,
            Integer status,
            String startDate,
            String endDate,
            IPage<TaskInfo> page) {
        
        QueryWrapper<TaskInfo> queryWrapper = new QueryWrapper<>();
        
        // 任务编号模糊查询
        if (taskNo != null && !taskNo.trim().isEmpty()) {
            queryWrapper.like("task_no", taskNo);
        }
        
        // 任务名称模糊查询
        if (taskName != null && !taskName.trim().isEmpty()) {
            queryWrapper.like("task_name", taskName);
        }
        
        // 任务类型精确查询
        if (taskType != null && !taskType.trim().isEmpty()) {
            queryWrapper.eq("task_type", taskType);
        }

        // 优先级精确查询
        if (priority != null && !priority.trim().isEmpty()) {
            queryWrapper.eq("priority", priority);
        }
        
        // 状态精确查询
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        
        // 时间范围查询 (查询计划开始时间)
        if (startDate != null && !startDate.trim().isEmpty()) {
            LocalDateTime startDateTime = LocalDateTime.parse(startDate + " 00:00:00", 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            queryWrapper.ge("plan_start_time", startDateTime);
        }
        
        if (endDate != null && !endDate.trim().isEmpty()) {
            LocalDateTime endDateTime = LocalDateTime.parse(endDate + " 23:59:59", 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            queryWrapper.le("plan_start_time", endDateTime);
        }
        
        queryWrapper.orderByDesc("create_time");
        
        return this.page(page, queryWrapper);
    }
} 