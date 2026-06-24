<template>
  <footer class="site-footer">
    <!-- 主体内容区 -->
    <div class="footer-main">
      <div class="container footer-grid">

        <!-- 第1列：公司信息 -->
        <div class="footer-col footer-col--brand">
          <div class="brand-logo">
            <svg width="32" height="32" viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect width="32" height="32" rx="4" fill="var(--red)"/>
              <path d="M8 22V10h4l4 7 4-7h4v12h-3v-7.5l-3.5 6h-3l-3.5-6V22H8z" fill="var(--paper)"/>
            </svg>
            <span class="brand-name">{{ siteConfig.siteName || '企业官网' }}</span>
          </div>
          <p class="brand-desc">{{ companyDesc || '致力于为客户提供优质的产品和专业的技术服务，以创新驱动发展，以品质赢得信赖。' }}</p>

        </div>

        <!-- 第2列：产品分类 -->
        <div class="footer-col">
          <h4 class="col-title">
            <span>产品中心</span>
            <button class="collapse-btn" @click="toggleSection('products')" aria-label="展开/收起">
              <svg :class="['chevron', { 'chevron--open': openSections.products }]" width="16" height="16" viewBox="0 0 16 16" fill="none">
                <path d="M4 6l4 4 4-4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </button>
          </h4>
          <ul :class="['col-list', { 'col-list--open': openSections.products }]">
            <li v-for="cat in productCategories" :key="cat.id">
              <router-link :to="`/products?categoryId=${cat.id}`">
                <svg width="6" height="6" viewBox="0 0 6 6" fill="none" class="dot-icon">
                  <circle cx="3" cy="3" r="3" fill="var(--gold)"/>
                </svg>
                {{ cat.name }}
              </router-link>
            </li>
            <li v-if="!productCategories.length && !loadingCategories">
              <router-link to="/products">
                <svg width="6" height="6" viewBox="0 0 6 6" fill="none" class="dot-icon"><circle cx="3" cy="3" r="3" fill="var(--gold)"/></svg>
                全部产品
              </router-link>
            </li>
          </ul>
        </div>

        <!-- 第3列：快速链接 -->
        <div class="footer-col">
          <h4 class="col-title">
            <span>快速链接</span>
            <button class="collapse-btn" @click="toggleSection('links')" aria-label="展开/收起">
              <svg :class="['chevron', { 'chevron--open': openSections.links }]" width="16" height="16" viewBox="0 0 16 16" fill="none">
                <path d="M4 6l4 4 4-4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </button>
          </h4>
          <ul :class="['col-list', { 'col-list--open': openSections.links }]">
            <li><router-link to="/"><svg width="6" height="6" viewBox="0 0 6 6" fill="none" class="dot-icon"><circle cx="3" cy="3" r="3" fill="var(--gold)"/></svg>首页</router-link></li>
            <li><router-link to="/about"><svg width="6" height="6" viewBox="0 0 6 6" fill="none" class="dot-icon"><circle cx="3" cy="3" r="3" fill="var(--gold)"/></svg>关于我们</router-link></li>
            <li><router-link to="/products"><svg width="6" height="6" viewBox="0 0 6 6" fill="none" class="dot-icon"><circle cx="3" cy="3" r="3" fill="var(--gold)"/></svg>产品中心</router-link></li>
            <li><router-link to="/news"><svg width="6" height="6" viewBox="0 0 6 6" fill="none" class="dot-icon"><circle cx="3" cy="3" r="3" fill="var(--gold)"/></svg>行业洞察</router-link></li>
            <li><router-link to="/business"><svg width="6" height="6" viewBox="0 0 6 6" fill="none" class="dot-icon"><circle cx="3" cy="3" r="3" fill="var(--gold)"/></svg>解决方案</router-link></li>
            <li><router-link to="/contact"><svg width="6" height="6" viewBox="0 0 6 6" fill="none" class="dot-icon"><circle cx="3" cy="3" r="3" fill="var(--gold)"/></svg>联系我们</router-link></li>
          </ul>
        </div>

        <!-- 第4列：联系方式 -->
        <div class="footer-col">
          <h4 class="col-title">
            <span>联系我们</span>
            <button class="collapse-btn" @click="toggleSection('contact')" aria-label="展开/收起">
              <svg :class="['chevron', { 'chevron--open': openSections.contact }]" width="16" height="16" viewBox="0 0 16 16" fill="none">
                <path d="M4 6l4 4 4-4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </button>
          </h4>
          <ul :class="['col-list contact-list', { 'col-list--open': openSections.contact }]">
            <li v-if="contactInfo.address" class="contact-item">
              <svg class="contact-icon" width="16" height="16" viewBox="0 0 16 16" fill="none">
                <path d="M8 1.5C5.515 1.5 3.5 3.515 3.5 6c0 3.375 4.5 8.5 4.5 8.5S12.5 9.375 12.5 6c0-2.485-2.015-4.5-4.5-4.5zm0 6a1.5 1.5 0 110-3 1.5 1.5 0 010 3z" fill="currentColor"/>
              </svg>
              <span>{{ contactInfo.address }}</span>
            </li>
            <li v-if="contactInfo.phone" class="contact-item">
              <svg class="contact-icon" width="16" height="16" viewBox="0 0 16 16" fill="none">
                <path d="M13.5 10.5l-2-2a1 1 0 00-1.414 0l-.793.793a8.034 8.034 0 01-2.586-2.586L7.5 5.914A1 1 0 007.5 4.5l-2-2a1 1 0 00-1.414 0L2.5 4.086C2.5 9.338 6.662 13.5 11.914 13.5l1.586-1.586a1 1 0 000-1.414z" fill="currentColor"/>
              </svg>
              <a :href="`tel:${contactInfo.phone}`" class="contact-link">{{ contactInfo.phone }}</a>
            </li>
            <li v-if="contactInfo.email" class="contact-item">
              <svg class="contact-icon" width="16" height="16" viewBox="0 0 16 16" fill="none">
                <path d="M2 4a1 1 0 011-1h10a1 1 0 011 1v8a1 1 0 01-1 1H3a1 1 0 01-1-1V4zm1.5.75L8 8.25l4.5-3.5" stroke="currentColor" stroke-width="1.2" fill="none" stroke-linecap="round"/>
              </svg>
              <a :href="`mailto:${contactInfo.email}`" class="contact-link">{{ contactInfo.email }}</a>
            </li>
            <li v-if="contactInfo.fax" class="contact-item">
              <svg class="contact-icon" width="16" height="16" viewBox="0 0 16 16" fill="none">
                <rect x="2" y="5" width="12" height="9" rx="1" stroke="currentColor" stroke-width="1.2"/>
                <path d="M5 5V3a1 1 0 011-1h4a1 1 0 011 1v2" stroke="currentColor" stroke-width="1.2"/>
                <rect x="5" y="9" width="6" height="3" rx="0.5" fill="currentColor" opacity="0.5"/>
              </svg>
              <span>{{ contactInfo.fax }}</span>
            </li>
          </ul>
        </div>

      </div>
    </div>

    <!-- 分割线 -->
    <div class="footer-divider"></div>

    <!-- 底部版权栏 -->
    <div class="footer-bottom">
      <div class="container footer-bottom-inner">
        <p class="copyright">
          © {{ currentYear }} {{ siteConfig.siteName || '企业官网' }} 版权所有
          <span v-if="siteConfig.icp" class="icp-link">
            <a href="https://beian.miit.gov.cn/" target="_blank" rel="noopener">{{ siteConfig.icp }}</a>
          </span>
        </p>
        <div class="social-icons">
          <!-- 微信 -->
          <a href="#" class="social-icon" aria-label="微信" title="微信">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M8.5 10.5c.55 0 1-.45 1-1s-.45-1-1-1-1 .45-1 1 .45 1 1 1zm5 0c.55 0 1-.45 1-1s-.45-1-1-1-1 .45-1 1 .45 1 1 1z" fill="currentColor"/>
              <path d="M21.5 13.5c0-3.59-3.58-6.5-8-6.5S5.5 9.91 5.5 13.5c0 1.93.92 3.67 2.4 4.88L7 21l3.1-1.55c.6.15 1.23.24 1.9.24 4.42 0 8-2.91 8-6.5zm-11-1.5c-.55 0-1-.45-1-1s.45-1 1-1 1 .45 1 1-.45 1-1 1zm6 0c-.55 0-1-.45-1-1s.45-1 1-1 1 .45 1 1-.45 1-1 1z" fill="currentColor" opacity="0.4"/>
              <path d="M15.5 5.5c0-2.76-2.69-5-6-5S3.5 2.74 3.5 5.5c0 1.47.71 2.79 1.84 3.73L5 11l2.42-1.21c.67.17 1.37.27 2.08.27.17 0 .33-.01.5-.02C10 9.71 10 9.36 10 9c0-1.9.76-3.6 2-4.84-.16-.44-.5-1.16-.5-1.66" fill="currentColor" opacity="0.7"/>
            </svg>
          </a>
          <!-- QQ -->
          <a href="#" class="social-icon" aria-label="QQ" title="QQ">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
              <path d="M21.395 15.035a39.548 39.548 0 00-.803-2.264l-1.079-2.695a.175.175 0 00-.014-.044C20.311 8.6 20.15 6.301 19.189 4.6 17.896 2.329 15.48 1 12 1S6.104 2.329 4.811 4.6c-.961 1.701-1.122 4-...(truncated)" opacity="0.9"/>
            </svg>
          </a>
          <!-- 微博 -->
          <a href="#" class="social-icon" aria-label="微博" title="微博">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none">
              <path d="M10.098 20c-4.197 0-7.6-2.309-7.6-5.158 0-1.61 1.028-3.386 2.814-4.874 2.367-1.956 5.127-2.55 6.178-1.32.417.484.51 1.178.276 1.952 1.058-.457 1.988-.498 2.492-.062.644.555.496 1.607-.082 2.779 1.638.015 2.924.848 2.924 2.327C17.1 18.03 13.934 20 10.098 20zm9.08-14.5c-.283-2.83-2.803-4.854-5.65-4.498l-.25.03a.75.75 0 00.18 1.49l.25-.03c1.987-.24 3.785 1.19 3.994 3.163l.03.25a.75.75 0 101.494-.18l-.048-.225zm-2.44 1.49a.75.75 0 00-.874.604l-.02.112c-.16.873-.871 1.512-1.754 1.612a.75.75 0 00.173 1.49c1.433-.166 2.605-1.192 2.879-2.644l.02-.112a.75.75 0 00-.424-.062z" fill="currentColor"/>
            </svg>
          </a>
          <!-- 抖音 -->
          <a href="#" class="social-icon" aria-label="抖音" title="抖音">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor">
              <path d="M19.321 5.562a5.124 5.124 0 01-.443-.258 6.228 6.228 0 01-1.138-1.009c-.849-.966-1.166-1.959-1.282-2.656H16.4c.063.36.094.73.094 1.11v12.95a2.918 2.918 0 01-2.913 2.788 2.918 2.918 0 01-2.913-2.913 2.918 2.918 0 012.913-2.913c.286 0 .563.043.824.118V9.193a7.43 7.43 0 00-.824-.046 7.353 7.353 0 00-7.343 7.343 7.353 7.353 0 007.343 7.343 7.353 7.353 0 007.343-7.343V9.83a11.67 11.67 0 006.833 2.198V7.514a6.609 6.609 0 01-3.436-1.952z"/>
            </svg>
          </a>
        </div>
      </div>
    </div>

    <!-- 回到顶部按钮 -->
    <Transition name="fade-up">
      <button v-if="showBackTop" class="back-top" @click="scrollToTop" aria-label="回到顶部">
        <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
          <path d="M10 15V5M10 5L5 10M10 5l5 5" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </button>
    </Transition>
  </footer>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue'
