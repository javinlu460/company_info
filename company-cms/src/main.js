import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import { createPinia } from 'pinia'
import router from './router'
import './permission'

const app = createApp(App)

// 注册所有 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 注册自定义权限指令
app.directive('hasPermi', {
  mounted(el, binding) {
    const { value } = binding
    const permissions = JSON.parse(localStorage.getItem('permissions') || '[]')
    if (value && value instanceof Array && value.length > 0) {
      const hasPermission = permissions.some((perm) => {
        return perm === '*:*:*' || value.includes(perm)
      })
      if (!hasPermission) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  }
})

app.use(createPinia())
app.use(router)
app.use(ElementPlus, { locale: zhCn })
app.mount('#app')
