import { defineStore } from 'pinia'
import { login, logout, getUserInfoApi } from '@/api/auth'
import { getMenuTree } from '@/api/menu'
import { loadView } from '@/router/index'
import Layout from '@/layout/index.vue'
import router from '@/router'

function generateRoutesFromMenus(menus, parentPath = '') {
  const routes = []
  menus.forEach((menu) => {
    if (menu.type === 2) return // 按钮类型不生成路由

    const route = {
      name: menu.id + '',
      path: menu.path || '',
      meta: {
        title: menu.name,
        icon: menu.icon || '',
        hidden: menu.visible === 0 ? false : true
      }
    }

    if (menu.component) {
      route.component = loadView(menu.component)
    }

    if (menu.children && menu.children.length > 0) {
      route.children = generateRoutesFromMenus(menu.children, route.path)
    }

    routes.push(route)
  })
  return routes
}

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: {},
    roles: [],
    permissions: [],
    menus: [],
    routes: []
  }),

  actions: {
    async loginAction(loginForm) {
      const res = await login(loginForm)
      const { token, userInfo } = res.data
      this.token = token
      this.userInfo = userInfo
      localStorage.setItem('token', token)
      return res
    },

    async getUserInfo() {
      const res = await getUserInfoApi()
      const data = res.data
      this.userInfo = data
      this.roles = data.roles || []
      this.permissions = data.permissions || []
      localStorage.setItem('permissions', JSON.stringify(this.permissions))
      return data
    },

    async generateRoutes() {
      const res = await getMenuTree()
      const menus = res.data || []
      this.menus = menus

      const layoutChildren = []
      menus.forEach((menu) => {
        if (menu.type === 2) return

        const route = {
          name: menu.id + '',
          path: menu.path || '',
          meta: {
            title: menu.name,
            icon: menu.icon || '',
            hidden: menu.visible === 0 ? false : true
          }
        }

        if (menu.children && menu.children.length > 0) {
          // 目录类型: 有子菜单
          const childRoutes = []
          menu.children.forEach((child) => {
            if (child.type === 2) return
            const childRoute = {
              name: child.id + '',
              path: child.path || '',
              meta: {
                title: child.name,
                icon: child.icon || '',
                hidden: child.visible === 0 ? false : true
              }
            }
            if (child.component) {
              childRoute.component = loadView(child.component)
            }
            if (child.children && child.children.length > 0) {
              childRoute.children = generateRoutesFromMenus(child.children, child.path)
            }
            childRoutes.push(childRoute)
          })
          route.children = childRoutes
        }

        layoutChildren.push(route)
      })

      const layoutRoute = {
        path: '/',
        component: Layout,
        redirect: '/dashboard',
        children: [
          {
            path: 'dashboard',
            name: 'Dashboard',
            component: () => import('@/views/dashboard/index.vue'),
            meta: { title: '仪表盘', icon: 'Odometer', affix: true }
          },
          ...layoutChildren
        ]
      }

      this.routes = layoutRoute.children
      return [layoutRoute]
    },

    async logoutAction() {
      try {
        await logout()
      } catch (e) {
        // ignore
      }
      this.resetState()
      router.push('/login')
    },

    resetState() {
      this.token = ''
      this.userInfo = {}
      this.roles = []
      this.permissions = []
      this.menus = []
      this.routes = []
      localStorage.removeItem('token')
      localStorage.removeItem('permissions')
    }
  }
})
