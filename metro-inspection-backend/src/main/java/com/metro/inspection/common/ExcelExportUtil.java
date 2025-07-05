package com.metro.inspection.common;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.metro.inspection.entity.SysUser;
import java.io.OutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.metro.inspection.entity.SysRole;
import com.metro.inspection.entity.TaskInfo;
import com.metro.inspection.entity.DefectInfo;

public class ExcelExportUtil {
    public static void exportSysUserList(List<SysUser> userList, OutputStream os) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("用户信息");
        Row header = sheet.createRow(0);
        String[] headers = {"用户ID", "用户名", "昵称", "真实姓名", "密码", "手机号", "邮箱", "部门ID", "状态", "最后登录时间", "创建时间", "备注"};
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int rowIdx = 1;
        for (SysUser user : userList) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(user.getUserId() != null ? user.getUserId() : 0);
            row.createCell(1).setCellValue(user.getUsername() != null ? user.getUsername() : "");
            row.createCell(2).setCellValue(user.getNickname() != null ? user.getNickname() : "");
            row.createCell(3).setCellValue(user.getRealName() != null ? user.getRealName() : "");
            row.createCell(4).setCellValue(user.getPassword() != null ? user.getPassword() : "");
            row.createCell(5).setCellValue(user.getPhone() != null ? user.getPhone() : "");
            row.createCell(6).setCellValue(user.getEmail() != null ? user.getEmail() : "");
            row.createCell(7).setCellValue(user.getDeptId() != null ? user.getDeptId() : 0);
            row.createCell(8).setCellValue(user.getStatus() != null ? user.getStatus() : 0);
            row.createCell(9).setCellValue(user.getLastLoginTime() != null ? user.getLastLoginTime().format(dtf) : "");
            row.createCell(10).setCellValue(user.getCreateTime() != null ? user.getCreateTime().format(dtf) : "");
            row.createCell(11).setCellValue(user.getRemark() != null ? user.getRemark() : "");
        }
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        workbook.write(os);
        workbook.close();
    }

    public static void exportSysRoleList(List<SysRole> roleList, java.io.OutputStream os) throws java.io.IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("角色信息");
        Row header = sheet.createRow(0);
        String[] headers = {"角色ID", "角色名称", "角色编码", "描述", "权限字符", "数据范围", "状态", "创建时间"};
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }
        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int rowIdx = 1;
        for (SysRole role : roleList) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(role.getRoleId() != null ? role.getRoleId() : 0);
            row.createCell(1).setCellValue(role.getRoleName() != null ? role.getRoleName() : "");
            row.createCell(2).setCellValue(role.getRoleCode() != null ? role.getRoleCode() : "");
            row.createCell(3).setCellValue(role.getDescription() != null ? role.getDescription() : "");
            row.createCell(4).setCellValue(role.getPermission() != null ? role.getPermission() : "");
            row.createCell(5).setCellValue(role.getDataScope() != null ? role.getDataScope() : "");
            row.createCell(6).setCellValue(role.getStatus() != null ? role.getStatus() : 0);
            row.createCell(7).setCellValue(role.getCreateTime() != null ? role.getCreateTime().format(dtf) : "");
        }
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        workbook.write(os);
        workbook.close();
    }

    public static void exportTaskInfoList(List<TaskInfo> taskList, OutputStream os) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("任务信息");
        Row header = sheet.createRow(0);
        String[] headers = {"任务ID", "任务编号", "任务名称", "任务类型", "优先级", "描述", "执行人ID", "协助人ID", "部门ID", "计划开始时间", "计划结束时间", "实际开始时间", "实际结束时间", "线路", "起始位置", "终止位置", "范围", "距离(公里)", "状态", "进度", "结果", "问题数", "创建时间"};
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int rowIdx = 1;
        for (TaskInfo task : taskList) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(task.getTaskId() != null ? task.getTaskId() : 0);
            row.createCell(1).setCellValue(task.getTaskNo() != null ? task.getTaskNo() : "");
            row.createCell(2).setCellValue(task.getTaskName() != null ? task.getTaskName() : "");
            row.createCell(3).setCellValue(task.getTaskType() != null ? task.getTaskType() : "");
            row.createCell(4).setCellValue(task.getPriority() != null ? task.getPriority() : "");
            row.createCell(5).setCellValue(task.getDescription() != null ? task.getDescription() : "");
            row.createCell(6).setCellValue(task.getExecutorId() != null ? task.getExecutorId() : 0);
            row.createCell(7).setCellValue(task.getAssistantId() != null ? task.getAssistantId() : 0);
            row.createCell(8).setCellValue(task.getDeptId() != null ? task.getDeptId() : 0);
            row.createCell(9).setCellValue(task.getPlanStartTime() != null ? task.getPlanStartTime().format(dtf) : "");
            row.createCell(10).setCellValue(task.getPlanEndTime() != null ? task.getPlanEndTime().format(dtf) : "");
            row.createCell(11).setCellValue(task.getActualStartTime() != null ? task.getActualStartTime().format(dtf) : "");
            row.createCell(12).setCellValue(task.getActualEndTime() != null ? task.getActualEndTime().format(dtf) : "");
            row.createCell(13).setCellValue(task.getLine() != null ? task.getLine() : "");
            row.createCell(14).setCellValue(task.getStartPosition() != null ? task.getStartPosition() : "");
            row.createCell(15).setCellValue(task.getEndPosition() != null ? task.getEndPosition() : "");
            row.createCell(16).setCellValue(task.getScope() != null ? task.getScope() : "");
            row.createCell(17).setCellValue(task.getDistance() != null ? task.getDistance() : 0);
            row.createCell(18).setCellValue(task.getStatus() != null ? task.getStatus() : 0);
            row.createCell(19).setCellValue(task.getProgress() != null ? task.getProgress().doubleValue() : 0);
            row.createCell(20).setCellValue(task.getResult() != null ? task.getResult() : "");
            row.createCell(21).setCellValue(task.getProblemCount() != null ? task.getProblemCount() : 0);
            row.createCell(22).setCellValue(task.getCreateTime() != null ? task.getCreateTime().format(dtf) : "");
        }
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        workbook.write(os);
        workbook.close();
    }

    public static void exportDefectInfoList(List<DefectInfo> defectList, OutputStream os) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("缺陷信息");
        Row header = sheet.createRow(0);
        String[] headers = {"缺陷ID", "缺陷编号", "缺陷类型", "描述", "严重程度", "是否属实", "上报时间", "上报人ID", "任务ID", "发现方式", "缺陷位置", "状态", "确认人ID", "确认时间", "处理人ID", "处理开始时间", "处理结束时间", "处理结果", "图片URL", "备注"};
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int rowIdx = 1;
        for (DefectInfo defect : defectList) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(defect.getDefectId() != null ? defect.getDefectId() : 0);
            row.createCell(1).setCellValue(defect.getDefectNo() != null ? defect.getDefectNo() : "");
            row.createCell(2).setCellValue(defect.getDefectType() != null ? defect.getDefectType() : "");
            row.createCell(3).setCellValue(defect.getDescription() != null ? defect.getDescription() : "");
            row.createCell(4).setCellValue(defect.getSeverity() != null ? defect.getSeverity() : 0);
            row.createCell(5).setCellValue(defect.getIsReal() != null ? defect.getIsReal() : 0);
            row.createCell(6).setCellValue(defect.getFoundTime() != null ? defect.getFoundTime().format(dtf) : "");
            row.createCell(7).setCellValue(defect.getFoundBy() != null ? defect.getFoundBy() : 0);
            row.createCell(8).setCellValue(defect.getTaskId() != null ? defect.getTaskId() : 0);
            row.createCell(9).setCellValue(defect.getFoundMethod() != null ? defect.getFoundMethod() : "");
            row.createCell(10).setCellValue(defect.getPosition() != null ? defect.getPosition() : "");
            row.createCell(11).setCellValue(defect.getStatus() != null ? defect.getStatus() : 0);
            row.createCell(12).setCellValue(defect.getConfirmBy() != null ? defect.getConfirmBy() : 0);
            row.createCell(13).setCellValue(defect.getConfirmTime() != null ? defect.getConfirmTime().format(dtf) : "");
            row.createCell(14).setCellValue(defect.getHandleBy() != null ? defect.getHandleBy() : 0);
            row.createCell(15).setCellValue(defect.getHandleStartTime() != null ? defect.getHandleStartTime().format(dtf) : "");
            row.createCell(16).setCellValue(defect.getHandleEndTime() != null ? defect.getHandleEndTime().format(dtf) : "");
            row.createCell(17).setCellValue(defect.getHandleResult() != null ? defect.getHandleResult() : "");
            row.createCell(18).setCellValue(defect.getImageUrl() != null ? defect.getImageUrl() : "");
            row.createCell(19).setCellValue(defect.getRemark() != null ? defect.getRemark() : "");
        }
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        workbook.write(os);
        workbook.close();
    }
} 