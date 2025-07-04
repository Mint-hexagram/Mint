import request from './request'

// 用户列表
export function getUserList(params) {
  return request.get('/api/user/list', { params })
}
// 新增用户
export function addUser(data) {
  return request.post('/api/user', data)
}
// 修改用户
export function updateUser(data) {
  return request.put('/api/user', data)
}
// 删除用户
export function deleteUser(id) {
  return request.delete(`/api/user/${id}`)
}
// 修改用户状态
export function updateUserStatus(userId, status) {
  return request({ url: `/api/sys-user/${userId}/status`, method: 'patch', data: { status } })
}
// 重置密码
export function resetPassword(userId, newPassword) {
  return request({ url: `/api/user/${userId}/reset-password`, method: 'post', data: newPassword ? { newPassword } : {} })
}
// 分配角色
export function assignRoles(userId, roleIds) {
  return request({ url: `/api/user/${userId}/roles`, method: 'post', data: { roleIds } })
}
// 获取用户角色ID
export function getUserRoleIds(userId) {
  return request({ url: `/api/user/${userId}/role-ids`, method: 'get' })
} 