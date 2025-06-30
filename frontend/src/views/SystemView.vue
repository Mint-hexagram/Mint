<template>
  <div class="task-bg">
    <div class="page-title">系统管理</div>
    <div class="wireframe">
      <el-tabs v-model="activeTab" class="system-tabs" tab-position="left">
        <el-tab-pane label="👤 用户管理" name="user">
          <div class="tab-content">
            <el-form :inline="true" :model="userQueryParams" class="search-form-wireframe">
              <el-form-item label="用户名称">
                <el-input v-model="userQueryParams.username" placeholder="请输入用户名称" clearable></el-input>
              </el-form-item>
              <el-form-item label="手机号码">
                <el-input v-model="userQueryParams.phone" placeholder="请输入手机号码" clearable></el-input>
              </el-form-item>
              <el-form-item label="状态">
                <el-select v-model="userQueryParams.status">
                  <el-option label="全部" :value="null"></el-option>
                  <el-option label="正常" :value="0"></el-option>
                  <el-option label="停用" :value="1"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="创建时间">
                 <el-date-picker
                    v-model="userDateRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                  ></el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="Search" @click="handleUserSearch">搜索</el-button>
                <el-button icon="Refresh" @click="resetUserQuery">重置</el-button>
              </el-form-item>
            </el-form>
            <div class="toolbar-wireframe">
              <div class="actions-group">
                <el-button type="primary" plain icon="Plus" @click="handleUserAdd">新增</el-button>
                <el-button type="success" plain icon="Edit" :disabled="selectedUsers.length !== 1" @click="handleUserUpdate()">修改</el-button>
                <el-button type="danger" plain icon="Delete" :disabled="selectedUsers.length === 0" @click="handleUserDelete()">删除</el-button>
                <el-button type="info" plain icon="Upload">导入</el-button>
                <el-button type="warning" plain icon="Download">导出</el-button>
              </div>
              <div class="actions-group">
                <el-button icon="Refresh" @click="handleUserSearch">刷新</el-button>
              </div>
            </div>
            <div class="table-container">
              <el-table :data="userList" v-loading="userLoading" @selection-change="handleSelectionChange" border>
                <el-table-column type="selection" width="55" align="center" />
                <el-table-column prop="userId" label="用户编号" align="center"></el-table-column>
                <el-table-column prop="username" label="用户名称"></el-table-column>
                <el-table-column prop="nickname" label="用户昵称"></el-table-column>
                <el-table-column label="部门" :formatter="row => row.dept ? row.dept.deptName : '无'"></el-table-column>
                <el-table-column prop="phone" label="手机号码"></el-table-column>
                <el-table-column prop="status" label="状态" align="center">
                  <template #default="{ row }">
                    <el-tag :type="row.status === 0 ? 'success' : 'danger'">{{ row.status === 0 ? '正常' : '停用' }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
                <el-table-column label="操作" width="180" align="center">
                  <template #default="{ row }">
                    <el-button
                      type="text"
                      icon="Edit"
                      @click="handleUserUpdate(row)"
                    >修改</el-button>
                    <el-button
                      type="text"
                      icon="Delete"
                      @click="handleUserDelete(row)"
                    >删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <div class="pagination-container" v-show="userTotal > 0">
              <el-pagination
                :total="userTotal"
                v-model:current-page="userQueryParams.current"
                v-model:page-size="userQueryParams.size"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="loadUserList"
                @current-change="loadUserList"
              />
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="🔑 角色管理" name="role">
          <div class="tab-content">
            <el-form :inline="true" :model="roleQueryParams" class="search-form-wireframe">
              <el-form-item label="角色名称">
                <el-input v-model="roleQueryParams.roleName" placeholder="请输入角色名称" clearable></el-input>
              </el-form-item>
              <el-form-item label="权限字符">
                <el-input v-model="roleQueryParams.permission" placeholder="请输入权限字符" clearable></el-input>
              </el-form-item>
              <el-form-item label="状态">
                <el-select v-model="roleQueryParams.status" placeholder="角色状态" clearable>
                  <el-option label="正常" :value="0"></el-option>
                  <el-option label="停用" :value="1"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="创建时间">
                 <el-date-picker
                    v-model="roleDateRange"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                  ></el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="Search" @click="handleRoleSearch">搜索</el-button>
                <el-button icon="Refresh" @click="resetRoleQuery">重置</el-button>
              </el-form-item>
            </el-form>

            <div class="toolbar-wireframe">
               <div class="actions-group">
                <el-button type="primary" plain icon="Plus" @click="handleRoleAdd">新增</el-button>
                <el-button type="success" plain icon="Edit" :disabled="selectedRoles.length !== 1" @click="handleRoleUpdate()">修改</el-button>
                <el-button type="danger" plain icon="Delete" :disabled="selectedRoles.length === 0" @click="handleRoleDelete()">删除</el-button>
                <el-button type="warning" plain icon="Download">导出</el-button>
              </div>
              <div class="actions-group">
                <el-button icon="Refresh" @click="handleRoleSearch">刷新</el-button>
              </div>
            </div>

            <div class="table-container">
            <el-table :data="roleList" v-loading="roleLoading" @selection-change="handleRoleSelectionChange" border>
              <el-table-column type="selection" width="55" align="center" />
              <el-table-column prop="roleId" label="角色编号" align="center"></el-table-column>
              <el-table-column prop="roleName" label="角色名称"></el-table-column>
              <el-table-column prop="permission" label="权限字符"></el-table-column>
              <el-table-column prop="status" label="状态" align="center">
                <template #default="{ row }">
                  <el-tag :type="row.status === 0 ? 'success' : 'danger'">{{ row.status === 0 ? '正常' : '停用' }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="180" align="center" />
              <el-table-column label="操作" width="130" align="center" fixed="right">
                <template #default="{ row }">
                  <el-button type="text" icon="Edit" @click="handleRoleUpdate(row)">修改</el-button>
                  <el-button type="text" icon="Delete" @click="handleRoleDelete(row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            </div>

            <div class="pagination-container" v-show="roleTotal > 0">
              <el-pagination
                :total="roleTotal"
                v-model:current-page="roleQueryParams.current"
                v-model:page-size="roleQueryParams.size"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="loadRoleList"
                @current-change="loadRoleList"
              />
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="📋 菜单管理" name="menu">
           <div class="tab-content">
            <el-form :inline="true" :model="menuQueryParams" class="search-form-wireframe">
              <el-form-item label="菜单名称">
                <el-input v-model="menuQueryParams.menuName" placeholder="请输入菜单名称" clearable></el-input>
              </el-form-item>
               <el-form-item label="状态">
                <el-select v-model="menuQueryParams.status" placeholder="菜单状态" clearable>
                  <el-option label="正常" :value="0"></el-option>
                  <el-option label="停用" :value="1"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="Search" @click="handleMenuSearch">搜索</el-button>
                <el-button icon="Refresh" @click="resetMenuQuery">重置</el-button>
              </el-form-item>
            </el-form>

             <div class="toolbar-wireframe">
               <div class="actions-group">
                <el-button type="primary" plain icon="Plus" @click="handleMenuAdd(null)">新增</el-button>
              </div>
              <div class="actions-group">
                <el-button icon="Refresh" @click="handleMenuSearch">刷新</el-button>
              </div>
            </div>

            <div class="table-container">
            <el-table :data="menuList" v-loading="menuLoading" border row-key="menuId" default-expand-all>
              <el-table-column prop="menuName" label="菜单名称" width="200"></el-table-column>
              <el-table-column prop="icon" label="图标" width="80" align="center"></el-table-column>
              <el-table-column prop="orderNum" label="排序" width="80" align="center"></el-table-column>
              <el-table-column prop="perms" label="权限标识"></el-table-column>
              <el-table-column prop="component" label="组件路径"></el-table-column>
              <el-table-column prop="status" label="状态" align="center">
                <template #default="{ row }">
                  <el-tag :type="row.status === 0 ? 'success' : 'danger'">{{ row.status === 0 ? '正常' : '停用' }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="180" align="center">
                <template #default="{ row }">
                  <el-button type="text" icon="Edit" @click="handleMenuUpdate(row)">修改</el-button>
                  <el-button type="text" icon="Plus" @click="handleMenuAdd(row)">新增</el-button>
                  <el-button type="text" icon="Delete" @click="handleMenuDelete(row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="🏢 部门管理" name="dept">
           <div class="tab-content">
            <el-form :inline="true" :model="deptQueryParams" class="search-form-wireframe">
              <el-form-item label="部门名称">
                <el-input v-model="deptQueryParams.deptName" placeholder="请输入部门名称" clearable></el-input>
              </el-form-item>
              <el-form-item label="状态">
                 <el-select v-model="deptQueryParams.status" placeholder="部门状态" clearable>
                  <el-option label="正常" :value="0"></el-option>
                  <el-option label="停用" :value="1"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="Search" @click="handleDeptSearch">搜索</el-button>
                <el-button icon="Refresh" @click="resetDeptQuery">重置</el-button>
              </el-form-item>
            </el-form>
             <div class="toolbar-wireframe">
              <div class="actions-group">
                <el-button type="primary" plain icon="Plus" @click="handleDeptAdd(null)">新增</el-button>
              </div>
              <div class="actions-group">
                <el-button icon="Refresh" @click="handleDeptSearch">刷新</el-button>
              </div>
            </div>
            <div class="table-container">
            <el-table :data="deptList" v-loading="deptLoading" border row-key="deptId" default-expand-all>
              <el-table-column prop="deptName" label="部门名称" width="250"></el-table-column>
              <el-table-column prop="orderNum" label="排序" width="100" align="center"></el-table-column>
              <el-table-column prop="status" label="状态" align="center">
                <template #default="{ row }">
                  <el-tag :type="row.status === 0 ? 'success' : 'danger'">{{ row.status === 0 ? '正常' : '停用' }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
              <el-table-column label="操作" align="center">
                <template #default="{ row }">
                  <el-button type="text" icon="Edit" @click="handleDeptUpdate(row)">修改</el-button>
                  <el-button type="text" icon="Plus" @click="handleDeptAdd(row)">新增</el-button>
                  <el-button type="text" icon="Delete" @click="handleDeptDelete(row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 用户新增/修改对话框 -->
    <el-dialog :title="userDialogTitle" v-model="userDialogVisible" width="600px" append-to-body @close="cancelUserForm">
      <el-form ref="userFormRef" :model="userForm" :rules="userFormRules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户昵称" prop="nickname">
              <el-input v-model="userForm.nickname" placeholder="请输入用户昵称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="归属部门" prop="deptId">
              <el-tree-select
                v-model="userForm.deptId"
                :data="deptOptions"
                :props="{ value: 'deptId', label: 'deptName', children: 'children' }"
                value-key="deptId"
                placeholder="请选择归属部门"
                check-strictly
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phone">
              <el-input v-model="userForm.phone" placeholder="请输入手机号码" maxlength="11" />
            </el-form-item>
          </el-col>
           <el-col :span="12">
            <el-form-item label="用户名称" prop="username">
              <el-input v-model="userForm.username" placeholder="请输入用户名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="!userForm.userId">
          <el-col :span="12">
            <el-form-item label="用户密码" prop="password">
              <el-input v-model="userForm.password" placeholder="请输入用户密码" type="password" show-password />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
             <el-form-item label="状态" prop="status">
               <el-radio-group v-model="userForm.status">
                 <el-radio :label="0">正常</el-radio>
                 <el-radio :label="1">停用</el-radio>
               </el-radio-group>
             </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="userForm.remark" type="textarea" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitUserForm">确 定</el-button>
          <el-button @click="cancelUserForm">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 角色新增/修改对话框 -->
    <el-dialog :title="roleDialogTitle" v-model="roleDialogVisible" width="500px" append-to-body @close="cancelRoleForm">
      <el-form ref="roleFormRef" :model="roleForm" :rules="roleFormRules" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="权限字符" prop="permission">
          <el-input v-model="roleForm.permission" placeholder="请输入权限字符" />
        </el-form-item>
         <el-form-item label="状态" prop="status">
           <el-radio-group v-model="roleForm.status">
             <el-radio :label="0">正常</el-radio>
             <el-radio :label="1">停用</el-radio>
           </el-radio-group>
         </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="roleForm.remark" type="textarea" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitRoleForm">确 定</el-button>
          <el-button @click="cancelRoleForm">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 部门新增/修改对话框 -->
    <el-dialog :title="deptDialogTitle" v-model="deptDialogVisible" width="600px" append-to-body @close="cancelDeptForm">
      <el-form ref="deptFormRef" :model="deptForm" :rules="deptFormRules" label-width="80px">
        <el-form-item label="上级部门" prop="parentId">
          <el-tree-select
            v-model="deptForm.parentId"
            :data="deptList"
            :props="{ value: 'deptId', label: 'deptName', children: 'children' }"
            value-key="deptId"
            placeholder="选择上级部门"
            check-strictly
          />
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="deptForm.deptName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="显示排序" prop="orderNum">
          <el-input-number v-model="deptForm.orderNum" controls-position="right" :min="0" />
        </el-form-item>
         <el-form-item label="状态" prop="status">
           <el-radio-group v-model="deptForm.status">
             <el-radio :label="0">正常</el-radio>
             <el-radio :label="1">停用</el-radio>
           </el-radio-group>
         </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitDeptForm">确 定</el-button>
          <el-button @click="cancelDeptForm">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 菜单新增/修改对话框 -->
    <el-dialog :title="menuDialogTitle" v-model="menuDialogVisible" width="680px" append-to-body @close="cancelMenuForm">
      <el-form ref="menuFormRef" :model="menuForm" :rules="menuFormRules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="上级菜单">
              <el-tree-select
                v-model="menuForm.parentId"
                :data="menuList"
                :props="{ value: 'menuId', label: 'menuName', children: 'children' }"
                value-key="menuId"
                placeholder="选择上级菜单"
                check-strictly
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="菜单类型" prop="menuType">
              <el-radio-group v-model="menuForm.menuType">
                <el-radio label="M">目录</el-radio>
                <el-radio label="C">菜单</el-radio>
                <el-radio label="F">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="菜单名称" prop="menuName">
              <el-input v-model="menuForm.menuName" placeholder="请输入菜单名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="menuForm.menuType !== 'F'">
            <el-form-item label="路由地址" prop="path">
              <el-input v-model="menuForm.path" placeholder="请输入路由地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="menuForm.menuType === 'C'">
            <el-form-item label="组件路径" prop="component">
              <el-input v-model="menuForm.component" placeholder="请输入组件路径" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="显示排序" prop="orderNum">
              <el-input-number v-model="menuForm.orderNum" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
           <el-col :span="12" v-if="menuForm.menuType !== 'M'">
            <el-form-item label="权限标识" prop="perms">
              <el-input v-model="menuForm.perms" placeholder="请输入权限标识" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
       <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitMenuForm">确 定</el-button>
          <el-button @click="cancelMenuForm">取 消</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, getCurrentInstance, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDeptTree, addDept, updateDept, deleteDept } from '../api/dept'
import { getUserList, addUser, updateUser, deleteUser } from '../api/user'
import { getRoleList, addRole, updateRole, deleteRole } from '../api/role'
import { getMenuTree, addMenu, updateMenu, deleteMenu } from '../api/menu'

const activeTab = ref('user')
const { proxy } = getCurrentInstance()

// 用户管理
const userList = ref([])
const userLoading = ref(false)
const userTotal = ref(0)
const selectedUsers = ref([])
const userDateRange = ref([])
const userQueryParams = ref({
  current: 1,
  size: 10,
  username: '',
  phone: '',
  status: null
})

// 用户表单
const userDialogVisible = ref(false)
const userDialogTitle = ref('')
const userFormRef = ref(null)

const defaultUserForm = {
  userId: undefined,
  username: '',
  nickname: '',
  password: '',
  deptId: undefined,
  phone: '',
  status: 0,
  remark: ''
}
const userForm = ref({...defaultUserForm})

const userFormRules = ref({
  username: [{ required: true, message: '用户名称不能为空', trigger: 'blur' }],
  nickname: [{ required: true, message: '用户昵称不能为空', trigger: 'blur' }],
  password: [{ required: true, message: '用户密码不能为空', trigger: 'blur' }],
  deptId: [{ required: true, message: '归属部门不能为空', trigger: 'change' }],
  permission: [{ required: true, message: '权限字符不能为空', trigger: 'blur' }]
})

// For user form department selection
const deptOptions = ref([])

function handleSelectionChange(selection) {
  selectedUsers.value = selection
}

function handleEditUser(row) {
  handleUserUpdate(row)
}

function handleUserSearch() {
  userQueryParams.value.current = 1
  loadUserList()
}

function resetUserQuery() {
  userQueryParams.value = {
    current: 1,
    size: 10,
    username: '',
    phone: '',
    status: null
  }
  userDateRange.value = []
  loadUserList()
}

function loadUserList() {
  userLoading.value = true
  getUserList(userQueryParams.value).then(res => {
    if (res.code === 200) {
      userList.value = res.data.records || []
      userTotal.value = res.data.total || 0
    } else {
      console.error('获取用户列表失败:', res.message)
    }
  }).catch(error => {
    console.error('获取用户列表错误:', error)
  }).finally(() => {
    userLoading.value = false
  })
}

// 角色管理
const roleList = ref([])
const roleLoading = ref(false)
const roleTotal = ref(0)
const selectedRoles = ref([])
const roleDateRange = ref([])
const roleQueryParams = ref({
  current: 1,
  size: 10,
  roleName: '',
  permission: '',
  status: undefined
})

const roleDialogVisible = ref(false)
const roleDialogTitle = ref('')
const roleFormRef = ref(null)
const defaultRoleForm = {
  roleId: undefined,
  roleName: '',
  permission: '',
  status: 0,
  remark: ''
}
const roleForm = ref({...defaultRoleForm})
const roleFormRules = ref({
  roleName: [{ required: true, message: '角色名称不能为空', trigger: 'blur' }],
  permission: [{ required: true, message: '权限字符不能为空', trigger: 'blur' }]
})

function handleRoleSelectionChange(selection) {
  selectedRoles.value = selection
}

function handleRoleSearch() {
  roleQueryParams.value.current = 1
  loadRoleList()
}

function resetRoleQuery() {
  roleQueryParams.value = {
    current: 1,
    size: 10,
    roleName: '',
    permission: '',
    status: undefined
  }
  roleDateRange.value = []
  loadRoleList()
}

function resetRoleForm() {
  roleForm.value = {...defaultRoleForm}
  if(roleFormRef.value) {
    roleFormRef.value.resetFields()
  }
}

function cancelRoleForm() {
  roleDialogVisible.value = false
  resetRoleForm()
}

function handleRoleAdd() {
  resetRoleForm()
  roleDialogTitle.value = '新增角色'
  roleDialogVisible.value = true
}

function handleRoleUpdate(row) {
  resetRoleForm()
  const roleToUpdate = row || selectedRoles.value[0]
  roleForm.value = { ...roleToUpdate }
  roleDialogTitle.value = '修改角色'
  roleDialogVisible.value = true
}

function submitRoleForm() {
  roleFormRef.value.validate(valid => {
    if (valid) {
      const action = roleForm.value.roleId ? updateRole : addRole
      action(roleForm.value).then(() => {
        ElMessage.success(roleForm.value.roleId ? '修改成功' : '新增成功')
        roleDialogVisible.value = false
        loadRoleList()
      })
    }
  })
}

function handleRoleDelete(row) {
  const roleIds = row ? [row.roleId] : selectedRoles.value.map(r => r.roleId)
  ElMessageBox.confirm(
    `是否确认删除角色编号为"${roleIds.join(',')}"的数据项？`,
    '系统提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    const deletePromises = roleIds.map(id => deleteRole(id))
    Promise.all(deletePromises).then(() => {
        ElMessage.success('删除成功')
        loadRoleList()
    }).catch((e) => {
        console.error(e)
        ElMessage.error('删除失败')
    });
  }).catch(() => {})
}

function loadRoleList() {
  roleLoading.value = true
  getRoleList(roleQueryParams.value).then(res => {
    if (res.code === 200) {
      roleList.value = res.data.records || []
      roleTotal.value = res.data.total || 0
    } else {
      console.error('获取角色列表失败:', res.message)
    }
  }).catch(error => {
    console.error('获取角色列表错误:', error)
  }).finally(() => {
    roleLoading.value = false
  })
}

// 菜单管理
const menuList = ref([])
const menuLoading = ref(false)
const menuQueryParams = ref({
  menuName: '',
  status: undefined
})

const menuDialogVisible = ref(false)
const menuDialogTitle = ref('')
const menuFormRef = ref(null)
const defaultMenuForm = {
    menuId: undefined,
    parentId: 0,
    menuName: '',
    menuType: 'M',
    path: '',
    component: '',
    perms: '',
    orderNum: 0,
    status: 0
}
const menuForm = ref({...defaultMenuForm})
const menuFormRules = ref({
    menuName: [{ required: true, message: '菜单名称不能为空', trigger: 'blur' }],
    orderNum: [{ required: true, message: '显示排序不能为空', trigger: 'blur' }],
    path: [{ required: true, message: '路由地址不能为空', trigger: 'blur' }]
})

function handleMenuSearch() {
  loadMenuList()
}

function resetMenuQuery() {
  menuQueryParams.value = {
    menuName: '',
    status: undefined
  }
  loadMenuList()
}

function loadMenuList() {
  menuLoading.value = true
  getMenuTree(menuQueryParams.value).then(res => {
    if (res.code === 200) {
      // 客户端过滤状态，因为后端不支持
      if (menuQueryParams.value.status !== undefined && menuQueryParams.value.status !== null && menuQueryParams.value.status !== '') {
         // This is a simplified filter for a tree. A real implementation would be recursive.
         menuList.value = res.data.filter(menu => menu.status == menuQueryParams.value.status)
      } else {
        menuList.value = res.data || []
      }
    } else {
      console.error('获取菜单列表失败:', res.message)
    }
  }).catch(error => {
    console.error('获取菜单列表错误:', error)
  }).finally(() => {
    menuLoading.value = false
  })
}

function resetMenuForm() {
  menuForm.value = {...defaultMenuForm};
  if (menuFormRef.value) {
    menuFormRef.value.resetFields();
  }
}

function cancelMenuForm() {
  menuDialogVisible.value = false;
  resetMenuForm();
}

function handleMenuAdd(row) {
  resetMenuForm();
  if (row && row.menuId) {
    menuForm.value.parentId = row.menuId;
  } else {
    menuForm.value.parentId = 0;
  }
  menuDialogTitle.value = '新增菜单';
  menuDialogVisible.value = true;
}

function handleMenuUpdate(row) {
  resetMenuForm();
  // Deep copy row to avoid table mutation
  const rowData = JSON.parse(JSON.stringify(row));
  // Remove children to avoid circular reference issues in the form
  delete rowData.children;
  menuForm.value = rowData;
  menuDialogTitle.value = '修改菜单';
  menuDialogVisible.value = true;
}

function submitMenuForm() {
  menuFormRef.value.validate(valid => {
    if (valid) {
      const action = menuForm.value.menuId ? updateMenu : addMenu;
      action(menuForm.value).then(() => {
        ElMessage.success(menuForm.value.menuId ? '修改成功' : '新增成功');
        menuDialogVisible.value = false;
        loadMenuList();
      });
    }
  });
}

function handleMenuDelete(row) {
    ElMessageBox.confirm(
    `是否确认删除名称为"${row.menuName}"的数据项？`,
    '系统提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    return deleteMenu(row.menuId);
  }).then(() => {
    ElMessage.success('删除成功');
    loadMenuList();
  }).catch(() => {});
}

// 部门管理相关
const deptList = ref([])
const deptLoading = ref(false)
const deptQueryParams = ref({
  deptName: '',
  status: undefined
})

const deptDialogVisible = ref(false)
const deptDialogTitle = ref('')
const deptFormRef = ref(null)
const defaultDeptForm = {
    deptId: undefined,
    parentId: 0,
    deptName: '',
    orderNum: 0,
    status: 0
}
const deptForm = ref({...defaultDeptForm})
const deptFormRules = ref({
    parentId: [{ required: true, message: '上级部门不能为空', trigger: 'change' }],
    deptName: [{ required: true, message: '部门名称不能为空', trigger: 'blur' }],
    orderNum: [{ required: true, message: '显示排序不能为空', trigger: 'blur' }]
})

function handleDeptSearch() {
  loadDeptList();
}

function resetDeptQuery() {
  deptQueryParams.value = {
    deptName: '',
    status: undefined
  };
  loadDeptList();
}

function resetDeptForm() {
    deptForm.value = {...defaultDeptForm};
    if (deptFormRef.value) {
        deptFormRef.value.resetFields();
    }
}

function cancelDeptForm() {
    deptDialogVisible.value = false;
    resetDeptForm();
}

function handleDeptAdd(row) {
    resetDeptForm();
    if (row && row.deptId) {
        deptForm.value.parentId = row.deptId;
    } else {
        deptForm.value.parentId = 0;
    }
    deptDialogTitle.value = '新增部门';
    deptDialogVisible.value = true;
}

function handleDeptUpdate(row) {
    resetDeptForm();
    const rowData = JSON.parse(JSON.stringify(row));
    delete rowData.children;
    deptForm.value = rowData;
    deptDialogTitle.value = '修改部门';
    deptDialogVisible.value = true;
}

function submitDeptForm() {
    deptFormRef.value.validate(valid => {
        if (valid) {
            const action = deptForm.value.deptId ? updateDept : addDept;
            action(deptForm.value).then(() => {
                ElMessage.success(deptForm.value.deptId ? '修改成功' : '新增成功');
                deptDialogVisible.value = false;
                loadDeptList();
            });
        }
    });
}

function handleDeptDelete(row) {
    ElMessageBox.confirm(
    `是否确认删除名称为"${row.deptName}"的数据项？`,
    '系统提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    return deleteDept(row.deptId);
  }).then(() => {
    ElMessage.success('删除成功');
    loadDeptList();
  }).catch(() => {});
}

