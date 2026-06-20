<template>
  <router-link :to="`/products/${product.id}`" class="product-card">
    <div class="card-image">
      <img
        v-if="imageUrl"
        :src="imageUrl"
        :alt="product.productName || product.name || '产品图片'"
      />
      <div v-else class="image-placeholder">
        <span>{{ (product.productName || product.name || '产品').charAt(0) }}</span>
      </div>
      <div class="card-overlay">
        <span class="overlay-text">查看详情</span>
      </div>
    </div>
    <div class="card-body">
      <h3 class="card-title">{{ product.productName || product.name }}</h3>
      <p v-if="product.description" class="card-desc">{{ product.description }}</p>
    </div>
  </router-link>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  product: {
    type: Object,
    required: true
  }
})

function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  if (url.startsWith('/api/')) return url
  return '/api' + (url.startsWith('/') ? url : '/' + url)
}

const imageUrl = computed(() => {
  const url = props.product.coverImage || props.product.productImage || props.product.imageUrl
  return getImageUrl(url)
})
</script>

<style scoped>
.product-card {
  display: block;
  background: var(--white);
  border-radius: var(--radius-md);
  overflow: hidden;
  box-shadow: var(--shadow-sm);
  text-decoration: none;
  transition: transform var(--transition-base), box-shadow var(--transition-base);
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

.card-image {
  position: relative;
  aspect-ratio: 16 / 10;
  overflow: hidden;
  border-radius: var(--radius-md) var(--radius-md) 0 0;
  background: var(--gray-200);
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-base);
}

.product-card:hover .card-image img {
  transform: scale(1.05);
}

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--graphite) 0%, var(--blue) 100%);
}

.image-placeholder span {
  font-family: var(--font-serif);
  font-size: 48px;
  color: var(--paper);
  opacity: 0.6;
}

.card-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(178, 43, 43, 0.72);
  opacity: 0;
  transition: opacity var(--transition-base);
}

.product-card:hover .card-overlay {
  opacity: 1;
}

.overlay-text {
  color: var(--white);
  font-size: 14px;
  font-weight: 500;
  padding: 8px 24px;
  border: 1px solid var(--white);
  border-radius: var(--radius-sm);
  transform: translateY(8px);
  transition: transform var(--transition-base);
}

.product-card:hover .overlay-text {
  transform: translateY(0);
}

.card-body {
  padding: 16px;
}

.card-title {
  font-size: 16px;
  font-weight: 500;
  color: var(--ink);
  line-height: 1.4;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-desc {
  font-size: 13px;
  color: var(--gray-500);
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin: 0;
}
</style>
