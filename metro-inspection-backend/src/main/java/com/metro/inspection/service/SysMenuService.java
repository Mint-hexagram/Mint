package com.metro.inspection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.metro.inspection.entity.SysMenu;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    // 获取菜单树
    List<SysMenu> getMenuTree(String menuName);
} 