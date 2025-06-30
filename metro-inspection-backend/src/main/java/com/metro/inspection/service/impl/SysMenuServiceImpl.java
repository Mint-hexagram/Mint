package com.metro.inspection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metro.inspection.entity.SysMenu;
import com.metro.inspection.mapper.SysMenuMapper;
import com.metro.inspection.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    // 继承ServiceImpl后，基础的CRUD操作已经由MyBatis-Plus提供
    // 可以在这里添加自定义的业务方法

    @Override
    public List<SysMenu> getMenuTree(String menuName) {
        // TODO: implement menu tree logic, currently returns all menus as a placeholder
        return baseMapper.selectList(null);
    }
} 