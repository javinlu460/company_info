<template>
  <div class="home-page">
    <!-- 1. Hero区 -->
    <section class="hero">
      <div class="hero-bg"></div>
      <div class="hero-content container">
        <span class="hero-tag animate-item">专业机械制造</span>
        <h1 class="hero-title animate-item">{{ config.siteName || '精工智造 · 品质致远' }}</h1>
        <p class="hero-subtitle animate-item">{{ config.siteDesc ? stripHtml(decodeHtml(config.siteDesc)).slice(0, 60) : '专注于机械制造领域，为客户提供高品质产品与全方位解决方案' }}</p>
        <div class="hero-actions animate-item">
          <router-link to="/products" class="btn-red">产品中心</router-link>
          <router-link to="/contact" class="btn-outline-light">获取报价</router-link>
        </div>
      </div>
    </section>

    <!-- 轮播展示区 -->
    <section class="showcase-carousel">
      <div class="container">
        <div class="carousel-wrapper">
          <div class="carousel-track" :style="{ transform: `translateX(-${currentSlide * 100}%)` }">
            <div class="carousel-slide" v-for="(slide, idx) in showcaseSlides" :key="idx">
              <div class="slide-content">
                <div class="slide-info">
                  <span class="slide-tag">{{ slide.tag }}</span>
                  <h3 class="slide-title">{{ slide.title }}</h3>
                  <p class="slide-desc">{{ slide.desc }}</p>
                  <router-link :to="slide.link" class="slide-link">了解更多 →</router-link>
                </div>
                <div class="slide-visual">
                  <div class="slide-icon" v-html="slide.icon"></div>
                </div>
              </div>
            </div>
          </div>
          <div class="carousel-controls">
            <button class="carousel-btn prev" @click="prevSlide" aria-label="上一个">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M15 18l-6-6 6-6"/></svg>
            </button>
            <div class="carousel-dots">
              <span v-for="(slide, idx) in showcaseSlides" :key="idx" class="dot" :class="{ active: currentSlide === idx }" @click="currentSlide = idx"></span>
            </div>
            <button class="carousel-btn next" @click="nextSlide" aria-label="下一个">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M9 18l6-6-6-6"/></svg>
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- 2. Trust Strip -->
    <section class="trust-strip">
      <div class="marquee-track animate-marquee">
        <div class="marquee-content">
          <span class="trust-item" v-for="item in trustItems" :key="item">{{ item }}</span>
        </div>
        <div class="marquee-content">
          <span class="trust-item" v-for="item in trustItems" :key="'dup-' + item">{{ item }}</span>
        </div>
      </div>
    </section>

    <!-- 3. 能力展示 -->
    <section class="capabilities section-dark" ref="capabilitiesRef">
      <div class="container">
        <div class="section-title reveal">
          <div class="title-line"></div>
          <h2>核心能力</h2>
          <p class="section-subtitle">Core Capabilities</p>
        </div>
        <div class="capabilities-grid">
          <div
            v-for="(item, idx) in capabilities"
            :key="idx"
            class="cap-card reveal"
          >
            <div class="cap-icon" v-html="item.icon"></div>
            <h3 class="cap-title">{{ item.title }}</h3>
            <p class="cap-desc">{{ item.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 4. 产品展示 -->
    <section class="products-section section-light">
      <div class="container">
        <div class="section-header reveal">
          <div class="section-title text-left">
            <div class="title-line"></div>
            <h2>产品中心</h2>
          </div>
          <router-link to="/products" class="view-all-link">查看全部 →</router-link>
        </div>
        <div v-if="categories.length" class="product-tabs reveal">
          <button
            class="tab-btn"
            :class="{ active: activeCategory === null }"
            @click="activeCategory = null"
          >全部</button>
          <button
            v-for="cat in categories"
            :key="cat.id"
            class="tab-btn"
            :class="{ active: activeCategory === cat.id }"
            @click="activeCategory = cat.id"
          >{{ cat.categoryName || cat.name }}</button>
        </div>
        <div v-if="loading" class="loading-wrap">
          <div class="loading-spinner"></div>
          <span>加载中...</span>
        </div>
        <div v-else-if="filteredProducts.length" class="products-grid reveal">
          <ProductCard
            v-for="product in filteredProducts.slice(0, 8)"
            :key="product.id"
            :product="product"
          />
        </div>
        <div v-else class="empty-state">
          <div class="empty-icon">📦</div>
          <p>暂无产品信息</p>
        </div>
        <div class="section-more reveal" v-if="filteredProducts.length">
          <router-link to="/products" class="btn-outline-dark">查看更多产品 →</router-link>
        </div>
      </div>
    </section>

    <!-- 5. 工艺流程 -->
    <section class="process section-white">
      <div class="container">
        <div class="section-title reveal">
          <h2>工艺流程</h2>
          <p class="section-subtitle">PROCESS</p>
          <div class="title-line"></div>
        </div>
        <div class="process-timeline">
          <div
            v-for="(step, idx) in processSteps"
            :key="idx"
            class="process-step reveal"
          >
            <div class="step-circle">{{ idx + 1 }}</div>
            <div v-if="idx < processSteps.length - 1" class="step-line"></div>
            <h4 class="step-title">{{ step.title }}</h4>
            <p class="step-desc">{{ step.desc }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 6. 解决方案 -->
    <section class="solutions section-dark">
      <div class="container">
        <div class="section-title reveal">
          <div class="title-line"></div>
          <h2>行业解决方案</h2>
          <p class="section-subtitle">Industry Solutions</p>
        </div>
        <div class="solutions-grid">
          <div
            v-for="(sol, idx) in solutions"
            :key="idx"
            class="solution-card reveal"
          >
            <div class="sol-icon" v-html="sol.icon"></div>
            <h3 class="sol-title">{{ sol.title }}</h3>
            <p class="sol-desc">{{ sol.desc }}</p>
            <router-link to="/business" class="sol-link">了解更多 →</router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- 7. 客户案例 -->
    <section id="cases" class="cases section-light">
      <div class="container">
        <div class="section-title reveal">
          <h2>客户案例</h2>
          <p class="section-subtitle">CASES</p>
          <div class="title-line"></div>
        </div>
        <div class="cases-grid">
          <div
            v-for="(c, idx) in cases"
            :key="idx"
            class="case-card reveal"
          >
            <div class="case-image" :style="{ backgroundImage: `url(${c.image})` }"></div>
            <div class="case-overlay">
              <h3 class="case-name">{{ c.name }}</h3>
              <span class="case-tag">{{ c.tag }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 8. 行业洞察 -->
    <section class="insights section-white">
      <div class="container">
        <div class="section-header reveal">
          <div class="section-title text-left">
            <div class="title-line"></div>
            <h2>行业洞察</h2>
          </div>
          <router-link to="/news" class="view-all-link">查看更多 →</router-link>
        </div>
        <div v-if="homeData.news && homeData.news.length" class="insights-grid reveal">
          <NewsItem
            v-for="item in homeData.news.slice(0, 6)"
            :key="item.id"
            :news="item"
          />
        </div>
        <div v-else class="empty-state">
          <div class="empty-icon">📰</div>
          <p>暂无新闻动态</p>
        </div>
      </div>
    </section>

    <!-- 9. FAQ -->
    <section id="faq" class="faq section-light">
      <div class="container">
        <div class="section-title reveal">
          <h2>常见问题</h2>
          <p class="section-subtitle">FAQ</p>
          <div class="title-line"></div>
        </div>
        <div class="faq-list reveal">
          <div
            v-for="(item, idx) in faqItems"
            :key="idx"
            class="faq-item"
            :class="{ active: activeFaq === idx }"
          >
            <div class="faq-question" @click="toggleFaq(idx)">
              <span>{{ item.q }}</span>
              <span class="faq-icon">{{ activeFaq === idx ? '−' : '+' }}</span>
            </div>
            <transition name="faq-expand">
              <div v-if="activeFaq === idx" class="faq-answer">
                <p>{{ item.a }}</p>
              </div>
            </transition>
          </div>
        </div>
      </div>
    </section>

    <!-- 10. 关于概览 -->
    <section class="about-overview section-dark">
      <div class="container">
        <div class="about-inner">
          <div class="about-stats reveal">
            <div class="stat-item">
              <span class="stat-num">{{ config.establishYear || '15' }}</span>
              <span class="stat-unit">年+</span>
              <span class="stat-label">成立年限</span>
            </div>
            <div class="stat-item">
              <span class="stat-num">{{ config.registeredCapital || '5000' }}</span>
              <span class="stat-unit">万</span>
              <span class="stat-label">注册资金</span>
            </div>
            <div class="stat-item">
              <span class="stat-num">{{ config.agentCount || '500' }}</span>
              <span class="stat-unit">+</span>
              <span class="stat-label">合作伙伴</span>
            </div>
            <div class="stat-item">
              <span class="stat-num">{{ config.caseCount || '1000' }}</span>
              <span class="stat-unit">+</span>
              <span class="stat-label">服务案例</span>
            </div>
          </div>
          <div class="about-text reveal">
            <h2>关于我们</h2>
            <div class="about-line"></div>
            <p class="about-desc">
              {{ config.siteDesc ? stripHtml(decodeHtml(config.siteDesc)).slice(0, 200) : '我们是一家专业的机械制造企业，致力于为客户提供优质的产品和服务，以创新驱动发展，以品质赢得信赖。' }}
            </p>
            <router-link to="/about" class="btn-gold">了解更多 →</router-link>
          </div>
        </div>
      </div>
    </section>

    <!-- 11. 联系/报价CTA -->
    <section class="cta-section">
      <div class="container">
        <h2 class="cta-title reveal">准备好开始合作了吗？</h2>
        <p class="cta-subtitle reveal">联系我们获取专属报价方案</p>
        <router-link to="/contact" class="btn-cta reveal">立即获取报价</router-link>
        <a v-if="config.phone" :href="'tel:' + config.phone" class="cta-phone reveal">{{ config.phone }}</a>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { getHomeData, getSiteConfig } from '../api/home'
import { getProductCategories } from '../api/product'
import ProductCard from '../components/ProductCard.vue'
import NewsItem from '../components/NewsItem.vue'

const homeData = ref({})
const config = ref({})
const categories = ref([])
const loading = ref(true)
const activeCategory = ref(null)
const activeFaq = ref(null)
const capabilitiesRef = ref(null)

// === 工具函数 ===
function decodeHtml(html) {
  if (!html) return ''
  return html
    .replace(/&lt;/g, '<')
    .replace(/&gt;/g, '>')
    .replace(/&amp;/g, '&')
    .replace(/&quot;/g, '"')
    .replace(/&#39;/g, "'")
}

function stripHtml(html) {
  if (!html) return ''
  const div = document.createElement('div')
  div.innerHTML = html
  return div.textContent || div.innerText || ''
}

// === 硬编码数据 ===
const trustItems = [
  'ISO9001认证',
  '10年+行业经验',
  '500+合作客户',
  '专业研发团队',
  '全国服务网络'
]

const capabilities = [
  {
    icon: `<svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><circle cx="24" cy="24" r="20" stroke="currentColor" stroke-width="2"/><path d="M16 24l6 6 10-12" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/></svg>`,
    title: '精密加工',
    desc: '先进加工设备，公差控制在0.01mm以内，确保每一个零部件的精度与品质。'
  },
  {
    icon: `<svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><rect x="8" y="8" width="32" height="32" rx="4" stroke="currentColor" stroke-width="2"/><path d="M18 20h12M18 28h8" stroke="currentColor" stroke-width="2" stroke-linecap="round"/></svg>`,
    title: '定制设计',
    desc: '专业设计团队，根据客户需求量身定制专属解决方案，满足个性化生产要求。'
  },
  {
    icon: `<svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M24 4l4 8 9 1.5-6.5 6 1.5 9-8-4.5-8 4.5 1.5-9-6.5-6 9-1.5z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/></svg>`,
    title: '质量管控',
    desc: '全流程质量管理体系，从原材料到成品，层层把关，确保产品零缺陷交付。'
  },
  {
    icon: `<svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><circle cx="24" cy="20" r="8" stroke="currentColor" stroke-width="2"/><path d="M12 40c0-6.627 5.373-12 12-12s12 5.373 12 12" stroke="currentColor" stroke-width="2" stroke-linecap="round"/><path d="M30 38l3 3 5-5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>`,
    title: '售后保障',
    desc: '终身技术支持，7×24小时响应，专业售后团队随时为客户提供高效服务。'
  }
]

const processSteps = [
  { title: '需求沟通', desc: '深入了解客户需求' },
  { title: '方案设计', desc: '定制专属解决方案' },
  { title: '生产制造', desc: '精密加工严格品控' },
  { title: '质量检测', desc: '全流程质量把关' },
  { title: '物流配送', desc: '安全快捷送达' },
  { title: '售后服务', desc: '终身技术支持' }
]

const solutions = [
  {
    icon: `<svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M8 38V18l16-10 16 10v20H8z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/><path d="M20 38V26h8v12" stroke="currentColor" stroke-width="2"/></svg>`,
    title: '矿山机械',
    desc: '为矿山开采提供高效、耐用的成套设备解决方案，适应各种恶劣工况环境。'
  },
  {
    icon: `<svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><rect x="6" y="24" width="12" height="18" stroke="currentColor" stroke-width="2"/><rect x="18" y="14" width="12" height="28" stroke="currentColor" stroke-width="2"/><rect x="30" y="6" width="12" height="36" stroke="currentColor" stroke-width="2"/></svg>`,
    title: '建筑工程',
    desc: '基础设施建设配套设备，助力城市工程建设高效、安全推进。'
  },
  {
    icon: `<svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><circle cx="24" cy="24" r="16" stroke="currentColor" stroke-width="2"/><path d="M24 12v12l8 4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>`,
    title: '石油化工',
    desc: '为石化行业提供耐腐蚀、高密封性的专业设备，保障生产安全稳定运行。'
  },
  {
    icon: `<svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M24 8c-8 0-14 6-14 14s14 22 14 22 14-14 14-22-6-14-14-14z" stroke="currentColor" stroke-width="2" stroke-linejoin="round"/><circle cx="24" cy="22" r="4" stroke="currentColor" stroke-width="2"/></svg>`,
    title: '环保设备',
    desc: '提供高效节能的环保处理设备，助力企业实现绿色可持续发展。'
  }
]

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

const faqItems = [
  {
    q: '你们的交货周期是多少？',
    a: '标准产品3-7个工作日即可发货，定制产品根据复杂程度通常为15-30个工作日。我们会在签订合同后提供详细的交货时间表，确保按时交付。'
  },
  {
    q: '是否提供定制服务？',
    a: '是的，我们提供全方位定制服务。从需求分析、方案设计到生产制造，专业团队全程跟进，确保产品完全符合客户的个性化需求。'
  },
  {
    q: '产品质保期多长？',
    a: '所有产品均享受12个月质保服务，质保期内因产品质量问题导致的故障，我们免费提供维修或更换。部分核心部件享有更长的质保期。'
  },
  {
    q: '如何获取报价？',
    a: '您可以通过在线表单提交需求，拨打我们的服务热线，或发送邮件至销售部门。我们会在收到需求后24小时内为您提供详细的报价方案。'
  },
  {
    q: '是否提供安装服务？',
    a: '我们提供专业安装调试服务，经验丰富的工程师团队现场指导安装，确保设备正确安装并顺利投入运行。同时提供操作培训，确保客户熟练使用。'
  },
  {
    q: '售后服务包括哪些内容？',
    a: '售后服务包括7×24小时技术支持、定期维护保养、零配件供应、远程诊断及现场维修等。我们建立了完善的客户服务档案，主动跟踪设备运行状态。'
  }
]

// === 轮播展示数据 ===
const showcaseSlides = ref([
  {
    tag: '产品中心',
    title: '精密机械零部件加工',
    desc: '泵类配件 / 传动运动件 / 农机配件 / 通用机械设备配件，支持来图定制',
    link: '/products',
    icon: '<svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg"><circle cx="32" cy="32" r="24" stroke="currentColor" stroke-width="2.5"/><path d="M32 16v16l12 8" stroke="currentColor" stroke-width="2.5" stroke-linecap="round"/></svg>'
  },
  {
    tag: '解决方案',
    title: 'OEM定制 · 按图加工 · 逆向工程',
    desc: '从图纸到成品，覆盖打样、小批量、批量生产全流程',
    link: '/business',
    icon: '<svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg"><rect x="12" y="12" width="40" height="40" rx="4" stroke="currentColor" stroke-width="2.5"/><path d="M22 32h20M32 22v20" stroke="currentColor" stroke-width="2.5" stroke-linecap="round"/></svg>'
  },
  {
    tag: '行业洞察',
    title: '机械加工专业知识与行业趋势',
    desc: '选材指南、精度知识、工艺指导、行业趋势分析',
    link: '/news',
    icon: '<svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg"><rect x="14" y="10" width="36" height="44" rx="4" stroke="currentColor" stroke-width="2.5"/><path d="M22 22h20M22 30h20M22 38h14" stroke="currentColor" stroke-width="2.5" stroke-linecap="round"/></svg>'
  },
  {
    tag: '关于我们',
    title: '15年精密加工经验 · 值得信赖',
    desc: '国家高新技术企业，ISO9001认证，500+合作客户',
    link: '/about',
    icon: '<svg viewBox="0 0 64 64" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M10 54V22l22-12 22 12v32" stroke="currentColor" stroke-width="2.5" stroke-linejoin="round"/><path d="M26 54V38h12v16" stroke="currentColor" stroke-width="2.5" stroke-linejoin="round"/></svg>'
  }
])

const currentSlide = ref(0)
let slideInterval = null

function nextSlide() {
  currentSlide.value = (currentSlide.value + 1) % showcaseSlides.value.length
}

function prevSlide() {
  currentSlide.value = (currentSlide.value - 1 + showcaseSlides.value.length) % showcaseSlides.value.length
}

function startAutoSlide() {
  slideInterval = setInterval(nextSlide, 5000)
}

function stopAutoSlide() {
  if (slideInterval) {
    clearInterval(slideInterval)
    slideInterval = null
  }
}

// === 计算属性 ===
const filteredProducts = computed(() => {
  if (!homeData.value.products) return []
  if (!activeCategory.value) return homeData.value.products
  return homeData.value.products.filter(p => p.categoryId === activeCategory.value)
})

// === 交互方法 ===
function toggleFaq(idx) {
  activeFaq.value = activeFaq.value === idx ? null : idx
}

// === 滚动动画 ===
let observer = null

function initScrollAnimations() {
  observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add('revealed')
          observer.unobserve(entry.target)
        }
      })
    },
    { threshold: 0.1, rootMargin: '0px 0px -40px 0px' }
  )
  document.querySelectorAll('.reveal').forEach((el) => {
    observer.observe(el)
  })
}

