package com.metro.inspection.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

@TableName("sys_oper_log")
public class SysOperLog {
    @TableId(value = "oper_id", type = IdType.AUTO)
    private Long operId;

    @TableField("user_id")
    private Integer userId;

    @TableField("module")
    private String module;

    @TableField("oper_type")
    private String operType;

    @TableField("oper_desc")
    private String operDesc;

    @TableField("request_param")
    private String requestParam;

    @TableField("oper_time")
    private LocalDateTime operTime;

    @TableField("ip")
    private String ip;

    @TableField("device")
    private String device;

    // getter/setter
    public Long getOperId() { return operId; }
    public void setOperId(Long operId) { this.operId = operId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getOperType() { return operType; }
    public void setOperType(String operType) { this.operType = operType; }
    public String getOperDesc() { return operDesc; }
    public void setOperDesc(String operDesc) { this.operDesc = operDesc; }
    public String getRequestParam() { return requestParam; }
    public void setRequestParam(String requestParam) { this.requestParam = requestParam; }
    public LocalDateTime getOperTime() { return operTime; }
    public void setOperTime(LocalDateTime operTime) { this.operTime = operTime; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public String getDevice() { return device; }
    public void setDevice(String device) { this.device = device; }
} 