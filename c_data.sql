-- 清空数据并重置自增ID，方便重新导入
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE `sys_dept`;
TRUNCATE TABLE `sys_user`;
TRUNCATE TABLE `sys_role`;
TRUNCATE TABLE `sys_user_role`;
TRUNCATE TABLE `sys_menu`;
TRUNCATE TABLE `sys_role_menu`;
TRUNCATE TABLE `task_info`;
TRUNCATE TABLE `defect_info`;
TRUNCATE TABLE `sys_login_log`;
TRUNCATE TABLE `sys_oper_log`;
TRUNCATE TABLE `sys_service_monitor`;
SET FOREIGN_key_checks = 1;

-- =================================================================
-- 1. 系统管理模块数据
-- =================================================================

-- 部门表（sys_dept）
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `dept_code`, `leader`, `phone`, `email`, `sort`, `status`, `create_time`) VALUES
(1, 0, '总部', 'HQ', '王总', '010-88888888', 'hq@metro.com', 1, 1, '2024-01-01 00:00:00'),
(2, 1, '技术部', 'TECH', '李工', '010-66666666', 'tech@metro.com', 2, 1, '2024-01-01 01:00:00'),
(3, 1, '运维部', 'OPS', '张队', '010-77777777', 'ops@metro.com', 3, 1, '2024-01-01 02:00:00'),
(4, 3, '运维一组', 'OPS-G1', '刘组长', '010-77777771', 'ops-g1@metro.com', 1, 1, '2024-01-02 09:00:00'),
(5, 3, '运维二组', 'OPS-G2', '赵组长', '010-77777772', 'ops-g2@metro.com', 2, 0, '2024-01-02 10:00:00');

-- 角色表（sys_role）
INSERT INTO `sys_role` (`role_id`, `role_name`, `role_code`, `description`, `permission`, `data_scope`, `status`, `create_time`) VALUES
(1, '超级管理员', 'admin', '拥有系统所有权限', 'admin', 'all', 1, '2024-01-01 00:00:00'),
(2, '运维人员', 'operator', '负责任务执行和缺陷处理', 'ops', 'dept', 1, '2024-01-10 10:00:00'),
(3, '普通用户', 'user', '仅有数据查看权限', 'user', 'self', 1, '2024-01-15 14:00:00'),
(4, '部门主管', 'manager', '拥有本部门管理权限', 'manager', 'dept_and_below', 1, '2024-01-16 15:00:00');

-- 用户表（sys_user）-- 密码均为BCrypt加密后的示例值，例如'aaaaaa', 'bbbbbb'等
INSERT INTO `sys_user` (`user_id`, `username`, `nickname`, `real_name`, `password`, `email`, `phone`, `dept_id`, `status`, `last_login_time`, `create_time`, `remark`) VALUES
(1, 'admin', '超级管理员', 'Admin', '$2a$10$N.R4b/1iPz8mcFRs39f4X.p0W.N4.kC.sB.o2rX/E5F.e/5D5w8mC', 'admin@metro.com', '15888888888', 1, 1, '2024-06-28 08:00:00', '2024-01-01 00:00:00', '系统初始化管理员'),
(2, 'zhangsan', '张三', '张三', '$2a$10$d6jO.EBMdO.qD3.A9M5/8.iGzO.vY9P5R.l3L.rT9L5H3s5z5o5gG', 'zhangsan@metro.com', '13666666666', 4, 1, '2024-06-28 08:30:00', '2024-01-10 10:00:00', '运维部一组员工'),
(3, 'lisi', '李四', '李四', '$2a$10$T8Z.U.T8P.y5H.p1z.v7z.u.e.r6L.s.t.o9p.q.r.e.y.u.i', 'lisi@metro.com', '13777777777', 2, 0, '2024-06-20 09:00:00', '2024-01-15 14:00:00', '技术部员工（已停用）'),
(4, 'wangwu', '王五', '王五', '$2a$10$N.R4b/1iPz8mcFRs39f4X.p0W.N4.kC.sB.o2rX/E5F.e/5D5w8mC', 'wangwu@metro.com', '13999999999', 4, 1, '2024-06-27 10:00:00', '2024-02-10 11:00:00', '运维部一组新员工'),
(5, 'zhaoliu', '赵六', '赵六', '$2a$10$d6jO.EBMdO.qD3.A9M5/8.iGzO.vY9P5R.l3L.rT9L5H3s5z5o5gG', 'zhaoliu@metro.com', '13555555555', 3, 1, NULL, '2024-03-05 14:30:00', '运维部主管');

