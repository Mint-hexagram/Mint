package com.metro.inspection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metro.inspection.entity.SysOperLog;
import com.metro.inspection.mapper.SysOperLogMapper;
import com.metro.inspection.service.SysOperLogService;
import org.springframework.stereotype.Service;

@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLog> implements SysOperLogService {
    // 继承ServiceImpl后，基础的CRUD操作已经由MyBatis-Plus提供
    // 可以在这里添加自定义的业务方法
} 