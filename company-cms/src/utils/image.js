/**
 * 处理图片URL，为相对路径添加/api前缀
 * @param {string} url 图片路径
 * @returns {string} 完整的可访问图片URL
 */
export function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  if (url.startsWith('/api/')) return url
  return '/api' + (url.startsWith('/') ? url : '/' + url)
}
