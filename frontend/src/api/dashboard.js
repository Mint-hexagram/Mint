import request from './request'

// 获取仪表盘统计数据
export function getDashboardSummary() {
  return request({ url: '/api/dashboard/summary', method: 'get' })
}

// 获取任务统计数据
export function getTaskStats() {
  return request({ url: '/api/dashboard/task-stats', method: 'get' })
}

// 获取缺陷统计数据
export function getDefectStats() {
  return request({ url: '/api/dashboard/defect-stats', method: 'get' })
}

// 获取用户统计数据
export function getUserStats() {
  return request({ url: '/api/dashboard/user-stats', method: 'get' })
} 