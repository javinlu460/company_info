import request from './request'

export function getNewsList(params) {
  return request.get('/web/news/list', { params })
}

export function getNewsDetail(id) {
  return request.get(`/web/news/${id}`)
}

export function getNewsCategories() {
  return request.get('/web/news/categories')
}
