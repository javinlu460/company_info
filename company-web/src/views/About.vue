<template>
  <div class="about-page">
    <!-- 页面 Banner -->
    <div class="page-banner">
      <div class="container">
        <nav class="breadcrumb">
          <router-link to="/">首页</router-link>
          <span class="separator">/</span>
          <span class="current">关于我们</span>
        </nav>
        <h1>关于我们</h1>
        <p class="banner-subtitle">ABOUT US</p>
      </div>
    </div>

    <!-- 公司简介区 -->
    <section class="section-light intro-section">
      <div class="container">
        <div class="intro-grid">
          <div class="intro-text">
            <div class="section-label">COMPANY PROFILE</div>
            <h2 class="intro-title">公司简介</h2>
            <div v-if="loading" class="loading-wrap">
              <div class="loading-spinner"></div>
              <span>加载中...</span>
            </div>
            <div v-else class="intro-body" v-html="decodeHtml(configData.aboutContent)"></div>
          </div>
          <div class="intro-visual">
            <div class="intro-image-wrap">
              <img v-if="siteConfig.logo" :src="getImageUrl(siteConfig.logo)" alt="公司形象" />
              <div v-else class="intro-image-fallback">
                <svg viewBox="0 0 120 120" fill="none" xmlns="http://www.w3.org/2000/svg">
                  <rect width="120" height="120" rx="8" fill="#23272B" />
                  <path d="M60 30L90 95H30L60 30Z" stroke="#BF8E2A" stroke-width="3" fill="none" />
                  <circle cx="60" cy="55" r="12" stroke="#BF8E2A" stroke-width="3" fill="none" />
                </svg>
              </div>
              <div class="intro-badge">
                <strong>{{ stats.founded }}</strong>
                <span>成立年份</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 核心数据统计 -->
    <section class="section-dark stats-section">
      <div class="container">
        <div class="stats-grid">
          <div v-for="(item, index) in statList" :key="index" class="stat-item">
            <div class="stat-number">{{ item.value }}</div>
            <div class="stat-label">{{ item.label }}</div>
          </div>
        </div>
      </div>
    </section>

    <!-- 发展历程 -->
    <section class="section-white timeline-section">
      <div class="container">
        <div class="section-title">
          <h2>发展历程</h2>
          <p>DEVELOPMENT HISTORY</p>
          <div class="title-line"></div>
        </div>
        <div class="timeline">
          <div v-for="(m, index) in milestones" :key="index" class="timeline-item">
            <div class="timeline-year">{{ m.year }}</div>
            <div class="timeline-dot"></div>
            <div class="timeline-content">
              <h4>{{ m.title }}</h4>
              <p>{{ m.desc }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 企业文化 Tab -->
    <section class="section-light culture-section">
      <div class="container">
        <div class="section-title">
          <h2>企业文化</h2>
          <p>CORPORATE CULTURE</p>
          <div class="title-line"></div>
        </div>

        <div class="culture-tabs">
          <button
            v-for="tab in tabs"
            :key="tab.key"
            class="tab-btn"
            :class="{ active: activeTab === tab.key }"
            @click="switchTab(tab.key)"
          >
            {{ tab.name }}
          </button>
        </div>

        <div class="culture-card">
          <div v-if="loading" class="loading-wrap">
            <div class="loading-spinner"></div>
            <span>加载中...</span>
          </div>
          <div v-else-if="tabContent" class="culture-content" v-html="tabContent"></div>
          <div v-else class="empty-state">
            <div class="empty-icon">📄</div>
            <p>暂无相关内容</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 资质荣誉 -->
    <section class="section-white honor-section">
      <div class="container">
        <div class="section-title">
          <h2>资质荣誉</h2>
          <p>QUALIFICATIONS & HONORS</p>
          <div class="title-line"></div>
        </div>

        <div v-if="loading" class="loading-wrap">
          <div class="loading-spinner"></div>
          <span>加载中...</span>
        </div>
        <div v-else-if="honorContent" class="honor-content" v-html="honorContent"></div>
        <div v-else class="honor-grid">
          <div v-for="n in 4" :key="n" class="honor-card">
            <div class="honor-icon">
              <svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg">
                <circle cx="32" cy="32" r="28" stroke="#BF8E2A" stroke-width="2" />
                <path d="M32 18L36 28H47L38 35L41 46L32 39L23 46L26 35L17 28H28L32 18Z" fill="#BF8E2A" />
              </svg>
            </div>
            <h4>荣誉资质 {{ n }}</h4>
            <p>行业认可与企业实力认证</p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { getSiteConfig } from '../api/home'

const tabs = [
  { key: 'about', name: '公司简介' },
  { key: 'culture', name: '企业文化' },
  { key: 'honor', name: '资质荣誉' },
  { key: 'vision', name: '公司愿景' }
]

const activeTab = ref('about')
const loading = ref(false)
const configData = ref({})
const siteConfig = ref({})

const stats = ref({
  founded: '2010',
  capital: '5000万',
  partners: '200+',
  cases: '1000+'
})

const statList = computed(() => [
  { value: stats.value.founded, label: '成立年份' },
  { value: stats.value.capital, label: '注册资金' },
  { value: stats.value.partners, label: '合作伙伴' },
  { value: stats.value.cases, label: '服务案例' }
])

const milestones = [
  { year: '2010', title: '公司成立', desc: '在行业内正式注册成立，开启专业化服务之路。' },
  { year: '2014', title: '规模扩张', desc: '业务覆盖全国多个省市，团队规模突破百人。' },
  { year: '2018', title: '技术升级', desc: '引进先进生产设备与管理体系，通过 ISO 质量体系认证。' },
  { year: '2022', title: '行业领先', desc: '成为细分领域标杆企业，服务客户超过千家。' },
  { year: '2026', title: '持续创新', desc: '持续加大研发投入，推动数字化转型与智能化升级。' }
]

const tabContent = computed(() => {
  const keyMap = {
    about: 'aboutContent',
    culture: 'cultureContent',
    honor: 'honorContent',
    vision: 'visionContent'
  }
  const field = keyMap[activeTab.value]
  return decodeHtml(configData.value[field] || '')
})

const honorContent = computed(() => decodeHtml(configData.value.honorContent || ''))

function decodeHtml(html) {
  if (!html) return ''
  return html
    .replace(/&lt;/g, '<')
    .replace(/&gt;/g, '>')
    .replace(/&amp;/g, '&')
    .replace(/&quot;/g, '"')
    .replace(/&#39;/g, "'")
}

function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  if (url.startsWith('/api/')) return url
  return '/api' + (url.startsWith('/') ? url : '/' + url)
}

async function loadConfig() {
  loading.value = true
  try {
    const data = await getSiteConfig()
    configData.value = data
    siteConfig.value = data
  } catch (e) {
    console.error('加载配置失败:', e)
  } finally {
    loading.value = false
  }
}

function switchTab(key) {
  activeTab.value = key
}

onMounted(() => {
  loadConfig()
})
</script>

<style scoped>
/* 页面 Banner */
.page-banner {
  background: var(--graphite);
  color: var(--paper);
  padding: 100px 0 64px;
  margin-top: var(--header-height);
}

.page-banner .container {
  position: relative;
  z-index: 1;
}

.page-banner h1 {
  font-family: var(--font-serif);
  font-size: 38px;
  font-weight: 700;
  color: #F2F3EF;
  margin-bottom: 8px;
  line-height: 1.2;
}

.banner-subtitle {
  font-size: 14px;
  color: var(--gold);
  letter-spacing: 4px;
  text-transform: uppercase;
  font-weight: 500;
  margin: 0;
}

.breadcrumb {
  padding: 0 0 16px;
  font-size: 14px;
  color: rgba(242, 243, 239, 0.7);
  background: transparent;
}

.breadcrumb a {
  color: rgba(242, 243, 239, 0.7);
  text-decoration: none;
  transition: color 0.2s;
}

.breadcrumb a:hover {
  color: var(--gold);
}

.breadcrumb .separator {
  margin: 0 10px;
  color: rgba(242, 243, 239, 0.4);
}

.breadcrumb .current {
  color: var(--gold);
}

/* 公司简介区 */
.intro-section {
  padding: var(--section-padding) 0;
}

.intro-grid {
  display: grid;
  grid-template-columns: 1.2fr 1fr;
  gap: 60px;
  align-items: center;
}

.section-label {
  font-size: 13px;
  font-weight: 600;
  color: var(--gold);
  letter-spacing: 2px;
  margin-bottom: 12px;
}

.intro-title {
  font-family: var(--font-serif);
  font-size: 34px;
  font-weight: 700;
  color: var(--ink);
  margin-bottom: 24px;
}

.intro-body {
  font-size: 15px;
  line-height: 1.9;
  color: var(--gray-600);
}

.intro-body :deep(p) {
  margin-bottom: 16px;
}

.intro-body :deep(h1),
.intro-body :deep(h2),
.intro-body :deep(h3) {
  font-family: var(--font-serif);
  color: var(--ink);
  margin: 24px 0 12px;
}

.intro-body :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: var(--radius-md);
  margin: 16px 0;
}