-- 用户-角色关联表（sys_user_role）
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1), -- admin → 超级管理员
(2, 2), -- zhangsan → 运维人员
(3, 3), -- lisi → 普通用户 (停用)
(4, 2), -- wangwu → 运维人员
(4, 3), -- wangwu → 同时也是普通用户
(5, 4); -- zhaoliu → 部门主管

-- 菜单表（sys_menu）-- 注意：parent_id基于本文件中的menu_id，如需修改需同步调整
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `type`, `path`, `component`, `permission`, `icon`, `sort`, `is_show`, `status`, `create_time`) VALUES
-- 目录
(1, 0, '数据大屏', 0, '/', 'DashboardView', 'dashboard:view', 'dashboard', 1, 1, 1, '2024-01-01 00:00:00'),
(2, 0, '任务管理', 0, '/task', 'Layout', 'task:view', 'task', 2, 1, 1, '2024-01-01 00:00:00'),
(3, 0, '缺陷管理', 0, '/defect', 'Layout', 'defect:view', 'defect', 3, 1, 1, '2024-01-01 00:00:00'),
(4, 0, '系统管理', 0, '/system', 'Layout', 'system:view', 'system', 98, 1, 1, '2024-01-01 00:00:00'),
(5, 0, '系统监控', 0, '/monitor', 'Layout', 'monitor:view', 'monitor', 99, 1, 1, '2024-01-01 00:00:00'),
-- 任务管理子菜单
(6, 2, '任务列表', 1, 'list', 'TaskView', 'task:list', 'list', 1, 1, 1, '2024-01-01 00:00:00'),
(7, 2, '新建任务', 2, '', '', 'task:create', 'add', 2, 0, 1, '2024-01-01 00:00:00'),
-- 缺陷管理子菜单
(8, 3, '缺陷列表', 1, 'list', 'DefectView', 'defect:list', 'list', 1, 1, 1, '2024-01-01 00:00:00'),
(9, 3, '缺陷上报', 2, '', '', 'defect:report', 'add', 2, 0, 1, '2024-01-01 00:00:00'),
-- 系统管理子菜单
(10, 4, '用户管理', 1, 'user', 'UserView', 'system:user:list', 'user', 1, 1, 1, '2024-01-01 00:00:00'),
(11, 4, '角色管理', 1, 'role', 'RoleView', 'system:role:list', 'role', 2, 1, 1, '2024-01-01 00:00:00'),
(12, 4, '菜单管理', 1, 'menu', 'MenuView', 'system:menu:list', 'menu', 3, 1, 1, '2024-01-01 00:00:00'),
(13, 4, '部门管理', 1, 'dept', 'DeptView', 'system:dept:list', 'dept', 4, 1, 1, '2024-01-01 00:00:00'),
(14, 10, '用户删除', 2, '', '', 'system:user:delete', 'delete', 3, 0, 1, '2024-01-01 00:00:00'),
-- 系统监控子菜单
(15, 5, '操作日志', 1, 'operlog', 'OperLogView', 'monitor:operlog:list', 'log', 1, 1, 1, '2024-01-01 00:00:00'),
(16, 5, '登录日志', 1, 'loginlog', 'LoginLogView', 'monitor:loginlog:list', 'log', 2, 1, 1, '2024-01-01 00:00:00'),
(17, 5, '服务状态', 1, 'status', 'ServiceMonitorView', 'monitor:service:list', 'monitor', 3, 1, 1, '2024-01-01 00:00:00');

