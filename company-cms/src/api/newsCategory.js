import request from './request'

export function getNewsCategoryList(params) {
  return request({ url: '/admin/news-category/list', method: 'get', params })
}

export function getNewsCategoryAll() {
  return request({ url: '/admin/news-category/all', method: 'get' })
}

export function getNewsCategoryDetail(id) {
  return request({ url: `/admin/news-category/${id}`, method: 'get' })
}

export function addNewsCategory(data) {
  return request({ url: '/admin/news-category', method: 'post', data })
}

export function updateNewsCategory(data) {
  return request({ url: '/admin/news-category', method: 'put', data })
}

export function deleteNewsCategory(id) {
  return request({ url: `/admin/news-category/${id}`, method: 'delete' })
}
