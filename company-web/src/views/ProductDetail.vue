<template>
  <div class="product-detail-page">
    <div class="page-banner">
      <div class="container">
        <nav class="breadcrumb">
          <router-link to="/">首页</router-link>
          <span class="separator">/</span>
          <router-link to="/products">产品中心</router-link>
          <span class="separator">/</span>
          <span class="current">{{ productName || '产品详情' }}</span>
        </nav>
        <h1>产品详情</h1>
        <p class="banner-subtitle">PRODUCT DETAIL</p>
      </div>
    </div>

    <div v-if="loading" class="loading-wrap">
      <div class="loading-spinner"></div>
      <span>加载中...</span>
    </div>

    <template v-else-if="product.id">
      <section class="section-light product-info-section">
        <div class="container">
          <div class="product-info">
            <div class="info-image">
              <img
                v-if="imageUrl"
                :src="imageUrl"
                :alt="productName"
              />
              <div v-else class="image-placeholder">
                <span>{{ productName.charAt(0) }}</span>
              </div>
            </div>
            <div class="info-content">
              <span v-if="categoryName" class="category-tag">{{ categoryName }}</span>
              <h1 class="product-name">{{ productName }}</h1>
              <p v-if="product.description" class="product-desc">{{ product.description }}</p>
              <router-link to="/contact" class="btn-red cta-btn">
                联系咨询
              </router-link>
            </div>
          </div>
        </div>
      </section>

      <section class="section-white content-section">
        <div class="container">
          <div class="content-card" v-html="decodedContent"></div>
        </div>
      </section>

      <section v-if="relatedProducts.length" class="section-light related-section">
        <div class="container">
          <div class="section-title">
            <h2>相关产品</h2>
            <div class="title-line"></div>
          </div>
          <div class="related-grid">
            <ProductCard
              v-for="item in relatedProducts"
              :key="item.id"
              :product="item"
            />
          </div>
        </div>
      </section>
    </template>

    <div v-else class="empty-state">
      <div class="empty-icon">📦</div>
      <p>产品不存在或已下架</p>
      <router-link to="/products" class="btn-red" style="margin-top: 16px;">返回产品列表</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getProductDetail, getProductList } from '../api/product'
import ProductCard from '../components/ProductCard.vue'

const route = useRoute()
const product = ref({})
const relatedProducts = ref([])
const loading = ref(false)

const productName = computed(() => product.value.productName || product.value.name || '')
const categoryName = computed(() => product.value.categoryName || '')
const imageUrl = computed(() => {
  const url = product.value.productImage || product.value.imageUrl || product.value.coverImage
  return getImageUrl(url)
})

const decodedContent = computed(() => {
  if (!product.value.content) return ''
  return decodeHtmlEntities(product.value.content)
})

function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  if (url.startsWith('/api/')) return url
  return '/api' + (url.startsWith('/') ? url : '/' + url)
}

function decodeHtmlEntities(text) {
  if (!text) return ''
  const textarea = document.createElement('textarea')
  textarea.innerHTML = text
  return textarea.value
}

async function loadProduct(id) {
  loading.value = true
  try {
    product.value = await getProductDetail(id)
    loadRelatedProducts()
  } catch (e) {
    console.error('加载产品详情失败:', e)
    product.value = {}
  } finally {
    loading.value = false
  }
}

async function loadRelatedProducts() {
  if (!product.value.categoryId) return
  try {
    const res = await getProductList({
      pageNum: 1,
      pageSize: 5,
      categoryId: product.value.categoryId
    })
    const list = res.records || res.list || []
    relatedProducts.value = list.filter(p => p.id !== product.value.id).slice(0, 4)
  } catch (e) {
    console.warn('加载相关产品失败:', e)
  }
}

onMounted(() => {
  loadProduct(route.params.id)
})

watch(() => route.params.id, (newId) => {
  if (newId) loadProduct(newId)
})
</script>

<style scoped>
.product-detail-page :deep(.page-banner) {
  min-height: 200px;
  padding: 80px 0 40px;
  display: flex;
  align-items: center;
  text-align: left;
  background: var(--graphite);
}

.product-detail-page :deep(.page-banner) .container {
  position: relative;
  z-index: 1;
}

.product-detail-page :deep(.page-banner) .breadcrumb {
  margin-bottom: 16px;
  padding: 0;
}

.product-detail-page :deep(.page-banner) h1 {
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

.product-info-section {
  padding: 60px 0;
}

.product-info {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 48px;
  align-items: center;
}

.info-image {
  border-radius: var(--radius-md);
  overflow: hidden;
  box-shadow: var(--shadow-lg);
  background: var(--white);
}

.info-image img {
  width: 100%;
  height: auto;
  max-height: 480px;
  object-fit: contain;
  display: block;
}

.image-placeholder {
  aspect-ratio: 16 / 10;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--graphite) 0%, var(--blue) 100%);
}

.image-placeholder span {
  font-family: var(--font-serif);
  font-size: 72px;
  color: var(--paper);
  opacity: 0.6;
}

.info-content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.category-tag {
  display: inline-block;
  padding: 6px 16px;
  font-size: 13px;
  color: var(--red);
  background: rgba(178, 43, 43, 0.08);
  border: 1px solid rgba(178, 43, 43, 0.2);
  border-radius: var(--radius-sm);
  margin-bottom: 16px;
}

.product-name {
  font-family: var(--font-serif);
  font-size: 36px;
  font-weight: 700;
  color: var(--ink);
  line-height: 1.3;
  margin-bottom: 20px;
}

.product-desc {
  font-size: 15px;
  color: var(--gray-600);
  line-height: 1.8;
  margin-bottom: 32px;
}

.cta-btn {
  padding: 14px 40px;
  font-size: 16px;
}

.content-section {
  padding: 60px 0;
}

.content-card {
  max-width: 900px;
  margin: 0 auto;
  padding: 40px;
  background: var(--white);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
  line-height: 1.8;
  font-size: 15px;
  color: var(--ink);
}

.content-card :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: var(--radius-sm);
  margin: 16px 0;
}

.content-card :deep(p) {
  margin-bottom: 16px;
}

.content-card :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 16px 0;
}

.content-card :deep(table td),
.content-card :deep(table th) {
  border: 1px solid var(--gray-300);
  padding: 10px 14px;
}

.related-section {
  padding: 80px 0;
}

.related-section .section-title {
  margin-bottom: 48px;
}

.related-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

@media (max-width: 992px) {
  .related-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .product-detail-page :deep(.page-banner) {
    min-height: 160px;
    padding: 64px 0 32px;
  }

  .product-detail-page :deep(.page-banner) h1 {
    font-size: 30px;
  }

  .product-info {
    grid-template-columns: 1fr;
    gap: 32px;
  }

  .product-name {
    font-size: 26px;
  }

  .content-card {
    padding: 24px;
  }

  .related-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
}
</style>
