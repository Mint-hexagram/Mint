import request from './request'

// 角色列表
export function getRoleList(params) {
  return request({ url: '/api/sys-role/list', method: 'get', params })
}
// 新增角色
export function addRole(data) {
  return request({ url: '/api/sys-role', method: 'post', data })
}
// 修改角色
export function updateRole(data) {
  return request({ url: '/api/sys-role', method: 'put', data })
}
// 删除角色
export function deleteRole(roleId) {
  return request({ url: `/api/sys-role/${roleId}`, method: 'delete' })
}
// 修改角色状态
export function updateRoleStatus(roleId, status) {
  return request({ url: `/api/sys-role/${roleId}/status`, method: 'patch', data: { status } })
}
// 分配权限（菜单）
export function assignMenus(roleId, menuIds) {
  return request({ url: `/api/sys-role/${roleId}/menus`, method: 'post', data: { menuIds } })
} 