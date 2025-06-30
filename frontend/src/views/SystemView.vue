<template>
  <div class="system-bg">
    <div class="page-title">系统管理</div>
    <div class="main-container">
      <!-- 左侧菜单 -->
      <div class="sidebar">
        <div :class="['menu-item', activeTab === 'user' ? 'active' : '']" @click="activeTab = 'user'">👤 用户管理</div>
        <div :class="['menu-item', activeTab === 'role' ? 'active' : '']" @click="activeTab = 'role'">🔑 角色管理</div>
        <div :class="['menu-item', activeTab === 'menu' ? 'active' : '']" @click="activeTab = 'menu'">📋 菜单管理</div>
        <div :class="['menu-item', activeTab === 'dept' ? 'active' : '']" @click="activeTab = 'dept'">🏢 部门管理</div>
        <div :class="['menu-item', activeTab === 'config' ? 'active' : '']" @click="activeTab = 'config'">⚙️ 参数配置</div>
      </div>
      <!-- 右侧内容区域 -->
      <div class="content-area">
        <!-- 用户管理 -->
        <div v-show="activeTab === 'user'" class="tab-content active">
          <div class="search-form">
            <div class="form-item"><label>用户名称</label><input type="text" placeholder="请输入用户名称"></div>
            <div class="form-item"><label>手机号码</label><input type="text" placeholder="请输入手机号码"></div>
            <div class="form-item"><label>状态</label><select><option value="">用户状态</option><option value="0">正常</option><option value="1">停用</option></select></div>
            <div class="form-item"><label>创建时间</label><input type="text" placeholder="选择日期范围"></div>
            <div class="search-buttons"><div class="btn btn-primary">搜索</div><div class="btn">重置</div></div>
          </div>
          <div class="toolbar">
            <div style="display: flex; gap: 10px;">
              <div class="btn btn-primary">新增</div>
              <div class="btn btn-success">修改</div>
              <div class="btn btn-danger">删除</div>
              <div class="btn">导入</div>
              <div class="btn">导出</div>
            </div>
            <div class="btn">刷新</div>
          </div>
          <table class="table">
            <thead>
              <tr>
                <th><input type="checkbox"></th>
                <th>用户编号</th>
                <th>用户名称</th>
                <th>用户昵称</th>
                <th>部门</th>
                <th>手机号码</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td><input type="checkbox"></td>
                <td>1</td>
                <td>admin</td>
                <td>超级管理员</td>
                <td>系统部门</td>
                <td>15888888888</td>
                <td><span class="status-tag status-normal">正常</span></td>
                <td>2024-01-01 00:00:00</td>
                <td>
                  <span class="link">修改</span>
                  <span class="link">删除</span>
                  <span class="link">重置密码</span>
                  <span class="link">分配角色</span>
                </td>
              </tr>
              <tr>
                <td><input type="checkbox"></td>
                <td>2</td>
                <td>zhangsan</td>
                <td>张三</td>
                <td>运维部</td>
                <td>13666666666</td>
                <td><span class="status-tag status-normal">正常</span></td>
                <td>2024-02-15 10:30:00</td>
                <td>
                  <span class="link">修改</span>
                  <span class="link">删除</span>
                  <span class="link">重置密码</span>
                  <span class="link">分配角色</span>
                </td>
              </tr>
              <tr>
                <td><input type="checkbox"></td>
                <td>3</td>
                <td>lisi</td>
                <td>李四</td>
                <td>技术部</td>
                <td>13777777777</td>
                <td><span class="status-tag status-disabled">停用</span></td>
                <td>2024-03-01 14:20:00</td>
                <td>
                  <span class="link">修改</span>
                  <span class="link">删除</span>
                  <span class="link">重置密码</span>
                  <span class="link">分配角色</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- 角色管理 -->
        <div v-show="activeTab === 'role'" class="tab-content">
          <div class="search-form">
            <div class="form-item"><label>角色名称</label><input type="text" placeholder="请输入角色名称"></div>
            <div class="form-item"><label>权限字符</label><input type="text" placeholder="请输入权限字符"></div>
            <div class="form-item"><label>状态</label><select><option value="">全部</option><option value="1">正常</option><option value="0">停用</option></select></div>
            <div class="form-item"><label>创建时间</label><input type="text" placeholder="选择日期范围"></div>
            <div class="search-buttons"><div class="btn btn-primary">搜索</div><div class="btn">重置</div></div>
          </div>
          <div class="toolbar">
            <div style="display: flex; gap: 10px;">
              <div class="btn btn-primary">新增</div>
              <div class="btn btn-success">修改</div>
              <div class="btn btn-danger">删除</div>
              <div class="btn">导出</div>
            </div>
            <div class="btn">刷新</div>
          </div>
          <table class="table">
            <thead>
              <tr>
                <th><input type="checkbox"></th>
                <th>角色ID</th>
                <th>角色名称</th>
                <th>权限字符</th>
                <th>显示顺序</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>备注</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td><input type="checkbox"></td>
                <td>1</td>
                <td>超级管理员</td>
                <td>sys:admin</td>
                <td>1</td>
                <td><span class="status-tag status-normal">正常</span></td>
                <td>2024-01-01 00:00:00</td>
                <td>系统最高权限</td>
                <td><span class="link">修改</span><span class="link">删除</span><span class="link">分配权限</span></td>
              </tr>
              <tr>
                <td><input type="checkbox"></td>
                <td>2</td>
                <td>普通用户</td>
                <td>sys:user</td>
                <td>2</td>
                <td><span class="status-tag status-normal">正常</span></td>
                <td>2024-02-01 10:00:00</td>
                <td>普通操作权限</td>
                <td><span class="link">修改</span><span class="link">删除</span><span class="link">分配权限</span></td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- 菜单管理 -->
        <div v-show="activeTab === 'menu'" class="tab-content">
          <div class="search-form">
            <div class="form-item"><label>菜单名称</label><input type="text" placeholder="请输入菜单名称"></div>
            <div class="form-item"><label>类型</label><select><option value="">全部</option><option value="0">目录</option><option value="1">菜单</option><option value="2">按钮</option></select></div>
            <div class="form-item"><label>状态</label><select><option value="">全部</option><option value="1">正常</option><option value="0">停用</option></select></div>
            <div class="search-buttons"><div class="btn btn-primary">搜索</div><div class="btn">重置</div><div class="btn">展开/折叠</div></div>
          </div>
          <div class="toolbar">
            <div style="display: flex; gap: 10px;">
              <div class="btn btn-primary">新增</div>
              <div class="btn btn-success">修改</div>
              <div class="btn btn-danger">删除</div>
            </div>
            <div class="btn">刷新</div>
          </div>
          <table class="table">
            <thead>
              <tr>
                <th>菜单ID</th>
                <th>菜单名称</th>
                <th>类型</th>
                <th>路由路径</th>
                <th>组件路径</th>
                <th>权限字符</th>
                <th>显示顺序</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>1</td>
                <td>系统管理</td>
                <td>目录</td>
                <td>/system</td>
                <td>views/SystemView.vue</td>
                <td>sys:system</td>
                <td>1</td>
                <td><span class="status-tag status-normal">正常</span></td>
                <td>2024-01-01 00:00:00</td>
                <td><span class="link">修改</span><span class="link">删除</span></td>
              </tr>
              <tr>
                <td>2</td>
                <td>用户管理</td>
                <td>菜单</td>
                <td>/system/user</td>
                <td>views/UserView.vue</td>
                <td>sys:user:list</td>
                <td>2</td>
                <td><span class="status-tag status-normal">正常</span></td>
                <td>2024-01-02 10:00:00</td>
                <td><span class="link">修改</span><span class="link">删除</span></td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- 部门管理 -->
        <div v-show="activeTab === 'dept'" class="tab-content">
          <div class="search-form">
            <div class="form-item"><label>部门名称</label><input type="text" placeholder="请输入部门名称" v-model="deptNameFilter"></div>
            <div class="form-item"><label>状态</label><select v-model="deptStatusFilter"><option value="">全部</option><option value="1">正常</option><option value="0">停用</option></select></div>
            <div class="search-buttons"><div class="btn btn-primary" @click="loadDeptTree">搜索</div><div class="btn" @click="() => {deptNameFilter='';deptStatusFilter='';loadDeptTree()}">重置</div><div class="btn">展开/折叠</div></div>
          </div>
          <div class="toolbar">
            <div style="display: flex; gap: 10px;">
              <div class="btn btn-primary" @click="handleDeptAdd">新增</div>
              <div class="btn btn-success">修改</div>
              <div class="btn btn-danger">删除</div>
            </div>
            <div class="btn" @click="loadDeptTree">刷新</div>
          </div>
          <table class="table">
            <thead>
              <tr>
                <th>部门ID</th>
                <th>部门名称</th>
                <th>父部门</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="dept in deptList" :key="dept.deptId">
                <td>{{ dept.deptId }}</td>
                <td>{{ dept.deptName }}</td>
                <td>{{ dept.parentId === 0 ? '-' : dept.parentId }}</td>
                <td><span :class="['status-tag', dept.status === 1 ? 'status-normal' : 'status-disabled']">{{ dept.status === 1 ? '正常' : '停用' }}</span></td>
                <td>{{ dept.createTime }}</td>
                <td>
                  <span class="link" @click="handleDeptEdit(dept)">修改</span>
                  <span class="link" @click="handleDeptDelete(dept)">删除</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- 参数配置 -->
        <div v-show="activeTab === 'config'" class="tab-content">
          <div class="search-form">
            <div class="form-item"><label>参数名称</label><input type="text" placeholder="请输入参数名称"></div>
            <div class="form-item"><label>参数键名</label><input type="text" placeholder="请输入参数键名"></div>
            <div class="form-item"><label>系统内置</label><select><option value="">全部</option><option value="Y">是</option><option value="N">否</option></select></div>
            <div class="search-buttons"><div class="btn btn-primary">搜索</div><div class="btn">重置</div></div>
          </div>
          <div class="toolbar">
            <div style="display: flex; gap: 10px;">
              <div class="btn btn-primary">新增</div>
              <div class="btn btn-success">修改</div>
              <div class="btn btn-danger">删除</div>
              <div class="btn">导出</div>
              <div class="btn">刷新缓存</div>
            </div>
            <div class="btn">刷新</div>
          </div>
          <table class="table">
            <thead>
              <tr>
                <th><input type="checkbox"></th>
                <th>参数ID</th>
                <th>参数名称</th>
                <th>参数键名</th>
                <th>参数键值</th>
                <th>系统内置</th>
                <th>备注</th>
                <th>创建时间</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td><input type="checkbox"></td>
                <td>1</td>
                <td>主框架页-默认皮肤样式名称</td>
                <td>sys.index.skinName</td>
                <td>skin-blue</td>
                <td>是</td>
                <td>蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow</td>
                <td>2024-01-01 00:00:00</td>
                <td><span class="link">修改</span><span class="link">删除</span></td>
              </tr>
              <tr>
                <td><input type="checkbox"></td>
                <td>2</td>
                <td>用户管理-账号初始密码</td>
                <td>sys.user.initPassword</td>
                <td>123456</td>
                <td>是</td>
                <td>初始化密码 123456</td>
                <td>2024-01-01 00:00:00</td>
                <td><span class="link">修改</span><span class="link">删除</span></td>
              </tr>
            </tbody>
          </table>
        </div>
        <div style="margin-top: 20px; text-align: right; border: 1px solid #666; padding: 10px; background: #f8f8f8;">
          分页组件: 共 50 条记录，每页 10 条 [1] [2] [3] ... [5]
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDeptTree, addDept, updateDept, deleteDept } from '../api/dept'
const activeTab = ref('user')