// === Hero入场动画 ===
function initHeroAnimations() {
  const items = document.querySelectorAll('.hero .animate-item')
  items.forEach((el, i) => {
    el.style.opacity = '0'
    el.style.transform = 'translateY(30px)'
    setTimeout(() => {
      el.style.transition = 'opacity 0.6s ease, transform 0.6s ease'
      el.style.opacity = '1'
      el.style.transform = 'translateY(0)'
    }, 300 + i * 200)
  })
}

// === 生命周期 ===
onMounted(async () => {
  initHeroAnimations()
  startAutoSlide()

  try {
    const [homeRes, configRes, catRes] = await Promise.all([
      getHomeData(),
      getSiteConfig(),
      getProductCategories()
    ])
    homeData.value = homeRes || {}
    config.value = configRes || {}
    categories.value = catRes || []
  } catch (e) {
    console.error('加载首页数据失败:', e)
  } finally {
    loading.value = false
  }

  // 等待DOM更新后初始化滚动动画
  setTimeout(() => {
    initScrollAnimations()
  }, 100)
})

onUnmounted(() => {
  if (observer) observer.disconnect()
  stopAutoSlide()
})
</script>

<style scoped>
/* ==================== Hero区 ==================== */
.hero {
  position: relative;
  height: 70vh;
  min-height: 480px;
  max-height: 600px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--graphite);
  margin-top: 0;
  overflow: hidden;
}

