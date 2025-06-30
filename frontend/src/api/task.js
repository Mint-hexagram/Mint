import request from './request'

// 获取任务列表（分页）
export function getTaskPage(params) {
  return request({ url: '/api/tasks', method: 'get', params })
}

// 获取所有任务列表
export function getTaskList(params) {
  return request({ url: '/api/tasks/all', method: 'get', params })
}

// 根据ID获取任务详情
export function getTaskById(id) {
  return request({ url: `/api/tasks/${id}`, method: 'get' })
}

// 创建任务
export function createTask(data) {
  return request({ url: '/api/tasks', method: 'post', data })
}

// 更新任务（被覆盖，兼容 TaskView.vue 用法）
export function updateTask(data) {
  return request({
    url: `/api/tasks/${data.taskId}`,
    method: 'put',
    data
  });
}

// 删除任务
export function deleteTask(id) {
  return request({ url: `/api/tasks/${id}`, method: 'delete' })
}

// 兼容 TaskView.vue 的 addTask 用法
export function addTask(data) {
  return createTask(data);
} 