// 部门管理相关
const deptList = ref([])
const deptLoading = ref(false)
const deptNameFilter = ref('')
const deptStatusFilter = ref('')

function loadDeptTree() {
  deptLoading.value = true
  getDeptTree({ deptName: deptNameFilter.value }).then(res => {
    if (res.code === 200) {
      deptList.value = res.data || []
    } else {
      console.error('获取部门列表失败:', res.message)
    }
  }).catch(error => {
    console.error('获取部门列表错误:', error)
  }).finally(() => {
    deptLoading.value = false
  })
}

function handleDeptAdd() {
  // 示例：弹窗/表单省略，直接添加
  const name = prompt('请输入新部门名称')
  if (name) {
    addDept({ deptName: name, parentId: 0, status: 1 }).then(res => {
      if (res.code === 200) {
        loadDeptTree()
      } else {
        console.error('添加部门失败:', res.message)
      }
    }).catch(error => {
      console.error('添加部门错误:', error)
    })
  }
}

function handleDeptEdit(dept) {
  const name = prompt('修改部门名称', dept.deptName)
  if (name && name !== dept.deptName) {
    updateDept({ ...dept, deptName: name }).then(res => {
      if (res.code === 200) {
        loadDeptTree()
      } else {
        console.error('更新部门失败:', res.message)
      }
    }).catch(error => {
      console.error('更新部门错误:', error)
    })
  }
}

