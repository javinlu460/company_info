import request from './request'

export function getCaseList(params) {
  return request({ url: '/admin/case/list', method: 'get', params })
}

export function getCaseDetail(id) {
  return request({ url: `/admin/case/${id}`, method: 'get' })
}

export function addCase(data) {
  return request({ url: '/admin/case', method: 'post', data })
}

export function updateCase(data) {
  return request({ url: '/admin/case', method: 'put', data })
}

export function deleteCase(id) {
  return request({ url: `/admin/case/${id}`, method: 'delete' })
}