import { getSiteConfig } from '../api/home'
import { getContactInfo } from '../api/contact'
import { getProductCategories } from '../api/product'

// ---- 数据 ----
const siteConfig = ref({})
const contactInfo = ref({})
const productCategories = ref([])
const loadingCategories = ref(true)

// ---- 手风琴（移动端折叠） ----
const openSections = reactive({ products: false, links: false, contact: false })
function toggleSection(key) {
  openSections[key] = !openSections[key]
}

// ---- 回到顶部 ----
const showBackTop = ref(false)
function onScroll() {
  showBackTop.value = window.scrollY > 300
}
function scrollToTop() {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// ---- 年份 ----
const currentYear = computed(() => new Date().getFullYear())

// ---- 公司简介（HTML实体解码 + 纯文本） ----
function stripHtml(html) {
  if (!html) return ''
  let text = html
    .replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&amp;/g, '&')
    .replace(/&quot;/g, '"').replace(/&#39;/g, "'")
  const div = document.createElement('div')
  div.innerHTML = text
  return (div.textContent || div.innerText || '').trim().substring(0, 200)
}
const companyDesc = computed(() => stripHtml(siteConfig.value.siteDesc))

// ---- 数据加载 ----
onMounted(async () => {
  window.addEventListener('scroll', onScroll, { passive: true })

  const [config, contact, categories] = await Promise.allSettled([
    getSiteConfig(),
    getContactInfo(),
    getProductCategories()
  ])
  if (config.status === 'fulfilled' && config.value) siteConfig.value = config.value
  if (contact.status === 'fulfilled' && contact.value) contactInfo.value = contact.value
  if (categories.status === 'fulfilled' && Array.isArray(categories.value)) {
    productCategories.value = categories.value
  }
  loadingCategories.value = false
})

onUnmounted(() => {
  window.removeEventListener('scroll', onScroll)
})
</script>

<style scoped>
/* ==============================
   Footer 根容器
   ============================== */
.site-footer {
  background: var(--graphite);
  color: var(--paper);
  font-family: var(--font-sans);
  font-size: 14px;
  position: relative;
}

/* ==============================
   主体网格
   ============================== */
.footer-main {
  padding: 64px 0 48px;
}

.footer-grid {
  display: grid;
  grid-template-columns: 1.6fr 1fr 1fr 1.2fr;
  gap: 48px;
}

/* ==============================
   第1列 - 品牌区
   ============================== */
.brand-logo {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.brand-name {
  font-size: 18px;
  font-weight: 700;
  font-family: var(--font-serif);
  color: var(--paper);
  letter-spacing: 0.02em;
}

.brand-desc {
  color: rgba(242, 243, 239, 0.65);
  line-height: 1.85;
  margin-bottom: 24px;
  font-size: 13.5px;
}

.brand-badges {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.badge {
  display: inline-block;
  padding: 3px 10px;
  border: 1px solid rgba(191, 142, 42, 0.5);
  color: var(--gold);
  font-size: 11px;
  letter-spacing: 0.06em;
  border-radius: 2px;
}

/* ==============================
   列标题
   ============================== */
.col-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--paper);
  margin-bottom: 24px;
  padding-bottom: 12px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.col-title span {
  position: relative;
}

.col-title span::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: -13px;
  width: 28px;
  height: 2px;
  background: var(--red);
}

/* ==============================
   折叠按钮（移动端）
   ============================== */
.collapse-btn {
  display: none;
  background: none;
  border: none;
  color: var(--paper);
  cursor: pointer;
  padding: 4px;
  opacity: 0.7;
  transition: opacity var(--transition-fast);
}

.collapse-btn:hover {
  opacity: 1;
}

.chevron {
  transition: transform var(--transition-base);
}

.chevron--open {
  transform: rotate(180deg);
}

/* ==============================
   列表项
   ============================== */
.col-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.col-list li a,
.col-list li router-link {
  display: flex;
  align-items: center;
  gap: 8px;
  color: rgba(242, 243, 239, 0.65);
  text-decoration: none;
  transition: color var(--transition-base), padding-left var(--transition-base);
  line-height: 1.5;
}

.col-list li a:hover {
  color: var(--paper);
  padding-left: 4px;
}

.col-list li a:hover .dot-icon circle {
  fill: var(--red);
}

.dot-icon {
  flex-shrink: 0;
  transition: fill var(--transition-fast);
}

/* ==============================
   联系方式列表
   ============================== */
.contact-list {
  gap: 14px;
}

.contact-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  color: rgba(242, 243, 239, 0.65);
  line-height: 1.6;
}

