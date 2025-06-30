import request from './request'

// 用户列表
export function getUserList(params) {
  return request({ url: '/api/sys-user/list', method: 'get', params })
}
// 新增用户
export function addUser(data) {
  return request({ url: '/api/sys-user', method: 'post', data })
}
// 修改用户
export function updateUser(data) {
  return request({ url: '/api/sys-user', method: 'put', data })
}
// 删除用户
export function deleteUser(userId) {
  return request({ url: `/api/sys-user/${userId}`, method: 'delete' })
}
// 修改用户状态
export function updateUserStatus(userId, status) {
  return request({ url: `/api/sys-user/${userId}/status`, method: 'patch', data: { status } })
}
// 重置密码
export function resetPassword(userId) {
  return request({ url: `/api/sys-user/${userId}/reset-password`, method: 'post' })
}
// 分配角色
export function assignRoles(userId, roleIds) {
  return request({ url: `/api/sys-user/${userId}/roles`, method: 'post', data: { roleIds } })
} 