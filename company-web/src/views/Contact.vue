<template>
  <div class="contact-page">
    <!-- 主体内容区 -->
    <section class="contact-body">
      <div class="container">
        <!-- 页面标题 -->
        <div class="section-header">
          <p class="ew-label">— CONTACT · 联系我们</p>
          <h2 class="section-title">联系工厂，直接对接</h2>
        </div>

        <!-- 两栏布局 -->
        <div class="contact-layout">
          <!-- 左侧：留言表单 -->
          <div class="form-column">
            <p class="ew-label form-section-label">— 留言/快速咨询</p>
            <form class="message-form" @submit.prevent="handleSubmit">
              <div class="form-row">
                <div class="form-group">
                  <label for="name">姓名</label>
                  <input
                    id="name"
                    v-model="form.name"
                    type="text"
                    placeholder="请输入姓名"
                    :class="{ 'input-error': errors.name }"
                  />
                  <span v-if="errors.name" class="error-msg">{{ errors.name }}</span>
                </div>
                <div class="form-group">
                  <label for="company">公司</label>
                  <input
                    id="company"
                    v-model="form.companyName"
                    type="text"
                    placeholder="请输入公司名称"
                  />
                </div>
              </div>

              <div class="form-group">
                <label for="email">邮箱 <span class="required">*</span></label>
                <input
                  id="email"
                  v-model="form.email"
                  type="email"
                  placeholder="请输入邮箱地址"
                  :class="{ 'input-error': errors.email }"
                />
                <span v-if="errors.email" class="error-msg">{{ errors.email }}</span>
              </div>

              <div class="form-group">
                <label for="content">留言内容</label>
                <textarea
                  id="content"
                  v-model="form.content"
                  rows="6"
                  placeholder="零件加工需求问题描述..."
                  :class="{ 'input-error': errors.content }"
                ></textarea>
                <span v-if="errors.content" class="error-msg">{{ errors.content }}</span>
              </div>

              <input type="hidden" v-model="form.phone" />

              <button type="submit" class="btn-submit" :disabled="submitting">
                {{ submitting ? '发送中...' : '发送留言' }}
              </button>
            </form>
          </div>

          <!-- 右侧：联系方式侧边栏 -->
          <div class="sidebar-column">
            <div class="sidebar-card">
              <h3 class="sidebar-title">工厂直接联系方式</h3>

              <ul class="contact-list">
                <li class="contact-item">
                  <div class="contact-icon">📞</div>
                  <div class="contact-info">
                    <div class="contact-header">
                      <span class="contact-label">联系电话</span>
                      <span class="badge">必拨</span>
                    </div>
                    <div class="contact-value">
                      <a v-if="contactInfo.phone" :href="'tel:' + contactInfo.phone">{{ contactInfo.phone }}</a>
                      <span v-else>暂未填写</span>
                    </div>
                    <div class="contact-hint">工作日 6:30—17:30</div>
                  </div>
                </li>

                <li class="contact-item">
                  <div class="contact-icon">✉</div>
                  <div class="contact-info">
                    <div class="contact-header">
                      <span class="contact-label">邮箱</span>
                      <span class="badge">非转发</span>
                    </div>
                    <div class="contact-value">
                      <a v-if="contactInfo.email" :href="'mailto:' + contactInfo.email">{{ contactInfo.email }}</a>
                      <span v-else>暂未填写</span>
                    </div>
                    <div class="contact-hint">回邮/询价请发至此</div>
                  </div>
                </li>

                <li class="contact-item">
                  <div class="contact-icon">💬</div>
                  <div class="contact-info">
                    <div class="contact-header">
                      <span class="contact-label">微信</span>
                      <span class="badge">非转发</span>
                    </div>
                    <div class="contact-value">
                      <span>{{ contactInfo.wechat || '请来电获取' }}</span>
                    </div>
                    <div class="contact-hint">国内客户优先推荐</div>
                  </div>
                </li>

                <li class="contact-item">
                  <div class="contact-icon">📍</div>
                  <div class="contact-info">
                    <div class="contact-header">
                      <span class="contact-label">工厂地址</span>
                    </div>
                    <div class="contact-value">
                      <span>{{ contactInfo.address || '山东省淄博市博山区白塔镇工业园' }}</span>
                    </div>
                  </div>
                </li>

                <li class="contact-item">
                  <div class="contact-icon">🚗</div>
                  <div class="contact-info">
                    <div class="contact-header">
                      <span class="contact-label">到厂</span>
                    </div>
                    <div class="contact-value">
                      <span>济南章丘出口，导航"白塔镇工业园"</span>
                    </div>
                  </div>
                </li>
              </ul>

              <!-- 报价流程 -->
              <div class="quote-process">
                <div class="process-title">
                  <span class="process-icon">📋</span>
                  <span>报价流程</span>
                </div>
                <ol class="process-steps">
                  <li>
                    <span class="step-num">1</span>
                    <span class="step-text">发送图纸/样品描述</span>
                  </li>
                  <li>
                    <span class="step-num">2</span>
                    <span class="step-text">工程师评估工艺（工作日）</span>
                  </li>
                  <li>
                    <span class="step-num">3</span>
                    <span class="step-text">确认报价：材质·交期</span>
                  </li>
                  <li>
                    <span class="step-num">4</span>
                    <span class="step-text">打样 → 确认 → 批量生产</span>
                  </li>
                  <li>
                    <span class="step-num">5</span>
                    <span class="step-text">跟检出报告 → 发货跟踪</span>
                  </li>
                </ol>
              </div>
            </div>
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
  email: '',
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
  errors.email = ''
  errors.content = ''

  if (!form.name.trim()) {
    errors.name = '请输入姓名'
    valid = false
  }
  if (!form.email.trim()) {
    errors.email = '请输入邮箱'
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
/* ===== Body ===== */
.contact-body {
  background: #FAFAF8;
  padding: 72px 0 80px;
  margin-top: var(--header-height);
}

/* ===== Section Header ===== */
.section-header {
  margin-bottom: 48px;
}

.ew-label {
  font-size: 13px;
  color: #B22B2B;
  letter-spacing: 1px;
  font-weight: 500;
  margin-bottom: 12px;
}

.section-title {
  font-family: var(--font-serif);
  font-size: 34px;
  font-weight: 700;
  color: #1E1F20;
  line-height: 1.25;
  margin: 0;
}

/* ===== Two-column layout ===== */
.contact-layout {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 40px;
  align-items: start;
}

/* ===== Form Column ===== */
.form-column {
  background: #fff;
  border: 1px solid #D0D3CC;
  border-radius: 8px;
  padding: 36px 40px;
}

.form-section-label {
  margin-bottom: 24px;
}

.message-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-size: 13px;
  font-weight: 500;
  color: #1E1F20;
  margin-bottom: 6px;
}

