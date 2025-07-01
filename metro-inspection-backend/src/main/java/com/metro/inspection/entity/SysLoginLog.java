package com.metro.inspection.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

@TableName("sys_login_log")
public class SysLoginLog {
    @TableId(value = "info_id", type = IdType.AUTO)
    private Long infoId;

    @TableField("user_name")
    private String userName;

    @TableField("ipaddr")
    private String ipaddr;

    @TableField("login_location")
    private String loginLocation;

    @TableField("browser")
    private String browser;

    @TableField("os")
    private String os;

    @TableField("status")
    private String status;

    @TableField("msg")
    private String msg;

    @TableField("login_time")
    private LocalDateTime loginTime;

    // 无参构造函数 - MyBatis-Plus必需
    public SysLoginLog() {
    }

    // getters and setters
    public Long getInfoId() { return infoId; }
    public void setInfoId(Long infoId) { this.infoId = infoId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getIpaddr() { return ipaddr; }
    public void setIpaddr(String ipaddr) { this.ipaddr = ipaddr; }
    public String getLoginLocation() { return loginLocation; }
    public void setLoginLocation(String loginLocation) { this.loginLocation = loginLocation; }
    public String getBrowser() { return browser; }
    public void setBrowser(String browser) { this.browser = browser; }
    public String getOs() { return os; }
    public void setOs(String os) { this.os = os; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public LocalDateTime getLoginTime() { return loginTime; }
    public void setLoginTime(LocalDateTime loginTime) { this.loginTime = loginTime; }
} 