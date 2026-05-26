<template>
  <article class="news-item" @click="$router.push(`/news/${news.id}`)">
    <div class="news-date">
      <span class="date-day">{{ day }}</span>
      <span class="date-year">{{ yearMonth }}</span>
    </div>
    <div class="news-content">
      <h3 class="news-title">{{ news.title }}</h3>
      <p v-if="news.summary || news.description" class="news-summary">
        {{ truncateText(news.summary || news.description, 120) }}
      </p>
      <span class="news-more">查看详情 &rarr;</span>
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

const day = computed(() => {
  const date = props.news.publishDate || props.news.createTime
  if (!date) return '--'
  return new Date(date).getDate()
})

const yearMonth = computed(() => {
  const date = props.news.publishDate || props.news.createTime
  if (!date) return ''
  const d = new Date(date)
  return `${d.getFullYear()}.${String(d.getMonth() + 1).padStart(2, '0')}`
})

function truncateText(text, maxLen) {
  if (!text) return ''
  return text.length > maxLen ? text.substring(0, maxLen) + '...' : text
}
</script>

<style scoped>
.news-item {
  display: flex;
  gap: 24px;
  padding: 20px 0;
  border-bottom: 1px solid var(--color-border);
  cursor: pointer;
  transition: background-color 0.3s;
}

.news-item:hover {
  background-color: rgba(0, 51, 102, 0.02);
}

.news-item:hover .news-title {
  color: var(--color-secondary);
}

.news-item:hover .news-more {
  color: var(--color-secondary);
}

.news-date {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 80px;
  height: 80px;
  background: var(--color-bg-gray);
  border-radius: 6px;
  border-left: 3px solid var(--color-primary);
}

.date-day {
  font-size: 28px;
  font-weight: 700;
  color: var(--color-primary);
  line-height: 1;
}

.date-year {
  font-size: 12px;
  color: var(--color-text-secondary);
  margin-top: 4px;
}

.news-content {
  flex: 1;
  min-width: 0;
}

.news-title {
  font-size: 17px;
  font-weight: 600;
  color: var(--color-text);
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color 0.3s;
}

.news-summary {
  font-size: 14px;
  color: var(--color-text-secondary);
  line-height: 1.6;
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.news-more {
  font-size: 13px;
  color: var(--color-primary);
  transition: color 0.3s;
}

@media (max-width: 768px) {
  .news-item {
    gap: 16px;
  }

  .news-date {
    width: 64px;
    height: 64px;
  }

  .date-day {
    font-size: 22px;
  }

  .news-title {
    font-size: 15px;
  }
}
</style>
