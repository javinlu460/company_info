import request from './request'

export function getContactInfo() {
  return request.get('/web/contact/info')
}

export function submitMessage(data) {
  return request.post('/web/contact/message', data)
}
