package com.metro.inspection.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;

@TableName("sys_role_menu")
public class SysRoleMenu {
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    @TableField("menu_id")
    private Long menuId;

    // 无参构造函数 - MyBatis-Plus必需
    public SysRoleMenu() {
    }

    // getter/setter
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
    public Long getMenuId() { return menuId; }
    public void setMenuId(Long menuId) { this.menuId = menuId; }
} 