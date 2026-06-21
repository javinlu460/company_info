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
        <p class="banner-subtitle">SOLUTIONS</p>
      </div>
    </div>

    <!-- 解决方案 -->
    <section class="solutions-section">
      <div class="container">
        <div class="section-head">
          <span class="ew">SOLUTIONS · 解决方案</span>
          <h2>覆盖多场景的专业制造服务</h2>
          <p class="lead">
            从图纸到成品，我们为客户提供灵活、可靠的定制化制造解决方案，满足新品开发、批量生产、维修替换等多元需求。
          </p>
        </div>

        <div v-if="loading" class="loading-wrap">
          <div class="loading-spinner"></div>
          <span>加载中...</span>
        </div>

        <template v-else>
          <div class="cap-grid">
            <div
              v-for="(item, index) in displayList"
              :key="item.id || index"
              class="cap-card"
            >
              <span class="cap-num">{{ item.num }}</span>
              <h3>{{ item.title }}</h3>
              <p>{{ item.summary }}</p>
              <div v-if="item.specs && item.specs.length" class="cap-specs">
                <span
                  v-for="(spec, sIdx) in item.specs"
                  :key="sIdx"
                  :class="['tag', { r: spec.highlight }]"
                >
                  {{ spec.label }}
                </span>
              </div>
            </div>
          </div>
        </template>
      </div>
    </section>

    <!-- 合作流程 -->
    <section class="process-section">
      <div class="container">
        <div class="section-head center">
          <span class="ew">PROCESS · 合作流程</span>
          <h2>从发图纸到收货——五步搞定</h2>
        </div>

        <div class="process-steps">
          <div
            v-for="(step, idx) in processSteps"
            :key="idx"
            class="process-step"
          >
            <div class="step-circle">{{ idx + 1 }}</div>
            <h5>{{ step.title }}</h5>
            <p>{{ step.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 底部 CTA -->
    <section class="cta-section">
      <div class="container">
        <router-link to="/contact" class="btn-red cta-btn">
          立即上传图纸 · 获取报价 →
        </router-link>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getNewsList, getNewsCategories } from '../api/news'

const businessList = ref([])
const loading = ref(false)

const defaultSolutions = [
  {
    id: 'default-1',
    num: 'A',
    title: 'OEM 定制 · 按图加工',
    summary: '客户提供图纸或样品，我们按规格进行精密加工与组装，确保尺寸、材质、工艺完全符合要求。',
    specs: [
      { label: '来图加工' },
      { label: '批量稳定' },
      { label: '保密协议', highlight: true }
    ]
  },
  {
    id: 'default-2',
    num: 'B',
    title: '快速打样 · 新品开发验证',
    summary: '针对新品开发阶段提供小批量打样服务，快速验证结构设计与工艺可行性，缩短研发周期。',
    specs: [
      { label: '7-15 天交样' },
      { label: '工艺建议' },
      { label: '小批量', highlight: true }
    ]
  },
  {
    id: 'default-3',
    num: 'C',
    title: '小批量 · 维修/替换用件',
    summary: '为设备维护、备件更换提供灵活的小批量生产，无需开模大批囤货，降低库存与资金压力。',
    specs: [
      { label: '按需生产' },
      { label: '交期灵活' },
      { label: '可追溯', highlight: true }
    ]
  },
  {
    id: 'default-4',
    num: 'D',
    title: '逆向工程 · 来样复制',
    summary: '仅有实物样品即可完成测绘、建模与生产复制，帮助客户延续供应或优化老旧零部件。',
    specs: [
      { label: '三维测绘' },
      { label: '结构优化' },
      { label: '老旧件替代', highlight: true }
    ]
  }
]

const processSteps = [
  { title: '提交图纸', desc: '上传图纸、样品或需求说明' },
  { title: '工程师审核', desc: '评估工艺、材料与交期' },
  { title: '确认打样', desc: '报价确认后安排样品制作' },
  { title: '质检/交样报告', desc: '全尺寸检测并出具报告' },
  { title: '批量生产及交付', desc: '按订单批量生产准时交付' }
]

const displayList = computed(() => {
  if (businessList.value && businessList.value.length) {
    return businessList.value.map((item, index) => ({
      id: item.id,
      num: String.fromCharCode(65 + index),
      title: item.title,
      summary: truncateText(item.summary || item.description, 140),
      specs: buildSpecs(item, index)
    }))
  }
  return defaultSolutions
})

function buildSpecs(item, index) {
  const tags = []
  if (item.categoryName || item.category) {
    tags.push({ label: item.categoryName || item.category })
  }
  const highlights = ['交期灵活', '保密协议', '可追溯', '小批量']
  const extra = highlights[index % highlights.length]
  if (extra) {
    tags.push({ label: extra, highlight: true })
  }
  return tags
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
      c.name.includes('业务') ||
      c.name.includes('领域') ||
      c.name.includes('应用') ||
      c.name.includes('方案')
    )
    const params = { pageNum: 1, pageSize: 20 }
    if (bizCat) params.categoryId = bizCat.id
    const res = await getNewsList(params)
    businessList.value = res.records || res.list || []
  } catch (e) {
    console.error('加载解决方案失败:', e)
    businessList.value = []
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

/* 公共标题区 */
.section-head {
  margin-bottom: 44px;
}

.section-head.center {
  text-align: center;
}

.ew {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  font-weight: 600;
  color: var(--red);
  letter-spacing: 1px;
  text-transform: uppercase;
  margin-bottom: 14px;
}

.ew::before {
  content: '';
  width: 18px;
  height: 2px;
  background: var(--red);
}

.section-head h2 {
  font-family: var(--font-serif);
  font-size: 34px;
  font-weight: 700;
  color: var(--ink);
  margin-bottom: 14px;
  line-height: 1.25;
}

.section-head .lead {
  font-size: 15px;
  color: #6A6D70;
  line-height: 1.8;
  max-width: 720px;
  margin: 0;
}

/* 解决方案网格 */
.solutions-section {
  padding: var(--section-padding) 0;
  background: #fff;
}

.cap-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 22px;
}

