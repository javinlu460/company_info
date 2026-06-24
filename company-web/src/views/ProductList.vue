<template>
  <div class="product-list-page">
    <div class="container main-content">
      <!-- 页面标签 -->
      <div class="section-head">
        <span class="ew">PRODUCTS · 产品中心</span>
        <h2>专业制造，精密加工</h2>
        <p class="lead">涵盖泵类配件、传动运动件、农机配件、通用机械设备配件等多个品类，支持来图定制。</p>
      </div>

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
      </template>
    </div>

    <!-- CTA -->
    <section class="cta-section">
      <div class="container">
        <router-link to="/contact" class="btn-red cta-btn">
          发图纸，获取报价及解决方案 →
        </router-link>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getProductList, getProductCategories } from '../api/product'
import ProductCard from '../components/ProductCard.vue'

const route = useRoute()
const router = useRouter()

const categories = ref([])
const products = ref([])
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
      pageNum: 1,
      pageSize: 999
    }
    if (activeCategory.value) {
      params.categoryId = activeCategory.value
    }
    const res = await getProductList(params)
    products.value = res.records || res.list || []
  } catch (e) {
    console.error('加载产品列表失败:', e)
  } finally {
    loading.value = false
  }
}

function selectCategory(catId) {
  activeCategory.value = catId
  loadProducts()
}

function initFromQuery() {
  const { categoryId } = route.query
  if (categoryId) activeCategory.value = Number(categoryId)
}

onMounted(() => {
  initFromQuery()
  loadCategories()
  loadProducts()
})
</script>

<style scoped>
.main-content {
  margin-top: var(--header-height);
  padding-top: 30px;
  padding-bottom: 60px;
}

.section-head {
  margin-bottom: 32px;
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
  max-height: calc(2 * 320px + 24px);
  overflow: hidden;
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
    padding-bottom: 24px;
  }

  .filter-btn {
    padding: 8px 18px;
    font-size: 13px;
  }
}

/* CTA */
.cta-section {
  padding: 60px 0 80px;
  background: #F2F3EF;
  text-align: center;
}

.cta-btn {
  padding: 14px 40px;
  font-size: 16px;
  font-weight: 600;
}
</style>