.required {
  color: #B22B2B;
}

.form-group input,
.form-group textarea {
  padding: 10px 14px;
  border: 1px solid #D0D3CC;
  border-radius: 4px;
  font-size: 14px;
  color: #1E1F20;
  background: #fff;
  transition: border-color 0.2s;
  outline: none;
  font-family: inherit;
}

.form-group input::placeholder,
.form-group textarea::placeholder {
  color: #9a9d9f;
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: #B22B2B;
}

.form-group input.input-error,
.form-group textarea.input-error {
  border-color: #B22B2B;
}

.error-msg {
  font-size: 12px;
  color: #B22B2B;
  margin-top: 4px;
}

.form-group textarea {
  resize: vertical;
  min-height: 130px;
}

.btn-submit {
  display: inline-block;
  padding: 12px 36px;
  background: #B22B2B;
  color: #fff;
  font-size: 15px;
  font-weight: 600;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s;
  align-self: flex-start;
  letter-spacing: 0.5px;
}

.btn-submit:hover {
  background: #9a1f1f;
}

.btn-submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

/* ===== Sidebar Column ===== */
.sidebar-column {
  position: sticky;
  top: calc(var(--header-height) + 24px);
}

.sidebar-card {
  background: #F8F9F6;
  border: 1px solid #D0D3CC;
  border-radius: 8px;
  padding: 28px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.sidebar-title {
  font-family: var(--font-serif);
  font-size: 16px;
  font-weight: 700;
  color: #1E1F20;
  margin: 0 0 20px;
  padding-bottom: 14px;
  border-bottom: 1px solid #D0D3CC;
}

/* Contact List */
.contact-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
}

