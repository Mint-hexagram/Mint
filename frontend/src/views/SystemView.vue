<template>
  <el-container style="background: #f5f5f5; min-height: 100vh;">
    <el-header style="background: white; border-bottom: 2px solid #333; padding: 0 0 0 0;">
      <div class="page-title">系统管理</div>
    </el-header>
    <el-container>
      <!-- 左侧菜单 -->
      <el-aside width="200px" style="background: #f8f8f8; border-right: 2px solid #333;">
        <el-menu :default-active="activeTab" @select="handleMenuSelect" class="el-menu-vertical-demo">
          <el-menu-item v-for="item in menuList" :key="item.key" :index="item.key">
            <span v-html="item.icon"></span> {{ item.label }}
          </el-menu-item>
        </el-menu>
      </el-aside>
      <!-- 右侧内容区域 -->
      <el-main style="background: white;">
        <!-- 用户管理 -->
        <div v-if="activeTab === 'user'">
          <el-form :inline="true" :model="userQuery" class="search-form" size="small">
            <el-form-item label="用户名称">
              <el-input v-model="userQuery.username" placeholder="请输入用户名称" clearable />
            </el-form-item>
            <el-form-item label="手机号码">
              <el-input v-model="userQuery.phone" placeholder="请输入手机号码" clearable />
            </el-form-item>
            <el-form-item label="状态">
              <el-select v-model="userQuery.status" placeholder="用户状态" clearable style="width: 120px;">
                <el-option label="正常" value="0" />
                <el-option label="停用" value="1" />
              </el-select>
            </el-form-item>
            <el-form-item label="创建时间">
              <el-date-picker v-model="userQuery.date" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" style="width: 220px;" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleUserSearch">搜索</el-button>
              <el-button @click="handleUserReset">重置</el-button>
            </el-form-item>
          </el-form>
          <div class="toolbar">
            <div>
              <el-button type="primary" size="small" @click="showUserDialog('add')">新增</el-button>
              <el-button type="success" size="small" @click="handleUserToolbarEdit">修改</el-button>
              <el-button type="danger" size="small" :disabled="!userSelection.length" @click="handleUserDelete">删除</el-button>
              <el-button size="small">导入</el-button>
              <el-button size="small">导出</el-button>
            </div>
            <el-button size="small" @click="handleUserRefresh">刷新</el-button>
          </div>
          <el-table :data="userTable" border style="width: 100%" @selection-change="userSelection = $event">
            <el-table-column type="selection" width="50" />
            <el-table-column prop="userId" label="用户编号" width="90" />
            <el-table-column prop="username" label="用户名称" width="120" />
            <el-table-column prop="nickname" label="用户昵称" width="120" />
            <el-table-column prop="dept" label="部门" width="120" />
            <el-table-column prop="phone" label="手机号码" width="130" />
            <el-table-column prop="status" label="状态" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.status == 1 ? 'success' : 'danger'">
                  {{ scope.row.status == 1 ? '正常' : '停用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="170" />
            <el-table-column label="操作" width="220">
              <template #default="scope">
                <el-button type="primary" link size="small" @click="showUserDialog('edit', scope.row)">修改</el-button>
                <el-button type="danger" link size="small" @click="handleUserDelete([scope.row])">删除</el-button>
                <el-button type="warning" link size="small" @click="showResetPwdDialog(scope.row)">重置密码</el-button>
                <el-button type="info" link size="small" @click="showAssignRoleDialog(scope.row)">分配角色</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            style="margin-top: 20px; text-align: right;"
            background
            layout="total, prev, pager, next, jumper"
            :total="userTotal"
            :page-size="userQuery.pageSize"
            :current-page="userQuery.pageNum"
            @current-change="val => userQuery.pageNum = val"
          />
          <!-- 用户新增/编辑弹窗 -->
          <el-dialog v-model="userDialog.visible" :title="userDialog.mode === 'add' ? '新增用户' : '编辑用户'" width="400px">
            <el-form :model="userDialog.form" label-width="80px" :rules="userRules" ref="userFormRef">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="userDialog.form.username" autocomplete="off" />
              </el-form-item>
              <el-form-item label="昵称" prop="nickname">
                <el-input v-model="userDialog.form.nickname" autocomplete="off" />
              </el-form-item>
              <el-form-item label="部门" prop="deptId">
                <el-tree-select
                  v-model="userDialog.form.deptId"
                  :data="deptTree"
                  :props="{ label: 'label', value: 'id', children: 'children' }"
                  placeholder="请选择部门"
                  style="width: 100%"
                  check-strictly
                  clearable
                />
              </el-form-item>
              <el-form-item label="手机" prop="phone">
                <el-input v-model="userDialog.form.phone" autocomplete="off" />
              </el-form-item>
              <el-form-item label="状态" prop="status">
                <el-select v-model="userDialog.form.status" style="width: 100%">
                  <el-option label="正常" value="0" />
                  <el-option label="停用" value="1" />
                </el-select>
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input v-model="userDialog.form.password" type="password" autocomplete="off" placeholder="如不修改请留空" show-password />
              </el-form-item>
            </el-form>
            <template #footer>
              <el-button @click="userDialog.visible = false">取消</el-button>
              <el-button @click="resetUserDialogForm">重置</el-button>
              <el-button @click="clearUserDialogForm">清空</el-button>
              <el-button type="primary" @click="handleUserDialogOk">确定</el-button>
            </template>
          </el-dialog>
          <!-- 重置密码弹窗 -->
          <el-dialog v-model="resetPwdDialog.visible" title="重置密码" width="400px">
            <el-form :model="resetPwdDialog.form" label-width="100px">
              <el-form-item label="新密码">
                <el-input v-model="resetPwdDialog.form.newPassword" placeholder="不填则重置为默认密码" show-password />
              </el-form-item>
            </el-form>
            <template #footer>
              <el-button @click="resetPwdDialog.visible = false">取消</el-button>
              <el-button type="primary" @click="handleResetPwdOk">确定</el-button>
            </template>
          </el-dialog>
          <!-- 分配角色弹窗 -->
          <el-dialog v-model="assignRoleDialog.visible" title="分配角色" width="500px">
            <div style="margin-bottom: 10px;">
              <span>原始分配角色：</span>
              <el-tag v-for="rid in assignRoleDialog.originRoleIds" :key="rid" style="margin-right: 4px;">
                {{ (allRoles.find(r => r.roleId === rid) || {}).roleName || rid }}
              </el-tag>
              <el-button size="small" style="margin-left: 10px;" @click="resetAssignRoleSelection">重置</el-button>
              <el-button size="small" style="margin-left: 4px;" @click="clearAssignRoleSelection">清空</el-button>
            </div>
            <el-table ref="assignRoleTableRef" :data="allRoles" border style="width: 100%"
              @selection-change="val => assignRoleDialog.form.roleIds = val.map(r => r.roleId)"
              :row-key="row => row.roleId">
              <el-table-column type="selection" width="50" />
              <el-table-column prop="roleName" label="角色名称" />
              <el-table-column prop="roleCode" label="角色编码" />
              <el-table-column prop="permission" label="权限字符" />
            </el-table>
            <template #footer>
              <el-button @click="assignRoleDialog.visible = false">取消</el-button>
              <el-button type="primary" @click="handleAssignRoleOk">确定</el-button>
            </template>
          </el-dialog>
        </div>
        <!-- 角色管理 -->
        <div v-if="activeTab === 'role'">
          <el-form :inline="true" :model="roleQuery" class="search-form" size="small">
            <el-form-item label="角色名称">
              <el-input v-model="roleQuery.name" placeholder="请输入角色名称" clearable />
            </el-form-item>
            <el-form-item label="权限字符">
              <el-input v-model="roleQuery.key" placeholder="请输入权限字符" clearable />
            </el-form-item>
            <el-form-item label="状态">
              <el-select v-model="roleQuery.status" placeholder="角色状态" clearable style="width: 120px;">
                <el-option label="正常" value="0" />
                <el-option label="停用" value="1" />
              </el-select>
            </el-form-item>
            <el-form-item label="创建时间">
              <el-date-picker v-model="roleQuery.date" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" style="width: 220px;" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleRoleSearch">搜索</el-button>
              <el-button @click="handleRoleReset">重置</el-button>
            </el-form-item>
          </el-form>
          <div class="toolbar">
            <div>
              <el-button type="primary" size="small" @click="showRoleDialog('add')">新增</el-button>
              <el-button type="success" size="small" @click="handleRoleToolbarEdit">修改</el-button>
              <el-button type="danger" size="small" :disabled="!roleSelection.length" @click="handleRoleDelete">删除</el-button>
              <el-button size="small">导出</el-button>
            </div>
            <el-button size="small" @click="handleRoleRefresh">刷新</el-button>
          </div>
          <el-table :data="roleTable" border style="width: 100%" @selection-change="roleSelection = $event">
            <el-table-column type="selection" width="50" />
            <el-table-column prop="roleId" label="角色编号" width="90" />
            <el-table-column prop="roleName" label="角色名称" width="120" />
            <el-table-column prop="roleCode" label="权限字符" width="120" />
            <el-table-column prop="roleId" label="显示顺序" width="100" />
            <el-table-column prop="status" label="状态" width="80">
              <template #default="scope">
                <el-tag :type="scope.row.status == 1 ? 'success' : 'danger'">
                  {{ scope.row.status == 1 ? '正常' : '停用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="170" />
            <el-table-column label="操作" width="220">
              <template #default="scope">
                <el-button type="primary" link size="small" @click="showRoleDialog('edit', scope.row)">修改</el-button>
                <el-button type="danger" link size="small" @click="handleRoleDelete([scope.row])">删除</el-button>
                <el-button type="warning" link size="small">数据权限</el-button>
                <el-button type="info" link size="small">分配用户</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            style="margin-top: 20px; text-align: right;"
            background
            layout="total, prev, pager, next, jumper"
            :total="roleTotal"
            :page-size="roleQuery.pageSize"
            :current-page="roleQuery.pageNum"
            @current-change="val => roleQuery.pageNum = val"
          />
          <!-- 角色新增/编辑弹窗 -->
          <el-dialog v-model="roleDialog.visible" :title="roleDialog.mode === 'add' ? '新增角色' : '编辑角色'" width="400px">
            <el-form :model="roleDialog.form" label-width="80px" :rules="roleRules" ref="roleFormRef">
              <el-form-item label="角色名称" prop="roleName">
                <el-input v-model="roleDialog.form.roleName" autocomplete="off" />
              </el-form-item>
              <el-form-item label="角色编码" prop="roleCode">
                <el-input v-model="roleDialog.form.roleCode" autocomplete="off" />
              </el-form-item>
              <el-form-item label="状态" prop="status">
                <el-select v-model="roleDialog.form.status" style="width: 100%">
                  <el-option label="正常" :value="1" />
                  <el-option label="停用" :value="0" />
                </el-select>
              </el-form-item>
            </el-form>
            <template #footer>
              <el-button @click="roleDialog.visible = false">取消</el-button>
              <el-button @click="resetRoleDialogForm">重置</el-button>
              <el-button @click="clearRoleDialogForm">清空</el-button>
              <el-button type="primary" @click="handleRoleDialogOk">确定</el-button>
            </template>
          </el-dialog>
        </div>
        <!-- 菜单管理 -->
        <div v-if="activeTab === 'menu'">
          <el-form :inline="true" :model="menuQuery" class="search-form" size="small">
            <el-form-item label="菜单名称">
              <el-input v-model="menuQuery.name" placeholder="请输入菜单名称" clearable />
            </el-form-item>
            <el-form-item label="状态">
              <el-select v-model="menuQuery.status" placeholder="菜单状态" clearable style="width: 120px;">
                <el-option label="显示" value="0" />
                <el-option label="隐藏" value="1" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleMenuSearch">搜索</el-button>
              <el-button @click="handleMenuReset">重置</el-button>
              <el-button @click="toggleMenuExpand">展开/折叠</el-button>
            </el-form-item>
          </el-form>
          <div class="toolbar">
            <div>
              <el-button type="primary" size="small" @click="showMenuDialog('add')">新增</el-button>
              <el-button type="success" size="small" :disabled="!menuSelection" @click="showMenuDialog('edit')">修改</el-button>
              <el-button type="danger" size="small" :disabled="!menuSelection" @click="handleMenuDelete">删除</el-button>
            </div>
            <el-button size="small" @click="handleMenuRefresh">刷新</el-button>
          </div>
          <el-table
            :data="menuTree"
            row-key="id"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
            border
            style="width: 100%;"
          >
            <el-table-column prop="label" label="菜单名称" />
            <el-table-column prop="icon" label="图标" />
            <el-table-column prop="type" label="类型" />
            <el-table-column label="操作" width="220">
              <template #default="scope">
                <el-button type="primary" link size="small" @click="showMenuDialog('edit', scope.row)">修改</el-button>
                <el-button type="success" link size="small" @click="showMenuDialog('add', scope.row)">新增</el-button>
                <el-button type="danger" link size="small" @click="handleMenuDelete([scope.row])">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <!-- 菜单新增/编辑弹窗 -->
          <el-dialog v-model="menuDialog.visible" :title="menuDialog.mode === 'add' ? '新增菜单' : '编辑菜单'" width="400px">
            <el-form :model="menuDialog.form" label-width="80px" :rules="menuRules" ref="menuFormRef">
              <el-form-item label="菜单名称" prop="label">
                <el-input v-model="menuDialog.form.label" autocomplete="off" />
              </el-form-item>
              <el-form-item label="图标" prop="icon">
                <el-input v-model="menuDialog.form.icon" autocomplete="off" />
              </el-form-item>
              <el-form-item label="类型" prop="type">
                <el-select v-model="menuDialog.form.type" style="width: 100%">
                  <el-option label="目录" value="parent" />
                  <el-option label="菜单" value="child" />
                </el-select>
              </el-form-item>
            </el-form>
            <template #footer>
              <el-button @click="menuDialog.visible = false">取消</el-button>
              <el-button @click="resetMenuDialogForm">重置</el-button>
              <el-button @click="clearMenuDialogForm">清空</el-button>
              <el-button type="primary" @click="handleMenuDialogOk">确定</el-button>
            </template>
          </el-dialog>
        </div>
        <!-- 部门管理 -->
        <div v-if="activeTab === 'dept'">
          <el-form :inline="true" :model="deptQuery" class="search-form" size="small">
            <el-form-item label="部门名称">
              <el-input v-model="deptQuery.name" placeholder="请输入部门名称" clearable />
            </el-form-item>
            <el-form-item label="状态">
              <el-select v-model="deptQuery.status" placeholder="部门状态" clearable style="width: 120px;">
                <el-option label="正常" value="0" />
                <el-option label="停用" value="1" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleDeptSearch">搜索</el-button>
              <el-button @click="handleDeptReset">重置</el-button>
              <el-button @click="toggleDeptExpand">展开/折叠</el-button>
            </el-form-item>
          </el-form>
          <div class="toolbar">
            <div>
              <el-button type="primary" size="small" @click="showDeptDialog('add')">新增</el-button>
              <el-button type="success" size="small" :disabled="!deptSelection" @click="showDeptDialog('edit')">修改</el-button>
              <el-button type="danger" size="small" :disabled="!deptSelection" @click="handleDeptDelete">删除</el-button>
            </div>
            <el-button size="small" @click="handleDeptRefresh">刷新</el-button>
          </div>
          <el-table
            :data="deptTree"
            row-key="id"
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
            border
            style="width: 100%;"
          >
            <el-table-column prop="label" label="部门名称" />
            <el-table-column prop="icon" label="图标" />
            <el-table-column prop="type" label="类型" />
            <el-table-column label="操作" width="220">
              <template #default="scope">
                <el-button type="primary" link size="small" @click="showDeptDialog('edit', scope.row)">修改</el-button>
                <el-button type="success" link size="small" @click="showDeptDialog('add', scope.row)">新增</el-button>
                <el-button type="danger" link size="small" @click="handleDeptDelete([scope.row])">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <!-- 部门新增/编辑弹窗 -->
          <el-dialog v-model="deptDialog.visible" :title="deptDialog.mode === 'add' ? '新增部门' : '编辑部门'" width="400px">
            <el-form :model="deptDialog.form" label-width="80px" :rules="deptRules" ref="deptFormRef">
              <el-form-item label="上级部门" prop="parentId">
                <el-tree-select
                  v-model="deptDialog.form.parentId"
                  :data="deptTree"
                  :props="{ label: 'label', value: 'id', children: 'children' }"
                  placeholder="请选择上级部门"
                  style="width: 100%"
                  check-strictly
                  clearable
                />
              </el-form-item>
              <el-form-item label="部门名称" prop="label">
                <el-input v-model="deptDialog.form.label" autocomplete="off" />
              </el-form-item>
              <el-form-item label="图标" prop="icon">
                <el-input v-model="deptDialog.form.icon" autocomplete="off" />
              </el-form-item>
              <el-form-item label="类型" prop="type">
                <el-select v-model="deptDialog.form.type" style="width: 100%">
                  <el-option label="主部门" value="parent" />
                  <el-option label="子部门" value="child" />
                </el-select>
              </el-form-item>
            </el-form>
            <template #footer>
              <el-button @click="deptDialog.visible = false">取消</el-button>
              <el-button @click="resetDeptDialogForm">重置</el-button>
              <el-button @click="clearDeptDialogForm">清空</el-button>
              <el-button type="primary" @click="handleDeptDialogOk">确定</el-button>
            </template>
          </el-dialog>
        </div>
        <!-- 参数配置 -->
        <div v-if="activeTab === 'config'">
          <el-form :inline="true" :model="configQuery" class="search-form" size="small">
            <el-form-item label="参数名称">
              <el-input v-model="configQuery.name" placeholder="请输入参数名称" clearable />
            </el-form-item>
            <el-form-item label="参数键名">
              <el-input v-model="configQuery.key" placeholder="请输入参数键名" clearable />
            </el-form-item>
            <el-form-item label="系统内置">
              <el-select v-model="configQuery.builtin" placeholder="系统内置" clearable style="width: 120px;">
                <el-option label="是" value="Y" />
                <el-option label="否" value="N" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleConfigSearch">搜索</el-button>
              <el-button @click="handleConfigReset">重置</el-button>
            </el-form-item>
          </el-form>
          <div class="toolbar">
            <div>
              <el-button type="primary" size="small" @click="showConfigDialog('add')">新增</el-button>
              <el-button type="success" size="small" :disabled="!configSelection.length" @click="showConfigDialog('edit')">修改</el-button>
              <el-button type="danger" size="small" :disabled="!configSelection.length" @click="handleConfigDelete">删除</el-button>
              <el-button size="small">导出</el-button>
              <el-button size="small">刷新缓存</el-button>
            </div>
            <el-button size="small" @click="handleConfigRefresh">刷新</el-button>
          </div>
          <el-table :data="configTable" border style="width: 100%" @selection-change="configSelection = $event">
            <el-table-column type="selection" width="50" />
            <el-table-column prop="id" label="参数编号" width="90" />
            <el-table-column prop="name" label="参数名称" width="180" />
            <el-table-column prop="key" label="参数键名" width="180" />
            <el-table-column prop="value" label="参数键值" width="120" />
            <el-table-column prop="builtin" label="系统内置" width="80" />
            <el-table-column prop="remark" label="备注" width="220" />
            <el-table-column prop="createTime" label="创建时间" width="170" />
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button type="primary" link size="small" @click="showConfigDialog('edit', scope.row)">修改</el-button>
                <el-button type="danger" link size="small" @click="handleConfigDelete([scope.row])">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            style="margin-top: 20px; text-align: right;"
            background
            layout="total, prev, pager, next, jumper"
            :total="configTotal"
            :page-size="configQuery.pageSize"
            :current-page="configQuery.pageNum"
            @current-change="val => configQuery.pageNum = val"
          />
          <!-- 参数新增/编辑弹窗 -->
          <el-dialog v-model="configDialog.visible" :title="configDialog.mode === 'add' ? '新增参数' : '编辑参数'" width="400px">
            <el-form :model="configDialog.form" label-width="80px" :rules="configRules" ref="configFormRef">
              <el-form-item label="参数名称" prop="name">
                <el-input v-model="configDialog.form.name" autocomplete="off" />
              </el-form-item>
              <el-form-item label="参数键名" prop="key">
                <el-input v-model="configDialog.form.key" autocomplete="off" />
              </el-form-item>
              <el-form-item label="参数键值" prop="value">
                <el-input v-model="configDialog.form.value" autocomplete="off" />
              </el-form-item>
              <el-form-item label="系统内置" prop="builtin">
                <el-select v-model="configDialog.form.builtin" style="width: 100%">
                  <el-option label="是" value="Y" />
                  <el-option label="否" value="N" />
                </el-select>
              </el-form-item>
              <el-form-item label="备注" prop="remark">
                <el-input v-model="configDialog.form.remark" autocomplete="off" />
              </el-form-item>
            </el-form>
            <template #footer>
              <el-button @click="configDialog.visible = false">取消</el-button>
              <el-button @click="resetConfigDialogForm">重置</el-button>
              <el-button @click="clearConfigDialogForm">清空</el-button>
              <el-button type="primary" @click="handleConfigDialogOk">确定</el-button>
            </template>
          </el-dialog>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, reactive, nextTick, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { getUserList, addUser, updateUser, deleteUser, resetPassword, getUserRoleIds, assignRoles } from '../api/user';
import { getDeptTree, addDept, updateDept, deleteDept } from '../api/dept';
import { getRoleList, addRole, updateRole, deleteRole } from '../api/role';

const menuList = [
  { key: 'user', label: '用户管理', icon: '👤' },
  { key: 'role', label: '角色管理', icon: '🔑' },
  { key: 'menu', label: '菜单管理', icon: '📋' },
  { key: 'dept', label: '部门管理', icon: '🏢' },
  { key: 'config', label: '参数配置', icon: '⚙️' },
];
const activeTab = ref('user');

function handleMenuSelect(key) {
  activeTab.value = key;
  if (key === 'dept') {
    fetchDeptTree();
  }
}

// 用户管理数据与交互
const userQuery = reactive({
  username: '',
  phone: '',
  status: '',
  date: '',
  pageNum: 1,
  pageSize: 10,
});
const userTable = ref([]);
const userTotal = ref(0);
const userSelection = ref([]);

async function fetchUserList() {
  const params = {
    pageNum: userQuery.pageNum,
    pageSize: userQuery.pageSize,
    username: userQuery.username,
    phone: userQuery.phone,
    status: userQuery.status,
    // 可扩展日期范围
  };
  const res = await getUserList(params);
  if (res && res.data) {
    userTable.value = res.data.records || res.data.list || [];
    userTotal.value = res.data.total || res.data.records?.length || 0;
  }
}

function handleUserSearch() {
  userQuery.pageNum = 1;
  fetchUserList();
}
function handleUserReset() {
  userQuery.username = '';
  userQuery.phone = '';
  userQuery.status = '';
  userQuery.date = '';
  handleUserRefresh();
}
function handleUserRefresh() {
  fetchUserList();
}
function handleUserDelete(rows) {
  const toDelete = rows || userSelection.value;
  if (!toDelete.length) return;
  ElMessageBox.confirm('确定要删除选中的用户吗？', '提示', { type: 'warning' })
    .then(async () => {
      for (const row of toDelete) {
        await deleteUser(row.id);
      }
      ElMessage.success('删除成功');
      fetchUserList();
    })
    .catch(() => {});
}

// 用户新增/编辑弹窗
const userDialog = reactive({
  visible: false,
  mode: 'add',
  form: {
    userId: null,
    username: '',
    nickname: '',
    deptId: '',
    phone: '',
    status: '0',
    password: '',
  },
  originForm: {
    userId: null,
    username: '',
    nickname: '',
    deptId: '',
    phone: '',
    status: '0',
    password: '',
  }
});
const userFormRef = ref();
const userRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  deptId: [{ required: true, message: '请选择部门', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机', trigger: 'blur' }],
};
function showUserDialog(mode, row) {
  fetchDeptTree();
  if (mode === 'edit') {
    if (userSelection && Array.isArray(userSelection.value) && userSelection.value.length > 1) {
      ElMessageBox.alert('不能同时修改多个用户，请只选择一个用户进行编辑。', '提示', { type: 'warning' });
      return;
    }
  }
  userDialog.mode = mode;
  userDialog.visible = true;
  if (mode === 'edit' && row) {
    userDialog.form.userId = row.userId;
    userDialog.form.username = row.username;
    userDialog.form.nickname = row.nickname;
    userDialog.form.deptId = row.deptId;
    userDialog.form.phone = row.phone;
    userDialog.form.status = String(row.status);
    userDialog.form.password = '';
    userDialog.originForm = {
      userId: row.userId,
      username: row.username,
      nickname: row.nickname,
      deptId: row.deptId,
      phone: row.phone,
      status: String(row.status),
      password: '',
    };
  } else {
    userDialog.form.userId = null;
    userDialog.form.username = '';
    userDialog.form.nickname = '';
    userDialog.form.deptId = '';
    userDialog.form.phone = '';
    userDialog.form.status = '0';
    userDialog.form.password = '';
    userDialog.originForm = {
      userId: null,
      username: '',
      nickname: '',
      deptId: '',
      phone: '',
      status: '0',
      password: '',
    };
  }
  nextTick(() => userFormRef.value && userFormRef.value.clearValidate());
}
function resetUserDialogForm() {
  // 仅当当前表单和originForm不一致时才重置
  const cur = JSON.stringify(userDialog.form);
  const origin = JSON.stringify(userDialog.originForm);
  if (cur === origin) return;
  Object.assign(userDialog.form, userDialog.originForm);
  nextTick(() => userFormRef.value && userFormRef.value.clearValidate());
}
function clearUserDialogForm() {
  userDialog.form.userId = null;
  userDialog.form.username = '';
  userDialog.form.nickname = '';
  userDialog.form.deptId = '';
  userDialog.form.phone = '';
  userDialog.form.status = '0';
  userDialog.form.password = '';
  nextTick(() => userFormRef.value && userFormRef.value.clearValidate());
}
async function handleUserDialogOk() {
  userFormRef.value.validate(async valid => {
    if (!valid) return;
    if (userDialog.mode === 'add') {
      // 新增：如果未填写密码，设置为默认密码
      if (!userDialog.form.password) {
        userDialog.form.password = '123456';
      }
      await addUser(userDialog.form);
      ElMessage.success('新增成功');
    } else {
      await updateUser(userDialog.form);
      ElMessage.success('修改成功');
    }
    userDialog.visible = false;
    fetchUserList();
  });
}

onMounted(() => {
  fetchUserList();
  fetchRoleList();
  if (activeTab.value === 'dept') {
    fetchDeptTree();
  }
});

// 角色管理数据与交互
const roleQuery = reactive({
  name: '',
  key: '',
  status: '',
  date: '',
  pageNum: 1,
  pageSize: 10,
});
const roleTable = ref([]);
const roleTotal = ref(0);
const roleSelection = ref([]);

async function fetchRoleList() {
  const res = await getRoleList();
  // 兼容后端返回格式
  roleTable.value = (res.data && res.data.records) || res.data || [];
  roleTotal.value = roleTable.value.length;
}

async function handleRoleSearch() {
  // 先拉取所有角色，再做前端过滤
  await fetchRoleList();
  roleTable.value = roleTable.value.filter(r => {
    return (!roleQuery.name || r.roleName?.includes(roleQuery.name) || r.name?.includes(roleQuery.name)) &&
      (!roleQuery.key || r.roleCode?.includes(roleQuery.key) || r.key?.includes(roleQuery.key)) &&
      (!roleQuery.status || String(r.status) === String(roleQuery.status));
  });
  roleTotal.value = roleTable.value.length;
}
async function handleRoleReset() {
  roleQuery.name = '';
  roleQuery.key = '';
  roleQuery.status = '';
  roleQuery.date = '';
  await handleRoleRefresh();
}
async function handleRoleRefresh() {
  await fetchRoleList();
}
function handleRoleDelete(rows) {
  const toDelete = rows || roleSelection.value;
  if (!toDelete.length) return;
  ElMessageBox.confirm('确定要删除选中的角色吗？', '提示', { type: 'warning' })
    .then(async () => {
      for (const row of toDelete) {
        await deleteRole(row.roleId);
      }
      ElMessage.success('删除成功');
      handleRoleRefresh();
    })
    .catch(() => {});
}

// 角色新增/编辑弹窗
const roleDialog = reactive({
  visible: false,
  mode: 'add',
  form: { roleId: '', roleName: '', roleCode: '', status: 0 },
  originForm: { roleId: '', roleName: '', roleCode: '', status: 0 }
});
const roleFormRef = ref();
const roleRules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
};
function showRoleDialog(mode, row) {
  roleDialog.mode = mode;
  roleDialog.visible = true;
  if (mode === 'edit' && row) {
    Object.assign(roleDialog.form, {
      roleId: row.id || row.roleId,
      roleName: row.name || row.roleName,
      roleCode: row.key || row.roleCode,
      status: Number(row.status)
    });
    roleDialog.originForm = { ...roleDialog.form };
  } else {
    Object.assign(roleDialog.form, { roleId: '', roleName: '', roleCode: '', status: 0 });
    roleDialog.originForm = { roleId: '', roleName: '', roleCode: '', status: 0 };
  }
  nextTick(() => roleFormRef.value && roleFormRef.value.clearValidate());
}
async function handleRoleDialogOk() {
  roleFormRef.value.validate(async valid => {
    if (!valid) return;
    if (roleDialog.mode === 'add') {
      const { roleName, roleCode, status } = roleDialog.form;
      const submitData = { roleName, roleCode, status };
      await addRole(submitData);
      ElMessage.success('新增成功');
    } else {
      await updateRole(roleDialog.form);
      ElMessage.success('修改成功');
    }
    roleDialog.visible = false;
    handleRoleRefresh();
  });
}
function resetRoleDialogForm() {
  if (roleDialog.form && roleDialog.originForm) {
    const cur = JSON.stringify(roleDialog.form);
    const origin = JSON.stringify(roleDialog.originForm);
    if (cur === origin) return;
    Object.assign(roleDialog.form, roleDialog.originForm);
    nextTick(() => roleFormRef.value && roleFormRef.value.clearValidate());
  }
}
function clearRoleDialogForm() {
  roleDialog.form.roleName = '';
  roleDialog.form.roleCode = '';
  roleDialog.form.status = 0;
  nextTick(() => roleFormRef.value && roleFormRef.value.clearValidate());
}

// 菜单管理数据与交互
const menuQuery = reactive({
  name: '',
  status: '',
});
const menuTree = ref([
  { id: 1, label: '系统管理', icon: '👤', children: [
    { id: 11, label: '用户管理', icon: '👤', children: [] },
    { id: 12, label: '角色管理', icon: '🔑', children: [] },
    { id: 13, label: '菜单管理', icon: '📋', children: [] },
    { id: 14, label: '部门管理', icon: '🏢', children: [] },
    { id: 15, label: '参数配置', icon: '⚙️', children: [] },
  ] },
  { id: 2, label: '业务模块', icon: '📋', children: [] },
]);
const menuExpandAll = ref(true);
const menuSelection = ref();

function handleMenuSearch() {
  // 这里可接API，当前为静态过滤
  menuQuery.pageNum = 1;
  menuTree.value = menuTree.value.filter(t => {
    return (!menuQuery.name || t.label.includes(menuQuery.name));
  });
}
function handleMenuReset() {
  menuQuery.name = '';
  handleMenuRefresh();
}
function handleMenuRefresh() {
  // 这里应重新拉取数据，当前重置为初始静态
  menuTree.value = [
    { id: 1, label: '系统管理', icon: '👤', children: [
      { id: 11, label: '用户管理', icon: '👤', children: [] },
      { id: 12, label: '角色管理', icon: '🔑', children: [] },
      { id: 13, label: '菜单管理', icon: '📋', children: [] },
      { id: 14, label: '部门管理', icon: '🏢', children: [] },
      { id: 15, label: '参数配置', icon: '⚙️', children: [] },
    ] },
    { id: 2, label: '业务模块', icon: '📋', children: [] },
  ];
}
function handleMenuDelete(rows) {
  const toDelete = rows || menuSelection.value;
  if (!toDelete.length) return;
  ElMessageBox.confirm('确定要删除选中的菜单吗？', '提示', { type: 'warning' })
    .then(() => {
      menuTree.value = menuTree.value.filter(t => !toDelete.some(d => d.id === t.id));
      ElMessage.success('删除成功');
    })
    .catch(() => {});
}

// 菜单新增/编辑弹窗
const menuDialog = reactive({
  visible: false,
  mode: 'add',
  form: { id: '', label: '', icon: '', type: 'parent' }
});
function showMenuDialog(mode, row) {
  if (mode === 'edit') {
    if (menuSelection && Array.isArray(menuSelection.value) && menuSelection.value.length > 1) {
      ElMessageBox.alert('不能同时修改多个菜单，请只选择一个菜单进行编辑。', '提示', { type: 'warning' });
      return;
    }
  }
  menuDialog.mode = mode;
  menuDialog.visible = true;
  if (mode === 'edit' && row) {
    Object.assign(menuDialog.form, row);
  } else {
    Object.assign(menuDialog.form, { id: '', label: '', icon: '', type: 'parent' });
  }
  nextTick(() => menuFormRef.value && menuFormRef.value.clearValidate());
}

// 部门管理数据与交互
const deptQuery = reactive({
  name: '',
  status: '',
});
const deptTree = ref([]);
const deptExpandAll = ref(true);
const deptSelection = ref();

async function fetchDeptTree() {
  const params = {
    deptName: deptQuery.name,
    // 可扩展状态等
  };
  const res = await getDeptTree(params);
  function adaptDeptTree(list) {
    return (list || []).map(item => ({
      id: item.deptId || item.id,
      label: item.deptName || item.label,
      icon: item.icon || '🏢',
      type: item.parentId === 0 ? 'parent' : 'child',
      children: adaptDeptTree(item.children)
    }));
  }
  if (res && res.data) {
    deptTree.value = adaptDeptTree(res.data);
  }
}

function handleDeptSearch() {
  fetchDeptTree();
}
function handleDeptReset() {
  deptQuery.name = '';
  fetchDeptTree();
}
function handleDeptRefresh() {
  fetchDeptTree();
}
function handleDeptDelete(rows) {
  const toDelete = rows || (deptSelection.value ? [deptSelection.value] : []);
  if (!toDelete.length) return;
  ElMessageBox.confirm('确定要删除选中的部门吗？', '提示', { type: 'warning' })
    .then(async () => {
      for (const row of toDelete) {
        await deleteDept(row.id || row.deptId);
      }
      ElMessage.success('删除成功');
      fetchDeptTree();
    })
    .catch(() => {});
}

// 部门新增/编辑弹窗
const deptDialog = reactive({
  visible: false,
  mode: 'add',
  form: { id: '', label: '', icon: '', type: 'parent', parentId: '' }
});
function showDeptDialog(mode, row) {
  if (mode === 'edit') {
    if (deptSelection && Array.isArray(deptSelection.value) && deptSelection.value.length > 1) {
      ElMessageBox.alert('不能同时修改多个部门，请只选择一个部门进行编辑。', '提示', { type: 'warning' });
      return;
    }
  }
  deptDialog.mode = mode;
  deptDialog.visible = true;
  if (mode === 'edit' && row) {
    deptDialog.form.id = row.id || row.deptId;
    deptDialog.form.label = row.label || row.deptName;
    deptDialog.form.icon = row.icon || '';
    deptDialog.form.type = row.type || (row.parentId ? 'child' : 'parent');
    deptDialog.form.parentId = row.parentId || '';
  } else {
    Object.assign(deptDialog.form, { id: '', label: '', icon: '', type: 'parent', parentId: '' });
  }
  nextTick(() => deptFormRef.value && deptFormRef.value.clearValidate());
}

// 参数配置数据与交互
const configQuery = reactive({
  name: '',
  key: '',
  builtin: '',
  pageNum: 1,
  pageSize: 10,
});
const configTable = ref([
  { id: 1, name: '系统名称', key: 'system.name', value: '系统管理平台', builtin: 'Y', remark: '系统名称', createTime: '2024-01-01 00:00:00' },
  { id: 2, name: '登录超时时间', key: 'system.loginTimeout', value: '30分钟', builtin: 'Y', remark: '用户登录超时时间', createTime: '2024-02-15 10:30:00' },
  { id: 3, name: '默认密码长度', key: 'system.defaultPasswordLength', value: '8位', builtin: 'Y', remark: '系统默认密码长度', createTime: '2024-03-01 14:20:00' },
]);
const configTotal = ref(3);
const configSelection = ref([]);

function handleConfigSearch() {
  // 这里可接API，当前为静态过滤
  configQuery.pageNum = 1;
  configTable.value = configTable.value.filter(c => {
    return (!configQuery.name || c.name.includes(configQuery.name)) &&
      (!configQuery.key || c.key.includes(configQuery.key)) &&
      (!configQuery.builtin || c.builtin === configQuery.builtin);
  });
  configTotal.value = configTable.value.length;
}
function handleConfigReset() {
  configQuery.name = '';
  configQuery.key = '';
  configQuery.builtin = '';
  handleConfigRefresh();
}
function handleConfigRefresh() {
  // 这里应重新拉取数据，当前重置为初始静态
  configTable.value = [
    { id: 1, name: '系统名称', key: 'system.name', value: '系统管理平台', builtin: 'Y', remark: '系统名称', createTime: '2024-01-01 00:00:00' },
    { id: 2, name: '登录超时时间', key: 'system.loginTimeout', value: '30分钟', builtin: 'Y', remark: '用户登录超时时间', createTime: '2024-02-15 10:30:00' },
    { id: 3, name: '默认密码长度', key: 'system.defaultPasswordLength', value: '8位', builtin: 'Y', remark: '系统默认密码长度', createTime: '2024-03-01 14:20:00' },
  ];
  configTotal.value = configTable.value.length;
}
function handleConfigDelete(rows) {
  const toDelete = rows || configSelection.value;
  if (!toDelete.length) return;
  ElMessageBox.confirm('确定要删除选中的参数吗？', '提示', { type: 'warning' })
    .then(() => {
      configTable.value = configTable.value.filter(c => !toDelete.some(d => d.id === c.id));
      configTotal.value = configTable.value.length;
      ElMessage.success('删除成功');
    })
    .catch(() => {});
}

// 参数新增/编辑弹窗
const configDialog = reactive({
  visible: false,
  mode: 'add',
  form: { id: '', name: '', key: '', value: '', builtin: 'Y', remark: '' }
});
function showConfigDialog(mode, row) {
  if (mode === 'edit' && row) {
    Object.assign(configDialog.form, row);
  } else {
    Object.assign(configDialog.form, { id: '', name: '', key: '', value: '', builtin: 'Y', remark: '' });
  }
  nextTick(() => configFormRef.value && configFormRef.value.clearValidate());
}

// 重置密码弹窗
const resetPwdDialog = reactive({
  visible: false,
  userId: null,
  form: { newPassword: '' }
});
function showResetPwdDialog(row) {
  resetPwdDialog.visible = true;
  resetPwdDialog.userId = row.userId;
  resetPwdDialog.form.newPassword = '';
}
async function handleResetPwdOk() {
  await resetPassword(resetPwdDialog.userId, resetPwdDialog.form.newPassword || undefined);
  ElMessage.success('密码重置成功');
  resetPwdDialog.visible = false;
}

// 分配角色弹窗
const assignRoleDialog = reactive({
  visible: false,
  userId: null,
  form: { roleIds: [] },
  originRoleIds: []
});
const allRoles = ref([]);
const assignRoleTableRef = ref();
async function showAssignRoleDialog(row) {
  assignRoleDialog.visible = true;
  assignRoleDialog.userId = row.id || row.userId;
  // 获取所有角色
  const res = await getRoleList();
  allRoles.value = (res.data && res.data.records) || res.data || [];
  // 获取用户已分配角色
  const roleRes = await getUserRoleIds(assignRoleDialog.userId);
  assignRoleDialog.form.roleIds = roleRes.data || [];
  assignRoleDialog.originRoleIds = [...assignRoleDialog.form.roleIds];
  nextTick(() => {
    if (assignRoleTableRef.value) {
      assignRoleTableRef.value.clearSelection();
      allRoles.value.forEach(role => {
        if (assignRoleDialog.form.roleIds.includes(role.roleId)) {
          assignRoleTableRef.value.toggleRowSelection(role, true);
        }
      });
    }
  });
}
function resetAssignRoleSelection() {
  // 如果当前选择和初始分配完全一致，则不做任何操作
  const cur = assignRoleDialog.form.roleIds.slice().sort().join(',');
  const origin = assignRoleDialog.originRoleIds.slice().sort().join(',');
  if (cur === origin) return;
  assignRoleDialog.form.roleIds = [...assignRoleDialog.originRoleIds];
  nextTick(() => {
    if (assignRoleTableRef.value) {
      assignRoleTableRef.value.clearSelection();
      allRoles.value.forEach(role => {
        if (assignRoleDialog.form.roleIds.includes(role.roleId)) {
          assignRoleTableRef.value.toggleRowSelection(role, true);
        }
      });
    }
  });
}
function clearAssignRoleSelection() {
  assignRoleDialog.form.roleIds = [];
  nextTick(() => {
    if (assignRoleTableRef.value) {
      assignRoleTableRef.value.clearSelection();
    }
  });
}
async function handleAssignRoleOk() {
  await assignRoles(assignRoleDialog.userId, assignRoleDialog.form.roleIds);
  ElMessage.success('分配角色成功');
  assignRoleDialog.visible = false;
}

function handleUserToolbarEdit() {
  if (!userSelection.value || userSelection.value.length !== 1) {
    ElMessageBox.alert('请只选择一个用户进行编辑。', '提示', { type: 'warning' });
    return;
  }
  showUserDialog('edit', userSelection.value[0]);
}
function handleRoleToolbarEdit() {
  if (!roleSelection.value || roleSelection.value.length !== 1) {
    ElMessageBox.alert('请只选择一个角色进行编辑。', '提示', { type: 'warning' });
    return;
  }
  showRoleDialog('edit', roleSelection.value[0]);
}

// 工具函数：将树形部门拍平成一维数组
function flattenDeptTree(tree) {
  const result = [];
  function traverse(list) {
    (list || []).forEach(item => {
      result.push({ id: item.id, label: item.label });
      if (item.children && item.children.length) traverse(item.children);
    });
  }
  traverse(tree);
  return result;
}
</script>

<style scoped>
.page-title {
  font-size: 24px;
  margin-bottom: 0;
  padding: 20px;
  background: white;
  border: none;
}
.toolbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  padding: 10px 0;
  border: none;
  background: none;
}
.search-form {
  margin-bottom: 20px;
  background: #f8f8f8;
  border: 1px solid #666;
  padding: 15px;
  border-radius: 4px;
}
</style> 