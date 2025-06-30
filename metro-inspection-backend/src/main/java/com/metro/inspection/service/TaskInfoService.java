package com.metro.inspection.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.metro.inspection.entity.TaskInfo;

public interface TaskInfoService extends IService<TaskInfo> {
    // 多条件搜索
    IPage<TaskInfo> findByConditions(
        String taskNo, 
        String taskName, 
        String startPoint, 
        Integer status, 
        String startDate, 
        String endDate, 
        IPage<TaskInfo> page
    );
} 