import request from './request'

export function getMessageList(params) {
  return request({ url: '/admin/message/list', method: 'get', params })
}

export function getMessageDetail(id) {
  return request({ url: `/admin/message/${id}`, method: 'get' })
}

export function markMessageRead(id) {
  return request({ url: `/admin/message/${id}/read`, method: 'put' })
}

export function deleteMessage(id) {
  return request({ url: `/admin/message/${id}`, method: 'delete' })
}
