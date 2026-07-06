<template>
  <div class="cases-page">
    <section class="cases section-light">
      <div class="container">
        <div class="section-head">
          <span class="ew">CASES · 客户案例</span>
          <h2>他们选择了我们</h2>
          <p class="lead">服务覆盖矿山、建筑、化工、环保、重工、智能制造等多个行业，累计服务客户超千家。</p>
        </div>
        <div class="cases-grid">
          <router-link
            v-for="(c, idx) in cases"
            :key="idx"
            :to="`/cases/${c.id}`"
            class="case-card"
          >
            <div class="case-image" :style="{ backgroundImage: `url(${c.coverImage || defaultCaseImage})` }"></div>
            <div class="case-overlay">
              <h3 class="case-name">{{ c.title }}</h3>
              <span class="case-tag">{{ c.industry }}</span>
            </div>
          </router-link>
        </div>
        <div class="cta-wrapper">
          <router-link to="/contact" class="btn-red cta-btn">发图纸，获取报价及解决方案 →</router-link>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCaseList } from '../api/case'

const cases = ref([])
const defaultCaseImage = 'https://images.unsplash.com/photo-1504328341540-c860949d477d?w=600&h=400&fit=crop'

async function loadCases() {
  try {
    const res = await getCaseList()
    cases.value = res || []
  } catch (e) {
    console.error('加载客户案例失败:', e)
    cases.value = []
  }
}

onMounted(() => {
  loadCases()
})
</script>

<style scoped>
.cases-page {
  margin-top: var(--header-height);
}

.section-head {
  margin-bottom: 32px;
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

.ew {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  font-weight: 600;
  color: var(--red);
  letter-spacing: 1px;
  text-transform: uppercase;
}

.ew::before {
  content: '';
  width: 18px;
  height: 2px;
  background: var(--red);
}

.cases {
  padding: 30px 0 var(--section-padding);
}

.cases-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.case-card {
  position: relative;
  display: block;
  text-decoration: none;
  border-radius: var(--radius-lg);
  overflow: hidden;
  aspect-ratio: 16 / 10;
  cursor: pointer;
}

.case-image {
  position: absolute;
  inset: 0;
  background-size: cover;
  background-position: center;
  transition: transform var(--transition-slow);
}

.case-card:hover .case-image {
  transform: scale(1.08);
}

.case-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(20, 20, 26, 0.85) 0%, transparent 60%);
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  padding: 24px;
}

.case-name {
  font-size: 18px;
  font-weight: 600;
  color: var(--white);
  margin-bottom: 8px;
}

.case-tag {
  display: inline-block;
  padding: 3px 12px;
  background: rgba(178, 43, 43, 0.8);
  color: var(--white);
  font-size: 12px;
  border-radius: var(--radius-sm);
  align-self: flex-start;
}

.cta-wrapper {
  text-align: center;
  margin-top: 48px;
}

.btn-red {
  display: inline-block;
  padding: 14px 36px;
  background: var(--red);
  color: var(--white);
  border-radius: var(--radius-md);
  text-decoration: none;
  font-weight: 600;
  font-size: 16px;
  transition: background var(--transition-base), transform var(--transition-base);
}

.btn-red:hover {
  background: #a02020;
  transform: translateY(-2px);
}

@media (max-width: 768px) {
  .cases-grid {
    grid-template-columns: 1fr;
  }
}
</style>
