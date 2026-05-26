import request from './request'

export function getHomeData() {
  return request.get('/web/home/index')
}

export function getSiteConfig() {
  return request.get('/web/config/site')
}
