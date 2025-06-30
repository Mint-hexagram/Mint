import request from './request'

// 获取缺陷列表（不分页）
export function getDefectList(params) {
  return request({ url: '/api/defect-info', method: 'get', params })
}

// 获取缺陷分页列表
export function getDefectPage(params) {
  return request({
    url: '/api/defect-info/page',
    method: 'get',
    params
  });
}

// 新增缺陷
export function createDefect(data) {
  return request({
    url: '/api/defect-info',
    method: 'post',
    data
  });
}

// 更新缺陷
export function updateDefect(id, data) {
  return request({
    url: `/api/defect-info/${id}`,
    method: 'put',
    data
  });
}

// 删除缺陷
export function deleteDefect(id) {
  return request({
    url: `/api/defect-info/${id}`,
    method: 'delete'
  });
}

// 确认缺陷
export function confirmDefect(id) {
  return request({
    url: `/api/defect-info/${id}/confirm`,
    method: 'post'
  });
}

// 标记为已整改
export function markDefectRectified(id) {
  return request({
    url: `/api/defect-info/${id}/rectify`,
    method: 'post'
  });
}

// 批量确认
export function batchConfirmDefects(ids) {
  return request({
    url: '/api/defect-info/batch-confirm',
    method: 'post',
    data: ids
  });
}

// 批量标记为已整改
export function batchMarkDefectRectified(ids) {
  return request({
    url: '/api/defect-info/batch-rectify',
    method: 'post',
    data: ids
  });
} 