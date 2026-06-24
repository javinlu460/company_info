import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/about',
    name: 'About',
    component: () => import('../views/About.vue')
  },
  {
    path: '/products',
    name: 'ProductList',
    component: () => import('../views/ProductList.vue')
  },
  {
    path: '/products/:id',
    name: 'ProductDetail',
    component: () => import('../views/ProductDetail.vue')
  },
  {
    path: '/news',
    name: 'NewsList',
    component: () => import('../views/NewsList.vue')
  },
  {
    path: '/news/:id',
    name: 'NewsDetail',
    component: () => import('../views/NewsDetail.vue')
  },
  {
    path: '/business',
    name: 'Business',
    component: () => import('../views/Business.vue')
  },
  {
    path: '/cases',
    name: 'Cases',
    component: () => import('../views/Cases.vue')
  },
  {
    path: '/faq',
    name: 'Faq',
    component: () => import('../views/Faq.vue')
  },
  {
    path: '/contact',
    name: 'Contact',
    component: () => import('../views/Contact.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (to.hash) {
      return { el: to.hash, behavior: 'smooth' }
    }
    if (savedPosition) return savedPosition
    return { top: 0 }
  }
})

export default router
