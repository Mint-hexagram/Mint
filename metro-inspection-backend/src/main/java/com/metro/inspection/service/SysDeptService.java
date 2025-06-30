package com.metro.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.metro.inspection.entity.SysDept;

import java.util.List;

public interface SysDeptService extends IService<SysDept> {
    // 获取部门树
    List<SysDept> getDeptTree(String deptName);
} 