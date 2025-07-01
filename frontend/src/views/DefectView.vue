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
        <el-form-item label="整改状态" prop="status">
          <el-select 
            v-model="searchForm.status" 
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

      <!-- 快速筛选 -->
      <div class="quick-filters">
        <el-radio-group v-model="quickFilter" @change="handleQuickFilterChange">
          <el-radio-button label="all">全部</el-radio-button>
          <el-radio-button label="today">今日新增</el-radio-button>
          <el-radio-button label="pending">待处理</el-radio-button>
        </el-radio-group>
      </div>

      <!-- 工具栏 -->
      <div class="toolbar">
        <div style="display: flex; gap: 10px;">
          <el-button type="primary" @click="handleAdd"><el-icon><Plus /></el-icon>新增缺陷</el-button>
          <el-button 
            type="success" 
            @click="handleBatchConfirm" 
            :disabled="selectedDefects.length === 0"
          >批量确认</el-button>
          <el-button 
            type="warning" 
            @click="handleBatchMarkRectified" 
            :disabled="selectedDefects.length === 0"
          >批量标记整改</el-button>
          <el-button @click="handleExport" :disabled="defectList.length === 0"><el-icon><Download /></el-icon>导出</el-button>
          <el-button @click="handleRefresh"><el-icon><Refresh /></el-icon>刷新</el-button>
        </div>
        <div class="toolbar-info">
          <span>共找到 {{ pagination.total }} 条记录</span>
        </div>
      </div>

      <!-- 数据表格 -->
      <div style="overflow-x: auto;">
        <el-table 
          :data="defectList" 
          v-loading="loading"
          border
          stripe
          style="min-width: 1200px; width: 100%"
          empty-text="暂无数据"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="defectId" label="缺陷ID" width="80">
            <template #default="scope">{{ scope.row.defectId || '-' }}</template>
          </el-table-column>
          <el-table-column prop="defectNo" label="缺陷编号" width="120">
            <template #default="scope">{{ scope.row.defectNo || '-' }}</template>
          </el-table-column>
          <el-table-column prop="defectType" label="缺陷类型" width="120">
            <template #default="scope">{{ scope.row.defectType || '-' }}</template>
          </el-table-column>
          <el-table-column prop="description" label="描述" width="200">
            <template #default="scope">{{ scope.row.description || '-' }}</template>
          </el-table-column>
          <el-table-column prop="severity" label="严重程度" width="100">
            <template #default="scope">
              <el-tag :type="getSeverityType(scope.row.severity)" size="small">
                {{ getSeverityText(scope.row.severity) || '-' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="isReal" label="是否属实" width="80" align="center">
            <template #default="scope">
              <el-tag :type="scope.row.isReal ? 'success' : 'danger'">
                {{ scope.row.isReal === 1 ? '是' : scope.row.isReal === 0 ? '否' : '-' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="foundTime" label="缺陷上报时间" width="150">
            <template #default="scope">{{ scope.row.foundTime || '-' }}</template>
          </el-table-column>
          <el-table-column prop="foundBy" label="上报人" width="80">
            <template #default="scope">{{ scope.row.foundBy || '-' }}</template>
          </el-table-column>
          <el-table-column prop="taskId" label="任务ID" width="80">
            <template #default="scope">{{ scope.row.taskId || '-' }}</template>
          </el-table-column>
          <el-table-column prop="foundMethod" label="发现方式" width="100">
            <template #default="scope">{{ scope.row.foundMethod || '-' }}</template>
          </el-table-column>
          <el-table-column prop="position" label="缺陷位置" width="120">
            <template #default="scope">{{ scope.row.position || '-' }}</template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="80">
            <template #default="scope">{{ scope.row.status || '-' }}</template>
          </el-table-column>
          <el-table-column prop="confirmBy" label="确认人" width="80">
            <template #default="scope">{{ scope.row.confirmBy || '-' }}</template>
          </el-table-column>
          <el-table-column prop="confirmTime" label="确认时间" width="150">
            <template #default="scope">{{ scope.row.confirmTime || '-' }}</template>
          </el-table-column>
          <el-table-column prop="handleBy" label="处理人" width="80">
            <template #default="scope">{{ scope.row.handleBy || '-' }}</template>
          </el-table-column>
          <el-table-column prop="handleStartTime" label="处理开始时间" width="150">
            <template #default="scope">{{ scope.row.handleStartTime || '-' }}</template>
          </el-table-column>
          <el-table-column prop="handleEndTime" label="处理结束时间" width="150">
            <template #default="scope">{{ scope.row.handleEndTime || '-' }}</template>
          </el-table-column>
          <el-table-column prop="handleResult" label="处理结果" width="200">
            <template #default="scope">{{ scope.row.handleResult || '-' }}</template>
          </el-table-column>
          <el-table-column prop="imageUrl" label="缺陷图片" width="100" align="center">
            <template #default="scope">
              <el-button v-if="scope.row.imageUrl" type="text" @click="showImageModal(scope.row)">查看图片</el-button>
              <span v-else>无图片</span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" width="150">
            <template #default="scope">{{ scope.row.remark || '-' }}</template>
          </el-table-column>
          <el-table-column label="操作" width="220" align="center" fixed="right">
            <template #default="scope">
              <el-button type="text" @click="showDetails(scope.row)">详情</el-button>
              <el-button type="text" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button 
                v-if="scope.row.isReal !== 1"
                type="text" 
                @click="handleConfirm(scope.row)"
              >确认</el-button>
              <el-button 
                v-if="scope.row.status !== 2"
                type="text" 
                @click="handleMarkRectified(scope.row)"
              >标记整改</el-button>
              <el-button type="text" @click="handleDelete(scope.row)" style="color: #f56c6c;">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

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
          @update:page-size="val => pagination.size = val"
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
          <el-descriptions-item label="缺陷上报时间" :span="2">{{ modalRow.foundTime }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>

    <!-- 新增/编辑缺陷对话框 -->
    <el-dialog v-model="editVisible" :title="editMode === 'add' ? '新增缺陷' : '编辑缺陷'" width="600px" @close="resetEditForm">
      <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="120px">
        <el-form-item label="缺陷编号" prop="defectNo">
          <el-input v-model="editForm.defectNo" placeholder="请输入缺陷编号" />
        </el-form-item>
        <el-form-item label="任务ID" prop="taskId">
          <el-select v-model="editForm.taskId" placeholder="请选择任务">
            <el-option v-for="item in taskOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="缺陷类型" prop="defectType">
          <el-input v-model="editForm.defectType" placeholder="请输入缺陷类型" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="editForm.description" placeholder="请输入缺陷描述" />
        </el-form-item>
        <el-form-item label="严重程度" prop="severity">
          <el-select v-model="editForm.severity" placeholder="请选择">
            <el-option label="轻微" :value="1" />
            <el-option label="一般" :value="2" />
            <el-option label="严重" :value="3" />
            <el-option label="非常严重" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否属实" prop="isReal">
          <el-select v-model="editForm.isReal" placeholder="请选择">
            <el-option label="属实" :value="1" />
            <el-option label="不属实" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="缺陷上报时间" prop="foundTime">
          <el-date-picker v-model="editForm.foundTime" type="datetime" placeholder="请选择上报时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="上报人" prop="foundBy" required>
          <el-select v-model="editForm.foundBy" placeholder="请选择上报人">
            <el-option v-for="item in userOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="发现方式" prop="foundMethod" required>
          <el-input v-model="editForm.foundMethod" placeholder="请输入发现方式" />
        </el-form-item>
        <el-form-item label="缺陷位置" prop="position">
          <el-input v-model="editForm.position" placeholder="请输入缺陷位置" />
        </el-form-item>
        <el-form-item label="整改状态" prop="status">
          <el-select v-model="editForm.status" placeholder="请选择">
            <el-option label="未整改" :value="0" />
            <el-option label="整改中" :value="1" />
            <el-option label="已整改" :value="2" />
            <el-option label="其它" :value="3" />
            <el-option label="已关闭" :value="4" />
            <el-option label="已作废" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="确认人" prop="confirmBy" required>
          <el-select v-model="editForm.confirmBy" placeholder="请选择确认人">
            <el-option v-for="item in userOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="确认时间" prop="confirmTime">
          <el-date-picker v-model="editForm.confirmTime" type="datetime" placeholder="请选择确认时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="处理人" prop="handleBy">
          <el-select v-model="editForm.handleBy" placeholder="请选择处理人">
            <el-option v-for="item in userOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理开始时间" prop="handleStartTime">
          <el-date-picker v-model="editForm.handleStartTime" type="datetime" placeholder="请选择处理开始时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="处理结束时间" prop="handleEndTime">
          <el-date-picker v-model="editForm.handleEndTime" type="datetime" placeholder="请选择处理结束时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="处理结果" prop="handleResult">
          <el-input v-model="editForm.handleResult" placeholder="请输入处理结果" />
        </el-form-item>
        <el-form-item label="建议整改方式" prop="rectifyMethod">
          <el-input v-model="editForm.rectifyMethod" type="textarea" placeholder="请输入建议的整改方式" />
        </el-form-item>
        <el-form-item label="缺陷图片" prop="imageUrl">
          <el-upload
            class="avatar-uploader"
            action="/api/defects/upload"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :before-upload="beforeUpload"
          >
            <img v-if="editForm.imageUrl" :src="editForm.imageUrl" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
          <el-input v-model="editForm.imageUrl" style="display: none;" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="editForm.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEditForm" :loading="editLoading">保存</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="缺陷详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="缺陷ID">{{ selectedDefect.defectId }}</el-descriptions-item>
        <el-descriptions-item label="缺陷编号">{{ selectedDefect.defectNo }}</el-descriptions-item>
        <el-descriptions-item label="缺陷类型">{{ selectedDefect.defectType }}</el-descriptions-item>
        <el-descriptions-item label="描述">{{ selectedDefect.description }}</el-descriptions-item>
        <el-descriptions-item label="严重程度">
          <el-tag :type="getSeverityType(selectedDefect.severity)">{{ getSeverityText(selectedDefect.severity) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="是否属实">
          <el-tag :type="getIsRealType(selectedDefect.isReal)">{{ getIsRealText(selectedDefect.isReal) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="缺陷上报时间">{{ selectedDefect.foundTime }}</el-descriptions-item>
        <el-descriptions-item label="上报人">{{ selectedDefect.foundBy }}</el-descriptions-item>
        <el-descriptions-item label="任务ID">{{ selectedDefect.taskId }}</el-descriptions-item>
        <el-descriptions-item label="发现方式">{{ selectedDefect.foundMethod }}</el-descriptions-item>
        <el-descriptions-item label="缺陷位置">{{ selectedDefect.position }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ getRectifyStatusText(selectedDefect.status) }}</el-descriptions-item>
        <el-descriptions-item label="确认人">{{ selectedDefect.confirmBy }}</el-descriptions-item>
        <el-descriptions-item label="确认时间">{{ selectedDefect.confirmTime }}</el-descriptions-item>
        <el-descriptions-item label="处理人">{{ selectedDefect.handleBy }}</el-descriptions-item>
        <el-descriptions-item label="处理开始时间">{{ selectedDefect.handleStartTime }}</el-descriptions-item>
        <el-descriptions-item label="处理结束时间">{{ selectedDefect.handleEndTime }}</el-descriptions-item>
        <el-descriptions-item label="处理结果">{{ selectedDefect.handleResult }}</el-descriptions-item>
        <el-descriptions-item label="缺陷图片">
          <el-image v-if="selectedDefect.imageUrl" :src="selectedDefect.imageUrl" :preview-src-list="[selectedDefect.imageUrl]" style="width: 100px; height: 100px;" fit="cover"></el-image>
          <span v-else>无</span>
        </el-descriptions-item>
        <el-descriptions-item label="备注">{{ selectedDefect.remark }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button type="primary" @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { getDefectPage, createDefect, updateDefect, deleteDefect, confirmDefect, markDefectRectified, batchConfirmDefects, batchMarkDefectRectified } from '../api/defect'
import { getUserList } from '../api/user'
import { getTaskList } from '../api/task'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, ArrowDown, ArrowUp, Download, Plus } from '@element-plus/icons-vue'

const modalVisible = ref(false)
const modalRow = ref({})
const defectList = ref([])
const loading = ref(false)
const searchFormRef = ref()
const showAdvancedSearch = ref(false)
const selectedDefects = ref([])
const quickFilter = ref('all')
const userOptions = ref([])
const taskOptions = ref([])

// 搜索表单
const searchForm = reactive({
  taskId: '',
  defectType: '',
  status: '',
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
  if (searchForm.status !== '') {
    const statusMap = { 0: '未整改', 1: '整改中', 2: '已整改' }
    conditions.status = { label: '整改状态', value: statusMap[searchForm.status] }
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
  loading.value = true;
  const params = {
    current: pagination.page,
    size: pagination.size
  };
  if (searchForm.taskId) params.taskId = searchForm.taskId;
  if (searchForm.defectType) params.defectType = searchForm.defectType;
  if (searchForm.status !== '') params.status = searchForm.status;
  if (searchForm.isReal !== '') params.isReal = searchForm.isReal;
  if (advancedSearchForm.severity) params.severity = advancedSearchForm.severity;
  if (searchForm.position) params.position = searchForm.position;
  if (advancedSearchForm.dateRange && advancedSearchForm.dateRange.length === 2) {
    params.startDate = advancedSearchForm.dateRange[0];
    params.endDate = advancedSearchForm.dateRange[1];
  }
  getDefectPage(params).then(res => {
    if (res.code === 200) {
      let records = res.data.records || res.data.content || [];
      // 本地过滤待处理
      if (quickFilter.value === 'pending') {
        records = records.filter(item => item.status !== 4 && item.status !== 5);
      }
      defectList.value = records;
      pagination.total = res.data.total || res.data.totalElements || records.length;
      if (hasSearchConditions.value) {
        ElMessage.success(`搜索完成，共找到 ${pagination.total} 条记录`);
      }
    } else {
      ElMessage.error(res.message || '获取缺陷列表失败');
    }
  }).catch(error => {
    console.error('获取缺陷列表错误:', error);
    ElMessage.error('获取缺陷列表失败');
  }).finally(() => {
    loading.value = false;
  });
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
    status: '',
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
  if (key === 'status') searchForm.status = ''
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

const editVisible = ref(false)
const editMode = ref('add')
const editFormRef = ref()
const editLoading = ref(false)
let editForm = reactive({
  defectId: null,
  taskId: null,
  defectType: '',
  position: '',
  isReal: 1,
  severity: 1,
  status: 0,
  rectifyMethod: '',
  imageUrl: ''
})

const editRules = {
  taskId: [{ required: true, message: '任务ID不能为空', trigger: 'blur' }],
  defectType: [{ required: true, message: '缺陷类型不能为空', trigger: 'blur' }],
  position: [{ required: true, message: '缺陷位置不能为空', trigger: 'blur' }],
  foundBy: [{ required: true, message: '上报人不能为空', trigger: 'change' }],
  foundMethod: [{ required: true, message: '发现方式不能为空', trigger: 'blur' }],
  confirmBy: [{ required: true, message: '确认人不能为空', trigger: 'change' }],
}

function resetEditForm() {
  editForm.defectId = null
  editForm.taskId = null
  editForm.defectType = ''
  editForm.position = ''
  editForm.isReal = 1
  editForm.severity = 1
  editForm.status = 0
  editForm.rectifyMethod = ''
  editForm.imageUrl = ''
  editFormRef.value?.clearValidate()
}

function handleAdd() {
  resetEditForm()
  editMode.value = 'add'
  editVisible.value = true
}

function handleEdit(row) {
  resetEditForm()
  Object.assign(editForm, row)
  editMode.value = 'edit'
  editVisible.value = true
}

function submitEditForm() {
  editFormRef.value.validate(async (valid) => {
    if (!valid) return
    editLoading.value = true
    try {
      if (editMode.value === 'add') {
        // 新增时不传defectId字段
        const { defectId, ...addData } = editForm;
        await createDefect(addData)
        ElMessage.success('新增成功')
      } else {
        await updateDefect(editForm.defectId, editForm)
        ElMessage.success('更新成功')
      }
      editVisible.value = false
      loadDefectList()
    } catch (error) {
      ElMessage.error('操作失败')
    } finally {
      editLoading.value = false
    }
  })
}

function handleDelete(row) {
  ElMessageBox.confirm(`确定要删除位于 "${row.position}" 的缺陷吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await deleteDefect(row.defectId)
      ElMessage.success('删除成功')
      loadDefectList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

const detailVisible = ref(false);
const selectedDefect = ref({});

function showDetails(row) {
  selectedDefect.value = { ...row };
  detailVisible.value = true;
}

function getIsRealType(isReal) {
  return isReal === 1 ? 'success' : 'danger';
}

function getIsRealText(isReal) {
  return isReal === 1 ? '属实' : '不属实';
}

function getRectifyStatusType(status) {
    const typeMap = { 0: 'info', 1: 'warning', 2: 'success' };
    return typeMap[status] || 'info';
}

function getRectifyStatusText(status) {
    const textMap = { 0: '未整改', 1: '整改中', 2: '已整改' };
    return textMap[status] || '未知';
}

function handleSelectionChange(selection) {
  selectedDefects.value = selection;
}

async function handleConfirm(row) {
  try {
    await confirmDefect(row.defectId);
    ElMessage.success('确认成功');
    loadDefectList();
  } catch (error) {
    ElMessage.error('确认失败');
  }
}

async function handleMarkRectified(row) {
  try {
    await markDefectRectified(row.defectId);
    ElMessage.success('标记成功');
    loadDefectList();
  } catch (error) {
    ElMessage.error('标记失败');
  }
}

function handleBatchConfirm() {
  const ids = selectedDefects.value.map(item => item.defectId);
  if (ids.length === 0) {
    ElMessage.warning('请至少选择一项');
    return;
  }
  ElMessageBox.confirm(`确定要批量确认选中的 ${ids.length} 项缺陷吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await batchConfirmDefects(ids);
      ElMessage.success('批量确认成功');
      loadDefectList();
    } catch (error) {
      ElMessage.error('批量确认失败');
    }
  });
}

function handleBatchMarkRectified() {
  const ids = selectedDefects.value.map(item => item.defectId);
  if (ids.length === 0) {
    ElMessage.warning('请至少选择一项');
    return;
  }
  ElMessageBox.confirm(`确定要批量标记选中的 ${ids.length} 项为已整改吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await batchMarkDefectRectified(ids);
      ElMessage.success('批量标记成功');
      loadDefectList();
    } catch (error) {
      ElMessage.error('批量标记失败');
    }
  });
}

function handleQuickFilterChange(value) {
  searchForm.taskId = '';
  searchForm.defectType = '';
  searchForm.status = '';
  searchForm.isReal = '';
  advancedSearchForm.severity = '';
  searchForm.position = '';
  advancedSearchForm.dateRange = [];

  if (value === 'today') {
    const today = new Date();
    const year = today.getFullYear();
    const month = (today.getMonth() + 1).toString().padStart(2, '0');
    const day = today.getDate().toString().padStart(2, '0');
    const todayStr = `${year}-${month}-${day}`;
    advancedSearchForm.dateRange = [todayStr, todayStr];
  } else if (value === 'pending') {
    // 不传status参数，前端本地过滤
    searchForm.status = '';
  }
  pagination.page = 1;
  loadDefectList();
}

function handleUploadSuccess(response) {
  if (response.code === 200) {
    editForm.imageUrl = response.data
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error(response.message || '图片上传失败')
  }
}

function beforeUpload(rawFile) {
  const isJpgOrPng = rawFile.type === 'image/jpeg' || rawFile.type === 'image/png'
  if (!isJpgOrPng) {
    ElMessage.error('上传图片只能是 JPG/PNG 格式!')
    return false
  }
  const isLt2M = rawFile.size / 1024 / 1024 < 2
  if (!isLt2M) {
    ElMessage.error('上传图片大小不能超过 2MB!')
    return false
  }
  return true
}

onMounted(() => {
  loadDefectList()
  // 拉取用户列表，带上大页码参数，避免分页导致无数据
  getUserList({ page: 1, size: 1000 }).then(res => {
    if (res.code === 200) {
      userOptions.value = (res.data?.records || []).map(u => ({ label: u.username + '（' + u.userId + '）', value: u.userId }))
    }
  })
  // 拉取任务列表
  getTaskList().then(res => {
    if (res.code === 200) {
      taskOptions.value = (res.data || []).map(t => ({ label: (t.taskName ? t.taskName : t.taskId) + '（' + t.taskId + '）', value: t.taskId }))
    }
  })
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

.quick-filters {
  margin-bottom: 20px;
}

.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style> 