function loadDeptList() {
  deptLoading.value = true;
  getDeptTree(deptQueryParams.value).then(res => {
    if (res.code === 200) {
      const data = res.data || [];
      if (deptQueryParams.value.deptName || deptQueryParams.value.status !== undefined) {
         const { deptName, status } = deptQueryParams.value;
          const filter = (depts) => {
              let result = [];
              for(const d of depts) {
                  const nameMatch = deptName ? d.deptName.includes(deptName) : true;
                  const statusMatch = status !== undefined ? d.status === status : true;
                  
                  let children = [];
                  if(d.children) {
                      children = filter(d.children);
                  }
                  
                  if((nameMatch && statusMatch) || children.length > 0) {
                      // Create a new node to avoid mutating the original
                      const newNode = {...d, children: children.length > 0 ? children : undefined};
                      result.push(newNode);
                  }
              }
              return result;
          }
          deptList.value = filter(data);
      }
      else {
        deptList.value = data;
      }
    } else {
      ElMessage.error('获取部门列表失败: ' + res.message);
    }
  }).finally(() => {
    deptLoading.value = false;
  });
}

function loadDeptOptions() {
  getDeptTree({}).then(res => {
    if (res.code === 200) {
      deptOptions.value = res.data || [];
    }
  });
}

/** 用户表单相关操作 **/
function resetUserForm() {
  userForm.value = {...defaultUserForm}
  if(userFormRef.value) {
    userFormRef.value.resetFields()
  }
}

