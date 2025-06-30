package com.metro.inspection.dto;

public class DashboardSummaryDTO {
    private int todayInspectionCount;
    private int yesterdayInspectionCount;
    private double todayInspectionDistance;
    private double yesterdayInspectionDistance;
    private int defectCount;
    private int confirmedDefectCount;
    private int falseDefectCount;

    // getters and setters
    public int getTodayInspectionCount() { return todayInspectionCount; }
    public void setTodayInspectionCount(int todayInspectionCount) { this.todayInspectionCount = todayInspectionCount; }
    public int getYesterdayInspectionCount() { return yesterdayInspectionCount; }
    public void setYesterdayInspectionCount(int yesterdayInspectionCount) { this.yesterdayInspectionCount = yesterdayInspectionCount; }
    public double getTodayInspectionDistance() { return todayInspectionDistance; }
    public void setTodayInspectionDistance(double todayInspectionDistance) { this.todayInspectionDistance = todayInspectionDistance; }
    public double getYesterdayInspectionDistance() { return yesterdayInspectionDistance; }
    public void setYesterdayInspectionDistance(double yesterdayInspectionDistance) { this.yesterdayInspectionDistance = yesterdayInspectionDistance; }
    public int getDefectCount() { return defectCount; }
    public void setDefectCount(int defectCount) { this.defectCount = defectCount; }
    public int getConfirmedDefectCount() { return confirmedDefectCount; }
    public void setConfirmedDefectCount(int confirmedDefectCount) { this.confirmedDefectCount = confirmedDefectCount; }
    public int getFalseDefectCount() { return falseDefectCount; }
    public void setFalseDefectCount(int falseDefectCount) { this.falseDefectCount = falseDefectCount; }
} 