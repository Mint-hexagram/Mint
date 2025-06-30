package com.metro.inspection.controller;

import com.metro.inspection.common.ApiResponse;
import com.metro.inspection.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public ApiResponse<Object> getSummary() {
        return ApiResponse.success(dashboardService.getSummary());
    }

    @GetMapping("/task-stats")
    public ApiResponse<Object> getTaskStats() {
        return ApiResponse.success(dashboardService.getTaskStats());
    }

    @GetMapping("/defect-stats")
    public ApiResponse<Object> getDefectStats() {
        return ApiResponse.success(dashboardService.getDefectStats());
    }

    @GetMapping("/user-stats")
    public ApiResponse<Object> getUserStats() {
        return ApiResponse.success(dashboardService.getUserStats());
    }
} 