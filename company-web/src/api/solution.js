import request from './request'

export function getSolutionList() {
  return request.get('/web/solution/list')
}
