<template>
  <div class="log-bg">
    <div class="page-title">系统日志</div>
    <el-card>
      <div style="margin-bottom: 16px; display: flex; align-items: center; gap: 10px;">
        <el-input v-model="searchUserId" placeholder="用户ID" style="width: 120px;" clearable />
        <el-date-picker
          v-model="searchTimeRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          value-format="YYYY-MM-DD HH:mm:ss"
          style="width: 350px;"
        />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </div>
      <el-table :data="logList" border stripe v-loading="loading" style="width: 100%">
        <el-table-column prop="operId" label="日志ID" width="80" />
        <el-table-column prop="userId" label="用户ID" width="80" />
        <el-table-column prop="module" label="模块" width="120" />
        <el-table-column prop="operType" label="操作类型" width="100" />
        <el-table-column prop="operDesc" label="操作描述" min-width="180" />
        <el-table-column prop="requestParam" label="请求参数" min-width="180">
          <template #default="scope">
            <span>
              {{ scope.row.requestParam && scope.row.requestParam.length > 50 ? scope.row.requestParam.slice(0, 50) + '...' : scope.row.requestParam }}
              <el-link v-if="scope.row.requestParam && scope.row.requestParam.length > 50" type="primary" @click="showParam(scope.row.requestParam)">查看全部</el-link>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="operTime" label="操作时间" width="170">
          <template #default="scope">{{ formatDateTime(scope.row.operTime) }}</template>
        </el-table-column>
        <el-table-column prop="ip" label="IP地址" width="120" />
        <el-table-column prop="device" label="设备" min-width="120">
          <template #default="scope">
            <span>
              {{ scope.row.device && scope.row.device.length > 30 ? scope.row.device.slice(0, 30) + '...' : scope.row.device }}
              <el-link v-if="scope.row.device && scope.row.device.length > 30" type="primary" @click="showDevice(scope.row.device)">查看全部</el-link>
            </span>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          v-model="pagination.currentPage"
          :page-size="pagination.size"
          :total="pagination.total"
          layout="total, prev, pager, next, jumper"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
    <el-dialog v-model="paramDialogVisible" title="请求参数" width="600px" :show-close="true">
      <pre style="white-space: pre-wrap;word-break: break-all;">{{ paramDialogContent }}</pre>
    </el-dialog>
    <el-dialog v-model="deviceDialogVisible" title="设备信息" width="600px" :show-close="true">
      <pre style="white-space: pre-wrap;word-break: break-all;">{{ deviceDialogContent }}</pre>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getLogPage } from '../api/log'
import { ElMessage } from 'element-plus'

const logList = ref([])
const loading = ref(false)
const pagination = reactive({
  currentPage: 1,
  size: 10,
  total: 0
})
const searchTimeRange = ref([])
const searchUserId = ref('')

function handleSearch() {
  pagination.currentPage = 1
  loadLogList()
}
function handleReset() {
  searchUserId.value = ''
  searchTimeRange.value = []
  pagination.currentPage = 1
  loadLogList()
}

const paramDialogVisible = ref(false)
const paramDialogContent = ref('')

function showParam(param) {
  paramDialogContent.value = param
  paramDialogVisible.value = true
}

const deviceDialogVisible = ref(false)
const deviceDialogContent = ref('')
function showDevice(device) {
  deviceDialogContent.value = device
  deviceDialogVisible.value = true
}

function formatDateTime(val) {
  if (!val) return '-'
  return new Date(val).toLocaleString('zh-CN')
}

function loadLogList() {
  loading.value = true
  const params = { current: pagination.currentPage, size: pagination.size }
  if (searchUserId.value) {
    params.userId = searchUserId.value
  }
  if (searchTimeRange.value && searchTimeRange.value.length === 2) {
    params.startTime = searchTimeRange.value[0]
    params.endTime = searchTimeRange.value[1]
  }
  getLogPage(params).then(res => {
    if (res.code === 200 && res.data && Array.isArray(res.data.records)) {
      logList.value = res.data.records
      pagination.total = res.data.total || 0
    } else if (Array.isArray(res)) {
      logList.value = res
      pagination.total = res.length
    } else if (res.code === 200 && Array.isArray(res.data)) {
      logList.value = res.data
      pagination.total = res.data.length
    } else {
      logList.value = []
      pagination.total = 0
      ElMessage.error(res.message || '获取日志失败')
    }
  }).catch(() => {
    logList.value = []
    pagination.total = 0
    ElMessage.error('获取日志失败')
  }).finally(() => {
    loading.value = false
  })
}

function handlePageChange(page) {
  pagination.currentPage = page
  loadLogList()
}

onMounted(() => {
  loadLogList()
})
</script>

<style scoped>
.log-bg {
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
.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding: 10px;
  background: #f8f8f8;
}
</style> 