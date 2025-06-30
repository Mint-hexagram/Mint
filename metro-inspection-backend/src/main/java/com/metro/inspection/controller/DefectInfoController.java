package com.metro.inspection.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.metro.inspection.common.ApiResponse;
import com.metro.inspection.entity.DefectInfo;
import com.metro.inspection.service.DefectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/defect-info")
public class DefectInfoController {
    @Autowired
    private DefectInfoService defectInfoService;

    @GetMapping
    public ApiResponse<List<DefectInfo>> list() {
        List<DefectInfo> defects = defectInfoService.list();
        return ApiResponse.success(defects);
    }

    @GetMapping("/{id}")
    public ApiResponse<DefectInfo> get(@PathVariable Long id) {
        DefectInfo defect = defectInfoService.getById(id);
        if (defect == null) {
            return ApiResponse.error("缺陷不存在");
        }
        return ApiResponse.success(defect);
    }

    @PostMapping
    public ApiResponse<DefectInfo> create(@RequestBody DefectInfo defect) {
        defectInfoService.save(defect);
        return ApiResponse.success(defect);
    }

    @PutMapping
    public ApiResponse<DefectInfo> update(@RequestBody DefectInfo defect) {
        defectInfoService.updateById(defect);
        return ApiResponse.success(defect);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        defectInfoService.removeById(id);
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/confirm")
    public ApiResponse<Void> confirmDefect(@PathVariable Long id) {
        DefectInfo defect = defectInfoService.getById(id);
        if (defect == null) {
            return ApiResponse.error("缺陷不存在");
        }
        defect.setIsReal(1); // 1 表示属实
        defectInfoService.updateById(defect);
        return ApiResponse.success(null);
    }

    @PostMapping("/{id}/rectify")
    public ApiResponse<Void> markAsRectified(@PathVariable Long id) {
        DefectInfo defect = defectInfoService.getById(id);
        if (defect == null) {
            return ApiResponse.error("缺陷不存在");
        }
        defect.setRectifyStatus(2); // 2 表示已整改
        defectInfoService.updateById(defect);
        return ApiResponse.success(null);
    }

    @PostMapping("/batch-confirm")
    public ApiResponse<Void> batchConfirmDefects(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ApiResponse.error("未选择任何缺陷");
        }
        List<DefectInfo> defectsToUpdate = new java.util.ArrayList<>();
        for (Long id : ids) {
            DefectInfo defect = defectInfoService.getById(id);
            if (defect != null && defect.getIsReal() != 1) {
                defect.setIsReal(1);
                defectsToUpdate.add(defect);
            }
        }
        if (!defectsToUpdate.isEmpty()) {
            defectInfoService.updateBatchById(defectsToUpdate);
        }
        return ApiResponse.success(null);
    }

    @PostMapping("/batch-rectify")
    public ApiResponse<Void> batchMarkAsRectified(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ApiResponse.error("未选择任何缺陷");
        }
        List<DefectInfo> defectsToUpdate = new java.util.ArrayList<>();
        for (Long id : ids) {
            DefectInfo defect = defectInfoService.getById(id);
            if (defect != null && defect.getRectifyStatus() != 2) {
                defect.setRectifyStatus(2);
                defectsToUpdate.add(defect);
            }
        }
        if (!defectsToUpdate.isEmpty()) {
            defectInfoService.updateBatchById(defectsToUpdate);
        }
        return ApiResponse.success(null);
    }

    @GetMapping("/page")
    public ApiResponse<IPage<DefectInfo>> getDefectsByPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long taskId,
            @RequestParam(required = false) String defectType,
            @RequestParam(required = false) Integer rectifyStatus,
            @RequestParam(required = false) Boolean isReal,
            @RequestParam(required = false) Integer severity,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        Page<DefectInfo> page = new Page<>(current, size);
        IPage<DefectInfo> result = defectInfoService.findByConditions(
            taskId, defectType, rectifyStatus, isReal, severity, position, startDate, endDate, page
        );
        return ApiResponse.success(result);
    }
} 