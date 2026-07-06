import request from './request'

export function getFaqList(params) {
  return request({ url: '/admin/faq/list', method: 'get', params })
}

export function getFaqDetail(id) {
  return request({ url: `/admin/faq/${id}`, method: 'get' })
}

export function addFaq(data) {
  return request({ url: '/admin/faq', method: 'post', data })
}

export function updateFaq(data) {
  return request({ url: '/admin/faq', method: 'put', data })
}

export function deleteFaq(id) {
  return request({ url: `/admin/faq/${id}`, method: 'delete' })
}
