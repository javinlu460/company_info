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
          <div
            v-for="(c, idx) in cases"
            :key="idx"
            class="case-card"
          >
            <div class="case-image" :style="{ backgroundImage: `url(${c.image})` }"></div>
            <div class="case-overlay">
              <h3 class="case-name">{{ c.name }}</h3>
              <span class="case-tag">{{ c.tag }}</span>
            </div>
          </div>
        </div>
        <div class="cta-wrapper">
          <router-link to="/contact" class="btn-red cta-btn">发图纸，获取报价及解决方案 →</router-link>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
const cases = [
  {
    name: '某矿业集团设备升级项目',
    tag: '矿山行业',
    image: 'https://images.unsplash.com/photo-1504328341540-c860949d477d?w=600&h=400&fit=crop'
  },
  {
    name: '城市基建工程配套',
    tag: '建筑工程',
    image: 'https://images.unsplash.com/photo-1541888946425-d81bb19240f5?w=600&h=400&fit=crop'
  },
  {
    name: '化工厂设备定制',
    tag: '石油化工',
    image: 'https://images.unsplash.com/photo-1504307651254-65602e4bba56?w=600&h=400&fit=crop'
  },
  {
    name: '环保水处理设备供应',
    tag: '环保行业',
    image: 'https://images.unsplash.com/photo-1581093458791-9d42e3c7e117?w=600&h=400&fit=crop'
  },
  {
    name: '大型钢结构项目',
    tag: '重工制造',
    image: 'https://images.unsplash.com/photo-1565193566173-7a0ee2e6514f?w=600&h=400&fit=crop'
  },
  {
    name: '自动化产线改造',
    tag: '智能制造',
    image: 'https://images.unsplash.com/photo-1565043666747-69f6646db940?w=600&h=400&fit=crop'
  }
]
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