.intro-body :deep(ul),
.intro-body :deep(ol) {
  padding-left: 20px;
  margin-bottom: 16px;
}

.intro-body :deep(li) {
  margin-bottom: 8px;
}

.intro-visual {
  display: flex;
  justify-content: center;
}

.intro-image-wrap {
  position: relative;
  width: 100%;
  max-width: 420px;
  aspect-ratio: 1 / 1;
  border-radius: var(--radius-lg);
  overflow: hidden;
  background: var(--graphite);
  box-shadow: var(--shadow-xl);
}

.intro-image-wrap img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.intro-image-fallback {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.intro-image-fallback svg {
  width: 60%;
  height: auto;
}

.intro-badge {
  position: absolute;
  bottom: 24px;
  right: -16px;
  background: var(--red);
  color: var(--white);
  padding: 18px 24px;
  border-radius: var(--radius-md);
  box-shadow: var(--shadow-lg);
  text-align: center;
  min-width: 120px;
}

.intro-badge strong {
  display: block;
  font-family: var(--font-serif);
  font-size: 32px;
  line-height: 1;
  margin-bottom: 4px;
}

.intro-badge span {
  font-size: 13px;
  opacity: 0.9;
}

/* 核心数据统计 */
.stats-section {
  padding: 80px 0;
  background: var(--graphite);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 30px;
}

.stat-item {
  text-align: center;
  padding: 20px;
  border-right: 1px solid rgba(242, 243, 239, 0.1);
}

.stat-item:last-child {
  border-right: none;
}

.stat-number {
  font-family: var(--font-serif);
  font-size: 52px;
  font-weight: 700;
  color: var(--gold);
  line-height: 1.1;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 15px;
  color: rgba(242, 243, 239, 0.75);
  letter-spacing: 1px;
}

/* 发展历程 */
.timeline-section {
  padding: var(--section-padding) 0;
}

.timeline {
  position: relative;
  max-width: 900px;
  margin: 0 auto;
  padding-left: 120px;
}

.timeline::before {
  content: '';
  position: absolute;
  left: 100px;
  top: 8px;
  bottom: 8px;
  width: 2px;
  background: linear-gradient(180deg, var(--gold), var(--red));
}

.timeline-item {
  position: relative;
  padding-left: 32px;
  padding-bottom: 40px;
}

.timeline-item:last-child {
  padding-bottom: 0;
}

.timeline-year {
  position: absolute;
  left: -110px;
  top: 2px;
  width: 70px;
  text-align: right;
  font-family: var(--font-serif);
  font-size: 16px;
  font-weight: 700;
  color: var(--gold);
}

.timeline-dot {
  position: absolute;
  left: -26px;
  top: 6px;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: var(--red);
  border: 2px solid var(--white);
  box-shadow: 0 0 0 2px var(--red);
}

.timeline-content {
  background: var(--gray-100);
  border-radius: var(--radius-md);
  padding: 24px 28px;
}

.timeline-content h4 {
  font-family: var(--font-serif);
  font-size: 18px;
  color: var(--ink);
  margin-bottom: 8px;
}

.timeline-content p {
  font-size: 14px;
  color: var(--gray-600);
  line-height: 1.7;
}

/* 企业文化 Tab */
.culture-section {
  padding: var(--section-padding) 0;
}

.culture-tabs {
  display: flex;
  justify-content: center;
  gap: 48px;
  margin-bottom: 40px;
  border-bottom: 1px solid var(--gray-300);
}

.tab-btn {
  position: relative;
  padding: 14px 4px;
  font-size: 16px;
  font-weight: 500;
  color: var(--gray-600);
  background: transparent;
  border: none;
  cursor: pointer;
  transition: color var(--transition-base);
}

.tab-btn::after {
  content: '';
  position: absolute;
  left: 0;
  right: 0;
  bottom: -1px;
  height: 3px;
  background: var(--red);
  transform: scaleX(0);
  transition: transform var(--transition-base);
}

.tab-btn:hover {
  color: var(--red);
}

.tab-btn.active {
  color: var(--red);
  font-weight: 600;
}

.tab-btn.active::after {
  transform: scaleX(1);
}

.culture-card {
  background: var(--white);
  border-radius: var(--radius-lg);
  padding: 48px;
  box-shadow: var(--shadow-md);
  min-height: 260px;
}

.culture-content {
  font-size: 15px;
  line-height: 1.9;
  color: var(--gray-700);
}

.culture-content :deep(p) {
  margin-bottom: 16px;
}

.culture-content :deep(h1),
.culture-content :deep(h2),
.culture-content :deep(h3) {
  font-family: var(--font-serif);
  color: var(--ink);
  margin: 24px 0 12px;
}

.culture-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: var(--radius-md);
  margin: 16px 0;
}

