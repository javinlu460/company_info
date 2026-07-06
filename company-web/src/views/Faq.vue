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
            v-for="item in filteredFaq"
            :key="item.id"
            class="faq-item"
            :class="{ active: activeFaq === item.id }"
          >
            <div class="faq-question" @click="toggleFaq(item.id)">
              <span>{{ item.question }}</span>
              <span class="faq-icon" :class="{ rotated: activeFaq === item.id }">&#8744;</span>
            </div>
            <transition name="faq-expand">
              <div v-if="activeFaq === item.id" class="faq-answer">
                <p>{{ item.answer }}</p>
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
import { ref, computed, onMounted } from 'vue'
import { getFaqList, getFaqCategories } from '../api/faq'

const activeFaq = ref(null)
const searchKeyword = ref('')
const activeCategory = ref('全部')
const categories = ref(['全部'])
const faqItems = ref([])

function toggleFaq(id) {
  activeFaq.value = activeFaq.value === id ? null : id
}

async function loadFaqData() {
  try {
    const [listRes, catRes] = await Promise.all([getFaqList(), getFaqCategories()])
    faqItems.value = listRes || []
    const cats = catRes || []
    categories.value = ['全部', ...cats]
  } catch (e) {
    console.error('加载FAQ失败:', e)
    faqItems.value = []
    categories.value = ['全部']
  }
}

const filteredFaq = computed(() => {
  let list = faqItems.value
  if (activeCategory.value !== '全部') {
    list = list.filter(item => item.category === activeCategory.value)
  }
  if (searchKeyword.value.trim()) {
    const kw = searchKeyword.value.trim().toLowerCase()
    list = list.filter(item =>
      item.question.toLowerCase().includes(kw) ||
      item.answer.toLowerCase().includes(kw)
    )
  }
  return list
})

onMounted(() => {
  loadFaqData()
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