function cancelUserForm() {
  userDialogVisible.value = false
  resetUserForm()
}

function handleUserAdd() {
  resetUserForm()
  userDialogTitle.value = '新增用户'
  userDialogVisible.value = true
}

function handleUserUpdate(row) {
  resetUserForm()
  const userToUpdate = row || selectedUsers.value[0]
  
  // 简单拷贝，避免响应式问题
  userForm.value = { ...userToUpdate }
  userForm.value.password = '' // 不显示密码
  
  userDialogTitle.value = '修改用户'
  userDialogVisible.value = true
}

function submitUserForm() {
  userFormRef.value.validate(valid => {
    if (valid) {
      const action = userForm.value.userId ? updateUser : addUser
      action(userForm.value).then(() => {
        ElMessage.success(userForm.value.userId ? '修改成功' : '新增成功')
        userDialogVisible.value = false
        loadUserList()
      })
    }
  })
}

function handleUserDelete(row) {
  const userIds = row ? [row.userId] : selectedUsers.value.map(u => u.userId)
  ElMessageBox.confirm(
    `是否确认删除用户编号为"${userIds.join(',')}"的数据项？`,
    '系统提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    const deletePromises = userIds.map(id => deleteUser(id));
    Promise.all(deletePromises).then(() => {
        ElMessage.success('删除成功')
        loadUserList()
    }).catch((e) => {
        console.error(e)
        ElMessage.error('删除失败')
    });
  }).catch(() => {})
}