-- 角色-菜单关联表（sys_role_menu）
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
-- 超级管理员（role_id=1）拥有所有权限
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11), (1, 12), (1, 13), (1, 14), (1, 15), (1, 16), (1, 17),
-- 运维人员（role_id=2）拥有任务和缺陷管理权限
(2, 2), (2, 3), (2, 6), (2, 7), (2, 8), (2, 9),
-- 普通用户（role_id=3）仅有查看权限
(3, 1), (3, 2), (3, 3), (3, 6), (3, 8),
-- 部门主管（role_id=4）拥有运维人员权限外加部门用户查看权限
(4, 2), (4, 3), (4, 4), (4, 6), (4, 7), (4, 8), (4, 9), (4, 10), (4, 13);

-- =================================================================
-- 2. 业务数据模块
-- =================================================================

-- 任务表（task_info）
INSERT INTO `task_info` (`task_no`, `task_name`, `task_type`, `priority`, `description`, `executor_id`, `assistant_id`, `dept_id`, `plan_start_time`, `plan_end_time`, `actual_start_time`, `actual_end_time`, `line`, `start_position`, `end_position`, `scope`, `status`, `progress`, `result`, `problem_count`, `create_time`) VALUES
('TASK-20240301-001', '1号线隧道巡检', '常规巡检', '高', '对1号线K10-K15段进行全面巡检', 2, 4, 4, '2024-03-01 08:00:00', '2024-03-01 12:00:00', '2024-03-01 08:05:00', '2024-03-01 11:30:00', '1号线', 'K10+000', 'K15+000', '全断面', 2, 100.00, '巡检完成，发现1处裂缝', 1, '2024-02-28 16:00:00'),
('TASK-20240302-001', '2号线设备检查', '专项检查', '中', '检查2号线通风设备', 2, NULL, 4, '2024-03-02 14:00:00', '2024-03-02 17:00:00', '2024-03-02 14:10:00', '2024-03-02 16:20:00', '2号线', 'A站', 'B站', '通风井', 2, 100.00, '设备正常，发现2处渗漏', 2, '2024-03-01 10:00:00'),
('TASK-20240303-001', '3号线日常巡视', '日常巡视', '低', '3号线例行巡视', 4, NULL, 4, '2024-03-03 08:30:00', '2024-03-03 12:30:00', '2024-03-03 08:30:00', '2024-03-03 12:00:00', '3号线', 'C站', 'D站', '轨道及两侧', 1, 80.00, '巡视中', 1, '2024-03-02 11:00:00'),
('TASK-20240304-001', '1号线隧道复检', '复检', '高', '对TASK-20240301-001发现的裂缝进行复检', 2, 4, 4, '2024-03-04 09:00:00', '2024-03-04 10:00:00', NULL, NULL, '1号线', 'K10+125', 'K10+126', '裂缝位置', 0, 0.00, '待执行', 0, '2024-03-03 15:00:00'),
('TASK-20240305-001', '全线路基检查', '专项检查', '高', '对所有线路的路基进行全面检查', 5, 2, 3, '2024-03-05 08:00:00', '2024-03-08 18:00:00', NULL, NULL, '全线', '各站', '各站', '路基结构', 3, 0.00, '已取消', 0, '2024-03-04 14:00:00');

