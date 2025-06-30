package com.metro.inspection.entity;

import com.baomidou.mybatisplus.annotation.*;

@TableName("sys_user_role")
public class SysUserRole {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @TableField("role_id")
    private Long roleId;

    // 无参构造函数 - MyBatis-Plus必需
    public SysUserRole() {
    }

    // getter/setter
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
} 