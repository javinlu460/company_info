import request from './request'

export function getCaseList() {
  return request.get('/web/case/list')
}

export function getCaseDetail(id) {
  return request.get(`/web/case/${id}`)
}
