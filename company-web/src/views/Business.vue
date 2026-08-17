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

        <div class="content-block">
          <div v-if="loading" class="loading-wrap">
            <div class="loading-spinner"></div>
            <span>加载中...</span>
          </div>

          <template v-else>
            <div class="cap-grid">
              <div
                v-for="(item, index) in displayList"
                :key="item.id || index"
                class="cap-wrap"
              >
                <div class="cap-card">
                  <div class="cap-top">
                    <div class="cap-icon" v-html="solutionIcons[index % solutionIcons.length]"></div>
                  </div>
                  <h3>{{ item.title }}</h3>
                  <span v-if="item.subtitle" class="cap-subtitle">{{ item.subtitle }}</span>
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
            </div>
          </template>

          <!-- 合作流程 -->
          <div class="process-container">
            <div class="section-head center">
              <span class="ew">PROCESS · 合作流程</span>
              <h2>从发图纸到收货——六步搞定</h2>
            </div>

            <div class="flow-grid">
              <div
                v-for="(step, idx) in processSteps"
                :key="idx"
                class="step-box-wrap"
              >
                <div class="step-box">
                  <span class="st-title">{{ step.title }}</span>
                  <span class="st-desc">{{ step.desc }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 底部 CTA -->
          <div class="cta-bar">
            <router-link to="/contact" class="btn-red cta-btn">
              发图纸，获取报价及解决方案 →
            </router-link>
          </div>
        </div>
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
    subtitle: 'Contract Manufacturing',
    summary: '客户提供图纸或样品，我们按规格进行精密加工与组装，确保尺寸、材质、工艺完全符合要求。',
    specs: [
      { label: '来图加工' },
      { label: '批量稳定', highlight: true }
    ]
  },
  {
    id: 'default-2',
    num: 'B',
    title: '快速打样 · 新品开发验证',
    subtitle: 'Rapid Prototyping',
    summary: '针对新品开发阶段提供小批量打样服务，快速验证结构设计与工艺可行性，缩短研发周期。',
    specs: [
      { label: '7-15 天交样' },
      { label: '小批量', highlight: true }
    ]
  },
  {
    id: 'default-3',
    num: 'C',
    title: '小批量 · 维修/替换用件',
    subtitle: 'Medium-Small Batch Production',
    summary: '为设备维护、备件更换提供灵活的小批量生产，无需开模大批囤货，降低库存与资金压力。',
    specs: [
      { label: '按需生产' },
      { label: '交期灵活', highlight: true }
    ]
  },
  {
    id: 'default-4',
    num: 'D',
    title: '逆向工程 · 来样复制',
    subtitle: 'Reverse Engineering',
    summary: '仅有实物样品即可完成测绘、建模与生产复制，帮助客户延续供应或优化老旧零部件。',
    specs: [
      { label: '三维测绘' },
      { label: '老旧件替代', highlight: true }
    ]
  }
]

// 解决方案卡片图标（贴合制造业务，白色描边，置于酒红渐变徽标块内）
const solutionIcons = [
  // OEM 按图加工 —— 齿轮/精密加工
  `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="3"/><path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 1 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-4 0v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 1 1-2.83-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1 0-4h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 1 1 2.83-2.83l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 4 0v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 1 1 2.83 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z"/></svg>`,
  // 快速打样 —— 闪电
  `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/></svg>`,
  // 小批量·维修/替换 —— 扳手
  `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M14.7 6.3a4 4 0 0 0-5.4 5.4L3 18l3 3 6.3-6.3a4 4 0 0 0 5.4-5.4l-2.7 2.7-2.4-.6-.6-2.4 2.7-2.7z"/></svg>`,
  // 逆向工程·来样复制 —— 循环/逆向箭头
  `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M23 4v6h-6"/><path d="M1 20v-6h6"/><path d="M20.49 9A9 9 0 0 0 5.64 5.64L1 10m22 4l-4.64 4.36A9 9 0 0 1 3.51 15"/></svg>`
]

const processSteps = [
  { title: '提交图纸', desc: '上传图纸、样品或需求说明' },
  { title: '工程师审核', desc: '评估工艺、材料与交期' },
  { title: '报价签约', desc: '双方拟定合同和正式签约' },
  { title: '确认打样', desc: '报价确认后安排样品制作' },
  { title: '质检/交样报告', desc: '检测/出具报告' },
  { title: '批量生产及交付', desc: '按合同交付' }
]

