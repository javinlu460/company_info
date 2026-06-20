<template>
  <div class="news-detail-page">
    <!-- 页面Banner -->
    <div class="page-banner detail-banner">
      <div class="container">
        <p class="banner-en">INDUSTRY INSIGHTS</p>
        <nav class="breadcrumb" aria-label="breadcrumb">
          <router-link to="/">首页</router-link>
          <span class="separator">/</span>
          <router-link to="/news">行业洞察</router-link>
          <span class="separator">/</span>
          <span class="current">{{ news.title || '文章详情' }}</span>
        </nav>
      </div>
    </div>

    <!-- 加载中 -->
    <div v-if="loading" class="loading-wrap" style="min-height: 400px;">
      <div class="loading-spinner"></div>
      <span>加载中...</span>
    </div>

    <!-- 文章内容 -->
    <div v-else-if="news.id" class="article-section section-white">
      <div class="article-container">
        <!-- 文章头部 -->
        <header class="article-header">
          <span v-if="news.categoryName" class="article-category">{{ news.categoryName }}</span>
          <h1 class="article-title">{{ news.title }}</h1>
          <div class="article-meta">
            <span v-if="news.publishDate || news.createTime">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="4" width="18" height="18" rx="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
              {{ formatDate(news.publishDate || news.createTime) }}
            </span>
            <span v-if="news.author">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
              {{ news.author }}
            </span>
            <span v-if="news.viewCount">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/></svg>
              {{ news.viewCount }} 次阅读
            </span>
          </div>
        </header>

        <!-- 文章正文 -->
        <div class="article-content" v-html="decodedContent"></div>

        <!-- 上下篇导航 -->
        <footer class="article-nav">
          <div class="nav-item nav-prev">
            <span class="nav-label">上一篇</span>
            <router-link v-if="prevNews" :to="`/news/${prevNews.id}`" class="nav-link">
              {{ prevNews.title }}
            </router-link>
            <span v-else class="nav-empty">没有了</span>
          </div>
          <div class="nav-divider"></div>
          <div class="nav-item nav-next">
            <span class="nav-label">下一篇</span>
            <router-link v-if="nextNews" :to="`/news/${nextNews.id}`" class="nav-link">
              {{ nextNews.title }}
            </router-link>
            <span v-else class="nav-empty">没有了</span>
          </div>
        </footer>
      </div>
    </div>

    <!-- 文章不存在 -->
    <div v-else class="empty-state" style="min-height: 400px;">
      <div class="empty-icon">📰</div>
      <p>文章不存在或已删除</p>
      <router-link to="/news" class="btn-red" style="margin-top: 20px; text-decoration: none;">返回行业洞察</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
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
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}-${m}-${day}`
}

function decodeHtml(html) {
  if (!html) return ''
  return html
    .replace(/&lt;/g, '<')
    .replace(/&gt;/g, '>')
    .replace(/&amp;/g, '&')
    .replace(/&quot;/g, '"')
    .replace(/&#39;/g, "'")
}

const decodedContent = computed(() => decodeHtml(news.value.content))

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
    console.warn('加载相邻文章失败:', e)
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
/* Banner */
.detail-banner {
  height: 160px;
  padding: 0;
  display: flex;
  align-items: flex-end;
  background: var(--graphite) !important;
  padding-bottom: 0;
}

.detail-banner .container {
  text-align: left;
  padding-bottom: 20px;
}

.banner-en {
  font-size: 11px;
  font-weight: 600;
  letter-spacing: 0.2em;
  color: var(--gold);
  margin-bottom: 10px;
  text-transform: uppercase;
}

/* 面包屑（在banner内） */
.detail-banner .breadcrumb {
  display: inline-flex;
  align-items: center;
  font-size: 13px;
  padding: 0;
  background: transparent;
  color: rgba(242, 243, 239, 0.65);
}

.detail-banner .breadcrumb a {
  color: rgba(242, 243, 239, 0.65);
  text-decoration: none;
  transition: color var(--transition-fast);
  max-width: 200px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.detail-banner .breadcrumb a:hover {
  color: var(--gold);
}

.detail-banner .breadcrumb .separator {
  margin: 0 8px;
  color: rgba(242, 243, 239, 0.35);
}

.detail-banner .breadcrumb .current {
  color: rgba(242, 243, 239, 0.9);
  max-width: 300px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 文章区域 */
.article-section {
  padding: 56px 0 80px;
}

.article-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 0 24px;
}

/* 文章头部 */
.article-header {
  margin-bottom: 40px;
  padding-bottom: 28px;
  border-bottom: 2px solid var(--gray-200);
}

.article-category {
  display: inline-block;
  padding: 4px 12px;
  font-size: 12px;
  font-weight: 600;
  color: var(--red);
  background: rgba(178, 43, 43, 0.08);
  border-radius: var(--radius-sm);
  letter-spacing: 0.03em;
  margin-bottom: 16px;
}

.article-title {
  font-family: var(--font-serif);
  font-size: 30px;
  font-weight: 700;
  color: var(--ink);
  line-height: 1.45;
  margin-bottom: 20px;
}

.article-meta {
  display: flex;
  align-items: center;
  gap: 20px;
  font-size: 13px;
  color: var(--gray-500);
  flex-wrap: wrap;
}

.article-meta span {
  display: flex;
  align-items: center;
  gap: 5px;
}

.article-meta svg {
  flex-shrink: 0;
  opacity: 0.7;
}

/* 文章正文 */
.article-content {
  font-size: 16px;
  line-height: 1.8;
  color: var(--ink);
}

.article-content :deep(p) {
  margin-bottom: 1.2em;
}

.article-content :deep(img) {
  display: block;
  max-width: 100%;
  height: auto;
  margin: 24px auto;
  border-radius: var(--radius-sm);
}

.article-content :deep(h1),
.article-content :deep(h2),
.article-content :deep(h3),
.article-content :deep(h4) {
  font-family: var(--font-serif);
  font-weight: 700;
  color: var(--ink);
  margin: 32px 0 16px;
  line-height: 1.4;
}

.article-content :deep(h2) { font-size: 22px; }
.article-content :deep(h3) { font-size: 18px; }

.article-content :deep(ul),
.article-content :deep(ol) {
  padding-left: 24px;
  margin-bottom: 1.2em;
}

.article-content :deep(li) {
  margin-bottom: 0.5em;
}

.article-content :deep(blockquote) {
  margin: 24px 0;
  padding: 16px 20px;
  border-left: 4px solid var(--red);
  background: var(--gray-100);
  color: var(--gray-600);
  font-style: italic;
}

.article-content :deep(a) {
  color: var(--red);
  text-decoration: underline;
  text-underline-offset: 3px;
}

/* 文章导航 */
.article-nav {
  margin-top: 48px;
  padding-top: 28px;
  border-top: 1px solid var(--gray-200);
  display: flex;
  gap: 0;
}

.nav-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.nav-divider {
  width: 1px;
  background: var(--gray-200);
  margin: 0 32px;
  flex-shrink: 0;
}

.nav-prev {
  align-items: flex-start;
}

.nav-next {
  align-items: flex-end;
  text-align: right;
}

.nav-label {
  font-size: 12px;
  font-weight: 600;
  color: var(--gray-400);
  text-transform: uppercase;
  letter-spacing: 0.08em;
}

.nav-link {
  font-size: 14px;
  color: var(--gray-600);
  text-decoration: none;
  line-height: 1.5;
  transition: color var(--transition-fast);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.nav-link:hover {
  color: var(--red);
}

.nav-empty {
  font-size: 14px;
  color: var(--gray-400);
}

/* 响应式 */
@media (max-width: 768px) {
  .detail-banner {
    height: 130px;
  }

  .article-title {
    font-size: 22px;
  }

  .article-meta {
    gap: 12px;
  }

  .article-content {
    font-size: 15px;
  }

  .article-nav {
    flex-direction: column;
    gap: 24px;
  }

  .nav-divider {
    width: 100%;
    height: 1px;
    margin: 0;
  }

  .nav-next {
    align-items: flex-start;
    text-align: left;
  }

  .detail-banner .breadcrumb .current {
    max-width: 160px;
  }
}
</style>