.hero-bg {
  position: absolute;
  inset: 0;
  background:
    repeating-linear-gradient(
      0deg,
      transparent,
      transparent 2px,
      rgba(255, 255, 255, 0.015) 2px,
      rgba(255, 255, 255, 0.015) 4px
    ),
    repeating-linear-gradient(
      90deg,
      transparent,
      transparent 2px,
      rgba(255, 255, 255, 0.015) 2px,
      rgba(255, 255, 255, 0.015) 4px
    );
  pointer-events: none;
}

.hero-content {
  position: relative;
  z-index: 1;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.hero-tag {
  font-size: 13px;
  color: var(--gold);
  letter-spacing: 4px;
  text-transform: uppercase;
  margin-bottom: 24px;
  font-weight: 500;
}

.hero-title {
  font-family: var(--font-serif);
  font-size: 44px;
  font-weight: 700;
  color: var(--white);
  line-height: 1.2;
  margin-bottom: 20px;
  max-width: 800px;
}

.hero-subtitle {
  font-size: 18px;
  color: rgba(242, 243, 239, 0.65);
  line-height: 1.6;
  margin-bottom: 28px;
  max-width: 560px;
}

.hero-actions {
  display: flex;
  gap: 16px;
}

/* ==================== 轮播展示区 ==================== */
.showcase-carousel {
  background: var(--graphite);
  padding: 48px 0;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
}

.carousel-wrapper {
  position: relative;
  overflow: hidden;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.08);
}

