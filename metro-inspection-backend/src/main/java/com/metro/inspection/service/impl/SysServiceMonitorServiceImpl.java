package com.metro.inspection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metro.inspection.entity.SysServiceMonitor;
import com.metro.inspection.mapper.SysServiceMonitorMapper;
import com.metro.inspection.service.SysServiceMonitorService;
import org.springframework.stereotype.Service;

@Service
public class SysServiceMonitorServiceImpl extends ServiceImpl<SysServiceMonitorMapper, SysServiceMonitor> implements SysServiceMonitorService {
    // 继承ServiceImpl后，基础的CRUD操作已经由MyBatis-Plus提供
    // 可以在这里添加自定义的业务方法
} 