-- 缺陷表（defect_info）
INSERT INTO `defect_info` (`defect_no`, `defect_type`, `description`, `severity`, `is_real`, `found_time`, `found_by`, `task_id`, `found_method`, `position`, `status`, `confirm_by`, `confirm_time`, `handle_by`, `handle_start_time`, `handle_end_time`, `handle_result`, `image_url`, `remark`) VALUES
('DEF-20240301-001', '结构裂缝', '侧壁出现横向裂缝，长约2.5米', 2, 1, '2024-03-01 10:30:00', 2, 1, '人工巡检', 'K10+125.6', 4, 5, '2024-03-01 14:00:00', 2, '2024-03-02 09:00:00', '2024-03-02 17:00:00', '已采用环氧树脂灌浆修复', '["/upload/defect/20240301_1.jpg"]', '修复效果良好'),
('DEF-20240302-001', '管线渗漏', '消防水管接头处持续滴水', 3, 1, '2024-03-02 15:45:00', 2, 2, '设备检测', 'A站通风井-67.2m', 3, 5, '2024-03-02 16:00:00', 4, '2024-03-03 10:00:00', NULL, '处理中', '["/upload/defect/20240302_1.jpg"]', '已上报维修班组'),
('DEF-20240303-001', '设备异常', '信号机灯光闪烁频率异常', 1, 0, '2024-03-03 09:15:00', 4, 3, '人工巡检', 'C站-234.8m', 5, 5, '2024-03-03 10:00:00', NULL, NULL, NULL, '确认为误报，设备正常', '["/upload/defect/20240303_1.jpg"]', '经确认为正常现象'),
('DEF-20240303-002', '轨道异物', '轨道上发现金属碎片', 2, 1, '2024-03-03 11:00:00', 4, 3, '人工巡检', 'D站-50.0m', 2, NULL, NULL, NULL, NULL, NULL, '待确认', '[]', '待主管确认'),
('DEF-20240302-002', '壁面腐蚀', '墙壁大面积腐蚀，有脱落风险', 3, 1, '2024-03-02 16:00:00', 2, 2, '人工巡检', 'B站-156.7m', 1, NULL, NULL, NULL, NULL, NULL, '待处理', '["/upload/defect/20240305_1.jpg"]', '已上报，等待处理方案');

-- =================================================================
-- 3. 日志与监控模块
-- =================================================================

-- 登录日志表（sys_login_log）
INSERT INTO `sys_login_log` (`user_id`, `login_time`, `logout_time`, `ip`, `device`, `status`, `error_msg`) VALUES
(1, '2024-06-28 08:00:00', '2024-06-28 12:00:00', '192.168.1.100', 'Chrome/120.0', 1, NULL),
(2, '2024-06-28 08:30:00', '2024-06-28 17:30:00', '192.168.1.101', 'Firefox/115.0', 1, NULL),
(3, '2024-06-28 09:00:00', NULL, '192.168.1.102', 'Safari/17.0', 0, '用户已停用'),
(1, '2024-06-27 10:00:00', NULL, '10.0.0.5', 'Mobile App/1.2', 0, '密码错误'),
(4, '2024-06-28 10:15:00', NULL, '192.168.1.103', 'Edge/118.0', 1, NULL);

-- 操作日志表（sys_oper_log）
INSERT INTO `sys_oper_log` (`user_id`, `module`, `oper_type`, `oper_desc`, `request_param`, `oper_time`, `ip`, `device`) VALUES
(1, '任务管理', '新增', '创建任务 TASK-20240301-001', '{"task_no":"TASK-20240301-001", "task_name":"1号线隧道巡检"}', '2024-02-28 16:00:00', '192.168.1.100', 'Chrome/120.0'),
(5, '缺陷管理', '修改', '确认缺陷 DEF-20240301-001', '{"defect_id":1, "status":4}', '2024-03-01 14:00:00', '192.168.1.101', 'Firefox/115.0'),
(1, '系统管理', '新增', '创建用户 wangwu', '{"username":"wangwu", "nickname":"王五"}', '2024-02-10 11:00:00', '192.168.1.100', 'Chrome/120.0'),
(1, '系统管理', '删除', '删除用户 lisi', '{"user_id":3}', '2024-06-29 10:00:00', '192.168.1.100', 'Chrome/120.0');

-- 服务监控表（sys_service_monitor）
INSERT INTO `sys_service_monitor` (`service_name`, `status`, `cpu_usage`, `memory_usage`, `record_time`) VALUES
('Backend-Master', 1, 20.5, 30.2, '2024-06-28 00:00:00'),
('Task-Scheduler', 1, 15.3, 25.6, '2024-06-28 00:00:00'),
('Data-Processor', 0, 0, 0, '2024-06-28 00:00:00'),
('Backend-Master', 1, 22.1, 32.5, '2024-06-28 01:00:00'),
('Task-Scheduler', 1, 16.2, 26.8, '2024-06-28 01:00:00'),
('Data-Processor', 1, 10.1, 15.3, '2024-06-28 01:00:00');