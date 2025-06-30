package com.metro.inspection.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@TableName("sys_service_monitor")
public class SysServiceMonitor {
    @TableId(value = "monitor_id", type = IdType.AUTO)
    private Long monitorId;

    @TableField("service_name")
    private String serviceName;

    @TableField("service_url")
    private String serviceUrl;

    @TableField("status")
    private Integer status;

    @TableField("response_time")
    private Long responseTime;

    @TableField("last_check_time")
    private LocalDateTime lastCheckTime;

    @TableField("error_count")
    private Integer errorCount;

    @TableField("remark")
    private String remark;

    @TableField("cpu_usage")
    private java.math.BigDecimal cpuUsage;

    @TableField("memory_usage")
    private java.math.BigDecimal memoryUsage;

    @TableField("record_time")
    private LocalDateTime recordTime;

    // 无参构造函数 - MyBatis-Plus必需
    public SysServiceMonitor() {
    }

    // getter/setter
    public Long getMonitorId() { return monitorId; }
    public void setMonitorId(Long monitorId) { this.monitorId = monitorId; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getServiceUrl() { return serviceUrl; }
    public void setServiceUrl(String serviceUrl) { this.serviceUrl = serviceUrl; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getResponseTime() { return responseTime; }
    public void setResponseTime(Long responseTime) { this.responseTime = responseTime; }
    public LocalDateTime getLastCheckTime() { return lastCheckTime; }
    public void setLastCheckTime(LocalDateTime lastCheckTime) { this.lastCheckTime = lastCheckTime; }
    public Integer getErrorCount() { return errorCount; }
    public void setErrorCount(Integer errorCount) { this.errorCount = errorCount; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public BigDecimal getCpuUsage() { return cpuUsage; }
    public void setCpuUsage(BigDecimal cpuUsage) { this.cpuUsage = cpuUsage; }
    public BigDecimal getMemoryUsage() { return memoryUsage; }
    public void setMemoryUsage(BigDecimal memoryUsage) { this.memoryUsage = memoryUsage; }
    public LocalDateTime getRecordTime() { return recordTime; }
    public void setRecordTime(LocalDateTime recordTime) { this.recordTime = recordTime; }
} 