const displayList = computed(() => {
  if (businessList.value && businessList.value.length) {
    return businessList.value.map((item, index) => ({
      id: item.id,
      num: String.fromCharCode(65 + index),
      title: item.title,
      subtitle: item.subtitle || '',
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
/* ========== 工业对比风格 (Final Contrast Industrial) ========== */
.business-page {
  --maroon: #8a352d;
  --surface: #ffffff;
  --tp: #1a1917;
  --tb: #55524a;
}

/* 公共标题区 */
.section-head {
  margin-bottom: 40px;
}

.section-head.center {
  text-align: center;
}

.section-head.center .lead {
  margin: 0 auto;
}

.ew {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  font-weight: 800;
  color: var(--maroon);
  letter-spacing: 0.1em;
  text-transform: uppercase;
  margin-bottom: 12px;
}

.ew::before {
  content: '';
  width: 20px;
  height: 2px;
  background: var(--maroon);
}

.section-head h2 {
  font-size: 36px;
  font-weight: 900;
  color: var(--tp);
  margin-bottom: 14px;
  line-height: 1.25;
}

.section-head .lead {
  font-size: 15px;
  color: var(--tb);
  line-height: 1.8;
  max-width: 720px;
  margin: 0;
}

/* 内容整合大框架 */
.content-block {
  background: #f9f8f4;
  border: 1px solid var(--maroon);
  border-radius: 4px;
  padding: 60px 32px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.05);
  position: relative;
}

.content-block::after {
  content: '';
  position: absolute;
  top: -5px;
  right: -5px;
  width: 10px;
  height: 10px;
  background: var(--maroon);
}

/* 解决方案 */
.solutions-section {
  padding: var(--section-padding) 0;
  padding-top: calc(var(--header-height) + 30px);
  background: var(--paper);
}

.cap-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

/* 卡片：底/右偏移酒红叠层 */
.cap-wrap {
  position: relative;
  z-index: 1;
  padding-bottom: 8px;
  padding-right: 8px;
}

.cap-wrap::after {
  content: '';
  position: absolute;
  top: 8px;
  left: 8px;
  right: -8px;
  bottom: -8px;
  background: var(--maroon);
  border-radius: 2px;
  z-index: -1;
  transition: all 0.3s ease;
}

.cap-card {
  position: relative;
  z-index: 1;
  background: var(--surface);
  border: 1.5px solid var(--maroon);
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 42px 35px;
  transition: transform 0.3s ease;
}

.cap-wrap:hover .cap-card {
  transform: translate(-4px, -4px);
}

.cap-top {
  margin-bottom: 20px;
}

.cap-icon {
  color: var(--maroon);
  display: inline-flex;
}

.cap-icon svg {
  width: 40px;
  height: 40px;
}

.cap-card h3 {
  font-size: 20px;
  font-weight: 900;
  color: var(--tp);
  margin-bottom: 4px;
  line-height: 1.35;
}

.cap-subtitle {
  font-size: 12px;
  color: #999;
  margin-bottom: 14px;
  display: block;
  font-family: 'Helvetica', sans-serif;
}

.cap-card p {
  font-size: 14px;
  color: var(--tb);
  line-height: 1.7;
  margin-bottom: 22px;
  flex: 1;
}

.cap-specs {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  border-top: 1px solid #f1f5f9;
  padding-top: 22px;
  margin-top: auto;
}

.tag {
  font-size: 12px;
  padding: 5px 14px;
  border: 1px solid #ddd;
  color: var(--tb);
  font-weight: 700;
  background: #fff;
}

.tag.r {
  border-color: var(--maroon);
  color: var(--maroon);
  background: rgba(138, 53, 45, 0.05);
}

/* 合作流程 */
.process-container {
  margin-top: 60px;
}

.flow-grid {
  display: flex;
  justify-content: space-between;
  align-items: stretch;
  gap: 22px;
  position: relative;
  padding: 0 10px;
}

.flow-grid::before {
  content: '';
  position: absolute;
  top: 30px;
  left: 60px;
  right: 60px;
  height: 4px;
  background: var(--maroon);
  z-index: 0;
}

.step-box-wrap {
  position: relative;
  flex: 1;
  z-index: 1;
  padding-bottom: 8px;
  padding-right: 8px;
}

.step-box-wrap::after {
  content: '';
  position: absolute;
  top: 8px;
  left: 8px;
  right: 0;
  bottom: 0;
  background: var(--maroon);
  z-index: -1;
}

.step-box {
  background: #fff;
  border: 2px solid var(--maroon);
  padding: 15px 10px;
  text-align: center;
  height: 100%;
}

.step-box .st-title {
  display: block;
  font-size: 13px;
  font-weight: 900;
  color: var(--tp);
  margin-bottom: 6px;
}

.step-box .st-desc {
  display: block;
  font-size: 11px;
  color: var(--tb);
  line-height: 1.35;
}

/* 底部 CTA */
.cta-bar {
  margin-top: 60px;
  text-align: center;
}

.cta-btn {
  padding: 16px 42px;
  font-size: 16px;
  font-weight: 800;
  background: var(--maroon);
  border-radius: 2px;
}

.cta-btn:hover {
  background: #722a23;
}

/* 响应式 */
@media (max-width: 992px) {
  .content-block {
    padding: 40px 24px;
  }

  .cap-grid {
    grid-template-columns: 1fr 1fr;
    gap: 24px;
  }

  .cap-card {
    padding: 35px 28px;
  }

  .cap-icon svg {
    width: 36px;
    height: 36px;
  }

  .cap-card h3 {
    font-size: 18px;
  }

  .cap-card p {
    font-size: 13px;
  }

  .section-head h2 {
    font-size: 28px;
  }
}

@media (max-width: 768px) {
  .content-block {
    padding: 32px 20px;
  }

  .cap-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .cap-card {
    padding: 32px 24px;
  }

  .flow-grid {
    flex-direction: column;
    gap: 16px;
    padding: 0;
  }

  .flow-grid::before {
    display: none;
  }

  .cta-btn {
    width: 100%;
    max-width: 320px;
    justify-content: center;
  }
}
</style>
