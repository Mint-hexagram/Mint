package com.metro.inspection.controller;

import com.metro.inspection.common.ApiResponse;
import com.metro.inspection.entity.SysUser;
import com.metro.inspection.entity.SysRole;
import com.metro.inspection.service.SysUserService;
import com.metro.inspection.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/hello")
    public ApiResponse<String> hello() {
        return ApiResponse.success("Hello from Metro Inspection Backend!");
    }

    @PostMapping("/create-test-user")
    public ApiResponse<String> createTestUser() {
        try {
            // 检查是否已存在测试用户
            SysUser existingUser = sysUserService.getByUsername("admin");
            if (existingUser != null) {
                return ApiResponse.success("测试用户已存在");
            }

            // 创建测试用户
            SysUser user = new SysUser();
            user.setUsername("admin");
            user.setNickname("管理员");
            user.setPassword(new BCryptPasswordEncoder().encode("123456"));
            user.setPhone("13800138000");
            user.setStatus(1); // 正常状态
            user.setCreateTime(LocalDateTime.now());
            user.setRemark("测试用户");

            sysUserService.save(user);
            return ApiResponse.success("测试用户创建成功，用户名：admin，密码：123456");
        } catch (Exception e) {
            return ApiResponse.error("创建测试用户失败：" + e.getMessage());
        }
    }

    @PostMapping("/init-roles")
    public ApiResponse<String> initRoles() {
        try {
            // 检查是否已存在角色
            List<SysRole> existingRoles = sysRoleService.list();
            if (!existingRoles.isEmpty()) {
                return ApiResponse.success("角色已存在，无需初始化");
            }

            // 创建默认角色
            SysRole adminRole = new SysRole();
            adminRole.setRoleName("admin");
            adminRole.setPermission("sys:*:*");
            adminRole.setSort(1);
            adminRole.setStatus(1);
            adminRole.setCreateTime(LocalDateTime.now());
            adminRole.setRemark("超级管理员");
            sysRoleService.save(adminRole);

            SysRole userRole = new SysRole();
            userRole.setRoleName("user");
            userRole.setPermission("sys:user:view,sys:task:view,sys:defect:view");
            userRole.setSort(2);
            userRole.setStatus(1);
            userRole.setCreateTime(LocalDateTime.now());
            userRole.setRemark("普通用户");
            sysRoleService.save(userRole);

            SysRole operatorRole = new SysRole();
            operatorRole.setRoleName("operator");
            operatorRole.setPermission("sys:user:view,sys:task:*,sys:defect:*");
            operatorRole.setSort(3);
            operatorRole.setStatus(1);
            operatorRole.setCreateTime(LocalDateTime.now());
            operatorRole.setRemark("操作员");
            sysRoleService.save(operatorRole);

            return ApiResponse.success("角色初始化成功：admin(超级管理员)、user(普通用户)、operator(操作员)");
        } catch (Exception e) {
            return ApiResponse.error("角色初始化失败：" + e.getMessage());
        }
    }

    @GetMapping("/users")
    public ApiResponse<List<SysUser>> getUsers() {
        try {
            List<SysUser> users = sysUserService.list();
            return ApiResponse.success(users);
        } catch (Exception e) {
            return ApiResponse.error("获取用户列表失败：" + e.getMessage());
        }
    }
} 