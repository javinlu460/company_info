import router from './router'
import { useUserStore } from './store/user'

const whiteList = ['/login']

router.beforeEach(async (to, from, next) => {
  const token = localStorage.getItem('token')

  if (token) {
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      const userStore = useUserStore()
      if (userStore.roles && userStore.roles.length > 0) {
        next()
      } else {
        try {
          await userStore.getUserInfo()
          const accessRoutes = await userStore.generateRoutes()
          accessRoutes.forEach((route) => {
            router.addRoute(route)
          })
          next({ ...to, replace: true })
        } catch (error) {
          userStore.resetState()
          next(`/login?redirect=${to.path}`)
        }
      }
    }
  } else {
    if (whiteList.includes(to.path)) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
    }
  }
})
