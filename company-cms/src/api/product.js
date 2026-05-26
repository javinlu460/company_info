import request from './request'

export function getProductList(params) {
  return request({ url: '/admin/product/list', method: 'get', params })
}

export function getProductDetail(id) {
  return request({ url: `/admin/product/${id}`, method: 'get' })
}

export function addProduct(data) {
  return request({ url: '/admin/product', method: 'post', data })
}

export function updateProduct(data) {
  return request({ url: '/admin/product', method: 'put', data })
}

export function deleteProduct(id) {
  return request({ url: `/admin/product/${id}`, method: 'delete' })
}

export function updateProductStatus(id, status) {
  return request({ url: `/admin/product/${id}/status`, method: 'put', data: { status } })
}
