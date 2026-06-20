<template>
  <div class="product-list-page">
    <div class="page-banner">
      <div class="container">
        <nav class="breadcrumb">
          <router-link to="/">首页</router-link>
          <span class="separator">/</span>
          <span class="current">产品中心</span>
        </nav>
        <h1>产品中心</h1>
        <p class="banner-subtitle">PRODUCTS</p>
      </div>
    </div>

    <div class="container main-content">
      <!-- 分类筛选 -->
      <div class="category-filter">
        <button
          class="filter-btn"
          :class="{ active: !activeCategory }"
          @click="selectCategory(null)"
        >
          全部
        </button>
        <button
          v-for="cat in categories"
          :key="cat.id"
          class="filter-btn"
          :class="{ active: activeCategory === cat.id }"
          @click="selectCategory(cat.id)"
        >
          {{ cat.categoryName }}
        </button>
      </div>

      <!-- 产品列表 -->
      <div v-if="loading" class="loading-wrap">
        <div class="loading-spinner"></div>
        <span>加载中...</span>
      </div>
      <template v-else>
        <div v-if="products.length" class="products-grid">
          <ProductCard
            v-for="product in products"
            :key="product.id"
            :product="product"
          />
        </div>
        <div v-else class="empty-state">
          <div class="empty-icon">📦</div>
          <p>暂无相关产品</p>
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
import { getProductList, getProductCategories } from '../api/product'
import ProductCard from '../components/ProductCard.vue'
import Pagination from '../components/Pagination.vue'

const route = useRoute()
const router = useRouter()

const categories = ref([])
const products = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(12)
const activeCategory = ref(null)
const loading = ref(false)

async function loadCategories() {
  try {
    categories.value = await getProductCategories()
  } catch (e) {
    console.warn('加载分类失败:', e)
  }
}

async function loadProducts() {
  loading.value = true
  try {
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize.value
    }
    if (activeCategory.value) {
      params.categoryId = activeCategory.value
    }
    const res = await getProductList(params)
    products.value = res.records || res.list || []
    total.value = res.total || 0
  } catch (e) {
    console.error('加载产品列表失败:', e)
  } finally {
    loading.value = false
  }
}

function selectCategory(catId) {
  activeCategory.value = catId
  currentPage.value = 1
  updateQuery()
  loadProducts()
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
  loadProducts()
})

onMounted(() => {
  initFromQuery()
  loadCategories()
  loadProducts()
})
</script>

<style scoped>
.product-list-page :deep(.page-banner) {
  min-height: 200px;
  padding: 80px 0 40px;
  display: flex;
  align-items: center;
  text-align: left;
  background: var(--graphite);
}

.product-list-page :deep(.page-banner) .container {
  position: relative;
  z-index: 1;
}

.product-list-page :deep(.page-banner) .breadcrumb {
  margin-bottom: 16px;
  padding: 0;
}

.product-list-page :deep(.page-banner) h1 {
  font-family: var(--font-serif);
  font-size: 42px;
  font-weight: 700;
  color: var(--white);
  margin-bottom: 8px;
}

.banner-subtitle {
  font-size: 14px;
  color: var(--gold);
  letter-spacing: 4px;
  text-transform: uppercase;
  margin: 0;
}

.main-content {
  padding-top: 40px;
  padding-bottom: 60px;
}

.category-filter {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  padding-bottom: 32px;
  justify-content: center;
}

.filter-btn {
  padding: 10px 28px;
  font-size: 14px;
  font-weight: 500;
  color: var(--ink);
  background: transparent;
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all var(--transition-base);
}

.filter-btn:hover {
  background: var(--gray-100);
  border-color: var(--gray-400);
}

.filter-btn.active {
  color: var(--white);
  background: var(--red);
  border-color: var(--red);
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  padding-bottom: 20px;
}

@media (max-width: 992px) {
  .products-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .product-list-page :deep(.page-banner) {
    min-height: 160px;
    padding: 64px 0 32px;
  }

  .product-list-page :deep(.page-banner) h1 {
    font-size: 30px;
  }

  .products-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }

  .category-filter {
    gap: 8px;
    padding-bottom: 24px;
  }

  .filter-btn {
    padding: 8px 18px;
    font-size: 13px;
  }
}
</style>