.carousel-track {
  display: flex;
  transition: transform 0.5s ease;
}

.carousel-slide {
  min-width: 100%;
  padding: 48px 56px;
}

.slide-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 48px;
}

.slide-info {
  flex: 1;
}

.slide-tag {
  display: inline-block;
  font-size: 12px;
  color: var(--gold);
  letter-spacing: 2px;
  text-transform: uppercase;
  margin-bottom: 16px;
  padding: 4px 12px;
  border: 1px solid rgba(191, 142, 42, 0.3);
  border-radius: 2px;
}

.slide-title {
  font-family: var(--font-serif);
  font-size: 28px;
  font-weight: 700;
  color: #F2F3EF;
  margin-bottom: 14px;
  line-height: 1.4;
}

.slide-desc {
  font-size: 15px;
  color: rgba(242, 243, 239, 0.6);
  line-height: 1.7;
  margin-bottom: 24px;
}

.slide-link {
  display: inline-flex;
  align-items: center;
  font-size: 14px;
  color: var(--gold);
  font-weight: 500;
  text-decoration: none;
  transition: gap 0.2s;
  gap: 4px;
}

.slide-link:hover {
  gap: 8px;
}

.slide-visual {
  width: 140px;
  height: 140px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: rgba(191, 142, 42, 0.08);
  border: 1px solid rgba(191, 142, 42, 0.2);
}

