<template>
  <div class="case-detail-page">
    <!-- 顶部 Banner -->
    <div class="page-banner">
      <div class="container">
        <nav class="breadcrumb">
          <router-link to="/">首页</router-link>
          <span class="separator">/</span>
          <router-link to="/cases">客户案例</router-link>
          <span class="separator">/</span>
          <span class="current">{{ caseData.title || '案例详情' }}</span>
        </nav>
        <h1>案例详情</h1>
        <p class="banner-subtitle">CASE DETAIL</p>
      </div>
    </div>

    <!-- 加载中 -->
    <div v-if="loading" class="loading-wrap">
      <div class="loading-spinner"></div>
      <span>加载中...</span>
    </div>

    <template v-else-if="caseData.id">
      <!-- 案例基本信息 -->
      <section class="section-light case-info-section">
        <div class="container">
          <div class="case-info">
            <!-- 封面图 -->
            <div class="info-image">
              <img
                v-if="coverImageUrl"
                :src="coverImageUrl"
                :alt="caseData.title"
              />
              <div v-else class="image-placeholder">
                <span>{{ caseData.title ? caseData.title.charAt(0) : 'C' }}</span>
              </div>
            </div>
            <!-- 文字内容 -->
            <div class="info-content">
              <span v-if="caseData.industry" class="industry-tag">{{ caseData.industry }}</span>
              <h1 class="case-title">{{ caseData.title }}</h1>
              <div v-if="caseData.clientName" class="meta-row">
                <span class="meta-label">客户名称</span>
                <span class="meta-value">{{ caseData.clientName }}</span>
              </div>
              <p v-if="caseData.description" class="case-desc">{{ caseData.description }}</p>
              <router-link to="/contact" class="btn-red cta-btn">
                发图纸，获取报价及解决方案 →
              </router-link>
            </div>
          </div>
        </div>
      </section>

      <!-- 详情富文本 -->
      <section v-if="decodedContent" class="section-white content-section">
        <div class="container">
          <div class="content-card" v-html="decodedContent"></div>
        </div>
      </section>

      <!-- 相关案例 -->
      <section v-if="relatedCases.length" class="section-light related-section">
        <div class="container">
          <div class="section-title-row">
            <h2>相关案例</h2>
            <div class="title-line"></div>
          </div>
          <div class="related-grid">
            <router-link
              v-for="item in relatedCases"
              :key="item.id"
              :to="`/cases/${item.id}`"
              class="related-card"
            >
              <div
                class="related-image"
                :style="{ backgroundImage: `url(${getImageUrl(item.coverImage) || defaultImage})` }"
              ></div>
              <div class="related-overlay">
                <h4>{{ item.title }}</h4>
                <span v-if="item.industry" class="related-tag">{{ item.industry }}</span>
              </div>
            </router-link>
          </div>
        </div>
      </section>
    </template>

    <!-- 不存在 -->
    <div v-else class="empty-state">
      <div class="empty-icon">📋</div>
      <p>案例不存在或已下架</p>
      <router-link to="/cases" class="btn-red" style="margin-top: 16px; display: inline-block; padding: 12px 28px; text-decoration: none; border-radius: 4px;">
        返回案例列表
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getCaseDetail, getCaseList } from '../api/case'

const route = useRoute()
const caseData = ref({})
const relatedCases = ref([])
const loading = ref(false)
const defaultImage = 'https://images.unsplash.com/photo-1504328341540-c860949d477d?w=600&h=400&fit=crop'

const coverImageUrl = computed(() => getImageUrl(caseData.value.coverImage))

