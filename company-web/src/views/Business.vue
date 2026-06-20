<template>
  <div class="business-page">
    <!-- 页面 Banner -->
    <div class="page-banner">
      <div class="container">
        <nav class="breadcrumb">
          <router-link to="/">首页</router-link>
          <span class="separator">/</span>
          <span class="current">解决方案</span>
        </nav>
        <h1>解决方案</h1>
        <p class="subtitle">SOLUTIONS</p>
      </div>
    </div>

    <!-- 解决方案网格 -->
    <section class="section-light solutions-section">
      <div class="container">
        <div class="section-title">
          <h2>我们的解决方案</h2>
          <p class="section-subtitle">覆盖多行业、多场景的专业服务能力，助力企业实现数字化转型与业务升级</p>
          <div class="title-line"></div>
        </div>

        <div v-if="loading" class="loading-wrap">
          <div class="loading-spinner"></div>
          <span>加载中...</span>
        </div>

        <template v-else>
          <div v-if="businessList.length" class="solutions-grid">
            <router-link
              v-for="(item, index) in businessList"
              :key="item.id"
              :to="`/news/${item.id}`"
              class="solution-card"
            >
              <div class="card-bg"></div>
              <div class="card-content">
                <div class="card-icon" v-html="getIconSvg(index)"></div>
                <h3 class="card-title">{{ item.title }}</h3>
                <p class="card-desc">{{ truncateText(item.summary || item.description, 110) }}</p>
                <span class="card-link">
                  了解更多
                  <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M5 12h14M12 5l7 7-7 7" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                  </svg>
                </span>
              </div>
            </router-link>
          </div>

          <div v-else class="empty-state">
            <div class="empty-icon">
              <svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg">
                <rect x="8" y="16" width="48" height="36" rx="4" stroke="currentColor" stroke-width="3" />
                <path d="M8 24l24 14 24-14" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" />
              </svg>
            </div>
            <p>暂无解决方案信息</p>
          </div>
        </template>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getNewsList, getNewsCategories } from '../api/news'

const businessList = ref([])
const loading = ref(false)

const iconSvgs = [
  `<svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg"><circle cx="32" cy="32" r="26" stroke="currentColor" stroke-width="3"/><path d="M32 20v12l8 8" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/></svg>`,
  `<svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M10 54V22l22-12 22 12v32H10z" stroke="currentColor" stroke-width="3" stroke-linejoin="round"/><path d="M22 54V34h20v20" stroke="currentColor" stroke-width="3" stroke-linejoin="round"/></svg>`,
  `<svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg"><circle cx="32" cy="26" r="14" stroke="currentColor" stroke-width="3"/><path d="M12 54c0-11 9-20 20-20s20 9 20 20" stroke="currentColor" stroke-width="3" stroke-linecap="round"/></svg>`,
  `<svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg"><rect x="12" y="12" width="40" height="40" rx="6" stroke="currentColor" stroke-width="3"/><path d="M20 32h24M32 20v24" stroke="currentColor" stroke-width="3" stroke-linecap="round"/></svg>`,
  `<svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M14 50V20l18-10 18 10v30H14z" stroke="currentColor" stroke-width="3" stroke-linejoin="round"/><circle cx="32" cy="30" r="6" stroke="currentColor" stroke-width="3"/><path d="M26 46h12" stroke="currentColor" stroke-width="3" stroke-linecap="round"/></svg>`,
  `<svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg"><circle cx="32" cy="32" r="22" stroke="currentColor" stroke-width="3"/><path d="M32 18v14l10 6" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/></svg>`,
  `<svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M10 48V24l22-12 22 12v24" stroke="currentColor" stroke-width="3" stroke-linejoin="round"/><path d="M10 24l22 12 22-12" stroke="currentColor" stroke-width="3" stroke-linejoin="round"/></svg>`,
  `<svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg"><rect x="14" y="10" width="36" height="44" rx="4" stroke="currentColor" stroke-width="3"/><path d="M22 22h20M22 32h20M22 42h14" stroke="currentColor" stroke-width="3" stroke-linecap="round"/></svg>`
]

function getIconSvg(index) {
  return iconSvgs[index % iconSvgs.length]
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
      c.name.includes('业务') || c.name.includes('领域') || c.name.includes('应用') || c.name.includes('方案')
    )
    const params = { pageNum: 1, pageSize: 20 }
    if (bizCat) params.categoryId = bizCat.id
    const res = await getNewsList(params)
    businessList.value = res.records || res.list || []
  } catch (e) {
    console.error('加载解决方案失败:', e)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadBusiness()
})
</script>