.slide-icon {
  width: 64px;
  height: 64px;
  color: var(--gold);
}

.carousel-controls {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  padding: 20px 0 28px;
}

.carousel-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: 1px solid rgba(255, 255, 255, 0.15);
  background: transparent;
  color: rgba(242, 243, 239, 0.7);
  cursor: pointer;
  transition: all 0.2s;
}

.carousel-btn:hover {
  border-color: var(--gold);
  color: var(--gold);
}

.carousel-dots {
  display: flex;
  gap: 8px;
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  cursor: pointer;
  transition: all 0.3s;
}

.dot.active {
  background: var(--gold);
  width: 24px;
  border-radius: 4px;
}

/* ==================== Trust Strip ==================== */
.trust-strip {
  background: var(--white);
  height: 80px;
  display: flex;
  align-items: center;
  overflow: hidden;
  border-bottom: 1px solid var(--gray-200);
}

.marquee-track {
  display: flex;
  width: max-content;
  animation: marquee 25s linear infinite;
}

.marquee-content {
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

.trust-item {
  display: inline-flex;
  align-items: center;
  padding: 0 40px;
  font-size: 15px;
  font-weight: 600;
  color: var(--ink);
  white-space: nowrap;
  letter-spacing: 0.5px;
  position: relative;
}

.trust-item::before {
  content: '';
  display: inline-block;
  width: 8px;
  height: 8px;
  background: var(--red);
  border-radius: 50%;
  margin-right: 12px;
  flex-shrink: 0;
}

/* ==================== 能力展示 ==================== */
.capabilities-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.cap-card {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: var(--radius-lg);
  padding: 36px 28px;
  text-align: center;
  transition: border-color var(--transition-base), transform var(--transition-base);
}

.cap-card:hover {
  border-color: var(--gold);
  transform: translateY(-4px);
}

.cap-icon {
  width: 56px;
  height: 56px;
  margin: 0 auto 20px;
  color: var(--gold);
}

.cap-icon svg {
  width: 100%;
  height: 100%;
}

.cap-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--white);
  margin-bottom: 10px;
}

