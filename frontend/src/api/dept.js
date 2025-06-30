import request from './request'

// 部门列表（树结构）
export function getDeptTree(params) {
  return request({ url: '/api/sys-dept/tree', method: 'get', params })
}
// 新增部门
export function addDept(data) {
  return request({ url: '/api/sys-dept', method: 'post', data })
}
// 修改部门
export function updateDept(data) {
  return request({ url: '/api/sys-dept', method: 'put', data })
}
// 删除部门
export function deleteDept(deptId) {
  return request({ url: `/api/sys-dept/${deptId}`, method: 'delete' })
} 