<template>
  <div class="sidebar-wrapper">
    <div class="logo-container" :class="{ collapse: appStore.sidebarCollapsed }">
      <img src="" alt="" class="logo-img" />
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
            :index="route.path"
          >
            <template #title>
              <el-icon v-if="route.meta && route.meta.icon">
                <component :is="route.meta.icon" />
              </el-icon>
              <span>{{ route.meta && route.meta.title }}</span>
            </template>
            <el-menu-item
              v-for="child in route.children"
              :key="child.name"
              :index="child.path"
            >
              <el-icon v-if="child.meta && child.meta.icon">
                <component :is="child.meta.icon" />
              </el-icon>
              <template #title>{{ child.meta && child.meta.title }}</template>
            </el-menu-item>
          </el-sub-menu>
        </template>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAppStore } from '@/store/app'
import { useUserStore } from '@/store/user'

const route = useRoute()
const appStore = useAppStore()
const userStore = useUserStore()

const activeMenu = computed(() => {
  return route.path
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