onMounted(() => {
  loadUserList()
  loadRoleList()
  loadMenuList()
  loadDeptList()
})

watch(activeTab, (newTab) => {
  switch (newTab) {
    case 'user':
      loadUserList()
      loadDeptOptions()
      break
    case 'role':
      loadRoleList()
      break
    case 'dept':
      loadDeptList()
      break
    case 'menu':
      loadMenuList()
      break
  }
}, { immediate: true })
</script>

<style scoped>
.task-bg {
  background: #f5f5f5;
  padding: 20px;
}

.page-title {
  font-size: 24px;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border: 2px solid #333;
  font-weight: normal;
}

.wireframe {
  display: flex;
  background: #fff;
  border: 2px solid #333;
  min-height: 600px;
}

.system-tabs {
  display: flex;
  width: 100%;
}

.system-tabs :deep(.el-tabs__header.is-left) {
  width: 200px;
  min-width: 200px;
  margin-right: 0;
  border-right: 2px solid #333;
  background: #f8f8f8;
  border-bottom: none;
}

.system-tabs :deep(.el-tabs__nav-wrap.is-left) {
  padding: 0;
}
.system-tabs :deep(.el-tabs__nav.is-left) {
  padding: 0;
}


.system-tabs :deep(.el-tabs__item.is-left) {
  height: auto;
  padding: 15px 20px;
  line-height: 1.5;
  font-size: 16px;
  font-weight: 500;
  text-align: left;
  border-left: 4px solid transparent;
  border-bottom: 1px solid #ddd;
  margin: 0;
  color: #333;
}
.system-tabs :deep(.el-tabs__item.is-left:last-child) {
  border-bottom: none;
}

