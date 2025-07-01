import request from './request'

// 菜单列表（树结构）
export function getMenuTree(params) {
  return request({ url: '/api/sys-menu/tree', method: 'get', params })
}
// 新增菜单
export function addMenu(data) {
  return request({ url: '/api/sys-menu', method: 'post', data })
}
// 修改菜单
export function updateMenu(data) {
  return request({ url: '/api/sys-menu', method: 'put', data })
}
// 删除菜单
export function deleteMenu(menuId) {
  return request({ url: `/api/sys-menu/${menuId}`, method: 'delete' })
} 