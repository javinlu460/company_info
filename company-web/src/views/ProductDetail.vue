<template>
  <div class="product-detail-page">
    <div class="page-banner">
      <h1>产品详情</h1>
      <p>PRODUCT DETAIL</p>
    </div>

    <div class="breadcrumb">
      <div class="container">
        <router-link to="/">首页</router-link>
        <span>/</span>
        <router-link to="/products">产品中心</router-link>
        <span>/</span>
        <span class="current">{{ product.name || '产品详情' }}</span>
      </div>
    </div>

    <div v-if="loading" class="loading-wrap">
      <div class="loading-spinner"></div>
      <span>加载中...</span>
    </div>

    <div v-else-if="product.id" class="container">
      <div class="detail-layout">
        <div class="detail-main">
          <h1 class="detail-title">{{ product.name }}</h1>
          <div v-if="product.imageUrl" class="detail-image">
            <img :src="getImageUrl(product.imageUrl)" :alt="product.name" />
          </div>
          <div v-if="product.description" class="detail-desc">
            <p>{{ product.description }}</p>
          </div>
          <div v-if="product.content" class="detail-content" v-html="product.content"></div>
        </div>

        <aside class="detail-sidebar">
          <h3 class="sidebar-title">相关产品</h3>
          <div v-if="relatedProducts.length" class="related-list">
            <div
              v-for="item in relatedProducts"
              :key="item.id"
              class="related-item"
              @click="$router.push(`/products/${item.id}`)"
            >
              <img v-if="item.imageUrl" :src="getImageUrl(item.imageUrl)" :alt="item.name" />
              <span>{{ item.name }}</span>
            </div>
          </div>
          <div v-else class="sidebar-empty">暂无相关产品</div>
        </aside>
      </div>
    </div>

    <div v-else class="empty-state">
      <div class="empty-icon">📦</div>
      <p>产品不存在或已下架</p>
      <router-link to="/products" class="btn-primary" style="margin-top: 16px;">返回产品列表</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getProductDetail, getProductList } from '../api/product'

const route = useRoute()
const product = ref({})
const relatedProducts = ref([])
const loading = ref(false)

function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return 'http://localhost:8080' + url
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
.breadcrumb .current {
  color: var(--color-text);
}

.detail-layout {
  display: flex;
  gap: 40px;
  padding: 30px 0 60px;
}

.detail-main {
  flex: 1;
  min-width: 0;
}

.detail-title {
  font-size: 26px;
  font-weight: 700;
  color: var(--color-text);
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 2px solid var(--color-primary);
}

.detail-image {
  margin-bottom: 24px;
  border-radius: 6px;
  overflow: hidden;
}

.detail-image img {
  width: 100%;
  max-height: 500px;
  object-fit: contain;
  background: var(--color-bg-gray);
}

.detail-desc {
  font-size: 15px;
  color: var(--color-text-secondary);
  line-height: 1.8;
  margin-bottom: 24px;
  padding: 16px;
  background: var(--color-bg-gray);
  border-radius: 6px;
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

.detail-content :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 16px 0;
}

.detail-content :deep(table td),
.detail-content :deep(table th) {
  border: 1px solid var(--color-border);
  padding: 8px 12px;
}

.detail-sidebar {
  width: 280px;
  flex-shrink: 0;
}

.sidebar-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-primary);
  padding-bottom: 12px;
  border-bottom: 2px solid var(--color-primary);
  margin-bottom: 16px;
}

.related-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 0;
  border-bottom: 1px solid var(--color-border);
  cursor: pointer;
  transition: color 0.3s;
}

.related-item:hover {
  color: var(--color-secondary);
}

.related-item img {
  width: 60px;
  height: 45px;
  object-fit: cover;
  border-radius: 4px;
  flex-shrink: 0;
}

.related-item span {
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.sidebar-empty {
  font-size: 14px;
  color: var(--color-text-secondary);
  text-align: center;
  padding: 30px 0;
}

@media (max-width: 768px) {
  .detail-layout {
    flex-direction: column;
    gap: 30px;
  }

  .detail-sidebar {
    width: 100%;
  }

  .detail-title {
    font-size: 22px;
  }
}
</style>
