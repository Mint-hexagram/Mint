-- 部门表
CREATE TABLE sys_dept (
  dept_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '部门ID',
  parent_id INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '父部门ID',
  dept_name VARCHAR(50) NOT NULL COMMENT '部门名称',
  dept_code VARCHAR(50) COMMENT '部门编码',
  leader VARCHAR(50) COMMENT '负责人',
  phone VARCHAR(20) COMMENT '联系电话',
  email VARCHAR(100) COMMENT '邮箱',
  sort INT COMMENT '显示顺序',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1正常，0停用',
  create_time DATETIME NOT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门管理表';

-- 用户表
CREATE TABLE sys_user (
  user_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
  username VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号',
  nickname VARCHAR(50) COMMENT '用户昵称',
  real_name VARCHAR(50) COMMENT '真实姓名',
  password VARCHAR(100) NOT NULL COMMENT '密码（BCrypt加密存储）',
  email VARCHAR(100) COMMENT '邮箱',
  phone VARCHAR(20) COMMENT '手机号',
  dept_id INT UNSIGNED COMMENT '所属部门ID',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1正常，0停用',
  last_login_time DATETIME COMMENT '最后登录时间',
  create_time DATETIME NOT NULL COMMENT '创建时间',
  remark VARCHAR(200) COMMENT '备注',
  FOREIGN KEY (dept_id) REFERENCES sys_dept(dept_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户管理表';

-- 角色表
CREATE TABLE sys_role (
  role_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
  role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
  role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
  description VARCHAR(200) COMMENT '角色描述',
  permission VARCHAR(100) COMMENT '权限字符',
  data_scope VARCHAR(50) COMMENT '数据权限范围',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1正常，0停用',
  create_time DATETIME NOT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色管理表';

-- 用户-角色关联表
CREATE TABLE sys_user_role (
  user_id INT UNSIGNED NOT NULL COMMENT '用户ID',
  role_id INT UNSIGNED NOT NULL COMMENT '角色ID',
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (role_id) REFERENCES sys_role(role_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 菜单表
CREATE TABLE sys_menu (
  menu_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '菜单ID',
  parent_id INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '父菜单ID',
  menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
  type TINYINT NOT NULL COMMENT '类型：0目录，1菜单，2按钮',
  path VARCHAR(200) COMMENT '路由路径',
  component VARCHAR(200) COMMENT '前端组件路径',
  permission VARCHAR(100) COMMENT '权限字符',
  icon VARCHAR(50) COMMENT '图标',
  sort INT COMMENT '显示顺序',
  is_show TINYINT DEFAULT 1 COMMENT '是否显示',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1正常，0停用',
  create_time DATETIME NOT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单管理表';

-- 角色-菜单关联表
CREATE TABLE sys_role_menu (
  role_id INT UNSIGNED NOT NULL COMMENT '角色ID',
  menu_id INT UNSIGNED NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (role_id, menu_id),
  FOREIGN KEY (role_id) REFERENCES sys_role(role_id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (menu_id) REFERENCES sys_menu(menu_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 任务表
CREATE TABLE task_info (
  task_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '任务ID',
  task_no VARCHAR(50) NOT NULL UNIQUE COMMENT '任务编号',
  task_name VARCHAR(50) NOT NULL COMMENT '任务名称',
  task_type VARCHAR(30) COMMENT '任务类型',
  priority VARCHAR(10) COMMENT '优先级',
  description VARCHAR(200) COMMENT '任务描述',
  executor_id INT UNSIGNED COMMENT '执行人ID',
  assistant_id INT UNSIGNED COMMENT '协助人ID',
  dept_id INT UNSIGNED COMMENT '所属部门ID',
  plan_start_time DATETIME COMMENT '计划开始时间',
  plan_end_time DATETIME COMMENT '计划结束时间',
  actual_start_time DATETIME COMMENT '实际开始时间',
  actual_end_time DATETIME COMMENT '实际结束时间',
  line VARCHAR(50) COMMENT '巡视线路',
  start_position VARCHAR(50) COMMENT '起始位置',
  end_position VARCHAR(50) COMMENT '结束位置',
  scope VARCHAR(100) COMMENT '巡视范围',
  status TINYINT COMMENT '任务状态',
  progress DECIMAL(5,2) COMMENT '完成度',
  result VARCHAR(200) COMMENT '执行结果',
  problem_count INT COMMENT '发现问题数',
  create_time DATETIME NOT NULL COMMENT '创建时间',
  FOREIGN KEY (executor_id) REFERENCES sys_user(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
  FOREIGN KEY (assistant_id) REFERENCES sys_user(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
  FOREIGN KEY (dept_id) REFERENCES sys_dept(dept_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务信息表';

-- 缺陷表
CREATE TABLE defect_info (
  defect_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '缺陷ID',
  defect_no VARCHAR(50) NOT NULL UNIQUE COMMENT '缺陷编号',
  defect_type VARCHAR(50) COMMENT '缺陷类型',
  description VARCHAR(200) COMMENT '缺陷描述',
  severity TINYINT COMMENT '严重程度',
  is_real TINYINT COMMENT '是否属实',
  found_time DATETIME COMMENT '发现时间',
  found_by INT UNSIGNED COMMENT '发现人ID',
  task_id BIGINT UNSIGNED COMMENT '关联任务ID',
  found_method VARCHAR(50) COMMENT '发现方式',
  position VARCHAR(50) COMMENT '位置',
  status TINYINT COMMENT '当前状态',
  confirm_by INT UNSIGNED COMMENT '确认人ID',
  confirm_time DATETIME COMMENT '确认时间',
  handle_by INT UNSIGNED COMMENT '处理人ID',
  handle_start_time DATETIME COMMENT '处理开始时间',
  handle_end_time DATETIME COMMENT '处理完成时间',
  handle_result VARCHAR(200) COMMENT '处理结果',
  image_url TEXT COMMENT '缺陷图片',
  remark VARCHAR(200) COMMENT '备注',
  FOREIGN KEY (found_by) REFERENCES sys_user(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
  FOREIGN KEY (confirm_by) REFERENCES sys_user(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
  FOREIGN KEY (handle_by) REFERENCES sys_user(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
  FOREIGN KEY (task_id) REFERENCES task_info(task_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缺陷信息表';

-- 操作日志表
CREATE TABLE sys_oper_log (
  oper_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
  user_id INT UNSIGNED COMMENT '操作用户ID',
  module VARCHAR(50) COMMENT '操作模块',
  oper_type VARCHAR(20) COMMENT '操作类型',
  oper_desc VARCHAR(200) COMMENT '操作描述',
  request_param TEXT COMMENT '请求参数',
  oper_time DATETIME NOT NULL COMMENT '操作时间',
  ip VARCHAR(50) COMMENT '客户端IP',
  device VARCHAR(200) COMMENT '设备信息',
  FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 登录日志表
CREATE TABLE sys_login_log (
  login_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
  user_id INT UNSIGNED COMMENT '用户ID',
  login_time DATETIME NOT NULL COMMENT '登录时间',
  logout_time DATETIME COMMENT '退出时间',
  ip VARCHAR(50) COMMENT '登录IP',
  device VARCHAR(200) COMMENT '设备信息',
  status TINYINT COMMENT '登录状态',
  error_msg VARCHAR(200) COMMENT '错误信息',
  FOREIGN KEY (user_id) REFERENCES sys_user(user_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

-- 服务监控表
CREATE TABLE sys_service_monitor (
  monitor_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY COMMENT '监控ID',
  service_name VARCHAR(50) NOT NULL COMMENT '服务名称',
  status TINYINT COMMENT '状态：1运行，0异常',
  cpu_usage DECIMAL(5,2) COMMENT 'CPU使用率',
  memory_usage DECIMAL(5,2) COMMENT '内存使用率',
  record_time DATETIME NOT NULL COMMENT '记录时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务监控表';