.contact-icon {
  flex-shrink: 0;
  margin-top: 2px;
  color: var(--gold);
}

.contact-link {
  color: rgba(242, 243, 239, 0.65);
  text-decoration: none;
  transition: color var(--transition-fast);
}

.contact-link:hover {
  color: var(--gold);
}

/* ==============================
   分割线
   ============================== */
.footer-divider {
  height: 1px;
  background: rgba(255, 255, 255, 0.1);
  margin: 0;
}

/* ==============================
   底部版权栏
   ============================== */
.footer-bottom {
  padding: 20px 0;
}

.footer-bottom-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.copyright {
  color: rgba(242, 243, 239, 0.45);
  font-size: 13px;
  line-height: 1.6;
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.icp-link a {
  color: rgba(242, 243, 239, 0.45);
  text-decoration: none;
  transition: color var(--transition-fast);
}

.icp-link a:hover {
  color: var(--gold);
}

/* ==============================
   社交图标
   ============================== */
.social-icons {
  display: flex;
  align-items: center;
  gap: 12px;
}

.social-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: 1px solid rgba(255, 255, 255, 0.15);
  color: rgba(242, 243, 239, 0.55);
  text-decoration: none;
  transition: all var(--transition-base);
}

.social-icon:hover {
  background: var(--red);
  border-color: var(--red);
  color: var(--paper);
  transform: translateY(-2px);
}

