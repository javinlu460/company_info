<template>
  <div class="faq-page">
    <section class="faq-section">
      <div class="container">
        <div class="section-head">
          <span class="ew">FAQ · 常见问题</span>
          <h2>加工咨询常见问题解答</h2>
        </div>

        <!-- 搜索框 -->
        <div class="search-box">
          <input
            v-model="searchKeyword"
            type="text"
            placeholder="搜索关键词，如：精度、起订量、交期、图纸…"
            class="search-input"
          />
        </div>

        <!-- 分类筛选 -->
        <div class="category-filter">
          <button
            v-for="cat in categories"
            :key="cat"
            class="filter-btn"
            :class="{ active: activeCategory === cat }"
            @click="activeCategory = cat"
          >
            {{ cat }}
          </button>
        </div>

        <!-- FAQ列表 -->
        <div class="faq-list">
          <div
            v-for="(item, idx) in filteredFaq"
            :key="idx"
            class="faq-item"
            :class="{ active: activeFaq === idx }"
          >
            <div class="faq-question" @click="toggleFaq(idx)">
              <span>{{ item.q }}</span>
              <span class="faq-icon" :class="{ rotated: activeFaq === idx }">&#8744;</span>
            </div>
            <transition name="faq-expand">
              <div v-if="activeFaq === idx" class="faq-answer">
                <p>{{ item.a }}</p>
              </div>
            </transition>
          </div>
        </div>

        <!-- CTA -->
        <div class="cta-wrapper">
          <router-link to="/contact" class="btn-red cta-btn">发图纸，获取报价及解决方案 →</router-link>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const activeFaq = ref(-1)
const searchKeyword = ref('')
const activeCategory = ref('全部')

const categories = ['全部', '加工能力', '定制流程', '交期与质量']

function toggleFaq(idx) {
  activeFaq.value = activeFaq.value === idx ? -1 : idx
}

const faqItems = [
  { q: '你们能加工的最大/最小直径是多少？', a: '我们的车削加工范围为Φ5mm~Φ1000mm，铣削最大行程为1200×600×500mm，可满足从小型精密件到中大型结构件的加工需求。', cat: '加工能力' },
  { q: '能达到的最高精度等级是多少？', a: '常规加工精度为IT7~IT8级，精密加工可达IT6级，表面粗糙度最低可达Ra0.4。具体精度需根据零件结构和材质综合评估。', cat: '加工能力' },
  { q: '可以加工哪些材质？', a: '支持碳钢、不锈钢（304/316L）、铝合金、铜合金、钛合金、工程塑料等多种材质。特殊材质可来样咨询。', cat: '加工能力' },
  { q: '没有图纸，只有旧零件样品，可以加工吗？', a: '可以。我们支持逆向工程服务，通过三坐标测量和3D扫描对样品进行数据采集，生成加工图纸后进行生产。', cat: '定制流程' },
  { q: '接受什么格式的图纸？', a: '支持STEP、IGES、DWG、DXF、PDF等常见格式。如果您有3D模型（SolidWorks、Pro/E等），也可以直接发送原始文件。', cat: '定制流程' },
  { q: '起订量是多少？', a: '单件起订，支持打样。小批量（1~50件）、中批量（50~500件）、批量（500件以上）均可灵活安排生产。', cat: '定制流程' },
  { q: '一般交货周期是多久？', a: '打样件通常5~7个工作日，小批量7~15个工作日，批量订单根据数量和工艺复杂度另行排产。急件可协商加急处理。', cat: '交期与质量' },
  { q: '如何保证产品质量？', a: '我们通过ISO9001体系管理，配备三坐标测量仪、粗糙度仪等专业检测设备，每批次提供完整的尺寸检测报告。', cat: '交期与质量' },
  { q: '出现质量问题如何处理？', a: '如因我方原因导致的质量不合格，我们承诺免费返工或重新生产，并承担相应物流费用。质保期内提供全程售后支持。', cat: '交期与质量' }
]

const filteredFaq = computed(() => {
  let list = faqItems
  if (activeCategory.value !== '全部') {
    list = list.filter(item => item.cat === activeCategory.value)
  }
  if (searchKeyword.value.trim()) {
    const kw = searchKeyword.value.trim().toLowerCase()
    list = list.filter(item => item.q.toLowerCase().includes(kw) || item.a.toLowerCase().includes(kw))
  }
  return list
})
</script>

<style scoped>
.faq-page {
  margin-top: var(--header-height);
  background: #F2F3EF;
  min-height: calc(100vh - var(--header-height));
}

.faq-section {
  padding: 30px 0 80px;
}

.section-head {
  margin-bottom: 32px;
}

.section-head h2 {
  font-family: var(--font-serif);
  font-size: 34px;
  font-weight: 700;
  color: var(--ink);
  margin-bottom: 0;
  line-height: 1.25;
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

/* 搜索框 */
.search-box {
  margin-bottom: 24px;
}

.search-input {
  width: 100%;
  padding: 16px 20px;
  font-size: 15px;
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md);
  background: #fff;
  color: var(--ink);
  outline: none;
  transition: border-color var(--transition-base);
}

.search-input:focus {
  border-color: var(--red);
}

.search-input::placeholder {
  color: var(--gray-400);
}

/* 分类筛选 */
.category-filter {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 28px;
}

.filter-btn {
  padding: 10px 24px;
  font-size: 14px;
  font-weight: 500;
  color: var(--ink);
  background: transparent;
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all var(--transition-base);
}

.filter-btn:hover {
  background: var(--gray-100);
  border-color: var(--gray-400);
}

.filter-btn.active {
  color: var(--white);
  background: var(--red);
  border-color: var(--red);
}

/* FAQ列表 */
.faq-list {
  margin-bottom: 0;
}

.faq-item {
  background: #fff;
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md);
  margin-bottom: 12px;
  overflow: hidden;
  transition: border-color var(--transition-base);
}

.faq-item.active {
  border-color: var(--red);
}

.faq-question {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  cursor: pointer;
  font-weight: 600;
  font-size: 15px;
  color: var(--ink);
  transition: background-color var(--transition-base);
}

.faq-question:hover {
  background: rgba(0, 0, 0, 0.02);
}

.faq-icon {
  font-size: 18px;
  color: var(--gray-400);
  flex-shrink: 0;
  margin-left: 16px;
  transition: transform 0.3s ease;
}

.faq-icon.rotated {
  transform: rotate(180deg);
  color: var(--red);
}

.faq-answer {
  padding: 0 24px 20px;
}

.faq-answer p {
  font-size: 14px;
  color: var(--gray-500);
  line-height: 1.8;
  margin: 0;
}

.faq-expand-enter-active,
.faq-expand-leave-active {
  transition: all 0.3s ease;
  max-height: 200px;
  overflow: hidden;
}

.faq-expand-enter-from,
.faq-expand-leave-to {
  max-height: 0;
  padding-top: 0;
  padding-bottom: 0;
  opacity: 0;
}

/* CTA */
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
  .section-head h2 {
    font-size: 26px;
  }

  .search-input {
    padding: 12px 16px;
    font-size: 14px;
  }

  .category-filter {
    gap: 8px;
  }

  .filter-btn {
    padding: 8px 16px;
    font-size: 13px;
  }

  .faq-question {
    padding: 16px 18px;
    font-size: 14px;
  }
}
</style>
