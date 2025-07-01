# 任务管理模块权限控制说明

本项目中的任务管理模块（包括新增、编辑、删除功能）已经实现了基于角色的访问控制（RBAC）。

## 1. 权限设计

根据业务需求，只有特定角色的用户才能执行任务的增、删、改操作：

- **超级管理员 (admin)**: 拥有所有操作权限。
- **运维人员 (operator)**: 拥有增、删、改操作权限。
- **普通用户**: 只有查看任务列表和详情的权限。

## 2. 后端实现

后端的权限控制通过 **Spring Security** 实现。

### 2.1. 开启方法级安全

在 `SecurityConfig.java` 配置类中，通过添加 `@EnableMethodSecurity(prePostEnabled = true)` 注解，开启了全局的方法级安全控制。

```java
// metro-inspection-backend/src/main/java/com/metro/inspection/config/SecurityConfig.java

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    // ...
}
```

### 2.2. 接口权限注解

在 `TaskInfoController.java` 中，对涉及写操作的接口（`create`, `update`, `delete`）使用了 `@PreAuthorize` 注解，限制了只有具备 `admin` 或 `operator` 权限的用户才能调用。

```java
// metro-inspection-backend/src/main/java/com/metro/inspection/controller/TaskInfoController.java

public class TaskInfoController {
    // ...

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin', 'operator')")
    public ApiResponse<TaskInfo> create(@Valid @RequestBody TaskInfo taskInfo) {
        // ...
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin', 'operator')")
    public ApiResponse<TaskInfo> update(@PathVariable Long id, @Valid @RequestBody TaskInfo taskInfo) {
        // ...
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin', 'operator')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        // ...
    }
}
```

如果无权限用户尝试调用这些接口，后端会返回 `403 Forbidden` 错误。

## 3. 前端实现

前端通过 **Vue** 的 `v-if` 指令，根据用户的权限动态渲染操作按钮，提升了用户体验。

### 3.1. 权限判断

在 `TaskView.vue` 组件中，增加了一个 `hasPermission` 函数，用于检查当前用户是否拥有所需的操作权限。

**注意**: 以下代码中的 `userStore` 是一个**模拟实现**。在实际项目中，它应该由您的全局状态管理方案（如 Pinia 或 Vuex）提供，在用户登录后从后端获取真实的权限列表。

```javascript
// frontend/src/views/TaskView.vue

// 模拟从store获取用户信息
const userStore = {
  // 假设这是登录后从后端获取并存储的用户权限
  permissions: ['admin'] // or ['operator'], or ['user'] etc.
}

// 权限检查
function hasPermission(requiredPermissions) {
  const userPermissions = userStore.permissions || []
  return requiredPermissions.some(p => userPermissions.includes(p))
}
```

### 3.2. 动态渲染

在模板中，"新增任务"、"编辑" 和 "删除" 按钮都通过 `v-if` 指令调用 `hasPermission` 函数来判断是否显示。

```html
<!-- frontend/src/views/TaskView.vue -->

<!-- 新增按钮 -->
<el-button type="primary" @click="handleAdd" v-if="hasPermission(['admin', 'operator'])">
  新增任务
</el-button>

<!-- 操作列按钮 -->
<el-table-column label="操作" width="200" align="center">
  <template #default="{ row }">
    <el-button type="text" @click="handleView(row)">查看</el-button>
    <template v-if="hasPermission(['admin', 'operator'])">
      <el-button type="text" @click="handleEdit(row)">编辑</el-button>
      <el-button type="text" @click="handleDelete(row)">删除</el-button>
    </template>
  </template>
</el-table-column>
```

这样，不具备权限的用户将不会看到相关操作按钮。 