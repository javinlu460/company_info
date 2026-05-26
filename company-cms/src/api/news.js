import request from './request'

export function getNewsList(params) {
  return request({ url: '/admin/news/list', method: 'get', params })
}

export function getNewsDetail(id) {
  return request({ url: `/admin/news/${id}`, method: 'get' })
}

export function addNews(data) {
  return request({ url: '/admin/news', method: 'post', data })
}

export function updateNews(data) {
  return request({ url: '/admin/news', method: 'put', data })
}

export function deleteNews(id) {
  return request({ url: `/admin/news/${id}`, method: 'delete' })
}

export function updateNewsStatus(id, status) {
  return request({ url: `/admin/news/${id}/status`, method: 'put', data: { status } })
}
