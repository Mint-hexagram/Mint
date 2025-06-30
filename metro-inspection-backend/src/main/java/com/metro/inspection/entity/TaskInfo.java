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

    @TableField("start_point")
    private String startPoint;

    @TableField("distance")
    private Double distance;

    @TableField("creator_id")
    private Long creatorId;

    @TableField("executor_id")
    private Long executorId;

    @TableField("last_execute_time")
    private LocalDateTime lastExecuteTime;

    @TableField("finish_time")
    private LocalDateTime finishTime;

    @TableField("upload_time")
    private LocalDateTime uploadTime;

    @TableField("status")
    private Integer status;

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
    public String getStartPoint() { return startPoint; }
    public void setStartPoint(String startPoint) { this.startPoint = startPoint; }
    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }
    public Long getCreatorId() { return creatorId; }
    public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }
    public Long getExecutorId() { return executorId; }
    public void setExecutorId(Long executorId) { this.executorId = executorId; }
    public LocalDateTime getLastExecuteTime() { return lastExecuteTime; }
    public void setLastExecuteTime(LocalDateTime lastExecuteTime) { this.lastExecuteTime = lastExecuteTime; }
    public LocalDateTime getFinishTime() { return finishTime; }
    public void setFinishTime(LocalDateTime finishTime) { this.finishTime = finishTime; }
    public LocalDateTime getUploadTime() { return uploadTime; }
    public void setUploadTime(LocalDateTime uploadTime) { this.uploadTime = uploadTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
} 