<template>
  <header class="site-header" :class="{ scrolled: isScrolled }">
    <div class="header-inner container">
      <!-- Logo -->
      <router-link to="/" class="logo">
        <img
          v-if="siteConfig.logo"
          :src="getImageUrl(siteConfig.logo)"
          alt="logo"
          class="logo-img"
        />
        <span class="logo-text">{{ siteConfig.siteName || '企业官网' }}</span>
      </router-link>

      <!-- 桌面端导航 -->
      <nav class="desktop-nav">
        <router-link
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          class="nav-link"
          :class="{ active: isActive(item) }"
        >
          {{ item.name }}
        </router-link>
      </nav>

      <!-- 桌面端 CTA -->
      <router-link to="/contact" class="cta-btn desktop-cta">
        获取报价
      </router-link>

      <!-- 移动端汉堡菜单 -->
      <button
        class="menu-toggle"
        @click="drawerOpen = true"
        aria-label="打开菜单"
      >
        <span class="hamburger"></span>
      </button>
    </div>

    <!-- 移动端侧拉抽屉 -->
    <transition name="fade">
      <div
        v-if="drawerOpen"
        class="drawer-overlay"
        @click="drawerOpen = false"
      ></div>
    </transition>
    <transition name="slide">
      <aside v-if="drawerOpen" class="mobile-drawer">
        <button
          class="drawer-close"
          @click="drawerOpen = false"
          aria-label="关闭菜单"
        ></button>
        <nav class="drawer-nav">
          <router-link
            v-for="item in navItems"
            :key="item.path"
            :to="item.path"
            class="drawer-nav-link"
            :class="{ active: isActive(item) }"
            @click="drawerOpen = false"
          >
            {{ item.name }}
          </router-link>
        </nav>
        <router-link
          to="/contact"
          class="cta-btn drawer-cta"
          @click="drawerOpen = false"
        >
          获取报价
        </router-link>
      </aside>
    </transition>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getSiteConfig } from '../api/home'

const route = useRoute()
const isScrolled = ref(false)
const drawerOpen = ref(false)
const siteConfig = ref({})

const navItems = [
  { name: '关于我们', path: '/about' },
  { name: '产品中心', path: '/products' },
  { name: '解决方案', path: '/business' },
  { name: '客户案例', path: '/#cases' },
  { name: '行业洞察', path: '/news' },
  { name: '常见问题', path: '/#faq' },
  { name: '联系我们', path: '/contact' }
]

function isActive(item) {
  if (item.path.startsWith('/#')) {
    return route.path === '/' && route.hash === item.path.substring(1)
  }
  return route.path.startsWith(item.path)
}

function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  if (url.startsWith('/api/')) return url
  return '/api' + (url.startsWith('/') ? url : '/' + url)
}

function handleScroll() {
  isScrolled.value = window.scrollY > 50
}

async function loadConfig() {
  try {
    siteConfig.value = await getSiteConfig()
  } catch (e) {
    console.warn('加载站点配置失败:', e)
  }
}

watch(drawerOpen, (open) => {
  document.body.style.overflow = open ? 'hidden' : ''
})

onMounted(() => {
  window.addEventListener('scroll', handleScroll, { passive: true })
  handleScroll()
  loadConfig()
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  document.body.style.overflow = ''
})
</script>

<style scoped>
.site-header {
  position: sticky;
  top: 0;
  left: 0;
  width: 100%;
  height: var(--header-height);
  z-index: 1000;
  background: #fff;
  color: var(--ink);
  border-bottom: 1px solid rgba(0,0,0,0.06);
  transition: background-color var(--transition-base), box-shadow var(--transition-base);
}

.site-header.scrolled {
  background: #fff;
  box-shadow: var(--shadow-md);
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
  color: var(--ink);
  flex-shrink: 0;
}

.logo-img {
  height: 40px;
  width: auto;
  object-fit: contain;
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  white-space: nowrap;
}

.desktop-nav {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nav-link {
  position: relative;
  padding: 8px 16px;
  font-size: 15px;
  color: var(--ink);
  text-decoration: none;
  transition: color var(--transition-base);
  white-space: nowrap;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 16px;
  right: 16px;
  height: 2px;
  background: var(--red);
  transform: scaleX(0);
  transition: transform var(--transition-base);
}

.nav-link:hover {
  color: var(--red);
}

.nav-link.active {
  color: var(--red);
}

.nav-link.active::after {
  transform: scaleX(1);
}

.cta-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 10px 24px;
  font-size: 15px;
  font-weight: 500;
  color: var(--white);
  background: var(--red);
  border-radius: var(--radius-md);
  text-decoration: none;
  transition: background-color var(--transition-base);
  white-space: nowrap;
}

.cta-btn:hover {
  background: #8f2222;
}

.menu-toggle {
  display: none;
  width: 40px;
  height: 40px;
  align-items: center;
  justify-content: center;
  background: none;
  border: none;
  cursor: pointer;
}

.hamburger {
  position: relative;
  display: block;
  width: 24px;
  height: 2px;
  background: var(--ink);
}

.hamburger::before,
.hamburger::after {
  content: '';
  position: absolute;
  left: 0;
  width: 24px;
  height: 2px;
  background: var(--ink);
}

.hamburger::before {
  top: -7px;
}

.hamburger::after {
  top: 7px;
}

/* 抽屉遮罩层动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity var(--transition-base);
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 抽屉滑入动画 */
.slide-enter-active,
.slide-leave-active {
  transition: transform var(--transition-base);
}

.slide-enter-from,
.slide-leave-to {
  transform: translateX(100%);
}

@media (max-width: 768px) {
  .desktop-nav,
  .desktop-cta {
    display: none;
  }

  .menu-toggle {
    display: flex;
  }

  .drawer-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    z-index: 1099;
  }

  .mobile-drawer {
    position: fixed;
    top: 0;
    right: 0;
    width: 280px;
    max-width: 80vw;
    height: 100vh;
    background: var(--graphite);
    z-index: 1100;
    display: flex;
    flex-direction: column;
    padding: 16px 20px 24px;
  }

  .drawer-close {
    align-self: flex-end;
    width: 40px;
    height: 40px;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    background: none;
    border: none;
    cursor: pointer;
    margin-bottom: 16px;
  }

  .drawer-close::before,
  .drawer-close::after {
    content: '';
    position: absolute;
    width: 20px;
    height: 2px;
    background: var(--paper);
  }

  .drawer-close::before {
    transform: rotate(45deg);
  }

  .drawer-close::after {
    transform: rotate(-45deg);
  }

  .drawer-nav {
    flex: 1;
    display: flex;
    flex-direction: column;
  }

  .drawer-nav-link {
    padding: 14px 0;
    font-size: 16px;
    color: var(--paper);
    text-decoration: none;
    border-bottom: 1px solid rgba(242, 243, 239, 0.1);
    transition: color var(--transition-base);
  }

  .drawer-nav-link:hover,
  .drawer-nav-link.active {
    color: var(--gold);
  }

  .drawer-cta {
    margin-top: auto;
  }
}
</style>
