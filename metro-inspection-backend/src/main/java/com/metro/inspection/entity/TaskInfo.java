package com.metro.inspection.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

@TableName("task_info")
public class TaskInfo {
    @TableId(value = "task_id", type = IdType.AUTO)
    private Long taskId;

    @TableField("task_no")
    private String taskNo;

    @TableField("task_name")
    private String taskName;

    @TableField("task_type")
    private String taskType;

    @TableField("priority")
    private String priority;

    @TableField("description")
    private String description;

    @TableField("executor_id")
    private Long executorId;

    @TableField("assistant_id")
    private Long assistantId;

    @TableField("dept_id")
    private Long deptId;

    @TableField("plan_start_time")
    private LocalDateTime planStartTime;

    @TableField("plan_end_time")
    private LocalDateTime planEndTime;

    @TableField("actual_start_time")
    private LocalDateTime actualStartTime;

    @TableField("actual_end_time")
    private LocalDateTime actualEndTime;

    @TableField("line")
    private String line;

    @TableField("start_position")
    private String startPosition;

    @TableField("end_position")
    private String endPosition;

    @TableField("scope")
    private String scope;

    @TableField("status")
    private Integer status;

    @TableField("progress")
    private java.math.BigDecimal progress;

    @TableField("result")
    private String result;

    @TableField("problem_count")
    private Integer problemCount;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 无参构造函数 - MyBatis-Plus必需
    public TaskInfo() {
    }

    // getters and setters
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public String getTaskNo() { return taskNo; }
    public void setTaskNo(String taskNo) { this.taskNo = taskNo; }
    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }
    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getExecutorId() { return executorId; }
    public void setExecutorId(Long executorId) { this.executorId = executorId; }
    public Long getAssistantId() { return assistantId; }
    public void setAssistantId(Long assistantId) { this.assistantId = assistantId; }
    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }
    public LocalDateTime getPlanStartTime() { return planStartTime; }
    public void setPlanStartTime(LocalDateTime planStartTime) { this.planStartTime = planStartTime; }
    public LocalDateTime getPlanEndTime() { return planEndTime; }
    public void setPlanEndTime(LocalDateTime planEndTime) { this.planEndTime = planEndTime; }
    public LocalDateTime getActualStartTime() { return actualStartTime; }
    public void setActualStartTime(LocalDateTime actualStartTime) { this.actualStartTime = actualStartTime; }
    public LocalDateTime getActualEndTime() { return actualEndTime; }
    public void setActualEndTime(LocalDateTime actualEndTime) { this.actualEndTime = actualEndTime; }
    public String getLine() { return line; }
    public void setLine(String line) { this.line = line; }
    public String getStartPosition() { return startPosition; }
    public void setStartPosition(String startPosition) { this.startPosition = startPosition; }
    public String getEndPosition() { return endPosition; }
    public void setEndPosition(String endPosition) { this.endPosition = endPosition; }
    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public java.math.BigDecimal getProgress() { return progress; }
    public void setProgress(java.math.BigDecimal progress) { this.progress = progress; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public Integer getProblemCount() { return problemCount; }
    public void setProblemCount(Integer problemCount) { this.problemCount = problemCount; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
} 