.cap-desc {
  font-size: 14px;
  color: rgba(242, 243, 239, 0.6);
  line-height: 1.7;
  margin: 0;
}

/* ==================== 产品展示 ==================== */
.section-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  margin-bottom: 36px;
}

.section-header .section-title {
  text-align: left;
  margin-bottom: 0;
}

.section-header .section-title .title-line {
  margin: 0 0 12px;
}

.view-all-link {
  font-size: 14px;
  color: var(--red);
  text-decoration: none;
  font-weight: 500;
  transition: color var(--transition-base);
  white-space: nowrap;
  margin-bottom: 20px;
}

.view-all-link:hover {
  color: #8f2222;
}

.product-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 32px;
}

.tab-btn {
  padding: 8px 20px;
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-sm);
  background: var(--white);
  color: var(--ink);
  font-size: 14px;
  cursor: pointer;
  transition: all var(--transition-base);
}

.tab-btn:hover {
  border-color: var(--red);
  color: var(--red);
}

.tab-btn.active {
  background: var(--red);
  border-color: var(--red);
  color: var(--white);
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.section-more {
  text-align: center;
  margin-top: 40px;
}

/* ==================== 工艺流程 ==================== */
.process-timeline {
  display: flex;
  justify-content: center;
  gap: 0;
  position: relative;
}

.process-step {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
  position: relative;
  padding: 0 8px;
}

.step-circle {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: var(--red);
  color: var(--white);
  font-size: 18px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  position: relative;
  z-index: 1;
}

.step-line {
  position: absolute;
  top: 24px;
  left: calc(50% + 24px);
  right: calc(-50% + 24px);
  height: 2px;
  border-top: 2px dashed var(--gray-300);
  z-index: 0;
}

.step-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--ink);
  margin-top: 16px;
  margin-bottom: 6px;
}

