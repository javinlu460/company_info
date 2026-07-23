<template>
  <div class="contact-page">
    <!-- 主体内容区 -->
    <section class="contact-body">
      <div class="container">
        <!-- 页面标题 -->
        <div class="section-header">
          <p class="ew-label">CONTACT · 联系我们</p>
          <h2 class="section-title">联系工厂，直接对接</h2>
        </div>

        <!-- 两栏布局 -->
        <div class="contact-layout">
          <!-- 左侧：留言表单 -->
          <div class="form-column">
            <p class="ew-label form-section-label">留言 / 咨询</p>
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
                  <div class="contact-icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72c.127.96.361 1.903.7 2.81a2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0 1 22 16.92z"/></svg></div>
                  <div class="contact-info">
                    <div class="contact-header">
                      <span class="contact-label">联系电话</span>
                    </div>
                    <div class="contact-value">
                      <a v-if="contactInfo.phone" :href="'tel:' + contactInfo.phone">{{ contactInfo.phone }}</a>
                      <span v-else>暂未填写</span>
                    </div>
                    <div class="contact-hint">{{ contactInfo.workTime || '工作日 6:30—17:30' }}</div>
                  </div>
                </li>

                <li class="contact-item">
                  <div class="contact-icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><rect x="2" y="4" width="20" height="16" rx="2"/><path d="m22 7-10 5L2 7"/></svg></div>
                  <div class="contact-info">
                    <div class="contact-header">
                      <span class="contact-label">邮箱</span>
                    </div>
                    <div class="contact-value">
                      <a v-if="contactInfo.email" :href="'mailto:' + contactInfo.email">{{ contactInfo.email }}</a>
                      <span v-else>暂未填写</span>
                    </div>
                    <div class="contact-hint">图纸/需求询价请发至此</div>
                  </div>
                </li>

                <li class="contact-item">
                  <div class="contact-icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"/></svg></div>
                  <div class="contact-info">
                    <div class="contact-header">
                      <span class="contact-label">微信</span>
                    </div>
                    <div class="contact-value">
                      <span>{{ contactInfo.wechat || '请来电获取' }}</span>
                    </div>
                  </div>
                </li>

                <li class="contact-item">
                  <div class="contact-icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/></svg></div>
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
                  <div class="contact-icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><rect x="1" y="3" width="15" height="13"/><path d="M16 8h4l3 3v5h-7V8z"/><circle cx="5.5" cy="18.5" r="2.5"/><circle cx="18.5" cy="18.5" r="2.5"/></svg></div>
                  <div class="contact-info">
                    <div class="contact-header">
                      <span class="contact-label">到厂</span>
                    </div>
                    <div class="contact-value">
                      <span>{{ contactInfo.route || '济南章丘出口，导航"白塔镇工业园"' }}</span>
                    </div>
                  </div>
                </li>
              </ul>

              <!-- 报价流程 -->
              <div class="quote-process">
                <div class="process-title">
                  <span class="process-icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"><path d="M9 2h6a1 1 0 0 1 1 1v1h1a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h1V3a1 1 0 0 1 1-1z"/><path d="M9 4h6"/></svg></span>
                  <span>报价流程</span>
                </div>
                <ol class="process-steps">
                  <li>
                    <span class="step-num">1</span>
                    <span class="step-text">发图纸/需求描述</span>
                  </li>
                  <li>
                    <span class="step-num">2</span>
                    <span class="step-text">工程师评估(工作日)</span>
                  </li>
                  <li>
                    <span class="step-num">3</span>
                    <span class="step-text">预估报价，材质，交期</span>
                  </li>
                  <li>
                    <span class="step-num">4</span>
                    <span class="step-text">正式签约</span>
                  </li>
                  <li>
                    <span class="step-num">5</span>
                    <span class="step-text">打样 → 确认 → 批量生产</span>
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
/* ========== 工业对比风格 (Final Contrast Industrial) ========== */
.contact-page {
  --maroon: #8a352d;
  --dark-panel: #1A1C20;
  --gold: #C29B40;
  --tp: #1a1917;
  --tb: #55524a;
}

/* ===== Body ===== */
.contact-body {
  background: var(--paper);
  padding: 30px 0 80px;
  padding-top: calc(var(--header-height) + 30px);
}

/* ===== Section Header ===== */
.section-header {
  margin-bottom: 40px;
}

.ew-label {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  color: var(--maroon);
  letter-spacing: 0.1em;
  font-weight: 800;
  margin-bottom: 12px;
  text-transform: uppercase;
}

.ew-label::before {
  content: '';
  width: 20px;
  height: 2px;
  background: var(--maroon);
}

.section-title {
  font-size: 36px;
  font-weight: 900;
  color: var(--tp);
  line-height: 1.25;
  margin: 0;
  text-align: left;
}

/* ===== 深浅对比两栏 ===== */
.contact-layout {
  display: grid;
  grid-template-columns: 1.4fr 1fr;
  gap: 0;
  border: 3px solid var(--maroon);
  background: #fff;
  overflow: hidden;
  box-shadow: 15px 15px 40px rgba(0, 0, 0, 0.1);
  align-items: stretch;
}

/* ===== 左：白底留言表单 ===== */
.form-column {
  background: #fff;
  border: none;
  border-right: 1px solid #eee;
  border-radius: 0;
  box-shadow: none;
  padding: 55px 55px 60px;
}

.form-section-label {
  display: flex;
  align-items: center;
  gap: 15px;
  color: var(--tp);
  font-size: 24px;
  font-weight: 900;
  text-transform: none;
  letter-spacing: 0;
  margin-bottom: 36px;
}