/* ==============================
   回到顶部按钮
   ============================== */
.back-top {
  position: fixed;
  right: 24px;
  bottom: 80px;
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: var(--red);
  color: var(--paper);
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 16px rgba(178, 43, 43, 0.45);
  transition: transform var(--transition-base), box-shadow var(--transition-base);
  z-index: 999;
}

.back-top:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(178, 43, 43, 0.6);
}

/* ==============================
   过渡动画
   ============================== */
.fade-up-enter-active,
.fade-up-leave-active {
  transition: opacity var(--transition-base), transform var(--transition-base);
}

.fade-up-enter-from,
.fade-up-leave-to {
  opacity: 0;
  transform: translateY(12px);
}

/* ==============================
   响应式：平板（≤992px）→ 2x2
   ============================== */
@media (max-width: 992px) {
  .footer-grid {
    grid-template-columns: 1fr 1fr;
    gap: 40px 32px;
  }

  .footer-col--brand {
    grid-column: 1 / -1;
  }
}

/* ==============================
   响应式：手机（≤768px）→ 单列手风琴
   ============================== */
@media (max-width: 768px) {
  .footer-main {
    padding: 40px 0 32px;
  }

  .footer-grid {
    grid-template-columns: 1fr;
    gap: 0;
  }

  .footer-col--brand {
    grid-column: auto;
    padding-bottom: 28px;
    margin-bottom: 8px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  }

  .footer-col:not(.footer-col--brand) {
    border-bottom: 1px solid rgba(255, 255, 255, 0.08);
    padding: 16px 0;
  }

  /* 移动端标题可点击展开 */
  .collapse-btn {
    display: flex;
  }

  .col-title {
    margin-bottom: 0;
    cursor: pointer;
  }

  /* 折叠状态：列表隐藏 */
  .col-list {
    overflow: hidden;
    max-height: 0;
    opacity: 0;
    transition: max-height 0.35s ease, opacity 0.3s ease, margin-top 0.3s ease;
    margin-top: 0;
  }

  /* 展开状态 */
  .col-list--open {
    max-height: 500px;
    opacity: 1;
    margin-top: 16px;
  }

  .col-title span::after {
    display: none;
  }

  .footer-bottom-inner {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .back-top {
    right: 16px;
    bottom: 60px;
    width: 40px;
    height: 40px;
  }
}
</style>
