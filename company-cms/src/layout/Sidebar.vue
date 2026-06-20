<template>
  <div class="sidebar-wrapper">
    <div class="logo-container" :class="{ collapse: appStore.sidebarCollapsed }">
      <img v-if="logoUrl" :src="logoUrl" alt="logo" class="logo-img" />
      <svg v-else class="logo-img" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
        <rect width="32" height="32" rx="6" fill="#409eff"/>
        <text x="16" y="22" text-anchor="middle" fill="white" font-size="16" font-weight="bold">C</text>
      </svg>
      <span v-if="!appStore.sidebarCollapsed" class="logo-title">企业信息管理系统</span>
    </div>
    <el-scrollbar>
      <el-menu
        :default-active="activeMenu"
        :collapse="appStore.sidebarCollapsed"
        :collapse-transition="false"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
        :unique-opened="true"
        router
      >
        <template v-for="route in userStore.routes" :key="route.name">
          <!-- 跳过隐藏菜单 -->
          <template v-if="!route.meta || !route.meta.hidden">
          <!-- 单个菜单项(无子菜单) -->
          <el-menu-item
            v-if="!route.children || route.children.length === 0"
            :index="route.path"
          >
            <el-icon v-if="route.meta && route.meta.icon">
              <component :is="route.meta.icon" />
            </el-icon>
            <template #title>{{ route.meta && route.meta.title }}</template>
          </el-menu-item>

          <!-- 有子菜单 -->
          <el-sub-menu
            v-else
            :index="route.path || route.name"
          >
            <template #title>
              <el-icon v-if="route.meta && route.meta.icon">
                <component :is="route.meta.icon" />
              </el-icon>
              <span>{{ route.meta && route.meta.title }}</span>
            </template>
            <template v-for="child in route.children" :key="child.name">
              <el-menu-item
                v-if="!child.meta || !child.meta.hidden"
                :index="child.path"
              >
                <el-icon v-if="child.meta && child.meta.icon">
                  <component :is="child.meta.icon" />
                </el-icon>
                <template #title>{{ child.meta && child.meta.title }}</template>
              </el-menu-item>
            </template>
          </el-sub-menu>
          </template>
        </template>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/store/app'
import { useUserStore } from '@/store/user'
import request from '@/api/request'

const route = useRoute()
const appStore = useAppStore()
const userStore = useUserStore()
const logoUrl = ref('')

const activeMenu = computed(() => {
  return route.path
})

onMounted(async () => {
  try {
    const res = await request.get('/admin/config/list')
    const configs = res.data || res
    const logoConfig = (Array.isArray(configs) ? configs : configs.records || []).find(c => c.configKey === 'company_logo')
    if (logoConfig && logoConfig.configValue) {
      const val = logoConfig.configValue
      logoUrl.value = val.startsWith('http') ? val : (val.startsWith('/api') ? val : '/api' + (val.startsWith('/') ? val : '/' + val))
    }
  } catch (e) {
    console.warn('加载Logo失败:', e)
  }
})
</script>

<style scoped>
.sidebar-wrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
}
.logo-container {
  height: 56px;
  display: flex;
  align-items: center;
  padding: 0 16px;
  background-color: #263445;
  overflow: hidden;
}
.logo-container.collapse {
  padding: 0;
  justify-content: center;
}
.logo-img {
  width: 32px;
  height: 32px;
  flex-shrink: 0;
}
.logo-title {
  color: #fff;
  font-size: 14px;
  font-weight: 600;
  margin-left: 10px;
  white-space: nowrap;
}
.el-scrollbar {
  flex: 1;
}
.el-menu {
  border-right: none;
}
</style>