.step-desc {
  font-size: 13px;
  color: var(--gray-500);
  text-align: center;
  margin: 0;
  line-height: 1.5;
}

/* ==================== 解决方案 ==================== */
.solutions-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.solution-card {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.08) 0%, rgba(255, 255, 255, 0.02) 100%);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: var(--radius-lg);
  padding: 36px 28px;
  transition: transform var(--transition-base), border-color var(--transition-base);
}

.solution-card:hover {
  transform: translateY(-4px);
  border-color: var(--gold);
}

.sol-icon {
  width: 48px;
  height: 48px;
  color: var(--gold);
  margin-bottom: 20px;
}

.sol-icon svg {
  width: 100%;
  height: 100%;
}

.sol-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--white);
  margin-bottom: 10px;
}

.sol-desc {
  font-size: 14px;
  color: rgba(242, 243, 239, 0.6);
  line-height: 1.7;
  margin-bottom: 20px;
}

.sol-link {
  font-size: 14px;
  color: var(--gold);
  text-decoration: none;
  font-weight: 500;
  transition: color var(--transition-base);
}

.sol-link:hover {
  color: var(--white);
}

/* ==================== 客户案例 ==================== */
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

/* ==================== 行业洞察 ==================== */
.insights-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

/* ==================== FAQ ==================== */
.faq-list {
  max-width: 800px;
  margin: 0 auto;
}

.faq-item {
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md);
  margin-bottom: 12px;
  overflow: hidden;
  transition: border-color var(--transition-base);
}

