<template>
  <div class="contact-page">
    <div class="page-banner">
      <h1>联系我们</h1>
      <p>CONTACT US</p>
    </div>

    <div class="container">
      <div class="contact-layout">
        <!-- 联系信息 -->
        <div class="contact-info-section">
          <h2 class="section-heading">联系信息</h2>
          <div class="info-list">
            <div v-if="contactInfo.address" class="info-item">
              <span class="info-icon">📍</span>
              <div>
                <strong>公司地址</strong>
                <p>{{ contactInfo.address }}</p>
              </div>
            </div>
            <div v-if="contactInfo.phone" class="info-item">
              <span class="info-icon">📞</span>
              <div>
                <strong>联系电话</strong>
                <p><a :href="'tel:' + contactInfo.phone">{{ contactInfo.phone }}</a></p>
              </div>
            </div>
            <div v-if="contactInfo.fax" class="info-item">
              <span class="info-icon">📠</span>
              <div>
                <strong>传真号码</strong>
                <p>{{ contactInfo.fax }}</p>
              </div>
            </div>
            <div v-if="contactInfo.email" class="info-item">
              <span class="info-icon">📧</span>
              <div>
                <strong>电子邮箱</strong>
                <p><a :href="'mailto:' + contactInfo.email">{{ contactInfo.email }}</a></p>
              </div>
            </div>
          </div>
        </div>

        <!-- 在线留言 -->
        <div class="contact-form-section">
          <h2 class="section-heading">在线留言</h2>
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
              <label for="content">留言内容 <span class="required">*</span></label>
              <textarea
                id="content"
                v-model="form.content"
                rows="5"
                placeholder="请输入您的留言内容"
                :class="{ error: errors.content }"
              ></textarea>
              <span v-if="errors.content" class="error-msg">{{ errors.content }}</span>
            </div>
            <button type="submit" class="btn-primary submit-btn" :disabled="submitting">
              {{ submitting ? '提交中...' : '提交留言' }}
            </button>
          </form>
        </div>
      </div>
    </div>

    <!-- 提交成功提示 -->
    <div v-if="showSuccess" class="success-overlay" @click="showSuccess = false">
      <div class="success-modal" @click.stop>
        <div class="success-icon">✓</div>
        <h3>提交成功！</h3>
        <p>感谢您的留言，我们会尽快与您联系。</p>
        <button class="btn-primary" @click="showSuccess = false">确定</button>
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
      address: form.address
    })
    showSuccess.value = true
    form.name = ''
    form.phone = ''
    form.email = ''
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
.contact-layout {
  display: flex;
  gap: 50px;
  padding: 40px 0 60px;
}

.contact-info-section {
  width: 360px;
  flex-shrink: 0;
}

.section-heading {
  font-size: 22px;
  font-weight: 700;
  color: var(--color-primary);
  margin-bottom: 24px;
  padding-bottom: 12px;
  border-bottom: 2px solid var(--color-accent);
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.info-item {
  display: flex;
  gap: 16px;
  align-items: flex-start;
}

.info-icon {
  font-size: 24px;
  flex-shrink: 0;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-bg-gray);
  border-radius: 50%;
}

.info-item strong {
  font-size: 14px;
  color: var(--color-text);
  display: block;
  margin-bottom: 4px;
}

.info-item p {
  font-size: 14px;
  color: var(--color-text-secondary);
  line-height: 1.6;
}

.info-item a {
  color: var(--color-secondary);
  transition: color 0.3s;
}

.info-item a:hover {
  color: var(--color-primary);
}

.contact-form-section {
  flex: 1;
  min-width: 0;
}

.message-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-row {
  display: flex;
  gap: 20px;
}

.form-group {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.form-group label {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text);
  margin-bottom: 6px;
}

.required {
  color: #e74c3c;
}

.form-group input,
.form-group textarea {
  padding: 10px 14px;
  border: 1px solid var(--color-border);
  border-radius: 4px;
  font-size: 14px;
  color: var(--color-text);
  background: #ffffff;
  transition: border-color 0.3s;
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: var(--color-primary);
}

.form-group input.error,
.form-group textarea.error {
  border-color: #e74c3c;
}

.error-msg {
  font-size: 12px;
  color: #e74c3c;
  margin-top: 4px;
}

.form-group textarea {
  resize: vertical;
}

.submit-btn {
  align-self: flex-start;
  padding: 12px 40px;
  font-size: 15px;
}

.submit-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.success-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
}

.success-modal {
  background: #ffffff;
  border-radius: 12px;
  padding: 40px;
  text-align: center;
  max-width: 400px;
  width: 90%;
}

.success-icon {
  width: 60px;
  height: 60px;
  line-height: 60px;
  font-size: 32px;
  color: #ffffff;
  background: #27ae60;
  border-radius: 50%;
  margin: 0 auto 16px;
}

.success-modal h3 {
  font-size: 20px;
  font-weight: 700;
  color: var(--color-text);
  margin-bottom: 8px;
}

.success-modal p {
  font-size: 14px;
  color: var(--color-text-secondary);
  margin-bottom: 20px;
}

@media (max-width: 768px) {
  .contact-layout {
    flex-direction: column;
    gap: 30px;
  }

  .contact-info-section {
    width: 100%;
  }

  .form-row {
    flex-direction: column;
    gap: 16px;
  }
}
</style>
