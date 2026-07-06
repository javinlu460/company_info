<template>
  <div class="business-page">
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
          发图纸，获取报价及解决方案 →
        </router-link>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getSolutionList } from '../api/solution'

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
  let tagArr = []
  if (item.tags) {
    try {
      tagArr = JSON.parse(item.tags)
      if (!Array.isArray(tagArr)) tagArr = []
    } catch (e) {
      tagArr = []
    }
  }
  tagArr.forEach((label, idx) => {
    tags.push({ label, highlight: idx === tagArr.length - 1 })
  })
  return tags
}

function truncateText(text, maxLen) {
  if (!text) return ''
  return text.length > maxLen ? text.substring(0, maxLen) + '...' : text
}

async function loadBusiness() {
  loading.value = true
  try {
    const res = await getSolutionList()
    businessList.value = res || []
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
  color: var(--gold);
  letter-spacing: 1px;
  text-transform: uppercase;
  margin-bottom: 14px;
}

.ew::before {
  content: '';
  width: 18px;
  height: 2px;
  background: var(--gold);
}

.section-head h2 {
  font-family: var(--font-serif);
  font-size: 34px;
  font-weight: 700;
  color: #F0F0EE;
  margin-bottom: 14px;
  line-height: 1.25;
}

.section-head .lead {
  font-size: 15px;
  color: rgba(242, 243, 239, 0.55);
  line-height: 1.8;
  max-width: 720px;
  margin: 0;
}

/* 解决方案网格 */
.solutions-section {
  padding: var(--section-padding) 0;
  padding-top: calc(var(--header-height) + 30px);
  background: #0d0d10;
}

.cap-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 2px;
}

.cap-card {
  position: relative;
  background: #111214;
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 0;
  padding: 36px 32px 32px;
  overflow: hidden;
  transition: background 0.3s, border-color 0.3s;
}

.cap-card::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: var(--gold);
  opacity: 0.5;
  transition: opacity 0.3s;
}

.cap-card:hover {
  background: #18191f;
  border-color: rgba(212, 175, 55, 0.35);
}

.cap-card:hover::before {
  opacity: 1;
}

.cap-num {
  display: block;
  font-family: var(--font-serif);
  font-size: 52px;
  font-weight: 700;
  color: rgba(212, 175, 55, 0.15);
  line-height: 1;
  margin-bottom: 12px;
}

.cap-card h3 {
  font-size: 18px;
  font-weight: 700;
  color: #F0F0EE;
  margin-bottom: 10px;
  line-height: 1.35;
}

.cap-card p {
  font-size: 14px;
  color: rgba(242, 243, 239, 0.5);
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
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: rgba(242, 243, 239, 0.55);
}

.tag.r {
  background: rgba(212, 175, 55, 0.12);
  border-color: rgba(212, 175, 55, 0.35);
  color: var(--gold);
}

/* 合作流程 */
.process-section {
  padding: var(--section-padding) 0;
  background: #111214;
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
  height: 1px;
  background: rgba(212, 175, 55, 0.2);
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
  background: #0d0d10;
  border: 2px solid var(--gold);
  color: var(--gold);
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
  color: #F0F0EE;
  margin-bottom: 6px;
}

.process-step p {
  font-size: 12.5px;
  color: rgba(242, 243, 239, 0.45);
  line-height: 1.6;
  margin: 0;
  max-width: 160px;
}

/* 底部 CTA */
.cta-section {
  padding: 60px 0 80px;
  background: #0d0d10;
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
  color: rgba(242, 243, 239, 0.45);
  font-size: 14px;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(255, 255, 255, 0.1);
  border-top-color: var(--gold);
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
