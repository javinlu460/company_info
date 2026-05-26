import request from './request'

export function getConfigList() {
  return request({ url: '/admin/config/list', method: 'get' })
}

export function getConfigByKey(key) {
  return request({ url: `/admin/config/${key}`, method: 'get' })
}

export function updateConfig(data) {
  return request({ url: '/admin/config', method: 'put', data })
}

export function batchUpdateConfig(data) {
  return request({ url: '/admin/config/batch', method: 'put', data })
}
