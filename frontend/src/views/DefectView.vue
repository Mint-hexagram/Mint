<template>
  <div class="defect-bg">
    <div class="page-title">缺陷管理</div>
    <div class="wireframe">
      <!-- 搜索表单 -->
      <el-form :model="searchForm" inline class="search-form" :rules="searchRules" ref="searchFormRef">
        <el-form-item label="任务ID" prop="taskId">
          <el-input 
            v-model="searchForm.taskId" 
            placeholder="请输入任务ID" 
            clearable 
            style="width: 150px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="缺陷类型" prop="defectType">
          <el-select 
            v-model="searchForm.defectType" 
            placeholder="请选择缺陷类型" 
            clearable 
            style="width: 150px"
            filterable
          >
            <el-option label="裂缝" value="裂缝" />
            <el-option label="破损" value="破损" />
            <el-option label="变形" value="变形" />
            <el-option label="渗漏" value="渗漏" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="整改状态" prop="rectifyStatus">
          <el-select 
            v-model="searchForm.rectifyStatus" 
            placeholder="请选择整改状态" 
            clearable 
            style="width: 150px"
          >
            <el-option label="未整改" :value="0" />
            <el-option label="整改中" :value="1" />
            <el-option label="已整改" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否属实" prop="isReal">
          <el-select 
            v-model="searchForm.isReal" 
            placeholder="请选择是否属实" 
            clearable 
            style="width: 150px"
          >
            <el-option label="属实" :value="true" />
            <el-option label="不属实" :value="false" />
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
            <el-form-item label="严重程度">
              <el-select v-model="advancedSearchForm.severity" placeholder="请选择严重程度" clearable style="width: 150px">
                <el-option label="轻微" :value="1" />
                <el-option label="一般" :value="2" />
                <el-option label="严重" :value="3" />
                <el-option label="非常严重" :value="4" />
              </el-select>
            </el-form-item>
            <el-form-item label="缺陷位置">
              <el-input v-model="advancedSearchForm.position" placeholder="请输入缺陷位置" clearable style="width: 150px" />
            </el-form-item>
            <el-form-item label="上报时间">
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
          <el-button @click="handleExport" :disabled="defectList.length === 0">
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

      <!-- 数据表格 -->
      <el-table 
        :data="defectList" 
        v-loading="loading"
        border
        stripe
        style="width: 100%"
        empty-text="暂无数据"
      >
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="taskId" label="任务ID" width="80" />
        <el-table-column prop="defectType" label="缺陷类型" width="120" />
        <el-table-column prop="position" label="缺陷位置" width="120" />
        <el-table-column label="缺陷图片" width="100" align="center">
          <template #default="scope">
            <el-button 
              v-if="scope.row.imageUrl" 
              type="text" 
              @click="showImageModal(scope.row)"
            >
              查看图片
            </el-button>
            <span v-else>无图片</span>
          </template>
        </el-table-column>
        <el-table-column label="是否属实" width="80" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.isReal ? 'success' : 'danger'">
              {{ scope.row.isReal ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="severity" label="严重程度" width="100">
          <template #default="scope">
            <el-tag 
              :type="getSeverityType(scope.row.severity)"
              size="small"
            >
              {{ getSeverityText(scope.row.severity) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="length" label="缺陷长度" width="100" />
        <el-table-column prop="area" label="缺陷面积" width="100" />
        <el-table-column prop="count" label="缺陷数量" width="100" />
        <el-table-column prop="rectifyMethod" label="建议整改方式" width="150" />
        <el-table-column prop="reportTime" label="缺陷上报时间" width="150" />
        <el-table-column label="操作" width="120" align="center">
          <template #default="scope">
            <el-button type="text" @click="showImageModal(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 图片预览模态框 -->
    <el-dialog
      v-model="modalVisible"
      title="缺陷详情"
      width="60%"
      :before-close="closeModal"
    >
      <div class="modal-content">
        <div class="modal-image" v-if="modalRow.imageUrl">
          <img :src="modalRow.imageUrl" alt="缺陷图片" style="max-width: 100%; height: auto;" />
        </div>
        <div class="modal-image" v-else>
          <div style="text-align: center; padding: 50px; color: #999;">暂无图片</div>
        </div>
        <el-descriptions :column="2" border style="margin-top: 20px;">
          <el-descriptions-item label="缺陷类型">{{ modalRow.defectType }}</el-descriptions-item>
          <el-descriptions-item label="所属任务">{{ modalRow.taskId }}</el-descriptions-item>
          <el-descriptions-item label="缺陷位置">{{ modalRow.position }}</el-descriptions-item>
          <el-descriptions-item label="严重程度">
            <el-tag :type="getSeverityType(modalRow.severity)">
              {{ getSeverityText(modalRow.severity) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="缺陷长度">{{ modalRow.length }}</el-descriptions-item>
          <el-descriptions-item label="缺陷面积">{{ modalRow.area }}</el-descriptions-item>
          <el-descriptions-item label="缺陷数量">{{ modalRow.count }}</el-descriptions-item>
          <el-descriptions-item label="是否属实">
            <el-tag :type="modalRow.isReal ? 'success' : 'danger'">
              {{ modalRow.isReal ? '是' : '否' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="建议整改方式" :span="2">{{ modalRow.rectifyMethod }}</el-descriptions-item>
          <el-descriptions-item label="缺陷上报时间" :span="2">{{ modalRow.reportTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { getDefectPage } from '../api/defect'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, ArrowDown, ArrowUp, Download } from '@element-plus/icons-vue'

const modalVisible = ref(false)
const modalRow = ref({})
const defectList = ref([])
const loading = ref(false)
const searchFormRef = ref()
const showAdvancedSearch = ref(false)

// 搜索表单
const searchForm = reactive({
  taskId: '',
  defectType: '',
  rectifyStatus: '',
  isReal: ''
})

// 高级搜索表单
const advancedSearchForm = reactive({
  severity: '',
  position: '',
  dateRange: []
})

// 搜索验证规则
const searchRules = {
  taskId: [
    { pattern: /^\d*$/, message: '任务ID只能包含数字', trigger: 'blur' }
  ]
}

// 分页参数
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 计算是否有搜索条件
const hasSearchConditions = computed(() => {
  return Object.values(searchForm).some(value => value !== '' && value !== null && value !== undefined) ||
         Object.values(advancedSearchForm).some(value => {
           if (Array.isArray(value)) return value.length > 0
           return value !== '' && value !== null && value !== undefined
         })
})

// 计算当前激活的搜索条件
const activeSearchConditions = computed(() => {
  const conditions = {}
  
  if (searchForm.taskId) {
    conditions.taskId = { label: '任务ID', value: searchForm.taskId }
  }
  if (searchForm.defectType) {
    conditions.defectType = { label: '缺陷类型', value: searchForm.defectType }
  }
  if (searchForm.rectifyStatus !== '') {
    const statusMap = { 0: '未整改', 1: '整改中', 2: '已整改' }
    conditions.rectifyStatus = { label: '整改状态', value: statusMap[searchForm.rectifyStatus] }
  }
  if (searchForm.isReal !== '') {
    conditions.isReal = { label: '是否属实', value: searchForm.isReal ? '属实' : '不属实' }
  }
  if (advancedSearchForm.severity) {
    const severityMap = { 1: '轻微', 2: '一般', 3: '严重', 4: '非常严重' }
    conditions.severity = { label: '严重程度', value: severityMap[advancedSearchForm.severity] }
  }
  if (advancedSearchForm.position) {
    conditions.position = { label: '缺陷位置', value: advancedSearchForm.position }
  }
  if (advancedSearchForm.dateRange && advancedSearchForm.dateRange.length === 2) {
    conditions.dateRange = { label: '上报时间', value: `${advancedSearchForm.dateRange[0]} 至 ${advancedSearchForm.dateRange[1]}` }
  }
  
  return conditions
})

// 获取严重程度类型
function getSeverityType(severity) {
  const typeMap = {
    1: 'info',
    2: 'warning',
    3: 'danger',
    4: 'danger'
  }
  return typeMap[severity] || 'info'
}

// 获取严重程度文本
function getSeverityText(severity) {
  const textMap = {
    1: '轻微',
    2: '一般',
    3: '严重',
    4: '非常严重'
  }
  return textMap[severity] || '未知'
}

// 加载缺陷列表
function loadDefectList() {
  loading.value = true
  
  const params = {
    current: pagination.page,
    size: pagination.size
  }
  
  // 添加基础搜索条件
  if (searchForm.taskId) params.taskId = searchForm.taskId
  if (searchForm.defectType) params.defectType = searchForm.defectType
  if (searchForm.rectifyStatus !== '') params.rectifyStatus = searchForm.rectifyStatus
  if (searchForm.isReal !== '') params.isReal = searchForm.isReal

  // 添加高级搜索条件
  if (advancedSearchForm.severity) params.severity = advancedSearchForm.severity
  if (advancedSearchForm.position) params.position = advancedSearchForm.position
  if (advancedSearchForm.dateRange && advancedSearchForm.dateRange.length === 2) {
    params.startDate = advancedSearchForm.dateRange[0]
    params.endDate = advancedSearchForm.dateRange[1]
  }

  getDefectPage(params).then(res => {
    if (res.code === 200) {
      defectList.value = res.data.records || res.data.content || []
      pagination.total = res.data.total || res.data.totalElements || 0
      
      if (hasSearchConditions.value) {
        ElMessage.success(`搜索完成，共找到 ${pagination.total} 条记录`)
      }
    } else {
      ElMessage.error(res.message || '获取缺陷列表失败')
    }
  }).catch(error => {
    console.error('获取缺陷列表错误:', error)
    ElMessage.error('获取缺陷列表失败')
  }).finally(() => {
    loading.value = false
  })
}

// 搜索
function handleSearch() {
  searchFormRef.value?.validate((valid) => {
    if (valid) {
      pagination.page = 1
      loadDefectList()
    }
  })
}

// 高级搜索
function handleAdvancedSearch() {
  pagination.page = 1
  loadDefectList()
}

// 重置
function handleReset() {
  searchFormRef.value?.resetFields()
  Object.assign(searchForm, {
    taskId: '',
    defectType: '',
    rectifyStatus: '',
    isReal: ''
  })
  Object.assign(advancedSearchForm, {
    severity: '',
    position: '',
    dateRange: []
  })
  pagination.page = 1
  loadDefectList()
  ElMessage.info('搜索条件已重置')
}

// 移除搜索条件
function removeSearchCondition(key) {
  if (key === 'taskId') searchForm.taskId = ''
  if (key === 'defectType') searchForm.defectType = ''
  if (key === 'rectifyStatus') searchForm.rectifyStatus = ''
  if (key === 'isReal') searchForm.isReal = ''
  if (key === 'severity') advancedSearchForm.severity = ''
  if (key === 'position') advancedSearchForm.position = ''
  if (key === 'dateRange') advancedSearchForm.dateRange = []
  
  pagination.page = 1
  loadDefectList()
}

// 切换高级搜索
function toggleAdvancedSearch() {
  showAdvancedSearch.value = !showAdvancedSearch.value
}

// 导出
function handleExport() {
  ElMessage.info('导出功能开发中...')
}

// 刷新
function handleRefresh() {
  loadDefectList()
  ElMessage.success('数据已刷新')
}

// 分页大小改变
function handleSizeChange(size) {
  pagination.size = size
  pagination.page = 1
  loadDefectList()
}

// 当前页改变
function handleCurrentChange(page) {
  pagination.page = page
  loadDefectList()
}

// 显示图片模态框
const showImageModal = (row) => {
  modalRow.value = row
  modalVisible.value = true
}

// 关闭模态框
const closeModal = () => {
  modalVisible.value = false
}

onMounted(() => {
  loadDefectList()
})
</script>

<style scoped>
.defect-bg {
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

.search-form {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #666;
  background: #f8f8f8;
}

.advanced-search {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #ddd;
  background: #fafafa;
  border-top: none;
}

.search-status {
  margin-bottom: 15px;
  padding: 10px;
  background: #f0f9ff;
  border: 1px solid #b3d8ff;
  border-radius: 4px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding: 10px;
  border: 1px solid #666;
  background: #f8f8f8;
}

.toolbar-info {
  color: #666;
  font-size: 14px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.modal-content {
  max-height: 70vh;
  overflow-y: auto;
}

.modal-image {
  text-align: center;
  margin-bottom: 20px;
}
</style> 