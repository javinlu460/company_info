import request from './request'

export function getSolutionList(params) {
  return request({ url: '/admin/solution/list', method: 'get', params })
}

export function getSolutionDetail(id) {
  return request({ url: `/admin/solution/${id}`, method: 'get' })
}

export function addSolution(data) {
  return request({ url: '/admin/solution', method: 'post', data })
}

export function updateSolution(data) {
  return request({ url: '/admin/solution', method: 'put', data })
}

export function deleteSolution(id) {
  return request({ url: `/admin/solution/${id}`, method: 'delete' })
}
