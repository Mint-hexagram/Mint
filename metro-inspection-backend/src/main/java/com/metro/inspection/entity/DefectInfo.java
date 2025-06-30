package com.metro.inspection.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("defect_info")
public class DefectInfo {
    @TableId(value = "defect_id", type = IdType.AUTO)
    private Long defectId;

    @TableField("defect_no")
    private String defectNo;

    @TableField("defect_type")
    private String defectType;

    @TableField("description")
    private String description;

    @TableField("severity")
    private Integer severity;

    @TableField("is_real")
    private Integer isReal;

    @TableField("found_time")
    private LocalDateTime foundTime;

    @TableField("found_by")
    private Long foundBy;

    @TableField("task_id")
    private Long taskId;

    @TableField("found_method")
    private String foundMethod;

    @TableField("position")
    private String position;

    @TableField("status")
    private Integer status;

    @TableField("confirm_by")
    private Long confirmBy;

    @TableField("confirm_time")
    private LocalDateTime confirmTime;

    @TableField("handle_by")
    private Long handleBy;

    @TableField("handle_start_time")
    private LocalDateTime handleStartTime;

    @TableField("handle_end_time")
    private LocalDateTime handleEndTime;

    @TableField("handle_result")
    private String handleResult;

    @TableField("image_url")
    private String imageUrl;

    @TableField("remark")
    private String remark;

    // 无参构造函数 - MyBatis-Plus必需
    public DefectInfo() {
    }

    // getters and setters
    public Long getDefectId() { return defectId; }
    public void setDefectId(Long defectId) { this.defectId = defectId; }
    public String getDefectNo() { return defectNo; }
    public void setDefectNo(String defectNo) { this.defectNo = defectNo; }
    public String getDefectType() { return defectType; }
    public void setDefectType(String defectType) { this.defectType = defectType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getSeverity() { return severity; }
    public void setSeverity(Integer severity) { this.severity = severity; }
    public Integer getIsReal() { return isReal; }
    public void setIsReal(Integer isReal) { this.isReal = isReal; }
    public LocalDateTime getFoundTime() { return foundTime; }
    public void setFoundTime(LocalDateTime foundTime) { this.foundTime = foundTime; }
    public Long getFoundBy() { return foundBy; }
    public void setFoundBy(Long foundBy) { this.foundBy = foundBy; }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public String getFoundMethod() { return foundMethod; }
    public void setFoundMethod(String foundMethod) { this.foundMethod = foundMethod; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getConfirmBy() { return confirmBy; }
    public void setConfirmBy(Long confirmBy) { this.confirmBy = confirmBy; }
    public LocalDateTime getConfirmTime() { return confirmTime; }
    public void setConfirmTime(LocalDateTime confirmTime) { this.confirmTime = confirmTime; }
    public Long getHandleBy() { return handleBy; }
    public void setHandleBy(Long handleBy) { this.handleBy = handleBy; }
    public LocalDateTime getHandleStartTime() { return handleStartTime; }
    public void setHandleStartTime(LocalDateTime handleStartTime) { this.handleStartTime = handleStartTime; }
    public LocalDateTime getHandleEndTime() { return handleEndTime; }
    public void setHandleEndTime(LocalDateTime handleEndTime) { this.handleEndTime = handleEndTime; }
    public String getHandleResult() { return handleResult; }
    public void setHandleResult(String handleResult) { this.handleResult = handleResult; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
} 