.form-section-label::before {
  content: '';
  width: 5px;
  height: 26px;
  background: var(--maroon);
  display: inline-block;
  flex-shrink: 0;
}

.message-form {
  display: flex;
  flex-direction: column;
  gap: 22px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 18px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-size: 12px;
  font-weight: 800;
  color: var(--maroon);
  text-transform: uppercase;
  letter-spacing: 0.04em;
  margin-bottom: 10px;
}

.required {
  color: var(--maroon);
  margin-left: 3px;
}

.form-group input,
.form-group textarea {
  padding: 14px 15px;
  border: 2px solid #ddd;
  border-radius: 0;
  font-size: 14px;
  color: var(--tp);
  background: #fafafa;
  transition: all var(--transition-base);
  outline: none;
  font-family: inherit;
}

.form-group input::placeholder,
.form-group textarea::placeholder {
  color: #a7a49c;
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: var(--maroon);
  background: #fff;
}

.form-group input.input-error,
.form-group textarea.input-error {
  border-color: var(--maroon);
}

.error-msg {
  font-size: 12px;
  color: var(--maroon);
  margin-top: 4px;
}

.form-group textarea {
  resize: vertical;
  min-height: 140px;
}

.btn-submit {
  display: block;
  width: 100%;
  padding: 18px 36px;
  background: var(--maroon);
  color: #fff;
  font-size: 16px;
  font-weight: 900;
  border: none;
  border-radius: 0;
  cursor: pointer;
  transition: all var(--transition-base);
  margin-top: 6px;
}

.btn-submit:hover {
  background: #722a23;
  transform: translateY(-2px);
}

.btn-submit:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
}

/* ===== 右：深色联系面板 ===== */
.sidebar-column {
  display: flex;
}

.sidebar-card {
  background: var(--dark-panel);
  color: #fff;
  border: none;
  border-radius: 0;
  box-shadow: none;
  padding: 55px 45px;
  width: 100%;
  display: flex;
  flex-direction: column;
}

.sidebar-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 11px;
  font-weight: 900;
  color: var(--gold);
  text-transform: uppercase;
  letter-spacing: 0.15em;
  margin: 0 0 36px;
}

.sidebar-title::after {
  content: '';
  height: 1px;
  flex: 1;
  background: rgba(255, 255, 255, 0.1);
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
  gap: 18px;
  padding: 16px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
}

.contact-item:last-child {
  border-bottom: none;
}

.contact-icon {
  width: 44px;
  height: 44px;
  border-radius: 4px;
  background: #2a2d33;
  color: var(--gold);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.contact-icon svg {
  width: 18px;
  height: 18px;
}

.contact-info {
  flex: 1;
  min-width: 0;
}

.contact-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.contact-label {
  font-size: 11px;
  font-weight: 800;
  color: var(--gold);
  text-transform: uppercase;
  letter-spacing: 0.1em;
}

.contact-value {
  font-size: 18px;
  font-weight: 800;
  color: #fff;
  margin-bottom: 2px;
  word-break: break-all;
}

.contact-value a {
  color: #fff;
  text-decoration: none;
  transition: color var(--transition-base);
}

.contact-value a:hover {
  color: var(--gold);
}

.contact-hint {
  font-size: 13px;
  color: #8e8e8e;
  margin-top: 3px;
}

/* Quote Process */
.quote-process {
  margin-top: auto;
  padding: 30px;
  background: rgba(255, 255, 255, 0.03);
  border: 1px dashed rgba(255, 255, 255, 0.2);
  border-radius: 0;
  position: relative;
}

.quote-process::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background: var(--maroon);
}

.process-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 800;
  color: var(--gold);
  margin-bottom: 20px;
}

.process-icon {
  display: inline-flex;
  color: var(--gold);
}

.process-icon svg {
  width: 16px;
  height: 16px;
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
  gap: 12px;
  padding: 0;
  margin-bottom: 12px;
}

.process-steps li:last-child {
  margin-bottom: 0;
}

.step-num {
  min-width: 20px;
  color: var(--gold);
  font-weight: 900;
  font-size: 14px;
  flex-shrink: 0;
  padding-top: 1px;
}

.step-text {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.8);
  font-weight: 600;
  line-height: 1.55;
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
  border: 3px solid var(--maroon);
  border-radius: 0;
  padding: 40px;
  text-align: center;
  max-width: 380px;
  width: 90%;
  box-shadow: 15px 15px 40px rgba(0, 0, 0, 0.15);
}

.success-icon {
  width: 56px;
  height: 56px;
  margin: 0 auto 16px;
}

.success-modal h3 {
  font-size: 20px;
  font-weight: 900;
  color: var(--tp);
  margin-bottom: 8px;
}

.success-modal p {
  font-size: 14px;
  color: var(--tb);
  margin-bottom: 24px;
}

.success-modal .btn-red {
  background: var(--maroon);
  border-radius: 0;
}

/* ===== Responsive ===== */
@media (max-width: 960px) {
  .contact-layout {
    grid-template-columns: 1fr;
  }

  .form-column {
    border-right: none;
    border-bottom: 1px solid #eee;
  }

  .sidebar-column {
    position: static;
  }
}

@media (max-width: 768px) {
  .contact-body {
    padding-top: calc(var(--header-height) + 24px);
    padding-bottom: 48px;
  }

  .form-column {
    padding: 32px 22px;
  }

  .sidebar-card {
    padding: 32px 22px;
  }

  .form-row {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .section-title {
    font-size: 28px;
  }
}
</style>