<style scoped>
/* 页面 Banner */
.page-banner {
  position: relative;
  background: linear-gradient(135deg, var(--graphite) 0%, var(--blue) 100%);
  color: var(--paper);
  padding: 120px 0 80px;
  text-align: center;
  margin-top: var(--header-height);
  overflow: hidden;
}

.page-banner::before {
  content: '';
  position: absolute;
  inset: 0;
  background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.03'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
  pointer-events: none;
}

.page-banner::after {
  content: '';
  position: absolute;
  inset: 0;
  background: rgba(0, 0, 0, 0.32);
  pointer-events: none;
}

.page-banner .container {
  position: relative;
  z-index: 1;
}

.page-banner h1 {
  font-family: var(--font-serif);
  font-size: 42px;
  font-weight: 700;
  margin-bottom: 8px;
  line-height: 1.2;
}

.page-banner .subtitle {
  font-family: var(--font-serif);
  font-size: 18px;
  color: var(--gold);
  letter-spacing: 4px;
  max-width: none;
  margin: 0 auto;
}

.breadcrumb {
  padding: 0 0 16px;
  font-size: 14px;
  color: rgba(242, 243, 239, 0.8);
  background: transparent;
}

.breadcrumb a {
  color: var(--paper);
  transition: color var(--transition-fast);
}

.breadcrumb a:hover {
  color: var(--gold);
}

.breadcrumb .separator {
  margin: 0 10px;
  color: rgba(242, 243, 239, 0.5);
}

.breadcrumb .current {
  color: var(--gold);
}

/* 解决方案网格 */
.solutions-section {
  padding: var(--section-padding) 0;
}

.solutions-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
  padding: 10px 0 60px;
}

.solution-card {
  position: relative;
  min-height: 360px;
  border-radius: var(--radius-md);
  overflow: hidden;
  text-decoration: none;
  display: flex;
  flex-direction: column;
  border: 1px solid transparent;
  transition: transform var(--transition-base), box-shadow var(--transition-base), border-color var(--transition-base);
}

.solution-card:hover {
  transform: scale(1.02);
  border-color: var(--gold);
  box-shadow: 0 0 24px rgba(191, 142, 42, 0.18), 0 12px 32px rgba(0, 0, 0, 0.2);
}

.card-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(145deg, #1e1f24 0%, #14141a 60%, #1a3f5c 100%);
  z-index: 0;
}

.card-bg::before {
  content: '';
  position: absolute;
  inset: 0;
  background: url("data:image/svg+xml,%3Csvg width='40' height='40' viewBox='0 0 40 40' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='%23ffffff' fill-opacity='0.025' fill-rule='evenodd'%3E%3Cpath d='M0 0h40v40H0V0zm20 20h20v20H20V20zM0 20h20v20H0V20zM20 0h20v20H20V0z'/%3E%3C/g%3E%3C/svg%3E");
  opacity: 0.6;
}

.card-bg::after {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(20, 20, 26, 0.2) 0%, rgba(20, 20, 26, 0.92) 100%);
}

.card-content {
  position: relative;
  z-index: 1;
  padding: 40px 32px;
  display: flex;
  flex-direction: column;
  flex: 1;
}

.card-icon {
  width: 64px;
  height: 64px;
  color: var(--gold);
  margin-bottom: 24px;
}

.card-title {
  font-family: var(--font-serif);
  font-size: 22px;
  font-weight: 700;
  color: var(--white);
  margin-bottom: 16px;
  line-height: 1.3;
}

.card-desc {
  font-size: 14px;
  color: rgba(242, 243, 239, 0.68);
  line-height: 1.8;
  margin-bottom: 24px;
  flex: 1;
}

.card-link {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 500;
  color: var(--gold);
  transition: gap var(--transition-fast);
}

.card-link svg {
  width: 16px;
  height: 16px;
  transition: transform var(--transition-fast);
}

.solution-card:hover .card-link {
  gap: 12px;
}

.solution-card:hover .card-link svg {
  transform: translateX(4px);
}

/* 空状态 */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 240px;
  color: var(--color-text-light);
  font-size: 15px;
  text-align: center;
}

.empty-icon {
  width: 56px;
  height: 56px;
  margin-bottom: 16px;
  opacity: 0.4;
}

/* 响应式 */
@media (max-width: 992px) {
  .solutions-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .page-banner {
    padding: 80px 0 60px;
  }

  .page-banner h1 {
    font-size: 30px;
  }

  .solutions-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .solution-card {
    min-height: 300px;
  }

  .card-content {
    padding: 28px 24px;
  }
}
</style>
