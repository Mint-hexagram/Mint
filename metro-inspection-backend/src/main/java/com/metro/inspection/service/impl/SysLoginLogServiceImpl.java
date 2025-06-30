package com.metro.inspection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metro.inspection.entity.SysLoginLog;
import com.metro.inspection.mapper.SysLoginLogMapper;
import com.metro.inspection.service.SysLoginLogService;
import org.springframework.stereotype.Service;

@Service
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog> implements SysLoginLogService {
    // 继承ServiceImpl后，基础的CRUD操作已经由MyBatis-Plus提供
    // 可以在这里添加自定义的业务方法
} 