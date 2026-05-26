import request from './request'

export function getBannerList(params) {
  return request({ url: '/admin/banner/list', method: 'get', params })
}

export function getBannerDetail(id) {
  return request({ url: `/admin/banner/${id}`, method: 'get' })
}

export function addBanner(data) {
  return request({ url: '/admin/banner', method: 'post', data })
}

export function updateBanner(data) {
  return request({ url: '/admin/banner', method: 'put', data })
}

export function deleteBanner(id) {
  return request({ url: `/admin/banner/${id}`, method: 'delete' })
}

export function updateBannerStatus(id, status) {
  return request({ url: `/admin/banner/${id}/status`, method: 'put', data: { status } })
}
