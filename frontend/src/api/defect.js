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