.culture-content :deep(ul),
.culture-content :deep(ol) {
  padding-left: 20px;
  margin-bottom: 16px;
}

/* 资质荣誉 */
.honor-section {
  padding: var(--section-padding) 0;
}

.honor-content {
  font-size: 15px;
  line-height: 1.8;
  color: var(--gray-700);
}

.honor-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: var(--radius-md);
}

.honor-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.honor-card {
  background: var(--white);
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md);
  padding: 32px 24px;
  text-align: center;
  transition: all var(--transition-base);
}

.honor-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: var(--gold);
}

.honor-icon {
  width: 56px;
  height: 56px;
  margin: 0 auto 16px;
}

.honor-card h4 {
  font-family: var(--font-serif);
  font-size: 16px;
  color: var(--ink);
  margin-bottom: 8px;
}

.honor-card p {
  font-size: 13px;
  color: var(--gray-500);
}

/* 响应式 */
@media (max-width: 992px) {
  .intro-grid {
    grid-template-columns: 1fr;
    gap: 40px;
  }

  .intro-visual {
    order: -1;
  }

  .intro-image-wrap {
    max-width: 320px;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .stat-item {
    border-right: none;
    border-bottom: 1px solid rgba(242, 243, 239, 0.1);
  }

  .stat-item:nth-child(3),
  .stat-item:nth-child(4) {
    border-bottom: none;
  }

  .honor-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .page-banner {
    padding: 80px 0 48px;
  }

  .page-banner h1 {
    font-size: 28px;
  }

  .intro-title {
    font-size: 26px;
  }

  .intro-badge {
    right: 12px;
    bottom: 12px;
    padding: 12px 18px;
    min-width: auto;
  }

  .intro-badge strong {
    font-size: 24px;
  }

  .stats-grid {
    grid-template-columns: 1fr 1fr;
    gap: 20px;
  }

  .stat-number {
    font-size: 36px;
  }

  .culture-tabs {
    gap: 20px;
    flex-wrap: wrap;
  }

  .tab-btn {
    font-size: 14px;
    padding: 12px 2px;
  }

  .culture-card {
    padding: 28px 20px;
  }

  .timeline {
    padding-left: 20px;
  }

  .timeline-year {
    left: -24px;
    width: 48px;
    font-size: 14px;
  }

  .timeline-content {
    padding: 18px 20px;
  }

  .honor-grid {
    grid-template-columns: 1fr;
  }
}
</style>
