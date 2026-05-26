import request from './request'

export function getRoleList(params) {
  return request({ url: '/admin/role/list', method: 'get', params })
}

export function getRoleDetail(id) {
  return request({ url: `/admin/role/${id}`, method: 'get' })
}

export function addRole(data) {
  return request({ url: '/admin/role', method: 'post', data })
}

export function updateRole(data) {
  return request({ url: '/admin/role', method: 'put', data })
}

export function deleteRole(id) {
  return request({ url: `/admin/role/${id}`, method: 'delete' })
}

export function getRoleAll() {
  return request({ url: '/admin/role/all', method: 'get' })
}