.system-tabs :deep(.el-tabs__item.is-left.is-active) {
  background-color: #e6f3ff;
  border-left-color: #0066cc;
  font-weight: bold;
}

.system-tabs :deep(.el-tabs__content) {
  flex: 1;
  padding: 20px;
  overflow: auto;
}

.tab-content {
  padding: 0;
}

.search-form-wireframe {
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
  align-items: flex-end;
  padding: 15px;
  margin-bottom: 20px;
  border: 1px solid #666;
  background: #f8f8f8;
}

.search-form-wireframe :deep(.el-form-item) {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 0 !important;
}

.search-form-wireframe :deep(.el-form-item__label) {
  text-align: left;
  font-weight: bold;
  font-size: 12px;
  color: #333;
  line-height: normal;
  padding-bottom: 5px;
  justify-content: flex-start;
}

.search-form-wireframe :deep(.el-form-item__content) {
  margin-left: 0 !important;
}

.toolbar-wireframe {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding: 10px;
  border: 1px solid #666;
  background: #f8f8f8;
  border-radius: 0;
}
.toolbar-wireframe .actions-group {
    display: flex;
    gap: 10px;
}


.table-container {
  border: 1px solid #666;
}
.table-container :deep(.el-table) {
  border: none;
}

.table-container :deep(th.el-table__cell) {
  background-color: #f0f0f0 !important;
  color: #333;
  font-weight: bold;
}
.table-container :deep(td.el-table__cell), .table-container :deep(th.el-table__cell) {
    border-color: #ccc;
}
.table-container :deep(.el-button.is-text) {
    color: #0066cc;
    text-decoration: underline;
    margin-right: 10px;
}


.pagination-container {
    margin-top: 20px;
    padding: 10px;
    border: 1px solid #666;
    background: #f8f8f8;
    display: flex;
    justify-content: flex-end;
}

/* Override button colors to match mockup */
:deep(.el-button--primary.is-plain) {
  background: #e6f3ff;
  border-color: #333;
  color: #333;
}
:deep(.el-button--success.is-plain) {
  background: #d4edda;
   border-color: #333;
  color: #333;
}
:deep(.el-button--danger.is-plain) {
  background: #f8d7da;
   border-color: #333;
  color: #333;
}

</style> 