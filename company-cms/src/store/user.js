import { defineStore } from 'pinia'
import { login, logout, getUserInfoApi } from '@/api/auth'
import { getMenuTree } from '@/api/menu'
import { loadView } from '@/router/index'
import Layout from '@/layout/index.vue'
import router from '@/router'

function generateRoutesFromMenus(menus, parentPath = '') {
  const routes = []
  menus.forEach((menu) => {
    if (menu.menuType === 3) return // 按钮类型不生成路由

    const route = {
      name: menu.id + '',
      path: menu.path || '',
      meta: {
        title: menu.menuName,
        icon: menu.icon || '',
        hidden: menu.visible === 0
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

      // 遍历菜单树，将所有叶子菜单扁平化为 layout 的直接子路由
      const flattenMenus = (menuList, parentPath = '') => {
        menuList.forEach((menu) => {
          if (menu.menuType === 3) return // 按钮类型跳过

          const currentPath = menu.path || ''

          if (menu.component) {
            // 有组件的菜单项，生成路由
            const fullPath = parentPath ? `${parentPath}/${currentPath}` : currentPath
            const route = {
              name: 'Menu' + menu.id,
              path: fullPath.startsWith('/') ? fullPath : '/' + fullPath,
              component: loadView(menu.component),
              meta: {
                title: menu.menuName,
                icon: menu.icon || '',
                hidden: menu.visible === 0
              }
            }
            layoutChildren.push(route)
          }

          // 继续遍历子菜单
          if (menu.children && menu.children.length > 0) {
            flattenMenus(menu.children, currentPath)
          }
        })
      }

      flattenMenus(menus)

      // 构建侧边栏显示用的路由树（保留层级结构）
      const sidebarRoutes = []
      menus.forEach((menu) => {
        if (menu.menuType === 3) return
        const route = {
          name: menu.id + '',
          path: menu.path || '',
          meta: {
            title: menu.menuName,
            icon: menu.icon || '',
            hidden: menu.visible === 0
          }
        }
        if (menu.children && menu.children.length > 0) {
          const childRoutes = []
          menu.children.forEach((child) => {
            if (child.menuType === 3) return
            const parentPath = menu.path || ''
            const childPath = child.path || ''
            const fullPath = parentPath ? `${parentPath}/${childPath}` : childPath
            childRoutes.push({
              name: 'Menu' + child.id,
              path: fullPath.startsWith('/') ? fullPath : '/' + fullPath,
              meta: {
                title: child.menuName,
                icon: child.icon || '',
                hidden: child.visible === 0
              }
            })
          })
          route.children = childRoutes
        }
        sidebarRoutes.push(route)
      })

      const dashboardRoute = {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '仪表盘', icon: 'Odometer', affix: true }
      }

      const layoutRoute = {
        path: '/',
        component: Layout,
        redirect: '/dashboard',
        children: [
          dashboardRoute,
          ...layoutChildren
        ]
      }

      // 侧边栏路由数据（带层级结构）
      this.routes = [
        { path: '/dashboard', meta: { title: '仪表盘', icon: 'Odometer' } },
        ...sidebarRoutes
      ]
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
