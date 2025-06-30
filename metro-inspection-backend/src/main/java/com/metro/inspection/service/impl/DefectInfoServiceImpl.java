package com.metro.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metro.inspection.entity.DefectInfo;
import com.metro.inspection.mapper.DefectInfoMapper;
import com.metro.inspection.service.DefectInfoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DefectInfoServiceImpl extends ServiceImpl<DefectInfoMapper, DefectInfo> implements DefectInfoService {

    @Override
    public IPage<DefectInfo> findByTaskId(Long taskId, IPage<DefectInfo> page) {
        QueryWrapper<DefectInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId);
        return this.page(page, queryWrapper);
    }

    @Override
    public IPage<DefectInfo> findByDefectType(String defectType, IPage<DefectInfo> page) {
        QueryWrapper<DefectInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("defect_type", defectType);
        return this.page(page, queryWrapper);
    }

    @Override
    public IPage<DefectInfo> findByRectifyStatus(Integer rectifyStatus, IPage<DefectInfo> page) {
        QueryWrapper<DefectInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rectify_status", rectifyStatus);
        return this.page(page, queryWrapper);
    }

    @Override
    public IPage<DefectInfo> findByConditions(
            Long taskId, 
            String defectType, 
            Integer rectifyStatus, 
            Boolean isReal, 
            Integer severity, 
            String position, 
            String startDate, 
            String endDate, 
            IPage<DefectInfo> page) {
        
        QueryWrapper<DefectInfo> queryWrapper = new QueryWrapper<>();
        
        // 任务ID条件
        if (taskId != null) {
            queryWrapper.eq("task_id", taskId);
        }
        
        // 缺陷类型条件
        if (defectType != null && !defectType.trim().isEmpty()) {
            queryWrapper.like("defect_type", defectType);
        }
        
        // 整改状态条件
        if (rectifyStatus != null) {
            queryWrapper.eq("rectify_status", rectifyStatus);
        }
        
        // 是否属实条件
        if (isReal != null) {
            queryWrapper.eq("is_real", isReal);
        }
        
        // 严重程度条件
        if (severity != null) {
            queryWrapper.eq("severity", severity);
        }
        
        // 缺陷位置条件
        if (position != null && !position.trim().isEmpty()) {
            queryWrapper.like("position", position);
        }
        
        // 日期范围条件
        if (startDate != null && !startDate.trim().isEmpty()) {
            LocalDateTime startDateTime = LocalDateTime.parse(startDate + " 00:00:00", 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            queryWrapper.ge("found_time", startDateTime);
        }
        
        if (endDate != null && !endDate.trim().isEmpty()) {
            LocalDateTime endDateTime = LocalDateTime.parse(endDate + " 23:59:59", 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            queryWrapper.le("found_time", endDateTime);
        }
        
        return this.page(page, queryWrapper);
    }
} 