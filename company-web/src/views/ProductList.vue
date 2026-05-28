<template>
  <div class="product-list-page">
    <div class="page-banner">
      <h1>产品中心</h1>
      <p>PRODUCTS CENTER</p>
    </div>

    <div class="container">
      <!-- 分类筛选 -->
      <div class="category-filter">
        <button
          class="filter-btn"
          :class="{ active: !activeCategory }"
          @click="selectCategory(null)"
        >
          全部产品
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
.category-filter {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  padding: 30px 0 20px;
  justify-content: center;
}

.filter-btn {
  padding: 8px 24px;
  font-size: 14px;
  color: var(--color-text);
  background: var(--color-bg-gray);
  border: 1px solid transparent;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.filter-btn:hover {
  color: var(--color-primary);
  border-color: var(--color-primary);
}

.filter-btn.active {
  color: #ffffff;
  background: var(--color-primary);
  border-color: var(--color-primary);
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  padding: 10px 0 20px;
}

@media (max-width: 992px) {
  .products-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .products-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }

  .category-filter {
    gap: 8px;
  }

  .filter-btn {
    padding: 6px 16px;
    font-size: 13px;
  }
}
</style>