.cap-card {
  position: relative;
  background: #fff;
  border: 1px solid #D0D3CC;
  border-radius: 4px;
  padding: 30px 28px;
  overflow: hidden;
  transition: box-shadow 0.3s ease, border-color 0.3s ease;
}

.cap-card::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: var(--red);
}

.cap-card:hover {
  border-color: var(--red);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.cap-num {
  display: block;
  font-family: var(--font-serif);
  font-size: 52px;
  font-weight: 700;
  color: #D0D3CC;
  line-height: 1;
  margin-bottom: 12px;
  opacity: 0.6;
}

.cap-card h3 {
  font-size: 18px;
  font-weight: 700;
  color: var(--ink);
  margin-bottom: 10px;
  line-height: 1.35;
}

.cap-card p {
  font-size: 14px;
  color: #6A6D70;
  line-height: 1.85;
  margin-bottom: 16px;
}

.cap-specs {
  display: flex;
  flex-wrap: wrap;
  gap: 7px;
}

.tag {
  display: inline-flex;
  align-items: center;
  font-size: 12px;
  padding: 4px 11px;
  border-radius: 2px;
  background: #E8EAE4;
  border: 1px solid #D0D3CC;
  color: #6A6D70;
}

.tag.r {
  background: #F2DADA;
  border-color: #E0C0C0;
  color: #8C1F1F;
}

/* 合作流程 */
.process-section {
  padding: var(--section-padding) 0;
  background: #F2F3EF;
}

.process-steps {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 0;
  position: relative;
}

.process-step {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 0 14px;
}

.process-step::before {
  content: '';
  position: absolute;
  top: 28px;
  left: 50%;
  right: -50%;
  height: 2px;
  background: #D0D3CC;
  z-index: 0;
}

.process-step:last-child::before {
  display: none;
}

.step-circle {
  position: relative;
  z-index: 1;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: #fff;
  border: 2px solid var(--red);
  color: var(--red);
  font-family: var(--font-serif);
  font-size: 18px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 18px;
}

.process-step h5 {
  font-size: 14px;
  font-weight: 700;
  color: var(--ink);
  margin-bottom: 6px;
}

.process-step p {
  font-size: 12.5px;
  color: #6A6D70;
  line-height: 1.6;
  margin: 0;
  max-width: 160px;
}

/* 底部 CTA */
.cta-section {
  padding: 60px 0 80px;
  background: #fff;
  text-align: center;
}

.cta-btn {
  padding: 14px 40px;
  font-size: 16px;
  font-weight: 600;
}

/* 加载状态 */
.loading-wrap {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 240px;
  color: #6A6D70;
  font-size: 14px;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #D0D3CC;
  border-top-color: var(--red);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* 响应式 */
@media (max-width: 992px) {
  .cap-grid {
    grid-template-columns: 1fr;
  }

  .section-head h2 {
    font-size: 28px;
  }
}

@media (max-width: 768px) {
  .page-banner {
    padding: 80px 0 48px;
  }

  .page-banner h1 {
    font-size: 28px;
  }

  .process-steps {
    grid-template-columns: 1fr;
    gap: 28px;
  }

  .process-step {
    flex-direction: row;
    align-items: flex-start;
    text-align: left;
    padding: 0;
    gap: 16px;
  }

  .process-step::before {
    display: none;
  }

  .step-circle {
    flex-shrink: 0;
    margin-bottom: 0;
  }

  .process-step h5 {
    margin-top: 4px;
  }

  .process-step p {
    max-width: none;
  }

  .cta-btn {
    width: 100%;
    max-width: 320px;
    justify-content: center;
  }
}
</style>
