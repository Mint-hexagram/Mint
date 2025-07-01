# 地铁隧道检测大数据仿真分析平台 - 后端

## 项目概述

本项目是地铁隧道检测大数据仿真分析平台的后端服务，使用Spring Boot 3.5.3 + MyBatis-Plus构建。

## 技术栈

- **Spring Boot**: 3.5.3
- **MyBatis-Plus**: 3.5.5
- **MySQL**: 8.0
- **Spring Security**: 6.x
- **JWT**: 0.12.5
- **Java**: 17

## 主要功能模块

### 1. 用户管理 (SysUser)
- 用户CRUD操作
- 用户认证和授权

### 2. 菜单管理 (SysMenu)
- 系统菜单管理
- 权限控制

### 3. 角色管理 (SysRole)
- 角色CRUD操作
- 角色权限分配

### 4. 部门管理 (SysDept)
- 部门树形结构管理

### 5. 任务管理 (TaskInfo)
- 检测任务管理
- 任务状态跟踪
- 多条件搜索

### 6. 缺陷管理 (DefectInfo)
- 缺陷信息管理
- 缺陷分类和统计
- 整改状态跟踪

### 7. 系统监控
- 登录日志 (SysLoginLog)
- 操作日志 (SysOperLog)
- 服务监控 (SysServiceMonitor)

## 数据库设计

### 核心表结构

1. **sys_user** - 用户表
2. **sys_menu** - 菜单表
3. **sys_role** - 角色表
4. **sys_dept** - 部门表
5. **task_info** - 任务信息表
6. **defect_info** - 缺陷信息表
7. **sys_login_log** - 登录日志表
8. **sys_oper_log** - 操作日志表
9. **sys_service_monitor** - 服务监控表
10. **sys_user_role** - 用户角色关联表
11. **sys_role_menu** - 角色菜单关联表

## 项目结构

```
src/main/java/com/metro/inspection/
├── config/                 # 配置类
│   └── MybatisPlusConfig.java
├── controller/            # 控制器
├── entity/               # 实体类
├── mapper/               # MyBatis-Plus Mapper接口
├── service/              # 服务接口
│   └── impl/             # 服务实现
├── dto/                  # 数据传输对象
└── MetroInspectionBackendApplication.java
```

## 配置说明

### 数据库配置
```properties
spring.datasource.url=jdbc:mysql://your-database-url:3306/your-database
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### MyBatis-Plus配置
```properties
mybatis-plus.configuration.map-underscore-to-camel-case=true
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
mybatis-plus.global-config.db-config.logic-delete-field=delFlag
mybatis-plus.global-config.db-config.logic-delete-value=1
mybatis-plus.global-config.db-config.logic-not-delete-value=0
```

## 运行说明

1. 确保MySQL数据库已启动并创建相应的数据库
2. 修改`application.properties`中的数据库连接信息
3. 运行`MetroInspectionBackendApplication.java`
4. 访问`http://localhost:8080/test/health`验证服务是否启动成功

## API测试

### 健康检查
```
GET /test/health
```

### 获取所有用户
```
GET /test/users
```

## 注意事项

1. 本项目已从JPA完全迁移到MyBatis-Plus
2. 所有实体类使用MyBatis-Plus注解
3. 服务层继承MyBatis-Plus的IService接口
4. 分页查询使用MyBatis-Plus的IPage
5. 条件查询使用QueryWrapper

## 开发团队

- 后端开发：使用Spring Boot + MyBatis-Plus
- 前端开发：Vue 3 + Element Plus 