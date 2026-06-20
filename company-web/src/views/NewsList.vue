<template>
  <div class="news-list-page">
    <!-- 页面Banner -->
    <div class="page-banner news-banner">
      <div class="container">
        <p class="banner-en">INDUSTRY INSIGHTS</p>
        <h1>行业洞察</h1>
        <nav class="breadcrumb" aria-label="breadcrumb">
          <router-link to="/">首页</router-link>
          <span class="separator">/</span>
          <span class="current">行业洞察</span>
        </nav>
      </div>
    </div>

    <!-- 分类筛选 -->
    <div class="category-bar">
      <div class="container">
        <div class="category-tabs">
          <button
            class="tab-btn"
            :class="{ active: !activeCategory }"
            @click="selectCategory(null)"
          >
            全部
          </button>
          <button
            v-for="cat in categories"
            :key="cat.id"
            class="tab-btn"
            :class="{ active: activeCategory === cat.id }"
            @click="selectCategory(cat.id)"
          >
            {{ cat.categoryName }}
          </button>
        </div>
      </div>
    </div>

    <!-- 新闻内容区 -->
    <div class="news-section section-light">
      <div class="container">
        <div v-if="loading" class="loading-wrap">
          <div class="loading-spinner"></div>
          <span>加载中...</span>
        </div>
        <template v-else>
          <div v-if="newsList.length" class="news-grid">
            <NewsItem
              v-for="item in newsList"
              :key="item.id"
              :news="item"
            />
          </div>
          <div v-else class="empty-state">
            <div class="empty-icon">📰</div>
            <p>暂无相关资讯</p>
          </div>
          <Pagination
            :total="total"
            v-model:currentPage="currentPage"
            :pageSize="pageSize"
          />
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getNewsList, getNewsCategories } from '../api/news'
import NewsItem from '../components/NewsItem.vue'
import Pagination from '../components/Pagination.vue'

const route = useRoute()
const router = useRouter()

const categories = ref([])
const newsList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(9)
const activeCategory = ref(null)
const loading = ref(false)

async function loadCategories() {
  try {
    categories.value = await getNewsCategories()
  } catch (e) {
    console.warn('加载分类失败:', e)
  }
}

async function loadNews() {
  loading.value = true
  try {
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize.value
    }
    if (activeCategory.value) {
      params.categoryId = activeCategory.value
    }
    const res = await getNewsList(params)
    newsList.value = res.records || res.list || []
    total.value = res.total || 0
  } catch (e) {
    console.error('加载新闻列表失败:', e)
  } finally {
    loading.value = false
  }
}

function selectCategory(catId) {
  activeCategory.value = catId
  currentPage.value = 1
  updateQuery()
  loadNews()
}

function updateQuery() {
  const query = { page: currentPage.value }
  if (activeCategory.value) query.categoryId = activeCategory.value
  router.replace({ query })
}

function initFromQuery() {
  const { page, categoryId } = route.query
  if (page) currentPage.value = Number(page)
  if (categoryId) activeCategory.value = Number(categoryId)
}

watch(currentPage, () => {
  updateQuery()
  loadNews()
})

onMounted(() => {
  initFromQuery()
  loadCategories()
  loadNews()
})
</script>

<style scoped>
/* Banner */
.news-banner {
  height: 200px;
  padding: 0;
  display: flex;
  align-items: center;
  background: var(--graphite) !important;
}

.news-banner .container {
  text-align: center;
}

.banner-en {
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 0.2em;
  color: var(--gold);
  margin-bottom: 8px;
  text-transform: uppercase;
}

.news-banner h1 {
  font-family: var(--font-serif);
  font-size: 32px;
  font-weight: 700;
  color: var(--white);
  margin-bottom: 14px;
  line-height: 1.2;
}

/* 面包屑（在banner内，浅色） */
.news-banner .breadcrumb {
  display: inline-flex;
  align-items: center;
  gap: 0;
  font-size: 13px;
  padding: 0;
  background: transparent;
  color: rgba(242, 243, 239, 0.7);
}

.news-banner .breadcrumb a {
  color: rgba(242, 243, 239, 0.7);
  text-decoration: none;
  transition: color var(--transition-fast);
}

.news-banner .breadcrumb a:hover {
  color: var(--gold);
}

.news-banner .breadcrumb .separator {
  margin: 0 8px;
  color: rgba(242, 243, 239, 0.4);
}

.news-banner .breadcrumb .current {
  color: rgba(242, 243, 239, 0.9);
}

/* 分类筛选条 */
.category-bar {
  background: var(--white);
  border-bottom: 1px solid var(--gray-200);
  position: sticky;
  top: var(--header-height);
  z-index: 10;
}

.category-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 16px 0;
}

.tab-btn {
  padding: 7px 22px;
  font-size: 14px;
  color: var(--gray-600);
  background: transparent;
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all var(--transition-fast);
  font-weight: 500;
}

.tab-btn:hover {
  color: var(--red);
  border-color: var(--red);
}

.tab-btn.active {
  color: var(--white);
  background: var(--red);
  border-color: var(--red);
}

/* 新闻区域 */
.news-section {
  padding: 48px 0 64px;
}

/* 3列网格 */
.news-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
  margin-bottom: 48px;
}

@media (max-width: 992px) {
  .news-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .news-banner {
    height: auto;
    padding: 40px 0;
  }

  .news-banner h1 {
    font-size: 26px;
  }

  .news-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .category-tabs {
    gap: 6px;
  }

  .tab-btn {
    padding: 6px 14px;
    font-size: 13px;
  }
}
</style>