.contact-item {
  display: flex;
  gap: 12px;
  padding: 14px 0;
  border-bottom: 1px solid #E8EAE4;
}

.contact-item:last-child {
  border-bottom: none;
}

.contact-icon {
  font-size: 18px;
  flex-shrink: 0;
  width: 22px;
  text-align: center;
  margin-top: 2px;
}

.contact-info {
  flex: 1;
  min-width: 0;
}

.contact-header {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 3px;
}

.contact-label {
  font-size: 13px;
  font-weight: 600;
  color: #1E1F20;
}

.badge {
  font-size: 11px;
  background: #B22B2B;
  color: #fff;
  padding: 1px 6px;
  border-radius: 2px;
  font-weight: 500;
  letter-spacing: 0.3px;
  flex-shrink: 0;
}

.contact-value {
  font-size: 13px;
  color: #1E1F20;
  margin-bottom: 2px;
  word-break: break-all;
}

.contact-value a {
  color: #1E1F20;
  text-decoration: none;
  transition: color 0.2s;
}

.contact-value a:hover {
  color: #B22B2B;
}

.contact-hint {
  font-size: 12px;
  color: #6A6D70;
}

/* Quote Process */
.quote-process {
  margin-top: 20px;
  padding-top: 18px;
  border-top: 1px dashed #D0D3CC;
}

.process-title {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  font-weight: 700;
  color: #1E1F20;
  margin-bottom: 14px;
}

.process-icon {
  font-size: 16px;
}

.process-steps {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
}

.process-steps li {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 6px 0;
  position: relative;
}

.process-steps li:not(:last-child)::after {
  content: '';
  position: absolute;
  left: 11px;
  top: 28px;
  bottom: -6px;
  width: 1px;
  border-left: 1px dashed #C0C3BC;
}

.step-num {
  width: 22px;
  height: 22px;
  background: #B22B2B;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 700;
  flex-shrink: 0;
}

.step-text {
  font-size: 13px;
  color: #3a3d40;
  line-height: 1.5;
  padding-top: 3px;
}

/* ===== Success Modal ===== */
.success-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.55);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.success-modal {
  background: #fff;
  border-radius: 8px;
  padding: 40px;
  text-align: center;
  max-width: 380px;
  width: 90%;
  box-shadow: 0 8px 40px rgba(0,0,0,0.18);
}

.success-icon {
  width: 56px;
  height: 56px;
  margin: 0 auto 16px;
}

.success-modal h3 {
  font-family: var(--font-serif);
  font-size: 20px;
  font-weight: 700;
  color: #1E1F20;
  margin-bottom: 8px;
}

.success-modal p {
  font-size: 14px;
  color: #6A6D70;
  margin-bottom: 24px;
}

/* ===== Responsive ===== */
@media (max-width: 960px) {
  .contact-layout {
    grid-template-columns: 1fr;
  }

  .sidebar-column {
    position: static;
  }
}

@media (max-width: 768px) {
  .contact-body {
    padding: 48px 0 60px;
  }

  .form-column {
    padding: 24px 20px;
  }

  .form-row {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .section-title {
    font-size: 26px;
  }
}
</style>
