import request from './request'

export function getProductCategoryTree() {
  return request({ url: '/admin/product-category/tree', method: 'get' })
}

export function getProductCategoryList(params) {
  return request({ url: '/admin/product-category/list', method: 'get', params })
}

export function getProductCategoryDetail(id) {
  return request({ url: `/admin/product-category/${id}`, method: 'get' })
}

export function addProductCategory(data) {
  return request({ url: '/admin/product-category', method: 'post', data })
}

export function updateProductCategory(data) {
  return request({ url: '/admin/product-category', method: 'put', data })
}

export function deleteProductCategory(id) {
  return request({ url: `/admin/product-category/${id}`, method: 'delete' })
}
