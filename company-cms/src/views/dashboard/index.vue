<template>
  <div class="dashboard-container">
    <el-row :gutter="20" class="stat-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-label">产品总数</div>
              <div class="stat-value">{{ stats.productCount }}</div>
            </div>
            <el-icon class="stat-icon" style="color: #409eff;"><Box /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-label">新闻总数</div>
              <div class="stat-value">{{ stats.newsCount }}</div>
            </div>
            <el-icon class="stat-icon" style="color: #67c23a;"><Document /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-label">未读留言</div>
              <div class="stat-value">{{ stats.unreadMessageCount }}</div>
            </div>
            <el-icon class="stat-icon" style="color: #e6a23c;"><ChatDotSquare /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-label">用户数</div>
              <div class="stat-value">{{ stats.userCount }}</div>
            </div>
            <el-icon class="stat-icon" style="color: #f56c6c;"><User /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="content-row">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>最近留言</span>
          </template>
          <el-table :data="recentMessages" size="small" max-height="320">
            <el-table-column prop="name" label="姓名" width="100" />
            <el-table-column prop="content" label="内容" show-overflow-tooltip />
            <el-table-column prop="createTime" label="时间" width="160" />
          </el-table>
          <el-empty v-if="recentMessages.length === 0" description="暂无留言" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <span>最近新闻</span>
          </template>
          <el-table :data="recentNews" size="small" max-height="320">
            <el-table-column prop="title" label="标题" show-overflow-tooltip />
            <el-table-column prop="author" label="作者" width="100" />
            <el-table-column prop="createTime" label="时间" width="160" />
          </el-table>
          <el-empty v-if="recentNews.length === 0" description="暂无新闻" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getProductList } from '@/api/product'
import { getNewsList } from '@/api/news'
import { getMessageList } from '@/api/message'
import { getUserList } from '@/api/user'

const stats = reactive({
  productCount: 0,
  newsCount: 0,
  unreadMessageCount: 0,
  userCount: 0
})

const recentMessages = ref([])
const recentNews = ref([])

onMounted(() => {
  loadStats()
  loadRecentData()
})

async function loadStats() {
  try {
    const [productRes, newsRes, messageRes, userRes] = await Promise.all([
      getProductList({ pageNum: 1, pageSize: 1 }),
      getNewsList({ pageNum: 1, pageSize: 1 }),
      getMessageList({ pageNum: 1, pageSize: 1, isRead: 0 }),
      getUserList({ pageNum: 1, pageSize: 1 })
    ])
    stats.productCount = productRes.data.total || 0
    stats.newsCount = newsRes.data.total || 0
    stats.unreadMessageCount = messageRes.data.total || 0
    stats.userCount = userRes.data.total || 0
  } catch (e) {
    // ignore
  }
}

async function loadRecentData() {
  try {
    const [messageRes, newsRes] = await Promise.all([
      getMessageList({ pageNum: 1, pageSize: 5 }),
      getNewsList({ pageNum: 1, pageSize: 5 })
    ])
    recentMessages.value = messageRes.data.records || []
    recentNews.value = newsRes.data.records || []
  } catch (e) {
    // ignore
  }
}
</script>

<style scoped>
.dashboard-container {
  padding: 0;
}
.stat-row {
  margin-bottom: 20px;
}
.stat-card {
  cursor: pointer;
}
.stat-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}
.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
}
.stat-icon {
  font-size: 48px;
  opacity: 0.8;
}
.content-row {
  margin-bottom: 20px;
}
</style>
