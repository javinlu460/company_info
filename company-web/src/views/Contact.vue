<template>
  <div class="contact-page">
    <!-- 页面 Banner -->
    <div class="page-banner">
      <div class="container">
        <nav class="breadcrumb">
          <router-link to="/">首页</router-link>
          <span class="separator">/</span>
          <span class="current">联系我们</span>
        </nav>
        <h1>联系我们</h1>
        <p class="banner-subtitle">CONTACT</p>
      </div>
    </div>

    <!-- 联系信息卡片区 -->
    <section class="section-light info-section">
      <div class="container">
        <div class="info-grid">
          <div class="info-card">
            <div class="info-icon">
              <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M24 4C16.268 4 10 9.82 10 17c0 7.18 10 23 14 23s14-15.82 14-23C38 9.82 31.732 4 24 4z" stroke="currentColor" stroke-width="2.5" stroke-linejoin="round" />
                <circle cx="24" cy="17" r="5" stroke="currentColor" stroke-width="2.5" />
              </svg>
            </div>
            <h3>公司地址</h3>
            <p>{{ contactInfo.address || '暂未填写地址' }}</p>
          </div>

          <div class="info-card">
            <div class="info-icon">
              <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M14 10h20a4 4 0 014 4v20a4 4 0 01-4 4H14a4 4 0 01-4-4V14a4 4 0 014-4z" stroke="currentColor" stroke-width="2.5" />
                <path d="M14 14l10 8 10-8" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" />
              </svg>
            </div>
            <h3>电子邮箱</h3>
            <p>
              <a v-if="contactInfo.email" :href="'mailto:' + contactInfo.email">{{ contactInfo.email }}</a>
              <span v-else>暂未填写邮箱</span>
            </p>
          </div>

          <div class="info-card">
            <div class="info-icon">
              <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M14 12c0 10 6 22 16 26" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" />
                <path d="M12 18c0 8 5 17 13 21" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" />
                <path d="M30 30c5-7 8-14 8-20a6 6 0 00-6-6c-4 0-6 3-6 6 0 4 3 8 6 12" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" />
              </svg>
            </div>
            <h3>联系电话</h3>
            <p>
              <a v-if="contactInfo.phone" :href="'tel:' + contactInfo.phone">{{ contactInfo.phone }}</a>
              <span v-else>暂未填写电话</span>
            </p>
          </div>

          <div class="info-card">
            <div class="info-icon">
              <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
                <circle cx="24" cy="24" r="16" stroke="currentColor" stroke-width="2.5" />
                <path d="M24 14v10l7 7" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" />
              </svg>
            </div>
            <h3>工作时间</h3>
            <p>{{ contactInfo.workTime || '周一至周五 09:00 - 18:00' }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- RFQ 报价表单区 -->
    <section class="rfq-section">
      <div class="container">
        <div class="rfq-layout">
          <div class="rfq-intro">
            <h2>获取报价</h2>
            <p class="rfq-subtitle">REQUEST FOR QUOTE</p>
            <p class="rfq-desc">留下您的需求信息，我们将在 24 小时内与您取得联系，为您提供专业的产品方案与报价服务。</p>
            <ul class="rfq-features">
              <li>
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M20 6L9 17l-5-5" stroke="#BF8E2A" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" /></svg>
                <span>快速响应</span>
              </li>
              <li>
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M20 6L9 17l-5-5" stroke="#BF8E2A" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" /></svg>
                <span>专业方案</span>
              </li>
              <li>
                <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M20 6L9 17l-5-5" stroke="#BF8E2A" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" /></svg>
                <span>全程跟踪</span>
              </li>
            </ul>
          </div>

          <div class="rfq-form-card">
            <h3 class="form-title">在线留言</h3>
            <form class="message-form" @submit.prevent="handleSubmit">
              <div class="form-row">
                <div class="form-group">
                  <label for="name">姓名 <span class="required">*</span></label>
                  <input
                    id="name"
                    v-model="form.name"
                    type="text"
                    placeholder="请输入您的姓名"
                    :class="{ error: errors.name }"
                  />
                  <span v-if="errors.name" class="error-msg">{{ errors.name }}</span>
                </div>
                <div class="form-group">
                  <label for="phone">电话 <span class="required">*</span></label>
                  <input
                    id="phone"
                    v-model="form.phone"
                    type="tel"
                    placeholder="请输入您的联系电话"
                    :class="{ error: errors.phone }"
                  />
                  <span v-if="errors.phone" class="error-msg">{{ errors.phone }}</span>
                </div>
              </div>

              <div class="form-row">
                <div class="form-group">
                  <label for="email">邮箱</label>
                  <input
                    id="email"
                    v-model="form.email"
                    type="email"
                    placeholder="请输入您的邮箱地址"
                  />
                </div>
                <div class="form-group">
                  <label for="company">公司名称</label>
                  <input
                    id="company"
                    v-model="form.companyName"
                    type="text"
                    placeholder="请输入您的公司名称"
                  />
                </div>
              </div>

              <div class="form-group">
                <label for="content">留言内容 <span class="required">*</span></label>
                <textarea
                  id="content"
                  v-model="form.content"
                  rows="5"
                  placeholder="请输入您的留言内容，包括产品需求、数量、应用场景等"
                  :class="{ error: errors.content }"
                ></textarea>
                <span v-if="errors.content" class="error-msg">{{ errors.content }}</span>
              </div>

              <button type="submit" class="btn-red submit-btn" :disabled="submitting">
                {{ submitting ? '提交中...' : '提交留言' }}
              </button>
            </form>
          </div>
        </div>
      </div>
    </section>

    <!-- 提交成功提示 -->
    <div v-if="showSuccess" class="success-overlay" @click="showSuccess = false">
      <div class="success-modal" @click.stop>
        <div class="success-icon">
          <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="24" cy="24" r="22" fill="#27ae60" />
            <path d="M14 24l7 7 13-13" stroke="#ffffff" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" />
          </svg>
        </div>
        <h3>提交成功！</h3>
        <p>感谢您的留言，我们会尽快与您联系。</p>
        <button class="btn-red" @click="showSuccess = false">确定</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { getContactInfo, submitMessage } from '../api/contact'

const contactInfo = ref({})
const showSuccess = ref(false)
const submitting = ref(false)

const form = reactive({
  name: '',
  phone: '',
  email: '',
  companyName: '',
  content: '',
  address: ''
})

const errors = reactive({
  name: '',
  phone: '',
  content: ''
})

async function loadContactInfo() {
  try {
    contactInfo.value = await getContactInfo()
  } catch (e) {
    console.warn('加载联系信息失败:', e)
  }
}

function validate() {
  let valid = true
  errors.name = ''
  errors.phone = ''
  errors.content = ''

  if (!form.name.trim()) {
    errors.name = '请输入姓名'
    valid = false
  }
  if (!form.phone.trim()) {
    errors.phone = '请输入联系电话'
    valid = false
  }
  if (!form.content.trim()) {
    errors.content = '请输入留言内容'
    valid = false
  }
  return valid
}

async function handleSubmit() {
  if (!validate()) return

  submitting.value = true
  try {
    await submitMessage({
      name: form.name,
      phone: form.phone,
      email: form.email,
      content: form.content,
      address: form.address,
      companyName: form.companyName
    })
    showSuccess.value = true
    form.name = ''
    form.phone = ''
    form.email = ''
    form.companyName = ''
    form.content = ''
    form.address = ''
  } catch (e) {
    console.error('提交留言失败:', e)
    alert('提交失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadContactInfo()
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

/* 联系信息卡片区 */
.info-section {
  padding: var(--section-padding) 0;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.info-card {
  background: var(--white);
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md);
  padding: 36px 24px;
  text-align: center;
  transition: all var(--transition-base);
}

.info-card:hover {
  box-shadow: var(--shadow-lg);
  transform: translateY(-6px);
  border-color: var(--gold);
}

.info-icon {
  width: 56px;
  height: 56px;
  margin: 0 auto 18px;
  color: var(--red);
}

.info-card h3 {
  font-family: var(--font-serif);
  font-size: 18px;
  color: var(--ink);
  margin-bottom: 10px;
}

.info-card p {
  font-size: 14px;
  color: var(--gray-600);
  line-height: 1.6;
  word-break: break-all;
}

.info-card a {
  color: var(--blue);
  text-decoration: none;
  transition: color var(--transition-fast);
}

.info-card a:hover {
  color: var(--red);
}

/* RFQ 报价表单区 */
.rfq-section {
  padding: var(--section-padding) 0;
  background: var(--graphite);
  color: var(--paper);
}

.rfq-layout {
  display: grid;
  grid-template-columns: 1fr 1.2fr;
  gap: 60px;
  align-items: start;
}

.rfq-intro h2 {
  font-family: var(--font-serif);
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 8px;
}

.rfq-subtitle {
  font-family: var(--font-serif);
  font-size: 15px;
  color: var(--gold);
  letter-spacing: 3px;
  margin-bottom: 24px;
}

.rfq-desc {
  font-size: 15px;
  line-height: 1.8;
  color: rgba(242, 243, 239, 0.72);
  margin-bottom: 32px;
  max-width: 420px;
}

.rfq-features {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.rfq-features li {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 16px;
}

.rfq-features svg {
  width: 22px;
  height: 22px;
  flex-shrink: 0;
}

.rfq-form-card {
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(242, 243, 239, 0.1);
  border-radius: var(--radius-lg);
  padding: 40px;
  backdrop-filter: blur(4px);
}

.form-title {
  font-family: var(--font-serif);
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 28px;
  color: var(--paper);
}

.message-form {
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 22px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-size: 14px;
  font-weight: 500;
  color: rgba(242, 243, 239, 0.85);
  margin-bottom: 8px;
}

.required {
  color: var(--red);
}

.form-group input,
.form-group textarea {
  padding: 12px 16px;
  border: 1px solid rgba(242, 243, 239, 0.15);
  border-radius: var(--radius-sm);
  font-size: 14px;
  color: var(--paper);
  background: rgba(255, 255, 255, 0.06);
  transition: all var(--transition-base);
  outline: none;
}

.form-group input::placeholder,
.form-group textarea::placeholder {
  color: rgba(242, 243, 239, 0.35);
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: var(--gold);
  background: rgba(255, 255, 255, 0.1);
}

.form-group input.error,
.form-group textarea.error {
  border-color: var(--red);
}

.error-msg {
  font-size: 12px;
  color: #ff7a7a;
  margin-top: 6px;
}

.form-group textarea {
  resize: vertical;
  min-height: 120px;
}

.submit-btn {
  width: 100%;
  padding: 14px 40px;
  font-size: 16px;
  font-weight: 600;
  border: none;
}

.submit-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

/* 成功提示 */
.success-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.success-modal {
  background: var(--white);
  border-radius: var(--radius-lg);
  padding: 40px;
  text-align: center;
  max-width: 400px;
  width: 90%;
  box-shadow: var(--shadow-xl);
}

.success-icon {
  width: 60px;
  height: 60px;
  margin: 0 auto 16px;
}

.success-modal h3 {
  font-family: var(--font-serif);
  font-size: 22px;
  font-weight: 700;
  color: var(--ink);
  margin-bottom: 8px;
}

.success-modal p {
  font-size: 14px;
  color: var(--gray-600);
  margin-bottom: 24px;
}

.success-modal .btn-red {
  padding: 10px 32px;
}

/* 响应式 */
@media (max-width: 992px) {
  .info-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .rfq-layout {
    grid-template-columns: 1fr;
    gap: 40px;
  }

  .rfq-intro {
    text-align: center;
  }

  .rfq-desc {
    margin-left: auto;
    margin-right: auto;
  }

  .rfq-features {
    align-items: center;
  }
}

@media (max-width: 768px) {
  .page-banner {
    padding: 80px 0 48px;
  }

  .page-banner h1 {
    font-size: 28px;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }

  .rfq-form-card {
    padding: 28px 20px;
  }

  .form-row {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .rfq-intro h2 {
    font-size: 28px;
  }
}
</style>
