<template>
  <div class="business-page">
    <div class="page-banner">
      <h1>业务领域</h1>
      <p>BUSINESS AREAS</p>
    </div>

    <div class="container">
      <div class="section-title" style="margin-top: 40px;">
        <h2>我们的业务领域</h2>
        <p>Professional Business Areas</p>
        <div class="title-line"></div>
      </div>

      <div v-if="loading" class="loading-wrap">
        <div class="loading-spinner"></div>
        <span>加载中...</span>
      </div>

      <template v-else>
        <div v-if="businessList.length" class="business-grid">
          <div
            v-for="item in businessList"
            :key="item.id"
            class="business-card"
            @click="$router.push(`/news/${item.id}`)"
          >
            <div class="card-icon">
              <span>{{ getIcon(item) }}</span>
            </div>
            <h3 class="card-title">{{ item.title }}</h3>
            <p class="card-desc">{{ truncateText(item.summary || item.description, 100) }}</p>
            <span class="card-link">了解详情 &rarr;</span>
          </div>
        </div>
        <div v-else class="empty-state">
          <div class="empty-icon">💼</div>
          <p>暂无业务领域信息</p>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getNewsList, getNewsCategories } from '../api/news'

const businessList = ref([])
const loading = ref(false)

const iconList = ['🔧', '⚡', '🏗️', '🏭', '🌐', '📊', '💡', '🔬']

function getIcon(item) {
  const index = item.id ? item.id % iconList.length : 0
  return iconList[index]
}

function truncateText(text, maxLen) {
  if (!text) return ''
  return text.length > maxLen ? text.substring(0, maxLen) + '...' : text
}

async function loadBusiness() {
  loading.value = true
  try {
    const categories = await getNewsCategories()
    const bizCat = categories.find(c =>
      c.name.includes('业务') || c.name.includes('领域') || c.name.includes('应用')
    )
    const params = { pageNum: 1, pageSize: 20 }
    if (bizCat) params.categoryId = bizCat.id
    const res = await getNewsList(params)
    businessList.value = res.records || res.list || []
  } catch (e) {
    console.error('加载业务领域失败:', e)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadBusiness()
})
</script>

<style scoped>
.business-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
  padding: 10px 0 60px;
}

.business-card {
  background: #ffffff;
  border: 1px solid var(--color-border);
  border-radius: 8px;
  padding: 36px 28px;
  text-align: center;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s, border-color 0.3s;
}

.business-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 30px rgba(0, 51, 102, 0.12);
  border-color: var(--color-primary);
}

.card-icon {
  width: 72px;
  height: 72px;
  margin: 0 auto 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  background: var(--color-bg-gray);
  border-radius: 50%;
  transition: background-color 0.3s;
}

.business-card:hover .card-icon {
  background: rgba(0, 51, 102, 0.08);
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--color-text);
  margin-bottom: 12px;
}

.card-desc {
  font-size: 14px;
  color: var(--color-text-secondary);
  line-height: 1.7;
  margin-bottom: 16px;
}

.card-link {
  font-size: 14px;
  color: var(--color-primary);
  transition: color 0.3s;
}

.business-card:hover .card-link {
  color: var(--color-secondary);
}

@media (max-width: 992px) {
  .business-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .business-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .business-card {
    padding: 28px 20px;
  }
}
</style>
