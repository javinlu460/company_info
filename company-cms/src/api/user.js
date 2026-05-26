import request from './request'

export function getUserList(params) {
  return request({ url: '/admin/user/list', method: 'get', params })
}

export function getUserDetail(id) {
  return request({ url: `/admin/user/${id}`, method: 'get' })
}

export function addUser(data) {
  return request({ url: '/admin/user', method: 'post', data })
}

export function updateUser(data) {
  return request({ url: '/admin/user', method: 'put', data })
}

export function deleteUser(id) {
  return request({ url: `/admin/user/${id}`, method: 'delete' })
}

export function resetUserPassword(id, password) {
  return request({ url: `/admin/user/${id}/resetPassword`, method: 'put', data: { password } })
}

export function updateUserStatus(id, status) {
  return request({ url: `/admin/user/${id}/status`, method: 'put', data: { status } })
}
