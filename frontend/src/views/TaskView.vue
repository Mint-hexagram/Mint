<template>
  <div class="task-bg">
    <div class="page-title">任务管理</div>
    <div style="margin-bottom: 16px; text-align: right;">
      <!-- 删除右上角新增任务按钮对应的el-button代码 -->
    </div>
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
          <el-form-item label="任务类型" prop="taskType">
            <el-select 
              v-model="searchForm.taskType" 
              placeholder="请选择类型" 
              clearable 
              style="width: 150px"
            >
              <el-option label="常规巡检" value="常规巡检" />
              <el-option label="专项检查" value="专项检查" />
              <el-option label="复检" value="复检" />
            </el-select>
          </el-form-item>
          <el-form-item label="优先级" prop="priority">
            <el-select 
              v-model="searchForm.priority" 
              placeholder="请选择优先级" 
              clearable 
              style="width: 150px"
            >
              <el-option label="高" value="高" />
              <el-option label="中" value="中" />
              <el-option label="低" value="低" />
            </el-select>
          </el-form-item>
          <el-form-item label="起始地点" prop="startPosition">
            <el-input 
              v-model="searchForm.startPosition" 
              placeholder="请输入起始地点" 
              clearable 
              style="width: 150px"
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="结束地点" prop="endPosition">
            <el-input 
              v-model="searchForm.endPosition" 
              placeholder="请输入结束地点" 
              clearable 
              style="width: 150px"
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="巡检线路" prop="line">
            <el-input 
              v-model="searchForm.line" 
              placeholder="请输入巡检线路" 
              clearable 
              style="width: 150px"
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="巡检范围" prop="scope">
            <el-input 
              v-model="searchForm.scope" 
              placeholder="请输入巡检范围" 
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
            <el-button type="primary" @click="handleAdd">
              <el-icon><Plus /></el-icon>
              新增任务
            </el-button>
            <el-button @click="handleExport" :disabled="taskList.length === 0">
              <el-icon><Download /></el-icon>
              导出
            </el-button>
            <el-button @click="loadTaskList">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
            <el-button type="primary" @click="showStatsDialog = true">
              任务统计分析
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
          <el-table-column prop="taskName" label="任务名称" min-width="150">
            <template #default="{ row }">
              <el-link type="primary" :underline="false" @click.stop="handleView(row)">{{ row.taskName }}</el-link>
            </template>
          </el-table-column>
          <el-table-column prop="taskType" label="任务类型" width="120" />
          <el-table-column prop="priority" label="优先级" width="100">
            <template #default="{ row }">
              <el-tag :type="getPriorityType(row.priority)">
                {{ row.priority }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="startPosition" label="起始地点" width="120" />
          <el-table-column prop="endPosition" label="结束地点" width="120" />
          <el-table-column prop="line" label="巡检线路" width="120" />
          <el-table-column prop="scope" label="巡检范围" width="120" />
          <el-table-column prop="progress" label="完成度(%)" width="100">
            <template #default="{ row }">
              {{ row.progress !== null && row.progress !== undefined ? row.progress : '-' }}
            </template>
          </el-table-column>
          <el-table-column prop="result" label="执行结果" width="120" />
          <el-table-column prop="problemCount" label="发现问题数" width="100">
            <template #default="{ row }">
              <el-link type="primary" :underline="false" @click.stop="handleProblemCountClick(row)">
                {{ row.problemCount }}
              </el-link>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="actualStartTime" label="最后执行时间" width="160">
            <template #default="{ row }">
              {{ formatDateTime(row.actualStartTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="actualEndTime" label="完成时间" width="160">
            <template #default="{ row }">
              {{ formatDateTime(row.actualEndTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="上传时间" width="160">
            <template #default="{ row }">
              {{ formatDateTime(row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="distance" label="距离(公里)" width="100" />
          <el-table-column prop="executorName" label="执行人" width="120" />
          <el-table-column prop="assistantName" label="协助人" width="120" />
          <el-table-column label="操作" width="200" align="center">
            <template #default="{ row }">
              <el-button type="text" @click="handleView(row)">查看</el-button>
              <el-button type="text" @click="handleEdit(row)">编辑</el-button>
              <el-button type="text" @click="handleDelete(row)" style="color: #f56c6c;">删除</el-button>
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
        <el-descriptions-item label="任务类型">{{ detailTask.taskType }}</el-descriptions-item>
        <el-descriptions-item label="优先级">{{ detailTask.priority }}</el-descriptions-item>
        <el-descriptions-item label="任务状态">
          <el-tag :type="getStatusType(detailTask.status)">
            {{ getStatusText(detailTask.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="起始地点">{{ detailTask.startPosition }}</el-descriptions-item>
        <el-descriptions-item label="结束地点">{{ detailTask.endPosition }}</el-descriptions-item>
        <el-descriptions-item label="巡检线路">{{ detailTask.line }}</el-descriptions-item>
        <el-descriptions-item label="巡检范围">{{ detailTask.scope }}</el-descriptions-item>
        <el-descriptions-item label="完成度(%)">{{ detailTask.progress }}</el-descriptions-item>
        <el-descriptions-item label="执行结果">{{ detailTask.result }}</el-descriptions-item>
        <el-descriptions-item label="发现问题数">{{ detailTask.problemCount }}</el-descriptions-item>
        <el-descriptions-item label="计划开始时间">{{ formatDateTime(detailTask.planStartTime) }}</el-descriptions-item>
        <el-descriptions-item label="计划完成时间">{{ formatDateTime(detailTask.planFinishTime) }}</el-descriptions-item>
        <el-descriptions-item label="执行人">{{ detailTask.executorName || detailTask.executorId }}</el-descriptions-item>
        <el-descriptions-item label="协助人">{{ detailTask.assistantName || detailTask.assistantId }}</el-descriptions-item>
        <el-descriptions-item label="任务描述">{{ detailTask.description }}</el-descriptions-item>
        <el-descriptions-item label="创建人ID">{{ detailTask.creatorId }}</el-descriptions-item>
        <el-descriptions-item label="执行人ID">{{ detailTask.executorId }}</el-descriptions-item>
        <el-descriptions-item label="最后执行时间">{{ formatDateTime(detailTask.actualStartTime) }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ formatDateTime(detailTask.actualEndTime) }}</el-descriptions-item>
        <el-descriptions-item label="上传时间">{{ formatDateTime(detailTask.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="距离(公里)">{{ detailTask.distance }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 新增/编辑任务弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :before-close="handleClose"
    >
      <el-form 
        :model="taskForm" 
        :rules="taskRules" 
        ref="taskFormRef" 
        label-width="120px"
      >
        <el-form-item label="任务编号" prop="taskNo">
          <el-input v-model="taskForm.taskNo" placeholder="请输入任务编号" @input="onTaskNoInput" />
        </el-form-item>
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="taskForm.taskName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="任务类型" prop="taskType">
          <el-select v-model="taskForm.taskType" placeholder="请选择任务类型">
            <el-option label="常规巡检" value="常规巡检" />
            <el-option label="专项检查" value="专项检查" />
            <el-option label="复检" value="复检" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-radio-group v-model="taskForm.priority">
            <el-radio-button label="高" />
            <el-radio-button label="中" />
            <el-radio-button label="低" />
          </el-radio-group>
        </el-form-item>
        <el-form-item label="起始地点" prop="startPosition">
          <el-input v-model="taskForm.startPosition" placeholder="请输入起始地点" />
        </el-form-item>
        <el-form-item label="结束地点" prop="endPosition">
          <el-input v-model="taskForm.endPosition" placeholder="请输入结束地点" />
        </el-form-item>
        <el-form-item label="巡检线路" prop="line">
          <el-input v-model="taskForm.line" placeholder="请输入巡检线路" />
        </el-form-item>
        <el-form-item label="巡检范围" prop="scope">
          <el-input v-model="taskForm.scope" placeholder="请输入巡检范围" />
        </el-form-item>
        <el-form-item label="任务状态" prop="status">
          <el-select v-model="taskForm.status" placeholder="请选择任务状态">
            <el-option label="未开始" :value="0" />
            <el-option label="进行中" :value="1" />
            <el-option label="已完成" :value="2" />
            <el-option label="已暂停" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="完成度(%)" prop="progress">
          <el-input-number v-model="taskForm.progress" :min="0" :max="100" :step="1" placeholder="请输入完成度" style="width: 150px" />
        </el-form-item>
        <el-form-item label="执行结果" prop="result">
          <el-input v-model="taskForm.result" placeholder="请输入执行结果" />
        </el-form-item>
        <el-form-item label="计划时间" prop="planTime">
          <el-date-picker
            v-model="taskForm.planTime"
            type="datetimerange"
            range-separator="至"
            start-placeholder="计划开始时间"
            end-placeholder="计划完成时间"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </el-form-item>
        <el-form-item label="执行人" prop="executorId">
          <el-input v-model="executorNameDisplay" placeholder="请选择执行人" readonly @click="showExecutorDialog = true" style="width: 150px; cursor:pointer;" />
          <UserSelectDialog v-model="showExecutorDialog" @select="onExecutorSelect" />
        </el-form-item>
        <el-form-item label="协助人" prop="assistantId">
          <el-input v-model="assistantNameDisplay" placeholder="请选择协助人" readonly @click="showAssistantDialog = true" style="width: 150px; cursor:pointer;" />
          <UserSelectDialog v-model="showAssistantDialog" @select="onAssistantSelect" />
        </el-form-item>
        <el-form-item label="任务描述" prop="description">
          <el-input 
            type="textarea"
            :rows="3"
            v-model="taskForm.description" 
            placeholder="请输入任务描述" 
          />
        </el-form-item>
        <el-form-item label="距离(公里)" prop="distance">
          <el-input-number v-model="taskForm.distance" :min="0" :step="0.01" placeholder="请输入距离" style="width: 150px" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saving">保 存</el-button>
      </template>
    </el-dialog>

    <!-- 任务统计分析弹窗 -->
    <el-dialog v-model="showStatsDialog" title="任务统计分析" width="600px">
      <div style="padding: 10px 0;">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card shadow="hover">
              <div style="font-size:18px;font-weight:bold;margin-bottom:8px;">任务总览</div>
              <el-descriptions :column="1" border size="small">
                <el-descriptions-item label="任务总数"><el-tag>{{ stats.total }}</el-tag></el-descriptions-item>
                <el-descriptions-item label="平均距离(公里)"><el-tag type="success">{{ stats.avgDistance }}</el-tag></el-descriptions-item>
                <el-descriptions-item label="总距离(公里)"><el-tag type="info">{{ stats.sumDistance }}</el-tag></el-descriptions-item>
                <el-descriptions-item label="最远距离(公里)"><el-tag type="warning">{{ stats.maxDistance }}</el-tag></el-descriptions-item>
                <el-descriptions-item label="最近距离(公里)"><el-tag type="warning">{{ stats.minDistance }}</el-tag></el-descriptions-item>
                <el-descriptions-item label="平均完成度(%)"><el-tag type="primary">{{ stats.avgProgress }}</el-tag></el-descriptions-item>
              </el-descriptions>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card shadow="hover">
              <div style="font-size:18px;font-weight:bold;margin-bottom:8px;">状态分布</div>
              <el-descriptions :column="1" border size="small">
                <el-descriptions-item label="未开始"><el-tag>{{ stats.status0 }}</el-tag></el-descriptions-item>
                <el-descriptions-item label="进行中"><el-tag type="warning">{{ stats.status1 }}</el-tag></el-descriptions-item>
                <el-descriptions-item label="已完成"><el-tag type="success">{{ stats.status2 }}</el-tag></el-descriptions-item>
                <el-descriptions-item label="已暂停"><el-tag type="info">{{ stats.status3 }}</el-tag></el-descriptions-item>
              </el-descriptions>
            </el-card>
            <el-card shadow="hover" style="margin-top:12px;">
              <div style="font-size:18px;font-weight:bold;margin-bottom:8px;">优先级分布</div>
              <el-descriptions :column="1" border size="small">
                <el-descriptions-item label="高"><el-tag type="danger">{{ stats.priorityHigh }}</el-tag></el-descriptions-item>
                <el-descriptions-item label="中"><el-tag type="warning">{{ stats.priorityMid }}</el-tag></el-descriptions-item>
                <el-descriptions-item label="低"><el-tag type="info">{{ stats.priorityLow }}</el-tag></el-descriptions-item>
              </el-descriptions>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <template #footer>
        <el-button @click="showStatsDialog = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick, watch } from 'vue'
import { getTaskPage, deleteTask, addTask, updateTask } from '../api/task'
import { getUserList } from '../api/user'
import { getDefectList } from '../api/defect'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, ArrowDown, ArrowUp, Download, Plus, ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import { useRouter, useRoute } from 'vue-router'
import dayjs from 'dayjs'
import UserSelectDialog from '../components/UserSelectDialog.vue'

const router = useRouter()
const route = useRoute()

// 响应式数据
const viewType = ref('list')
const taskList = ref([])
const loading = ref(false)
const searchFormRef = ref()
const showAdvancedSearch = ref(false)
const detailVisible = ref(false)
const detailTask = ref({})
const selectedTasks = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const taskFormRef = ref(null)
const saving = ref(false)
const userList = ref([])
const showExecutorDialog = ref(false)
const showAssistantDialog = ref(false)
// executorNameDisplay/assistantNameDisplay 改为ref，便于直接赋值
const executorNameDisplay = ref('')
const assistantNameDisplay = ref('')
const showStatsDialog = ref(false)
// 统计数据结构和计算
const stats = reactive({
  total: 0,
  status0: 0,
  status1: 0,
  status2: 0,
  status3: 0,
  avgDistance: 0,
  sumDistance: 0,
  maxDistance: 0,
  minDistance: 0,
  avgProgress: 0,
  priorityHigh: 0,
  priorityMid: 0,
  priorityLow: 0
})
function calcStats() {
  const list = taskList.value
  stats.total = list.length
  stats.status0 = list.filter(t => t.status === 0).length
  stats.status1 = list.filter(t => t.status === 1).length
  stats.status2 = list.filter(t => t.status === 2).length
  stats.status3 = list.filter(t => t.status === 3).length
  const dists = list.map(t => Number(t.distance) || 0)
  stats.avgDistance = dists.length ? (dists.reduce((a, b) => a + b, 0) / dists.length).toFixed(2) : 0
  stats.sumDistance = dists.length ? dists.reduce((a, b) => a + b, 0).toFixed(2) : 0
  stats.maxDistance = dists.length ? Math.max(...dists).toFixed(2) : 0
  stats.minDistance = dists.length ? Math.min(...dists).toFixed(2) : 0
  const progresses = list.map(t => Number(t.progress) || 0)
  stats.avgProgress = progresses.length ? (progresses.reduce((a, b) => a + b, 0) / progresses.length).toFixed(2) : 0
  stats.priorityHigh = list.filter(t => t.priority === '高').length
  stats.priorityMid = list.filter(t => t.priority === '中').length
  stats.priorityLow = list.filter(t => t.priority === '低').length
}

// 修复：补充 taskForm 的响应式声明
const taskForm = ref({
  taskId: null,
  taskNo: '',
  taskName: '',
  taskType: '常规巡检',
  priority: '中',
  startPosition: '',
  endPosition: '',
  line: '',
  scope: '',
  status: 0,
  progress: 0,
  result: '',
  problemCount: 0,
  planTime: [],
  planStartTime: '',
  planFinishTime: '',
  executorId: null,
  assistantId: null,
  description: '',
  distance: 0,
})

// 搜索表单
const searchForm = ref({
  taskNo: '',
  taskName: '',
  taskType: '',
  priority: '',
  status: null,
  startPosition: '',
  endPosition: '',
  line: '',
  scope: '',
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
  if (searchForm.value.taskNo) {
    conditions.taskNo = { label: '任务编号', value: searchForm.value.taskNo }
  }
  if (searchForm.value.taskName) {
    conditions.taskName = { label: '任务名称', value: searchForm.value.taskName }
  }
  if (searchForm.value.taskType) {
    conditions.taskType = { label: '任务类型', value: searchForm.value.taskType }
  }
  if (searchForm.value.priority) {
    conditions.priority = { label: '优先级', value: searchForm.value.priority }
  }
  if (searchForm.value.status !== null && searchForm.value.status !== undefined && searchForm.value.status !== '') {
    const statusMap = { 0: '未开始', 1: '进行中', 2: '已完成', 3: '已暂停' }
    conditions.status = { label: '任务状态', value: statusMap[searchForm.value.status] }
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
  const month = currentMonth.value
  const today = new Date()
  // 获取本月天数
  const lastDay = new Date(year, month, 0).getDate()
  for (let i = 1; i <= lastDay; i++) {
    const date = new Date(year, month - 1, i)
    const isToday = date.toDateString() === today.toDateString()
    // 获取当天的任务
    const dayTasks = taskList.value.filter(task => {
      if (task.planStartTime) {
        const taskDate = new Date(task.planStartTime)
        return taskDate.toDateString() === date.toDateString()
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

// 获取优先级类型
function getPriorityType(priority) {
  if (priority === '高') return 'danger'
  if (priority === '中') return 'warning'
  return 'info'
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
  if (searchForm.value.taskNo) params.taskNo = searchForm.value.taskNo
  if (searchForm.value.taskName) params.taskName = searchForm.value.taskName
  if (searchForm.value.taskType) params.taskType = searchForm.value.taskType
  if (searchForm.value.priority) params.priority = searchForm.value.priority
  if (searchForm.value.status !== null) params.status = searchForm.value.status

  // 添加高级搜索条件
  if (advancedSearchForm.dateRange && advancedSearchForm.dateRange.length === 2) {
    params.startDate = advancedSearchForm.dateRange[0]
    params.endDate = advancedSearchForm.dateRange[1]
  }

  getTaskPage(params).then(async res => {
    if (res.code === 200 && res.data && Array.isArray(res.data.records)) {
      let tasks = res.data.records.map(task => ({
        ...task,
        executorName: task.executor ? task.executor.realName : (userList.value.find(u => u.userId === task.executorId)?.realName || task.executorId),
        assistantName: task.assistant ? task.assistant.realName : (userList.value.find(u => u.userId === task.assistantId)?.realName || task.assistantId),
        problemCount: 0 // 默认0，后续统计
      }))
      pagination.total = res.data.total || res.data.totalElements || 0
      // 只统计每个任务下的缺陷数
      if (tasks.length > 0) {
        const promises = tasks.map(task =>
          getDefectList({ taskId: task.taskId }).then(res => {
            if (res.code === 200 && Array.isArray(res.data)) {
              // 只统计该任务下的缺陷数
              return res.data.filter(defect => defect.taskId === task.taskId).length
            } else if (res.code === 200 && res.data.records) {
              // 兼容后端返回records
              return res.data.records.filter(defect => defect.taskId === task.taskId).length
            } else {
              return 0
            }
          }).catch(() => 0)
        )
        const counts = await Promise.all(promises)
        tasks = tasks.map((task, idx) => ({ ...task, problemCount: counts[idx] }))
      }
      taskList.value = tasks
    } else {
      taskList.value = [];
      pagination.total = 0;
      ElMessage.error(res.message || '获取任务列表失败')
    }
  }).catch(error => {
    taskList.value = [];
    pagination.total = 0;
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
  searchForm.value = {
    taskNo: '',
    taskName: '',
    taskType: '',
    priority: '',
    status: null,
    startPosition: '',
    endPosition: '',
    line: '',
    scope: '',
  }
  advancedSearchForm.dateRange = []
  pagination.page = 1
  loadTaskList()
  ElMessage.info('搜索条件已重置')
}

// 移除搜索条件
function removeSearchCondition(key) {
  if (key === 'taskNo') searchForm.value.taskNo = ''
  if (key === 'taskName') searchForm.value.taskName = ''
  if (key === 'taskType') searchForm.value.taskType = ''
  if (key === 'priority') searchForm.value.priority = ''
  if (key === 'status') searchForm.value.status = null
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
  console.log('点击了新增任务，弹窗前 dialogVisible:', dialogVisible.value)
  dialogTitle.value = '新增任务'
  dialogVisible.value = true
  nextTick(() => {
    try {
      taskFormRef.value?.resetFields()
      taskForm.value.taskId = null
      executorNameDisplay.value = ''
      assistantNameDisplay.value = ''
    } catch (e) {
      console.error('表单重置失败', e)
    }
    console.log('弹窗后 dialogVisible:', dialogVisible.value)
  })
}

// 编辑任务
function handleEdit(row) {
  dialogTitle.value = '编辑任务'
  dialogVisible.value = true
  nextTick(() => {
    Object.assign(taskForm.value, row)
    taskForm.value.planTime = [row.planStartTime, row.planFinishTime]
    // 同步显示
    const executor = userList.value.find(u => u.userId === row.executorId)
    executorNameDisplay.value = executor ? executor.realName : ''
    const assistant = userList.value.find(u => u.userId === row.assistantId)
    assistantNameDisplay.value = assistant ? assistant.realName : ''
  })
}

// 查看任务详情
function handleView(row) {
  // 查找执行人真实姓名
  const executor = userList.value.find(u => u.userId === row.executorId)
  const assistant = userList.value.find(u => u.userId === row.assistantId)
  detailTask.value = { ...row, executorName: executor ? executor.realName : row.executorId, assistantName: assistant ? assistant.realName : row.assistantId }
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
  const token = localStorage.getItem('token') || sessionStorage.getItem('token');
  fetch('/api/tasks/export', {
    method: 'GET',
    headers: token ? { 'Authorization': 'Bearer ' + token } : {},
  })
    .then(res => {
      if (!res.ok) throw new Error('导出失败');
      return res.blob();
    })
    .then(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = '任务信息.xlsx';
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
    })
    .catch(() => {
      ElMessage.error('导出失败');
    });
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

// 保存任务
function handleSave() {
  taskFormRef.value?.validate((valid) => {
    if (valid) {
      saving.value = true
      const [startTime, finishTime] = taskForm.value.planTime
      const dataToSave = {
        ...taskForm.value,
        planStartTime: dayjs(startTime).format('YYYY-MM-DDTHH:mm:ss'),
        planFinishTime: dayjs(finishTime).format('YYYY-MM-DDTHH:mm:ss'),
      }
      delete dataToSave.planTime // 移除临时字段
      const apiCall = dataToSave.taskId ? updateTask : addTask
      apiCall(dataToSave).then(res => {
        if (res.code === 200) {
          ElMessage.success(dataToSave.taskId ? '更新成功' : '新增成功')
          dialogVisible.value = false
          loadTaskList()
        } else {
          ElMessage.error(res.message || '操作失败')
        }
      }).catch(error => {
        console.error('保存任务失败:', error)
        ElMessage.error('操作失败')
      }).finally(() => {
        saving.value = false
      })
    }
  })
}

// 关闭弹窗
function handleClose() {
  dialogVisible.value = false
  taskFormRef.value?.resetFields()
  taskForm.value = {
    taskId: null,
    taskNo: '',
    taskName: '',
    taskType: '常规巡检',
    priority: '中',
    startPosition: '',
    endPosition: '',
    line: '',
    scope: '',
    status: 0,
    progress: 0,
    result: '',
    problemCount: 0,
    planTime: [],
    planStartTime: '',
    planFinishTime: '',
    executorId: null,
    assistantId: null,
    description: '',
    distance: 0,
  }
}

// 获取用户列表
async function fetchUserList() {
  try {
    const res = await getUserList() // 假设此API返回 { code: 200, data: [...] }
    console.log('getUserList 返回：', res)
    if (res.code === 200) {
      userList.value = res.data.records || []
      console.log('userList.value:', userList.value)
    } else {
      ElMessage.error('获取用户列表失败')
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
  }
}

// 权限判断方法
function hasPermission(roles) {
  // 假设用户信息保存在localStorage，包含角色数组
  const userInfoStr = localStorage.getItem('userInfo')
  if (!userInfoStr) return false
  try {
    const userInfo = JSON.parse(userInfoStr)
    if (Array.isArray(userInfo.roles)) {
      return userInfo.roles.some(role => roles.includes(role))
    }
    // 兼容单角色字符串
    if (typeof userInfo.role === 'string') {
      return roles.includes(userInfo.role)
    }
  } catch (e) {
    return false
  }
  return false
}

onMounted(() => {
  fetchUserList()
  loadTaskList()
  // 自动弹出详情
  if (route.query.suffix || route.query.taskNo || route.query.taskId) {
    let hasShown = false
    const unwatch = watch(taskList, (list) => {
      if (list && list.length > 0 && !hasShown) {
        let target = null
        if (route.query.suffix) {
          target = list.find(t => String(t.taskNo).endsWith(route.query.suffix))
        } else if (route.query.taskNo) {
          target = list.find(t => String(t.taskNo) === String(route.query.taskNo))
        } else if (route.query.taskId) {
          target = list.find(t => String(t.taskId) === String(route.query.taskId))
        }
        if (target) {
          handleView(target)
          hasShown = true
          router.replace({ path: '/task' })
        } else {
          ElMessage.warning('未找到对应的任务，可能已被删除！')
          hasShown = true
          router.replace({ path: '/task' })
        }
        unwatch()
      }
    }, { immediate: true })
  }
})

const taskRules = {
  taskNo: [
    { required: true, message: '请输入任务编号', trigger: 'blur' }
  ],
  taskName: [
    { required: true, message: '请输入任务名称', trigger: 'blur' }
  ],
  taskType: [
    { required: true, message: '请选择任务类型', trigger: 'change' }
  ],
  priority: [
    { required: true, message: '请选择优先级', trigger: 'change' }
  ],
  startPosition: [
    { required: true, message: '请输入起始地点', trigger: 'blur' }
  ],
  endPosition: [
    { required: true, message: '请输入结束地点', trigger: 'blur' }
  ],
  line: [
    { required: true, message: '请输入巡检线路', trigger: 'blur' }
  ],
  scope: [
    { required: true, message: '请输入巡检范围', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择任务状态', trigger: 'change' }
  ],
  progress: [
    { required: true, message: '请输入完成度', trigger: 'blur' }
  ],
  result: [
    { required: false }
  ],
  planTime: [
    { required: true, type: 'array', min: 2, message: '请选择计划时间', trigger: 'change' }
  ],
  executorId: [
    { required: true, message: '请选择执行人', trigger: 'change' }
  ],
  assistantId: [
    { required: false }
  ],
  description: [
    { required: false }
  ],
  distance: [
    { required: false, type: 'number', min: 0, message: '请输入有效距离', trigger: 'blur' }
  ]
}

function onTaskNoInput(val) {
  // 只在新增任务时自动加前缀，且避免重复加
  if (dialogTitle.value === '新增任务') {
    if (!val.startsWith('TASK-')) {
      taskForm.value.taskNo = 'TASK-' + val.replace(/^TASK-/, '')
    }
  }
}

function handleProblemCountClick(row) {
  // 跳转到缺陷管理页面，并带上任务ID参数（带前缀）
  router.push({
    path: '/defect',
    query: {
      taskId: row.taskId // 保证带前缀
    }
  })
}

function onExecutorSelect(user) {
  taskForm.value.executorId = user.userId
  // 直接回填显示
  executorNameDisplay.value = user.realName
  // 若userList中没有该用户则补充
  if (!userList.value.find(u => u.userId === user.userId)) {
    userList.value.push(user)
  }
}
function onAssistantSelect(user) {
  taskForm.value.assistantId = user.userId
  assistantNameDisplay.value = user.realName
  if (user && !userList.value.find(u => u.userId === user.userId)) {
    userList.value.push(user)
  }
}

// 保证统计数据实时刷新
watch(taskList, () => {
  calcStats()
}, { deep: true })
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