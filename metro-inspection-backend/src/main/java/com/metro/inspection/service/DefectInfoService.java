package com.metro.inspection.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.metro.inspection.entity.DefectInfo;

public interface DefectInfoService extends IService<DefectInfo> {
    IPage<DefectInfo> findByTaskId(Long taskId, IPage<DefectInfo> page);
    IPage<DefectInfo> findByDefectType(String defectType, IPage<DefectInfo> page);
    IPage<DefectInfo> findByRectifyStatus(Integer rectifyStatus, IPage<DefectInfo> page);
    
    // 多条件搜索
    IPage<DefectInfo> findByConditions(
        Long taskId, 
        String defectType, 
        Integer rectifyStatus, 
        Boolean isReal, 
        Integer severity, 
        String position, 
        String startDate, 
        String endDate, 
        IPage<DefectInfo> page
    );
} 