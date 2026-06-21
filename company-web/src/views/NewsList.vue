<template>
  <div class="news-list-page">
    <!-- 页面Banner -->
    <div class="page-banner">
      <div class="container">
        <nav class="breadcrumb">
          <router-link to="/">首页</router-link>
          <span class="separator">/</span>
          <span class="current">行业洞察</span>
        </nav>
        <h1>行业洞察</h1>
        <p class="banner-subtitle">INSIGHTS</p>
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
.page-banner {
  background: var(--graphite);
  color: var(--paper);
  padding: 100px 0 64px;
  margin-top: var(--header-height);
}

.page-banner .container {
  position: relative;
  z-index: 1;
}

.page-banner h1 {
  font-family: var(--font-serif);
  font-size: 38px;
  font-weight: 700;
  color: #F2F3EF;
  margin-bottom: 8px;
  line-height: 1.2;
}

.banner-subtitle {
  font-size: 14px;
  color: var(--gold);
  letter-spacing: 4px;
  text-transform: uppercase;
  font-weight: 500;
  margin: 0;
}

.breadcrumb {
  padding: 0 0 16px;
  font-size: 14px;
  color: rgba(242, 243, 239, 0.7);
  background: transparent;
}

.breadcrumb a {
  color: rgba(242, 243, 239, 0.7);
  text-decoration: none;
  transition: color 0.2s;
}

.breadcrumb a:hover {
  color: var(--gold);
}

.breadcrumb .separator {
  margin: 0 10px;
  color: rgba(242, 243, 239, 0.4);
}

.breadcrumb .current {
  color: var(--gold);
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
  .page-banner {
    padding: 80px 0 48px;
  }

  .page-banner h1 {
    font-size: 28px;
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
