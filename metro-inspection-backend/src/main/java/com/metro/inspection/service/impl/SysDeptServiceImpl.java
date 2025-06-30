package com.metro.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metro.inspection.entity.SysDept;
import com.metro.inspection.mapper.SysDeptMapper;
import com.metro.inspection.service.SysDeptService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
    // 继承ServiceImpl后，基础的CRUD操作已经由MyBatis-Plus提供
    // 可以在这里添加自定义的业务方法

    @Override
    public List<SysDept> getDeptTree(String deptName) {
        LambdaQueryWrapper<SysDept> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(deptName)) {
            queryWrapper.like(SysDept::getDeptName, deptName);
        }
        
        // 按部门ID排序，因为数据库设计中没有order_num字段
        queryWrapper.orderByAsc(SysDept::getDeptId);
        
        List<SysDept> allDepts = this.list(queryWrapper);
        
        // 构建树形结构
        return buildDeptTree(allDepts);
    }
    
    private List<SysDept> buildDeptTree(List<SysDept> allDepts) {
        // 按父ID分组
        Map<Long, List<SysDept>> parentMap = allDepts.stream()
                .collect(Collectors.groupingBy(dept -> dept.getParentId() != null ? dept.getParentId() : 0L));
        
        // 获取根部门（parentId为0或null的部门）
        List<SysDept> rootDepts = parentMap.get(0L);
        if (rootDepts == null) {
            return new ArrayList<>();
        }
        
        // 递归构建树形结构
        rootDepts.forEach(dept -> setChildren(dept, parentMap));
        
        return rootDepts;
    }
    
    private void setChildren(SysDept parent, Map<Long, List<SysDept>> parentMap) {
        List<SysDept> children = parentMap.get(parent.getDeptId());
        if (children != null) {
            children.forEach(child -> setChildren(child, parentMap));
            // 这里需要为SysDept添加children字段，暂时返回空列表
        }
    }
} 