import request from './request'

// 参数列表
export function getConfigList(params) {
  return request({ url: '/api/config/list', method: 'get', params })
}
// 新增参数
export function addConfig(data) {
  return request({ url: '/api/config', method: 'post', data })
}
// 修改参数
export function updateConfig(data) {
  return request({ url: '/api/config', method: 'put', data })
}
// 删除参数
export function deleteConfig(configId) {
  return request({ url: `/api/config/${configId}`, method: 'delete' })
}
// 刷新缓存
export function refreshConfigCache() {
  return request({ url: '/api/config/refresh-cache', method: 'post' })
} 