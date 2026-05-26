import request from './request'

export function getProductList(params) {
  return request.get('/web/product/list', { params })
}

export function getProductDetail(id) {
  return request.get(`/web/product/${id}`)
}

export function getProductCategories() {
  return request.get('/web/product/categories')
}
