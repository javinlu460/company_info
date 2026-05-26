import request from './request'

export function getMenuList(params) {
  return request({ url: '/admin/menu/list', method: 'get', params })
}

export function getMenuTree() {
  return request({ url: '/admin/menu/tree', method: 'get' })
}

export function getMenuDetail(id) {
  return request({ url: `/admin/menu/${id}`, method: 'get' })
}

export function addMenu(data) {
  return request({ url: '/admin/menu', method: 'post', data })
}

export function updateMenu(data) {
  return request({ url: '/admin/menu', method: 'put', data })
}

export function deleteMenu(id) {
  return request({ url: `/admin/menu/${id}`, method: 'delete' })
}
