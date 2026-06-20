<template>
  <article class="news-card" @click="$router.push(`/news/${news.id}`)">
    <!-- 图片区域 -->
    <div class="card-image">
      <img
        v-if="coverUrl"
        :src="coverUrl"
        :alt="news.title"
        class="card-img"
      />
      <div v-else class="card-placeholder"></div>
      <span v-if="news.categoryName" class="card-category-tag">{{ news.categoryName }}</span>
    </div>

    <!-- 内容区域 -->
    <div class="card-body">
      <time class="card-date">{{ formattedDate }}</time>
      <h3 class="card-title">{{ news.title }}</h3>
      <p v-if="summary" class="card-summary">{{ summary }}</p>
    </div>
  </article>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  news: {
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

const coverUrl = computed(() => {
  const raw = props.news.coverImage || props.news.cover || props.news.thumbnail || props.news.image || ''
  return getImageUrl(raw)
})

const formattedDate = computed(() => {
  const date = props.news.publishDate || props.news.createTime
  if (!date) return ''
  const d = new Date(date)
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}.${m}.${day}`
})

const summary = computed(() => {
  const text = props.news.summary || props.news.description || ''
  return text.length > 80 ? text.substring(0, 80) + '...' : text
})
</script>

<style scoped>
.news-card {
  background: var(--white);
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-sm);
  overflow: hidden;
  cursor: pointer;
  transition: transform var(--transition-base), box-shadow var(--transition-base);
  display: flex;
  flex-direction: column;
}

.news-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-md);
}

/* 图片区域 */
.card-image {
  position: relative;
  width: 100%;
  padding-top: 56.25%; /* 16:9 */
  overflow: hidden;
  background: var(--gray-200);
  flex-shrink: 0;
}

.card-img {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--transition-base);
}

.news-card:hover .card-img {
  transform: scale(1.05);
}

.card-placeholder {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, #23272B 0%, #1A3F5C 100%);
}

.card-category-tag {
  position: absolute;
  top: 12px;
  left: 12px;
  padding: 3px 10px;
  font-size: 12px;
  font-weight: 500;
  color: var(--white);
  background: rgba(20, 20, 26, 0.72);
  border-radius: var(--radius-sm);
  backdrop-filter: blur(4px);
  z-index: 1;
}

/* 内容区域 */
.card-body {
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex: 1;
}

.card-date {
  font-size: 12px;
  color: var(--gray-500);
  letter-spacing: 0.02em;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--ink);
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  transition: color var(--transition-fast);
}

.news-card:hover .card-title {
  color: var(--red);
}

.card-summary {
  font-size: 14px;
  color: var(--gray-500);
  line-height: 1.65;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin: 0;
}
</style>