const decodedContent = computed(() => {
  if (!caseData.value.content) return ''
  return decodeHtmlEntities(caseData.value.content)
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

async function loadCase(id) {
  loading.value = true
  try {
    const res = await getCaseDetail(id)
    caseData.value = res || {}
    loadRelatedCases()
  } catch (e) {
    console.error('加载案例详情失败:', e)
    caseData.value = {}
  } finally {
    loading.value = false
  }
}

async function loadRelatedCases() {
  try {
    const list = await getCaseList()
    relatedCases.value = (list || [])
      .filter(c => c.id !== caseData.value.id)
      .slice(0, 3)
  } catch (e) {
    console.warn('加载相关案例失败:', e)
  }
}

onMounted(() => {
  loadCase(route.params.id)
})

watch(() => route.params.id, (newId) => {
  if (newId) loadCase(newId)
})
</script>

<style scoped>
/* ===== Banner ===== */
.page-banner {
  margin-top: var(--header-height);
  min-height: 200px;
  padding: 80px 0 40px;
  display: flex;
  align-items: center;
  background: var(--graphite);
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 16px;
  font-size: 13px;
}

.breadcrumb a {
  color: rgba(255,255,255,0.6);
  text-decoration: none;
  transition: color 0.2s;
}

.breadcrumb a:hover {
  color: var(--white);
}

.separator {
  color: rgba(255,255,255,0.3);
}

.current {
  color: var(--white);
  font-weight: 500;
}

.page-banner h1 {
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

/* ===== Loading ===== */
.loading-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120px 0;
  gap: 16px;
  color: #6A6D70;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #e0e0e0;
  border-top-color: var(--red);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* ===== Case Info ===== */
.case-info-section {
  padding: 60px 0;
}

.case-info {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 48px;
  align-items: center;
}

.info-image {
  border-radius: var(--radius-md, 8px);
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0,0,0,0.12);
  background: #f5f5f5;
}

.info-image img {
  width: 100%;
  height: auto;
  max-height: 480px;
  object-fit: cover;
  display: block;
}

.image-placeholder {
  aspect-ratio: 16 / 10;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #2a2a2e 0%, #3a3d4a 100%);
}

.image-placeholder span {
  font-family: var(--font-serif);
  font-size: 72px;
  color: rgba(255,255,255,0.4);
}

.info-content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.industry-tag {
  display: inline-block;
  padding: 5px 14px;
  font-size: 13px;
  color: var(--white);
  background: rgba(178, 43, 43, 0.85);
  border-radius: 3px;
  margin-bottom: 16px;
}

.case-title {
  font-family: var(--font-serif);
  font-size: 34px;
  font-weight: 700;
  color: var(--ink);
  line-height: 1.3;
  margin-bottom: 16px;
}

.meta-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.meta-label {
  font-size: 13px;
  color: #6A6D70;
  background: #f0f0ee;
  padding: 3px 10px;
  border-radius: 3px;
}

.meta-value {
  font-size: 15px;
  font-weight: 600;
  color: var(--ink);
}

.case-desc {
  font-size: 15px;
  color: #6A6D70;
  line-height: 1.8;
  margin-bottom: 32px;
}

.btn-red {
  display: inline-block;
  padding: 13px 32px;
  background: var(--red);
  color: var(--white);
  border-radius: 4px;
  text-decoration: none;
  font-weight: 600;
  font-size: 15px;
  transition: background 0.2s, transform 0.2s;
}

.btn-red:hover {
  background: #a02020;
  transform: translateY(-2px);
}

/* ===== Content ===== */
.content-section {
  padding: 60px 0;
}

.content-card {
  max-width: 900px;
  margin: 0 auto;
  padding: 40px;
  background: var(--white);
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  line-height: 1.8;
  font-size: 15px;
  color: var(--ink);
}

.content-card :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 6px;
  margin: 16px 0;
}

.content-card :deep(p) {
  margin-bottom: 16px;
}

.content-card :deep(h2),
.content-card :deep(h3) {
  font-family: var(--font-serif);
  margin: 24px 0 12px;
  color: var(--ink);
}

/* ===== Related ===== */
.related-section {
  padding: 80px 0;
}

.section-title-row {
  margin-bottom: 40px;
}

.section-title-row h2 {
  font-family: var(--font-serif);
  font-size: 26px;
  font-weight: 700;
  color: var(--ink);
  margin-bottom: 12px;
}

.title-line {
  width: 40px;
  height: 3px;
  background: var(--red);
  border-radius: 2px;
}

.related-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.related-card {
  position: relative;
  display: block;
  border-radius: 8px;
  overflow: hidden;
  aspect-ratio: 16 / 10;
  text-decoration: none;
  cursor: pointer;
}

.related-image {
  position: absolute;
  inset: 0;
  background-size: cover;
  background-position: center;
  transition: transform 0.4s ease;
}

.related-card:hover .related-image {
  transform: scale(1.06);
}

.related-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(20,20,26,0.85) 0%, transparent 60%);
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  padding: 20px;
}

.related-overlay h4 {
  font-size: 16px;
  font-weight: 600;
  color: var(--white);
  margin-bottom: 8px;
}

.related-tag {
  display: inline-block;
  padding: 2px 10px;
  background: rgba(178,43,43,0.8);
  color: var(--white);
  font-size: 12px;
  border-radius: 3px;
}

/* ===== Empty ===== */
.empty-state {
  text-align: center;
  padding: 120px 0;
  color: #6A6D70;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

/* ===== Responsive ===== */
@media (max-width: 768px) {
  .page-banner {
    min-height: 160px;
    padding: 64px 0 32px;
  }

  .page-banner h1 {
    font-size: 28px;
  }

  .case-info {
    grid-template-columns: 1fr;
    gap: 32px;
  }

  .case-title {
    font-size: 26px;
  }

  .content-card {
    padding: 24px;
  }

  .related-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }
}
</style>
