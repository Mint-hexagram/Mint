<template>
  <el-dialog v-model="visible" title="选择人员" width="600px" @close="handleClose">
    <el-input v-model="search" placeholder="搜索姓名/账号" @input="fetchUsers" style="margin-bottom: 10px;" />
    <el-table :data="userList" highlight-current-row @current-change="handleSelect" :row-key="row => row.userId" style="width: 100%">
      <el-table-column prop="realName" label="姓名" />
      <el-table-column prop="username" label="账号" />
      <el-table-column prop="deptName" label="部门" />
    </el-table>
    <el-pagination
      v-model:current-page="page"
      :page-size="size"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="fetchUsers"
      style="margin-top: 10px; text-align: right;"
    />
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" :disabled="!selectedUser" @click="handleConfirm">确定</el-button>
    </template>
  </el-dialog>
</template>
<script setup>
import { ref, watch } from 'vue'
import { getUserList } from '../api/user'

const props = defineProps({
  modelValue: Boolean,
})
const emit = defineEmits(['update:modelValue', 'select'])

const visible = ref(false)
const search = ref('')
const userList = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)
const selectedUser = ref(null)

watch(() => props.modelValue, v => {
  visible.value = v
  if (v) fetchUsers()
})

function fetchUsers() {
  getUserList({ page: page.value, size: size.value, keyword: search.value }).then(res => {
    if (res.code === 200 && res.data) {
      userList.value = res.data.records || res.data.list || []
      total.value = res.data.total || 0
    }
  })
}
function handleSelect(row) {
  selectedUser.value = row
}
function handleConfirm() {
  emit('select', selectedUser.value)
  handleClose()
}
function handleClose() {
  emit('update:modelValue', false)
  selectedUser.value = null
}
</script> 