<template>
  <div class="task-bg">
    <div class="page-title">任务管理</div>
    <div class="wireframe">
      <!-- 视图切换标签 -->
      <div class="tabs">
        <div :class="['tab', viewType === 'list' ? 'active' : '']" @click="viewType = 'list'">列表视图</div>
        <div :class="['tab', viewType === 'calendar' ? 'active' : '']" @click="viewType = 'calendar'">日历视图</div>
      </div>

      <!-- 列表视图 -->
      <div v-show="viewType === 'list'" class="list-view">
        <!-- 搜索表单 -->
        <el-form :model="searchForm" inline class="search-form" :rules="searchRules" ref="searchFormRef">
          <el-form-item label="任务编号" prop="taskNo">
            <el-input 
              v-model="searchForm.taskNo" 
              placeholder="请输入任务编号" 
              clearable 
              style="width: 150px"
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="任务名称" prop="taskName">
            <el-input 
              v-model="searchForm.taskName" 
              placeholder="请输入任务名称" 
              clearable 
              style="width: 150px"
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="起始地点" prop="startPoint">
            <el-input 
              v-model="searchForm.startPoint" 
              placeholder="请输入起始地点" 
              clearable 
              style="width: 150px"
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="任务状态" prop="status">
            <el-select 
              v-model="searchForm.status" 
              placeholder="请选择任务状态" 
              clearable 
              style="width: 150px"
            >
              <el-option label="未开始" :value="0" />
              <el-option label="进行中" :value="1" />
              <el-option label="已完成" :value="2" />
              <el-option label="已暂停" :value="3" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch" :loading="loading">
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
            <el-button @click="toggleAdvancedSearch" type="text">
              {{ showAdvancedSearch ? '收起' : '高级搜索' }}
              <el-icon><ArrowDown v-if="!showAdvancedSearch" /><ArrowUp v-else /></el-icon>
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 高级搜索 -->
        <el-collapse-transition>
          <div v-show="showAdvancedSearch" class="advanced-search">
            <el-form :model="advancedSearchForm" inline>
              <el-form-item label="执行时间">
                <el-date-picker
                  v-model="advancedSearchForm.dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  style="width: 250px"
                />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleAdvancedSearch" :loading="loading">
                  高级搜索
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-collapse-transition>

        <!-- 搜索状态显示 -->
        <div v-if="hasSearchConditions" class="search-status">
          <el-tag 
            v-for="(condition, key) in activeSearchConditions" 
            :key="key"
            closable
            @close="removeSearchCondition(key)"
            style="margin-right: 8px; margin-bottom: 8px;"
          >
            {{ condition.label }}: {{ condition.value }}
          </el-tag>
          <el-button type="text" @click="handleReset" style="margin-left: 8px;">
            清除所有条件
          </el-button>
        </div>

        <!-- 工具栏 -->
        <div class="toolbar">
          <div style="display: flex; gap: 10px;">
            <el-button type="primary" @click="handleAdd" v-if="hasPermission(['admin', 'operator'])">
              <el-icon><Plus /></el-icon>
              新增任务
            </el-button>
            <el-button @click="handleExport" :disabled="taskList.length === 0">
              <el-icon><Download /></el-icon>
              导出
            </el-button>
            <el-button @click="handleRefresh">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </div>
          <div class="toolbar-info">
            <span>共找到 {{ pagination.total }} 条记录</span>
          </div>
        </div>

        <!-- 任务列表表格 -->
        <el-table 
          :data="taskList" 
          v-loading="loading" 
          border 
          stripe 
          style="width: 100%"
          empty-text="暂无数据"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="taskNo" label="任务编号" width="120" />
          <el-table-column prop="taskName" label="任务名称" min-width="150" />
          <el-table-column prop="startPoint" label="起始地点" width="120" />
          <el-table-column prop="distance" label="任务距离(km)" width="100">
            <template #default="{ row }">
              {{ row.distance ? row.distance.toFixed(2) : '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="lastExecuteTime" label="最后执行时间" width="160">
            <template #default="{ row }">
              {{ formatDateTime(row.lastExecuteTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="finishTime" label="完成时间" width="160">
            <template #default="{ row }">
              {{ formatDateTime(row.finishTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="uploadTime" label="上传时间" width="160">
            <template #default="{ row }">
              {{ formatDateTime(row.uploadTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" align="center">
            <template #default="{ row }">
              <el-button type="text" @click="handleView(row)">查看</el-button>
              <template v-if="hasPermission(['admin', 'operator'])">
                <el-button type="text" @click="handleEdit(row)">编辑</el-button>
                <el-button type="text" @click="handleDelete(row)" style="color: #f56c6c;">删除</el-button>
              </template>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页组件 -->
        <div class="pagination-container">
          <el-pagination
            v-model="pagination.page"
            :page-size="pagination.size"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>

      <!-- 日历视图 -->
      <div v-show="viewType === 'calendar'" class="calendar-view">
        <div class="calendar-toolbar">
          <h3>{{ currentYear }}年{{ currentMonth }}月 任务日历</h3>
          <div class="calendar-nav">
            <el-button @click="previousMonth" size="small">
              <el-icon><ArrowLeft /></el-icon>
              上月
            </el-button>
            <el-button @click="goToToday" size="small">今日</el-button>
            <el-button @click="nextMonth" size="small">
              下月
              <el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
        </div>
        <div class="calendar-grid">
          <div class="calendar-header">
            <div class="calendar-header-cell" v-for="day in weekdays" :key="day">{{ day }}</div>
          </div>
          <div class="calendar-body">
            <div 
              v-for="day in calendarDays" 
              :key="day.date" 
              class="calendar-cell"
              :class="{ 'today': day.isToday }"
            >
              <div class="date-number">{{ day.dayNumber }}</div>
              <div class="day-tasks">
                <div 
                  v-for="task in day.tasks" 
                  :key="task.taskId"
                  class="task-event"
                  @click="handleView(task)"
                >
                  {{ task.taskName }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 任务详情对话框 -->
    <el-dialog v-model="detailVisible" title="任务详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="任务编号">{{ detailTask.taskNo }}</el-descriptions-item>
        <el-descriptions-item label="任务名称">{{ detailTask.taskName }}</el-descriptions-item>
        <el-descriptions-item label="起始地点">{{ detailTask.startPoint }}</el-descriptions-item>
        <el-descriptions-item label="任务距离">{{ detailTask.distance }} km</el-descriptions-item>
        <el-descriptions-item label="任务状态">
          <el-tag :type="getStatusType(detailTask.status)">
            {{ getStatusText(detailTask.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建人ID">{{ detailTask.creatorId }}</el-descriptions-item>
        <el-descriptions-item label="执行人ID">{{ detailTask.executorId }}</el-descriptions-item>
        <el-descriptions-item label="最后执行时间">{{ formatDateTime(detailTask.lastExecuteTime) }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ formatDateTime(detailTask.finishTime) }}</el-descriptions-item>
        <el-descriptions-item label="上传时间">{{ formatDateTime(detailTask.uploadTime) }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 新增/编辑任务对话框 -->
    <el-dialog v-model="editVisible" :title="dialogTitle" width="600px" @close="cancelEdit">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="100px">
        <el-form-item label="任务编号" prop="taskNo">
          <el-input v-model="editForm.taskNo" placeholder="请输入任务编号" />
        </el-form-item>
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="editForm.taskName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="起始地点" prop="startPoint">
          <el-input v-model="editForm.startPoint" placeholder="请输入起始地点" />
        </el-form-item>
        <el-form-item label="任务距离" prop="distance">
          <el-input-number v-model="editForm.distance" :precision="2" :step="0.1" placeholder="请输入任务距离" />
        </el-form-item>
        <el-form-item label="任务状态" prop="status">
          <el-select v-model="editForm.status" placeholder="请选择任务状态">
            <el-option label="未开始" :value="0" />
            <el-option label="进行中" :value="1" />
            <el-option label="已完成" :value="2" />
            <el-option label="已暂停" :value="3" />
          </el-select>
        </el-form-item>
        <!-- 以下字段仅在新增时显示 -->
        <template v-if="!editForm.taskId">
          <el-form-item label="最后执行时间" prop="lastExecuteTime">
            <el-date-picker
              v-model="editForm.lastExecuteTime"
              type="datetime"
              placeholder="选择最后执行时间"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="完成时间" prop="finishTime">
            <el-date-picker
              v-model="editForm.finishTime"
              type="datetime"
              placeholder="选择完成时间"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="上传时间" prop="uploadTime">
            <el-date-picker
              v-model="editForm.uploadTime"
              type="datetime"
              placeholder="选择上传时间"
              style="width: 100%"
            />
          </el-form-item>
        </template>
      </el-form>
      <template #footer>
        <el-button @click="cancelEdit">取 消</el-button>
        <el-button type="primary" @click="submitEditForm">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { getTaskPage, deleteTask, createTask, updateTask } from '../api/task'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, ArrowDown, ArrowUp, Download, Plus, ArrowLeft, ArrowRight } from '@element-plus/icons-vue'

// 模拟从store获取用户信息
const userStore = {
  // 假设这是登录后从后端获取并存储的用户权限
  // 在实际应用中，这应该来自你的状态管理库（如Pinia）
  permissions: ['admin'] // or ['operator'], or ['user'] etc.
}

// 权限检查
function hasPermission(requiredPermissions) {
  const userPermissions = userStore.permissions || []
  return requiredPermissions.some(p => userPermissions.includes(p))
}

// 响应式数据
const viewType = ref('list')
const taskList = ref([])
const loading = ref(false)
const searchFormRef = ref()
const showAdvancedSearch = ref(false)
const detailVisible = ref(false)
const detailTask = ref({})
const selectedTasks = ref([])

// 新增/编辑对话框
const editVisible = ref(false)
const dialogTitle = ref('')
const editFormRef = ref()
const editForm = reactive({
  taskId: null,
  taskNo: '',
  taskName: '',
  startPoint: '',
  distance: 0,
  status: 0,
  lastExecuteTime: null,
  finishTime: null,
  uploadTime: null
})

// 编辑表单验证规则
const editFormRules = {
  taskNo: [
    { required: true, message: '任务编号不能为空', trigger: 'blur' }
  ],
  taskName: [
    { required: true, message: '任务名称不能为空', trigger: 'blur' }
  ],
  startPoint: [
    { required: true, message: '起始地点不能为空', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择任务状态', trigger: 'change' }
  ]
}

// 搜索表单
const searchForm = reactive({
  taskNo: '',
  taskName: '',
  startPoint: '',
  status: ''
})

// 高级搜索表单
const advancedSearchForm = reactive({
  dateRange: []
})

// 搜索验证规则
const searchRules = {
  taskNo: [
    { pattern: /^[A-Za-z0-9-]*$/, message: '任务编号只能包含字母、数字和连字符', trigger: 'blur' }
  ]
}

// 分页参数
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 日历相关
const currentYear = ref(new Date().getFullYear())
const currentMonth = ref(new Date().getMonth() + 1)
const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']

// 计算是否有搜索条件
const hasSearchConditions = computed(() => {
  return Object.values(searchForm).some(value => value !== '' && value !== null && value !== undefined) ||
         (advancedSearchForm.dateRange && advancedSearchForm.dateRange.length > 0)
})

// 计算当前激活的搜索条件
const activeSearchConditions = computed(() => {
  const conditions = {}
  
  if (searchForm.taskNo) {
    conditions.taskNo = { label: '任务编号', value: searchForm.taskNo }
  }
  if (searchForm.taskName) {
    conditions.taskName = { label: '任务名称', value: searchForm.taskName }
  }
  if (searchForm.startPoint) {
    conditions.startPoint = { label: '起始地点', value: searchForm.startPoint }
  }
  if (searchForm.status !== '') {
    const statusMap = { 0: '未开始', 1: '进行中', 2: '已完成', 3: '已暂停' }
    conditions.status = { label: '任务状态', value: statusMap[searchForm.status] }
  }
  if (advancedSearchForm.dateRange && advancedSearchForm.dateRange.length === 2) {
    conditions.dateRange = { label: '执行时间', value: `${advancedSearchForm.dateRange[0]} 至 ${advancedSearchForm.dateRange[1]}` }
  }
  
  return conditions
})

// 计算日历天数
const calendarDays = computed(() => {
  const days = []
  const year = currentYear.value
  const month = currentMonth.value - 1 // JS月份从0开始
  const lastDay = new Date(year, month + 1, 0).getDate()
  const today = new Date()

  for (let i = 1; i <= lastDay; i++) {
    const date = new Date(year, month, i)
    const isToday = date.toDateString() === today.toDateString()
    // 获取当天的任务
    const dayTasks = taskList.value.filter(task => {
      if (task.lastExecuteTime) {
        const taskDate = new Date(task.lastExecuteTime)
        return taskDate.getFullYear() === year && taskDate.getMonth() === month && taskDate.getDate() === i
      }
      return false
    })
    days.push({
      date: date.toISOString(),
      dayNumber: i,
      isCurrentMonth: true,
      isToday,
      tasks: dayTasks
    })
  }
  return days
})

// 获取状态类型
function getStatusType(status) {
  const typeMap = {
    0: 'info',
    1: 'warning',
    2: 'success',
    3: 'danger'
  }
  return typeMap[status] || 'info'
}

// 获取状态文本
function getStatusText(status) {
  const textMap = {
    0: '未开始',
    1: '进行中',
    2: '已完成',
    3: '已暂停'
  }
  return textMap[status] || '未知'
}

// 格式化日期时间
function formatDateTime(dateTime) {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN')
}

// 加载任务列表
function loadTaskList() {
  loading.value = true
  
  const params = {
    current: pagination.page,
    size: pagination.size
  }
  
  // 添加基础搜索条件
  if (searchForm.taskNo) params.taskNo = searchForm.taskNo
  if (searchForm.taskName) params.taskName = searchForm.taskName
  if (searchForm.startPoint) params.startPoint = searchForm.startPoint
  if (searchForm.status !== '') params.status = searchForm.status

  // 添加高级搜索条件
  if (advancedSearchForm.dateRange && advancedSearchForm.dateRange.length === 2) {
    params.startDate = advancedSearchForm.dateRange[0]
    params.endDate = advancedSearchForm.dateRange[1]
  }

  getTaskPage(params).then(res => {
    if (res.code === 200) {
      taskList.value = res.data.records || res.data.content || []
      pagination.total = res.data.total || res.data.totalElements || 0
      
      if (hasSearchConditions.value) {
        ElMessage.success(`搜索完成，共找到 ${pagination.total} 条记录`)
      }
    } else {
      ElMessage.error(res.message || '获取任务列表失败')
    }
  }).catch(error => {
    console.error('获取任务列表错误:', error)
    ElMessage.error('获取任务列表失败')
  }).finally(() => {
    loading.value = false
  })
}

// 搜索
function handleSearch() {
  searchFormRef.value?.validate((valid) => {
    if (valid) {
      pagination.page = 1
      loadTaskList()
    }
  })
}

// 高级搜索
function handleAdvancedSearch() {
  pagination.page = 1
  loadTaskList()
}

// 重置
function handleReset() {
  searchFormRef.value?.resetFields()
  Object.assign(searchForm, {
    taskNo: '',
    taskName: '',
    startPoint: '',
    status: ''
  })
  Object.assign(advancedSearchForm, {
    dateRange: []
  })
  pagination.page = 1
  loadTaskList()
  ElMessage.info('搜索条件已重置')
}

// 移除搜索条件
function removeSearchCondition(key) {
  if (key === 'taskNo') searchForm.taskNo = ''
  if (key === 'taskName') searchForm.taskName = ''
  if (key === 'startPoint') searchForm.startPoint = ''
  if (key === 'status') searchForm.status = ''
  if (key === 'dateRange') advancedSearchForm.dateRange = []
  
  pagination.page = 1
  loadTaskList()
}

// 切换高级搜索
function toggleAdvancedSearch() {
  showAdvancedSearch.value = !showAdvancedSearch.value
}

// 新增任务
function handleAdd() {
  Object.assign(editForm, {
    taskId: null,
    taskNo: '',
    taskName: '',
    startPoint: '',
    distance: 0,
    status: 0,
    lastExecuteTime: null,
    finishTime: null,
    uploadTime: null
  })
  dialogTitle.value = '新增任务'
  editVisible.value = true
  // 清除之前的校验
  if (editFormRef.value) {
    editFormRef.value.clearValidate()
  }
}

// 编辑任务
function handleEdit(row) {
  Object.assign(editForm, row)
  dialogTitle.value = '编辑任务'
  editVisible.value = true
  // 清除之前的校验
  if (editFormRef.value) {
    editFormRef.value.clearValidate()
  }
}

// 提交编辑表单
function submitEditForm() {
  editFormRef.value?.validate(valid => {
    if (valid) {
      if (editForm.taskId) {
        // 编辑
        updateTask(editForm.taskId, editForm).then(res => {
          if (res.code === 200) {
            ElMessage.success('编辑任务成功')
            editVisible.value = false
            loadTaskList()
          } else {
            ElMessage.error(res.message || '编辑任务失败')
          }
        }).catch(() => {
          ElMessage.error('编辑任务失败')
        })
      } else {
        // 新增
        createTask(editForm).then(res => {
          if (res.code === 200) {
            ElMessage.success('新增任务成功')
            editVisible.value = false
            loadTaskList()
          } else {
            ElMessage.error(res.message || '新增任务失败')
          }
        }).catch(() => {
          ElMessage.error('新增任务失败')
        })
      }
    }
  })
}

// 取消编辑
function cancelEdit() {
  editVisible.value = false
}

// 查看任务详情
function handleView(row) {
  detailTask.value = row
  detailVisible.value = true
}

// 删除任务
function handleDelete(row) {
  ElMessageBox.confirm(
    `确定要删除任务"${row.taskName}"吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    deleteTask(row.taskId).then(res => {
      if (res.code === 200) {
        ElMessage.success('删除成功')
        loadTaskList()
      } else {
        ElMessage.error('删除失败')
      }
    }).catch(error => {
      console.error('删除任务错误:', error)
      ElMessage.error('删除失败')
    })
  })
}

// 导出
function handleExport() {
  ElMessage.info('导出功能开发中...')
}

// 刷新
function handleRefresh() {
  loadTaskList()
  ElMessage.success('数据已刷新')
}

// 选择变化
function handleSelectionChange(selection) {
  selectedTasks.value = selection
}

// 分页大小改变
function handleSizeChange(size) {
  pagination.size = size
  pagination.page = 1
  loadTaskList()
}

// 当前页改变
function handleCurrentChange(page) {
  pagination.page = page
  loadTaskList()
}

// 日历导航
function previousMonth() {
  if (currentMonth.value === 1) {
    currentMonth.value = 12
    currentYear.value--
  } else {
    currentMonth.value--
  }
}

function nextMonth() {
  if (currentMonth.value === 12) {
    currentMonth.value = 1
    currentYear.value++
  } else {
    currentMonth.value++
  }
}

function goToToday() {
  const today = new Date()
  currentYear.value = today.getFullYear()
  currentMonth.value = today.getMonth() + 1
}

onMounted(() => {
  loadTaskList()
})
</script>

<style scoped>
.task-bg {
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

.wireframe {
  border: 2px solid #333;
  background: white;
  min-height: 600px;
  padding: 20px;
}

.tabs {
  display: flex;
  margin-bottom: 20px;
  border: 1px solid #333;
  background: white;
}

.tab {
  flex: 1;
  padding: 15px;
  text-align: center;
  border-right: 1px solid #333;
  cursor: pointer;
  background: #f8f8f8;
}

.tab.active {
  background: #e6f3ff;
  font-weight: bold;
}

.tab:last-child {
  border-right: none;
}

.list-view {
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

.advanced-search {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #666;
  background: #f8f8f8;
}

.search-status {
  margin-bottom: 20px;
  padding: 10px;
  background: #f8f9fa;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
  padding: 10px;
  border: 1px solid #666;
  background: #f8f8f8;
}

.toolbar-info {
  display: flex;
  align-items: center;
  color: #666;
  font-size: 14px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding: 10px;
  border: 1px solid #666;
  background: #f8f8f8;
}

/* 日历样式 */
.calendar-view {
  display: block;
}

.calendar-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 10px;
  border: 1px solid #666;
  background: #f8f8f8;
}

.calendar-toolbar h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}

.calendar-nav {
  display: flex;
  gap: 10px;
}

.calendar-grid {
  border: 1px solid #666;
  height: 500px;
}

.calendar-header {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  border-bottom: 2px solid #333;
}

.calendar-header-cell {
  padding: 10px;
  text-align: center;
  font-weight: bold;
  border-right: 1px solid #666;
  background: #f0f0f0;
}

.calendar-header-cell:last-child {
  border-right: none;
}

.calendar-body {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  grid-template-rows: repeat(6, 1fr);
  height: calc(100% - 50px);
}

.calendar-cell {
  border-right: 1px solid #ccc;
  border-bottom: 1px solid #ccc;
  padding: 5px;
  position: relative;
  min-height: 80px;
}

.calendar-cell:nth-child(7n) {
  border-right: none;
}

.calendar-cell.other-month {
  background: #f8f8f8;
  color: #ccc;
}

.calendar-cell.today {
  background: #e6f7ff;
}

.date-number {
  font-weight: bold;
  margin-bottom: 5px;
}

.day-tasks {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.task-event {
  background: #e6f3ff;
  border: 1px solid #0066cc;
  padding: 2px 5px;
  margin-bottom: 2px;
  font-size: 12px;
  border-radius: 3px;
  cursor: pointer;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.task-event:hover {
  background: #cce7ff;
}
</style> 