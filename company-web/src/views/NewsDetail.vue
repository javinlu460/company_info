<template>
  <div class="news-detail-page">
    <div class="page-banner">
      <h1>新闻详情</h1>
      <p>NEWS DETAIL</p>
    </div>

    <div class="breadcrumb">
      <div class="container">
        <router-link to="/">首页</router-link>
        <span>/</span>
        <router-link to="/news">新闻中心</router-link>
        <span>/</span>
        <span class="current">{{ news.title || '新闻详情' }}</span>
      </div>
    </div>

    <div v-if="loading" class="loading-wrap">
      <div class="loading-spinner"></div>
      <span>加载中...</span>
    </div>

    <div v-else-if="news.id" class="container">
      <article class="news-detail">
        <header class="detail-header">
          <h1 class="detail-title">{{ news.title }}</h1>
          <div class="detail-meta">
            <span v-if="news.publishDate || news.createTime">
              发布时间：{{ formatDate(news.publishDate || news.createTime) }}
            </span>
            <span v-if="news.author">作者：{{ news.author }}</span>
            <span v-if="news.viewCount">浏览：{{ news.viewCount }}</span>
          </div>
        </header>
        <div class="detail-content" v-html="news.content"></div>
      </article>

      <div class="nav-links">
        <div v-if="prevNews" class="nav-prev">
          <span class="nav-label">上一篇：</span>
          <router-link :to="`/news/${prevNews.id}`">{{ prevNews.title }}</router-link>
        </div>
        <div v-else class="nav-prev">
          <span class="nav-label">上一篇：</span>
          <span class="nav-empty">没有更多了</span>
        </div>
        <div v-if="nextNews" class="nav-next">
          <span class="nav-label">下一篇：</span>
          <router-link :to="`/news/${nextNews.id}`">{{ nextNews.title }}</router-link>
        </div>
        <div v-else class="nav-next">
          <span class="nav-label">下一篇：</span>
          <span class="nav-empty">没有更多了</span>
        </div>
      </div>
    </div>

    <div v-else class="empty-state">
      <div class="empty-icon">📰</div>
      <p>新闻不存在或已删除</p>
      <router-link to="/news" class="btn-primary" style="margin-top: 16px;">返回新闻列表</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getNewsDetail, getNewsList } from '../api/news'

const route = useRoute()
const news = ref({})
const prevNews = ref(null)
const nextNews = ref(null)
const loading = ref(false)

function formatDate(dateStr) {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

async function loadNews(id) {
  loading.value = true
  try {
    news.value = await getNewsDetail(id)
    loadAdjacentNews()
  } catch (e) {
    console.error('加载新闻详情失败:', e)
    news.value = {}
  } finally {
    loading.value = false
  }
}

async function loadAdjacentNews() {
  try {
    const res = await getNewsList({
      pageNum: 1,
      pageSize: 100,
      categoryId: news.value.categoryId
    })
    const list = res.records || res.list || []
    const currentIndex = list.findIndex(item => item.id === news.value.id)
    prevNews.value = currentIndex > 0 ? list[currentIndex - 1] : null
    nextNews.value = currentIndex < list.length - 1 ? list[currentIndex + 1] : null
  } catch (e) {
    console.warn('加载相邻新闻失败:', e)
  }
}

onMounted(() => {
  loadNews(route.params.id)
})

watch(() => route.params.id, (newId) => {
  if (newId) loadNews(newId)
})
</script>

<style scoped>
.breadcrumb .current {
  color: var(--color-text);
}

.news-detail {
  padding: 30px 0 40px;
}

.detail-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--color-border);
}

.detail-title {
  font-size: 26px;
  font-weight: 700;
  color: var(--color-text);
  margin-bottom: 16px;
  line-height: 1.4;
}

.detail-meta {
  display: flex;
  gap: 24px;
  font-size: 14px;
  color: var(--color-text-secondary);
}

.detail-content {
  line-height: 1.8;
  font-size: 15px;
  color: var(--color-text);
}

.detail-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 4px;
  margin: 16px 0;
}

.detail-content :deep(p) {
  margin-bottom: 12px;
}

.detail-content :deep(h1),
.detail-content :deep(h2),
.detail-content :deep(h3) {
  font-weight: 600;
  color: var(--color-primary);
  margin: 24px 0 12px;
}

.nav-links {
  display: flex;
  justify-content: space-between;
  padding: 20px 0 40px;
  border-top: 1px solid var(--color-border);
  gap: 20px;
}

.nav-prev,
.nav-next {
  font-size: 14px;
  max-width: 48%;
}

.nav-label {
  color: var(--color-text-secondary);
  font-weight: 600;
}

.nav-prev a,
.nav-next a {
  color: var(--color-secondary);
  transition: color 0.3s;
}

.nav-prev a:hover,
.nav-next a:hover {
  color: var(--color-primary);
}

.nav-empty {
  color: #999;
}

@media (max-width: 768px) {
  .detail-title {
    font-size: 22px;
  }

  .detail-meta {
    flex-wrap: wrap;
    gap: 12px;
  }

  .nav-links {
    flex-direction: column;
    gap: 12px;
  }

  .nav-prev,
  .nav-next {
    max-width: 100%;
  }
}
</style>
