package com.metro.inspection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metro.inspection.entity.SysMenu;
import com.metro.inspection.mapper.SysMenuMapper;
import com.metro.inspection.service.SysMenuService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    // 继承ServiceImpl后，基础的CRUD操作已经由MyBatis-Plus提供
    // 可以在这里添加自定义的业务方法

    @Override
    public List<SysMenu> getMenuTree(String menuName) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(menuName)) {
            queryWrapper.like(SysMenu::getMenuName, menuName);
        }
        
        queryWrapper.orderByAsc(SysMenu::getSort);
        
        List<SysMenu> allMenus = this.list(queryWrapper);
        
        // 构建树形结构
        return buildMenuTree(allMenus);
    }
    
    private List<SysMenu> buildMenuTree(List<SysMenu> allMenus) {
        // 按父ID分组
        Map<Long, List<SysMenu>> parentMap = allMenus.stream()
                .collect(Collectors.groupingBy(menu -> menu.getParentId() != null ? menu.getParentId() : 0L));
        
        // 获取根菜单（parentId为0或null的菜单）
        List<SysMenu> rootMenus = parentMap.get(0L);
        if (rootMenus == null) {
            return new ArrayList<>();
        }
        
        // 递归构建树形结构
        rootMenus.forEach(menu -> setChildren(menu, parentMap));
        
        return rootMenus;
    }
    
    private void setChildren(SysMenu parent, Map<Long, List<SysMenu>> parentMap) {
        List<SysMenu> children = parentMap.get(parent.getMenuId());
        if (children != null) {
            children.forEach(child -> setChildren(child, parentMap));
            // 这里需要为SysMenu添加children字段，暂时返回空列表
        }
    }
} 