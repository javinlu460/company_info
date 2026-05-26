<template>
  <header class="site-header" :class="{ scrolled: isScrolled }">
    <div class="header-inner container">
      <router-link to="/" class="logo">
        <img v-if="siteConfig.logo" :src="getImageUrl(siteConfig.logo)" alt="logo" class="logo-img" />
        <span class="logo-text">{{ siteConfig.siteName || '企业官网' }}</span>
      </router-link>

      <button class="menu-toggle" @click="menuOpen = !menuOpen" aria-label="菜单">
        <span class="hamburger" :class="{ active: menuOpen }"></span>
      </button>

      <nav class="nav-menu" :class="{ open: menuOpen }">
        <router-link
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          class="nav-link"
          :class="{ active: isCurrentPath(item.path) }"
          @click="menuOpen = false"
        >
          {{ item.name }}
        </router-link>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getSiteConfig } from '../api/home'

const route = useRoute()
const isScrolled = ref(false)
const menuOpen = ref(false)
const siteConfig = ref({})

const navItems = [
  { name: '网站首页', path: '/' },
  { name: '关于我们', path: '/about' },
  { name: '产品中心', path: '/products' },
  { name: '新闻中心', path: '/news' },
  { name: '业务领域', path: '/business' },
  { name: '联系我们', path: '/contact' }
]

function isCurrentPath(path) {
  if (path === '/') return route.path === '/'
  return route.path.startsWith(path)
}

function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return 'http://localhost:8080' + url
}

function handleScroll() {
  isScrolled.value = window.scrollY > 10
}

async function loadConfig() {
  try {
    siteConfig.value = await getSiteConfig()
  } catch (e) {
    console.warn('加载站点配置失败:', e)
  }
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
  loadConfig()
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.site-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: var(--header-height);
  background: #ffffff;
  z-index: 1000;
  transition: box-shadow 0.3s;
}

.site-header.scrolled {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.header-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  flex-shrink: 0;
}

.logo-img {
  height: 40px;
  width: auto;
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  color: var(--color-primary);
  white-space: nowrap;
}

.nav-menu {
  display: flex;
  align-items: center;
  gap: 6px;
}

.nav-link {
  padding: 8px 16px;
  font-size: 15px;
  color: var(--color-text);
  border-radius: 4px;
  transition: color 0.3s, background-color 0.3s;
  white-space: nowrap;
}

.nav-link:hover,
.nav-link.active {
  color: var(--color-primary);
  background-color: rgba(0, 51, 102, 0.06);
}

.menu-toggle {
  display: none;
  width: 36px;
  height: 36px;
  align-items: center;
  justify-content: center;
  background: none;
  border: none;
  cursor: pointer;
}

.hamburger {
  display: block;
  width: 22px;
  height: 2px;
  background: var(--color-primary);
  position: relative;
  transition: background-color 0.3s;
}

.hamburger::before,
.hamburger::after {
  content: '';
  position: absolute;
  left: 0;
  width: 22px;
  height: 2px;
  background: var(--color-primary);
  transition: transform 0.3s;
}

.hamburger::before {
  top: -7px;
}

.hamburger::after {
  bottom: -7px;
}

.hamburger.active {
  background-color: transparent;
}

.hamburger.active::before {
  top: 0;
  transform: rotate(45deg);
}

.hamburger.active::after {
  bottom: 0;
  transform: rotate(-45deg);
}

@media (max-width: 768px) {
  .menu-toggle {
    display: flex;
  }

  .nav-menu {
    position: fixed;
    top: var(--header-height);
    left: 0;
    right: 0;
    background: #ffffff;
    flex-direction: column;
    padding: 10px 20px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-100%);
    opacity: 0;
    pointer-events: none;
    transition: transform 0.3s, opacity 0.3s;
  }

  .nav-menu.open {
    transform: translateY(0);
    opacity: 1;
    pointer-events: auto;
  }

  .nav-link {
    width: 100%;
    text-align: center;
    padding: 12px 16px;
  }
}
</style>
