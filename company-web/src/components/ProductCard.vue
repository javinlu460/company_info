<template>
  <article class="product-card" @click="$router.push(`/products/${product.id}`)">
    <div class="card-img">
      <img :src="getImageUrl(product.imageUrl)" :alt="product.name" />
      <div class="card-overlay">
        <span>查看详情</span>
      </div>
    </div>
    <div class="card-body">
      <h3 class="card-title">{{ product.name }}</h3>
      <p v-if="product.description" class="card-desc">{{ truncateText(product.description, 50) }}</p>
    </div>
  </article>
</template>

<script setup>
const props = defineProps({
  product: {
    type: Object,
    required: true
  }
})

function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return 'http://localhost:8080' + url
}

function truncateText(text, maxLen) {
  if (!text) return ''
  return text.length > maxLen ? text.substring(0, maxLen) + '...' : text
}
</script>

<style scoped>
.product-card {
  background: #ffffff;
  border-radius: 6px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  border: 1px solid var(--color-border);
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.card-img {
  position: relative;
  overflow: hidden;
  aspect-ratio: 4 / 3;
}

.card-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s;
}

.product-card:hover .card-img img {
  transform: scale(1.05);
}

.card-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0, 51, 102, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.card-overlay span {
  color: #ffffff;
  font-size: 14px;
  padding: 6px 20px;
  border: 1px solid #ffffff;
  border-radius: 4px;
}

.product-card:hover .card-overlay {
  opacity: 1;
}

.card-body {
  padding: 16px;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text);
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.card-desc {
  font-size: 13px;
  color: var(--color-text-secondary);
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
