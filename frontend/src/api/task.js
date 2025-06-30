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

// 更新任务
export function updateTask(id, data) {
  return request({ url: `/api/tasks/${id}`, method: 'put', data })
}

// 删除任务
export function deleteTask(id) {
  return request({ url: `/api/tasks/${id}`, method: 'delete' })
}

// 导出任务
// 返回Excel文件流
export function exportTasks() {
  return request({
    url: '/api/tasks/export',
    method: 'get',
    responseType: 'blob'
  }).then(res => {
    // 兼容前端直接下载docx
    if (res instanceof Blob) {
      res.name = '任务列表.docx'
    }
    return res
  })
} 