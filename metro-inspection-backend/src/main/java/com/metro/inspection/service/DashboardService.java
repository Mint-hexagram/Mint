package com.metro.inspection.service;

import com.metro.inspection.dto.DashboardSummaryDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {
    public DashboardSummaryDTO getSummary() {
        // TODO: 实际应从数据库统计，这里先返回模拟数据
        DashboardSummaryDTO dto = new DashboardSummaryDTO();
        dto.setTodayInspectionCount(123);
        dto.setYesterdayInspectionCount(98);
        dto.setTodayInspectionDistance(1.2);
        dto.setYesterdayInspectionDistance(0.9);
        dto.setDefectCount(456);
        dto.setConfirmedDefectCount(321);
        dto.setFalseDefectCount(135);
        return dto;
    }

    public Map<String, Object> getTaskStats() {
        // TODO: 实际应从数据库统计，这里先返回模拟数据
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalTasks", 150);
        stats.put("completedTasks", 120);
        stats.put("pendingTasks", 30);
        stats.put("completionRate", 80.0);
        return stats;
    }

    public Map<String, Object> getDefectStats() {
        // TODO: 实际应从数据库统计，这里先返回模拟数据
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDefects", 456);
        stats.put("confirmedDefects", 321);
        stats.put("falseDefects", 135);
        stats.put("confirmationRate", 70.4);
        return stats;
    }

    public Map<String, Object> getUserStats() {
        // TODO: 实际应从数据库统计，这里先返回模拟数据
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", 50);
        stats.put("activeUsers", 45);
        stats.put("inactiveUsers", 5);
        stats.put("activeRate", 90.0);
        return stats;
    }
} 