<template>
  <div class="news-list-page">
    <!-- 页面标签 -->
    <div class="page-label">
      <div class="container">
        <div class="section-head">
          <span class="ew">INSIGHTS · 行业洞察</span>
          <h2>深入行业前沿，洞察制造趋势</h2>
          <p class="lead">分享行业动态、制造工艺、技术创新等专业内容，助力客户了解行业最新发展。</p>
        </div>
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
          <div class="cta-wrapper">
            <router-link to="/contact" class="btn-red cta-btn">
              发图纸，获取报价及解决方案 →
            </router-link>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getNewsList, getNewsCategories } from '../api/news'
import NewsItem from '../components/NewsItem.vue'

const route = useRoute()
const router = useRouter()

const categories = ref([])
const newsList = ref([])
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
      pageNum: 1,
      pageSize: 999
    }
    if (activeCategory.value) {
      params.categoryId = activeCategory.value
    }
    const res = await getNewsList(params)
    newsList.value = res.records || res.list || []
  } catch (e) {
    console.error('加载新闻列表失败:', e)
  } finally {
    loading.value = false
  }
}

function selectCategory(catId) {
  activeCategory.value = catId
  loadNews()
}

function initFromQuery() {
  const { categoryId } = route.query
  if (categoryId) activeCategory.value = Number(categoryId)
}

onMounted(() => {
  initFromQuery()
  loadCategories()
  loadNews()
})
</script>

<style scoped>
/* 页面标签 */
.page-label {
  margin-top: var(--header-height);
  padding: 30px 0 0;
  background: #F2F3EF;
}

.section-head {
  margin-bottom: 0;
}

.section-head h2 {
  font-family: var(--font-serif);
  font-size: 34px;
  font-weight: 700;
  color: var(--ink);
  margin-bottom: 14px;
  line-height: 1.25;
}

.section-head .lead {
  font-size: 15px;
  color: #6A6D70;
  line-height: 1.8;
  max-width: 720px;
  margin: 0;
}

.ew {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  font-weight: 600;
  color: var(--red);
  letter-spacing: 1px;
  text-transform: uppercase;
}

.ew::before {
  content: '';
  width: 18px;
  height: 2px;
  background: var(--red);
}

/* 分类筛选条 */
.category-bar {
  background: #F2F3EF;
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
  background: #F2F3EF;
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

/* CTA */
.cta-wrapper {
  text-align: center;
  margin-top: 48px;
  padding-bottom: 20px;
}

.cta-btn {
  display: inline-block;
  padding: 14px 40px;
  font-size: 16px;
  font-weight: 600;
  background: var(--red);
  color: var(--white);
  border-radius: var(--radius-md);
  text-decoration: none;
  transition: background var(--transition-base), transform var(--transition-base);
}

.cta-btn:hover {
  background: #a02020;
  transform: translateY(-2px);
}
</style>
