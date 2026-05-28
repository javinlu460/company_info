<template>
  <div class="news-list-page">
    <div class="page-banner">
      <h1>新闻中心</h1>
      <p>NEWS CENTER</p>
    </div>

    <div class="container">
      <!-- 新闻分类Tab -->
      <div class="news-tabs">
        <button
          class="tab-btn"
          :class="{ active: !activeCategory }"
          @click="selectCategory(null)"
        >
          全部新闻
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

      <!-- 新闻列表 -->
      <div v-if="loading" class="loading-wrap">
        <div class="loading-spinner"></div>
        <span>加载中...</span>
      </div>
      <template v-else>
        <div v-if="newsList.length" class="news-list">
          <NewsItem
            v-for="item in newsList"
            :key="item.id"
            :news="item"
          />
        </div>
        <div v-else class="empty-state">
          <div class="empty-icon">📰</div>
          <p>暂无相关新闻</p>
        </div>
        <Pagination
          :total="total"
          v-model:currentPage="currentPage"
          :pageSize="pageSize"
        />
      </template>
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
const pageSize = ref(10)
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
.news-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  padding: 30px 0 20px;
  justify-content: center;
}

.tab-btn {
  padding: 8px 24px;
  font-size: 14px;
  color: var(--color-text);
  background: var(--color-bg-gray);
  border: 1px solid transparent;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.tab-btn:hover {
  color: var(--color-primary);
  border-color: var(--color-primary);
}

.tab-btn.active {
  color: #ffffff;
  background: var(--color-primary);
  border-color: var(--color-primary);
}

.news-list {
  padding: 10px 0;
}

@media (max-width: 768px) {
  .news-tabs {
    gap: 8px;
  }

  .tab-btn {
    padding: 6px 16px;
    font-size: 13px;
  }
}
</style>