.faq-item.active {
  border-color: var(--red);
  border-left: 3px solid var(--red);
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
  font-size: 20px;
  color: var(--red);
  flex-shrink: 0;
  margin-left: 16px;
  font-weight: 300;
  line-height: 1;
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

/* ==================== 关于概览 ==================== */
.about-inner {
  display: flex;
  gap: 60px;
  align-items: center;
}

.about-stats {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
  flex-shrink: 0;
  min-width: 340px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 28px 20px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: var(--radius-md);
  transition: border-color var(--transition-base);
}

.stat-item:hover {
  border-color: var(--gold);
}

.stat-num {
  font-size: 42px;
  font-weight: 700;
  color: var(--gold);
  line-height: 1;
  font-family: var(--font-serif);
}

.stat-unit {
  font-size: 16px;
  color: var(--gold);
  margin-top: 4px;
}

.stat-label {
  font-size: 13px;
  color: rgba(242, 243, 239, 0.6);
  margin-top: 8px;
}

.about-text {
  flex: 1;
}

.about-text h2 {
  font-family: var(--font-serif);
  font-size: 32px;
  font-weight: 700;
  color: var(--white);
  margin-bottom: 16px;
}

.about-line {
  width: 48px;
  height: 3px;
  background: var(--gold);
  margin-bottom: 20px;
}

.about-desc {
  font-size: 15px;
  color: rgba(242, 243, 239, 0.65);
  line-height: 1.8;
  margin-bottom: 28px;
}

/* ==================== CTA区 ==================== */
.cta-section {
  background: linear-gradient(135deg, var(--red) 0%, #8f2222 100%);
  padding: 80px 0;
  text-align: center;
}

.cta-title {
  font-family: var(--font-serif);
  font-size: 36px;
  font-weight: 700;
  color: var(--white);
  margin-bottom: 12px;
}

.cta-subtitle {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.85);
  margin-bottom: 32px;
}

.btn-cta {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 14px 40px;
  font-size: 16px;
  font-weight: 600;
  color: var(--red);
  background: var(--white);
  border-radius: var(--radius-sm);
  text-decoration: none;
  transition: all var(--transition-base);
}

.btn-cta:hover {
  background: var(--paper);
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

.cta-phone {
  display: block;
  margin-top: 20px;
  font-size: 20px;
  font-weight: 700;
  color: var(--white);
  text-decoration: none;
  opacity: 0.9;
  transition: opacity var(--transition-base);
}

.cta-phone:hover {
  opacity: 1;
}

/* ==================== 滚动动画 ==================== */
.reveal {
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 0.6s ease, transform 0.6s ease;
}

.revealed {
  opacity: 1;
  transform: translateY(0);
}

/* ==================== 响应式 ==================== */
@media (max-width: 992px) {
  .hero-title {
    font-size: 42px;
  }

  .capabilities-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .products-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .solutions-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .cases-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .insights-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .about-inner {
    flex-direction: column;
    gap: 40px;
  }

  .about-stats {
    width: 100%;
    min-width: 0;
  }
}

@media (max-width: 768px) {
  .hero {
    min-height: 400px;
  }

  .hero-title {
    font-size: 32px;
  }

  .hero-subtitle {
    font-size: 15px;
  }

  .hero-actions {
    flex-direction: column;
    width: 100%;
    max-width: 260px;
  }

  .hero-actions .btn-red,
  .hero-actions .btn-outline-light {
    width: 100%;
    justify-content: center;
  }

  .carousel-slide {
    padding: 32px 24px;
  }

  .slide-content {
    flex-direction: column;
    text-align: center;
    gap: 24px;
  }

  .slide-title {
    font-size: 22px;
  }

  .slide-visual {
    width: 100px;
    height: 100px;
  }

  .slide-icon {
    width: 48px;
    height: 48px;
  }

  .capabilities-grid {
    grid-template-columns: 1fr;
  }

  .products-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }

  .process-timeline {
    flex-direction: column;
    align-items: stretch;
  }

  .process-step {
    flex-direction: row;
    text-align: left;
    padding: 0;
    gap: 16px;
  }

  .step-line {
    display: none;
  }

  .step-circle {
    flex-shrink: 0;
  }

  .step-title {
    margin-top: 0;
  }

  .step-desc {
    text-align: left;
  }

  .solutions-grid {
    grid-template-columns: 1fr;
  }

  .cases-grid {
    grid-template-columns: 1fr;
  }

  .insights-grid {
    grid-template-columns: 1fr;
  }

  .about-stats {
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;
  }

  .stat-item {
    padding: 20px 12px;
  }

  .stat-num {
    font-size: 32px;
  }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
    margin-bottom: 24px;
  }

  .section-header .section-title {
    margin-bottom: 0;
  }

  .cta-title {
    font-size: 26px;
  }
}
</style>
