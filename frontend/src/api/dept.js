import request from './request'

// 部门列表（树结构）
export function getDeptTree(params) {
  return request.get('/api/dept/tree', { params })
}
// 新增部门
export function addDept(data) {
  return request.post('/api/dept', data)
}
// 修改部门
export function updateDept(data) {
  return request.put('/api/dept', data)
}
// 删除部门
export function deleteDept(id) {
  return request.delete(`/api/dept/${id}`)
} 