import request from './request'

export function getFaqList() {
  return request.get('/web/faq/list')
}

export function getFaqCategories() {
  return request.get('/web/faq/categories')
}
