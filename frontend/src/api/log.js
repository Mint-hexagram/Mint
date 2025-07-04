import request from './request'

// 获取系统日志全部列表（临时方案，无分页）
export function getLogPage() {
  return request({
    url: '/api/sys-oper-log',
    method: 'get'
  })
} 