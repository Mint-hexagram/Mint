package com.metro.inspection.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("defect_info")
public class DefectInfo {
    @TableId(value = "defect_id", type = IdType.AUTO)
    private Long defectId;

    @TableField("task_id")
    private Long taskId;

    @TableField("defect_type")
    private String defectType;

    @TableField("position")
    private String position;

    @TableField("image_url")
    private String imageUrl;

    @TableField("is_real")
    private Integer isReal;

    @TableField("severity")
    private Integer severity;

    @TableField("length")
    private java.math.BigDecimal length;

    @TableField("area")
    private java.math.BigDecimal area;

    @TableField("count")
    private Integer count;

    @TableField("rectify_method")
    private String rectifyMethod;

    @TableField("report_time")
    private java.time.LocalDateTime reportTime;

    @TableField("rectify_status")
    private Integer rectifyStatus;

    // 无参构造函数 - MyBatis-Plus必需
    public DefectInfo() {
    }

    // getters and setters
    public Long getDefectId() { return defectId; }
    public void setDefectId(Long defectId) { this.defectId = defectId; }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public String getDefectType() { return defectType; }
    public void setDefectType(String defectType) { this.defectType = defectType; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Integer getIsReal() { return isReal; }
    public void setIsReal(Integer isReal) { this.isReal = isReal; }
    public Integer getSeverity() { return severity; }
    public void setSeverity(Integer severity) { this.severity = severity; }
    public BigDecimal getLength() { return length; }
    public void setLength(BigDecimal length) { this.length = length; }
    public BigDecimal getArea() { return area; }
    public void setArea(BigDecimal area) { this.area = area; }
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    public String getRectifyMethod() { return rectifyMethod; }
    public void setRectifyMethod(String rectifyMethod) { this.rectifyMethod = rectifyMethod; }
    public LocalDateTime getReportTime() { return reportTime; }
    public void setReportTime(LocalDateTime reportTime) { this.reportTime = reportTime; }
    public Integer getRectifyStatus() { return rectifyStatus; }
    public void setRectifyStatus(Integer rectifyStatus) { this.rectifyStatus = rectifyStatus; }
} 