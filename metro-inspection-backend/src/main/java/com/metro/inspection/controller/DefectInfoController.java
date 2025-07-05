package com.metro.inspection.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.metro.inspection.common.ApiResponse;
import com.metro.inspection.entity.DefectInfo;
import com.metro.inspection.service.DefectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import jakarta.servlet.http.HttpServletResponse;
import com.metro.inspection.common.ExcelExportUtil;

import java.util.List;
import com.metro.inspection.entity.SysOperLog;
import com.metro.inspection.service.SysOperLogService;
import com.metro.inspection.common.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/defect-info")
public class DefectInfoController {
    @Autowired
    private DefectInfoService defectInfoService;
    @Autowired
    private SysOperLogService sysOperLogService;
    @Autowired
    private JwtUtils jwtUtils;

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
    public ApiResponse<DefectInfo> create(@RequestBody DefectInfo defect, HttpServletRequest request) {
        defectInfoService.save(defect);
        // 记录系统操作日志
        String token = request.getHeader("Authorization");
        Long userId = null;
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            userId = jwtUtils.getUserIdFromToken(token);
        }
        SysOperLog log = new SysOperLog();
        log.setUserId(userId != null ? userId.intValue() : null);
        log.setModule("缺陷管理");
        log.setOperType("新增");
        log.setOperDesc("新增缺陷：" + defect.getDefectType() + " (ID:" + defect.getDefectId() + ")");
        log.setRequestParam(defect.toString());
        log.setOperTime(LocalDateTime.now());
        log.setIp(request.getRemoteAddr());
        log.setDevice(request.getHeader("User-Agent"));
        sysOperLogService.save(log);
        return ApiResponse.success(defect);
    }

    @PutMapping
    public ApiResponse<DefectInfo> update(@RequestBody DefectInfo defect, HttpServletRequest request) {
        defectInfoService.updateById(defect);
        // 记录系统操作日志
        String token = request.getHeader("Authorization");
        Long userId = null;
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            userId = jwtUtils.getUserIdFromToken(token);
        }
        SysOperLog log = new SysOperLog();
        log.setUserId(userId != null ? userId.intValue() : null);
        log.setModule("缺陷管理");
        log.setOperType("编辑");
        log.setOperDesc("编辑缺陷：" + defect.getDefectType() + " (ID:" + defect.getDefectId() + ")");
        log.setRequestParam(defect.toString());
        log.setOperTime(LocalDateTime.now());
        log.setIp(request.getRemoteAddr());
        log.setDevice(request.getHeader("User-Agent"));
        sysOperLogService.save(log);
        return ApiResponse.success(defect);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        defectInfoService.removeById(id);
        // 记录系统操作日志
        String token = request.getHeader("Authorization");
        Long userId = null;
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            userId = jwtUtils.getUserIdFromToken(token);
        }
        SysOperLog log = new SysOperLog();
        log.setUserId(userId != null ? userId.intValue() : null);
        log.setModule("缺陷管理");
        log.setOperType("删除");
        log.setOperDesc("删除缺陷 (ID:" + id + ")");
        log.setRequestParam("defectId=" + id);
        log.setOperTime(LocalDateTime.now());
        log.setIp(request.getRemoteAddr());
        log.setDevice(request.getHeader("User-Agent"));
        sysOperLogService.save(log);
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
        defect.setStatus(2); // 2 表示已整改
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
            if (defect != null && defect.getStatus() != 2) {
                defect.setStatus(2);
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

    @PostMapping("/upload")
    public ApiResponse<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.error("上传文件为空");
        }
        try {
            String uploadDir = System.getProperty("user.dir") + "/static/upload/";
            Files.createDirectories(Paths.get(uploadDir));
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String ext = originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";
            String newFileName = UUID.randomUUID().toString().replace("-","") + ext;
            File dest = new File(uploadDir + newFileName);
            file.transferTo(dest);
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/static/upload/")
                    .path(newFileName)
                    .toUriString();
            return ApiResponse.success(fileUrl);
        } catch (IOException e) {
            return ApiResponse.error("图片上传失败: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    public void exportAllDefects(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=defects.xlsx");
            List<DefectInfo> defectList = defectInfoService.list();
            ExcelExportUtil.exportDefectInfoList(defectList, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            throw new RuntimeException("导出缺陷信息失败: " + e.getMessage(), e);
        }
    }
} 