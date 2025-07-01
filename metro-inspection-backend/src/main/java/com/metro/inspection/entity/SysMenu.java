package com.metro.inspection.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@TableName("sys_menu")
public class SysMenu {
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;

    @TableField("parent_id")
    private Long parentId;

    @TableField("menu_name")
    private String menuName;

    @TableField("path")
    private String path;

    @TableField("component")
    private String component;

    @TableField("icon")
    private String icon;

    @TableField("type")
    private Integer type;

    @TableField("permission")
    private String permission;

    @TableField("sort")
    private Integer sort;

    @TableField("is_show")
    private Integer isShow;

    @TableField("status")
    private Integer status;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(exist = false)
    private List<SysMenu> children;

    // 无参构造函数 - MyBatis-Plus必需
    public SysMenu() {
    }

    // getter/setter
    public Long getMenuId() { return menuId; }
    public void setMenuId(Long menuId) { this.menuId = menuId; }
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getMenuName() { return menuName; }
    public void setMenuName(String menuName) { this.menuName = menuName; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    public String getComponent() { return component; }
    public void setComponent(String component) { this.component = component; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public String getPermission() { return permission; }
    public void setPermission(String permission) { this.permission = permission; }
    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }
    public Integer getIsShow() { return isShow; }
    public void setIsShow(Integer isShow) { this.isShow = isShow; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public List<SysMenu> getChildren() {
        return children;
    }
    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }
} 