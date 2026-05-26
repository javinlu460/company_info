<template>
  <el-container class="app-wrapper">
    <el-aside :width="appStore.sidebarCollapsed ? '64px' : '220px'" class="sidebar-container">
      <Sidebar />
    </el-aside>
    <el-container class="main-container">
      <el-header class="navbar-container" height="56px">
        <Navbar />
      </el-header>
      <AppMain />
    </el-container>
  </el-container>
</template>

<script setup>
import Sidebar from './Sidebar.vue'
import Navbar from './Navbar.vue'
import AppMain from './AppMain.vue'
import { useAppStore } from '@/store/app'

const appStore = useAppStore()
</script>

<style scoped>
.app-wrapper {
  height: 100vh;
  width: 100%;
  overflow: hidden;
}
.sidebar-container {
  background-color: #304156;
  transition: width 0.28s;
  overflow: hidden;
}
.main-container {
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
.navbar-container {
  padding: 0;
  border-bottom: 1px solid #e6e6e6;
  background: #fff;
}
</style>