function handleDeptDelete(dept) {
  if (confirm('确定删除该部门？')) {
    deleteDept(dept.deptId).then(res => {
      if (res.code === 200) {
        loadDeptTree()
      } else {
        console.error('删除部门失败:', res.message)
      }
    }).catch(error => {
      console.error('删除部门错误:', error)
    })
  }
}

onMounted(() => {
  loadDeptTree()
})
</script>

<style scoped>
.system-bg {
  min-height: 100vh;
  background: #f5f5f5;
  padding: 20px;
}
.page-title {
  font-size: 24px;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border: 2px solid #333;
}
.main-container {
  display: flex;
  gap: 0;
  border: 2px solid #333;
  background: white;
  min-height: 600px;
}
.sidebar {
  width: 200px;
  background: #f8f8f8;
  border-right: 2px solid #333;
}
.menu-item {
  padding: 15px 20px;
  border-bottom: 1px solid #ddd;
  cursor: pointer;
  background: #f8f8f8;
  font-weight: 500;
}
.menu-item.active {
  background: #e6f3ff;
  font-weight: bold;
  border-left: 4px solid #0066cc;
}
.menu-item:hover {
  background: #e6f3ff;
}
.content-area {
  flex: 1;
  padding: 20px;
}
.tab-content {
  display: none;
}
.tab-content.active,
.tab-content[style*="display: block"] {
  display: block;
}
.search-form {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #666;
  background: #f8f8f8;
  flex-wrap: wrap;
}
.form-item {
  display: flex;
  flex-direction: column;
  gap: 5px;
}
.form-item label {
  font-weight: bold;
  font-size: 12px;
}
.form-item input,
.form-item select {
  border: 1px solid #999;
  padding: 8px;
  width: 120px;
}
.search-buttons {
  display: flex;
  gap: 10px;
  align-items: end;
}
.btn {
  border: 1px solid #333;
  padding: 8px 15px;
  background: #f0f0f0;
  cursor: pointer;
}
.btn-primary {
  background: #e6f3ff;
}
.btn-success {
  background: #d4edda;
}
.btn-danger {
  background: #f8d7da;
}
.toolbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  padding: 10px;
  border: 1px solid #666;
  background: #f8f8f8;
}
.table {
  width: 100%;
  border-collapse: collapse;
  border: 1px solid #666;
}
.table th,
.table td {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: left;
}
.table th {
  background: #f0f0f0;
  font-weight: bold;
}
.table tbody tr:nth-child(even) {
  background: #f9f9f9;
}
.link {
  color: #0066cc;
  text-decoration: underline;
  cursor: pointer;
  margin-right: 10px;
}
.status-tag {
  padding: 4px 8px;
  border-radius: 3px;
  font-size: 12px;
  display: inline-block;
}
.status-normal {
  background: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}
.status-disabled {
  background: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}
.tree-view {
  border: 1px solid #666;
  background: #f9f9f9;
  padding: 15px;
  max-height: 400px;
  overflow-y: auto;
}
.tree-node {
  margin: 5px 0;
  padding: 5px;
  border: 1px solid #ddd;
  background: white;
}
.tree-node.parent {
  font-weight: bold;
  background: #e6f3ff;
}
.tree-node.child {
  margin-left: 30px;
  background: #f8f9fa;
}
</style> 