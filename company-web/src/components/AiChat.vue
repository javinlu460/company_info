<template>
  <!-- 浮动按钮 -->
  <button
    v-show="!isOpen"
    class="ai-chat-fab"
    @click="togglePanel"
    aria-label="打开智能客服"
  >
    <svg viewBox="0 0 24 24" width="28" height="28" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      <path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z" />
    </svg>
    <span class="ai-chat-fab__badge"></span>
  </button>

  <!-- 聊天面板 -->
  <Transition name="scale-panel">
    <div v-if="isOpen" class="ai-chat-panel">
      <!-- 头部 -->
      <header class="ai-chat-header">
        <div class="ai-chat-header__info">
          <div class="ai-chat-header__avatar">
            <svg viewBox="0 0 24 24" width="22" height="22" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <rect x="3" y="11" width="18" height="10" rx="2" />
              <circle cx="12" cy="5" r="2" />
              <path d="M12 7v4" />
              <line x1="8" y1="16" x2="8" y2="16" />
              <line x1="16" y1="16" x2="16" y2="16" />
            </svg>
          </div>
          <div class="ai-chat-header__text">
            <h3 class="ai-chat-header__title">智能客服</h3>
            <div class="ai-chat-header__status">
              <span class="ai-chat-header__dot"></span>
              <span>在线</span>
            </div>
          </div>
        </div>
        <button class="ai-chat-header__close" @click="togglePanel" aria-label="关闭">
          <svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="18" y1="6" x2="6" y2="18" />
            <line x1="6" y1="6" x2="18" y2="18" />
          </svg>
        </button>
      </header>

      <!-- 消息区域 -->
      <div class="ai-chat-body" ref="bodyRef">
        <!-- 欢迎消息 -->
        <div class="ai-chat-msg ai-chat-msg--ai animate-fadeInUp">
          <div class="ai-chat-msg__avatar">
            <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <rect x="3" y="11" width="18" height="10" rx="2" />
              <circle cx="12" cy="5" r="2" />
              <path d="M12 7v4" />
            </svg>
          </div>
          <div class="ai-chat-msg__bubble">
            您好！我是智能客服助手，有什么可以帮您的吗？
          </div>
        </div>

        <!-- 预设问题快捷按钮 -->
        <div class="ai-chat-quick" v-if="showQuickActions">
          <button
            v-for="q in quickQuestions"
            :key="q"
            class="ai-chat-quick__btn"
            @click="sendQuickMessage(q)"
          >
            {{ q }}
          </button>
        </div>

        <!-- 对话消息列表 -->
        <div
          v-for="(msg, index) in messages"
          :key="index"
          class="ai-chat-msg animate-fadeInUp"
          :class="msg.role === 'user' ? 'ai-chat-msg--user' : 'ai-chat-msg--ai'"
        >
          <div class="ai-chat-msg__avatar" v-if="msg.role === 'ai'">
            <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <rect x="3" y="11" width="18" height="10" rx="2" />
              <circle cx="12" cy="5" r="2" />
              <path d="M12 7v4" />
            </svg>
          </div>
          <div class="ai-chat-msg__bubble">{{ msg.content }}</div>
        </div>

        <!-- 正在输入指示器 -->
        <div v-if="isTyping" class="ai-chat-msg ai-chat-msg--ai">
          <div class="ai-chat-msg__avatar">
            <svg viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <rect x="3" y="11" width="18" height="10" rx="2" />
              <circle cx="12" cy="5" r="2" />
              <path d="M12 7v4" />
            </svg>
          </div>
          <div class="ai-chat-typing">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
      </div>

      <!-- 输入区域 -->
      <footer class="ai-chat-footer">
        <input
          v-model="inputText"
          type="text"
          class="ai-chat-footer__input"
          placeholder="输入您的问题..."
          @keyup.enter="sendMessage"
        />
        <button class="ai-chat-footer__send" @click="sendMessage" :disabled="!inputText.trim()" aria-label="发送">
          <svg viewBox="0 0 24 24" width="20" height="20" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="22" y1="2" x2="11" y2="13" />
            <polygon points="22 2 15 22 11 13 2 9 22 2" />
          </svg>
        </button>
      </footer>
    </div>
  </Transition>
</template>

<script setup>
import { ref, nextTick, onMounted, onUnmounted } from 'vue'

const isOpen = ref(false)
const inputText = ref('')
const messages = ref([])
const isTyping = ref(false)
const showQuickActions = ref(true)
const bodyRef = ref(null)

const quickQuestions = ['产品咨询', '获取报价', '售后服务', '合作洽谈']

const replies = {
  '产品咨询': '感谢您的咨询！我们提供多种工业机械产品，您可以访问产品中心了解详情，或拨打热线电话获取专业建议。',
  '获取报价': '请您填写联系我们页面的报价表单，我们的销售团队将在24小时内为您提供详细报价。',
  '售后服务': '我们提供完善的售后服务，包括设备安装、调试、培训及维修保养。请拨打售后热线或留言，我们将尽快处理。',
  '合作洽谈': '欢迎合作！请通过联系我们页面留下您的信息，我们的商务团队将与您联系。'
}

const defaultReply = '感谢您的留言，我们的客服人员将尽快为您解答。您也可以拨打热线电话获取即时帮助。'

function togglePanel() {
  isOpen.value = !isOpen.value
}

function scrollToBottom() {
  nextTick(() => {
    if (bodyRef.value) {
      bodyRef.value.scrollTop = bodyRef.value.scrollHeight
    }
  })
}

function sendMessage() {
  const text = inputText.value.trim()
  if (!text) return

  messages.value.push({ role: 'user', content: text })
  showQuickActions.value = false
  inputText.value = ''
  scrollToBottom()

  // 模拟AI回复
  isTyping.value = true
  scrollToBottom()

  setTimeout(() => {
    isTyping.value = false
    const reply = replies[text] || defaultReply
    messages.value.push({ role: 'ai', content: reply })
    scrollToBottom()
  }, 500)
}

function sendQuickMessage(question) {
  messages.value.push({ role: 'user', content: question })
  showQuickActions.value = false
  scrollToBottom()

  isTyping.value = true
  scrollToBottom()

  setTimeout(() => {
    isTyping.value = false
    const reply = replies[question] || defaultReply
    messages.value.push({ role: 'ai', content: reply })
    scrollToBottom()
  }, 500)
}

function handleResize() {
  // 响应式时保持滚动到底部
  scrollToBottom()
}

onMounted(() => {
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
/* ==================== 浮动按钮 ==================== */
.ai-chat-fab {
  position: fixed;
  bottom: 24px;
  right: 24px;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: var(--red);
  color: var(--white);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: var(--shadow-lg);
  z-index: 9999;
  transition: transform var(--transition-base), box-shadow var(--transition-base);
  animation: pulse 2s ease-in-out infinite;
}

.ai-chat-fab:hover {
  transform: scale(1.1);
  box-shadow: 0 12px 40px rgba(178, 43, 43, 0.4);
}

.ai-chat-fab:active {
  transform: scale(0.95);
}

.ai-chat-fab__badge {
  position: absolute;
  top: 4px;
  right: 4px;
  width: 12px;
  height: 12px;
  background: #4ade80;
  border: 2px solid var(--red);
  border-radius: 50%;
}

@keyframes pulse {
  0%, 100% {
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.16), 0 0 0 0 rgba(178, 43, 43, 0.4);
  }
  50% {
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.16), 0 0 0 12px rgba(178, 43, 43, 0);
  }
}

/* ==================== 面板 ==================== */
.ai-chat-panel {
  position: fixed;
  bottom: 24px;
  right: 24px;
  width: 380px;
  height: 500px;
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-xl);
  z-index: 9999;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  transform-origin: bottom right;
}

/* 面板展开动画 */
.scale-panel-enter-active {
  transition: transform 0.3s ease, opacity 0.3s ease;
}
.scale-panel-leave-active {
  transition: transform 0.25s ease, opacity 0.25s ease;
}
.scale-panel-enter-from {
  transform: scale(0);
  opacity: 0;
}
.scale-panel-leave-to {
  transform: scale(0);
  opacity: 0;
}

/* ==================== 头部 ==================== */
.ai-chat-header {
  background: var(--graphite);
  padding: 16px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-shrink: 0;
}

.ai-chat-header__info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.ai-chat-header__avatar {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: var(--red);
  color: var(--white);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.ai-chat-header__title {
  font-size: 16px;
  font-weight: 600;
  color: var(--paper);
  margin-bottom: 2px;
}

.ai-chat-header__status {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 12px;
  color: rgba(242, 243, 239, 0.7);
}

.ai-chat-header__dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #4ade80;
  box-shadow: 0 0 6px rgba(74, 222, 128, 0.6);
}

.ai-chat-header__close {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  color: rgba(242, 243, 239, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all var(--transition-fast);
}

.ai-chat-header__close:hover {
  background: rgba(255, 255, 255, 0.1);
  color: var(--paper);
}

/* ==================== 消息区域 ==================== */
.ai-chat-body {
  flex: 1;
  overflow-y: auto;
  padding: 20px 16px;
  background: var(--paper);
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.ai-chat-body::-webkit-scrollbar {
  width: 5px;
}

.ai-chat-body::-webkit-scrollbar-track {
  background: transparent;
}

.ai-chat-body::-webkit-scrollbar-thumb {
  background: var(--gray-400);
  border-radius: 3px;
}

/* ==================== 消息气泡 ==================== */
.ai-chat-msg {
  display: flex;
  align-items: flex-end;
  gap: 8px;
  max-width: 100%;
}

.ai-chat-msg--ai {
  justify-content: flex-start;
}

.ai-chat-msg--user {
  justify-content: flex-end;
}

.ai-chat-msg__avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: var(--graphite);
  color: var(--paper);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.ai-chat-msg__bubble {
  padding: 10px 14px;
  font-size: 14px;
  line-height: 1.5;
  max-width: 75%;
  word-break: break-word;
}

.ai-chat-msg--ai .ai-chat-msg__bubble {
  background: var(--white);
  color: var(--ink);
  border-radius: 4px var(--radius-md) var(--radius-md) var(--radius-md);
  box-shadow: var(--shadow-sm);
}

.ai-chat-msg--user .ai-chat-msg__bubble {
  background: var(--red);
  color: var(--white);
  border-radius: var(--radius-md) 4px var(--radius-md) var(--radius-md);
}

/* ==================== 预设问题快捷按钮 ==================== */
.ai-chat-quick {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
  padding: 4px 0;
}

.ai-chat-quick__btn {
  padding: 8px 12px;
  font-size: 13px;
  background: var(--white);
  color: var(--ink);
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
  text-align: center;
}

.ai-chat-quick__btn:hover {
  background: var(--red);
  color: var(--white);
  border-color: var(--red);
}

/* ==================== 正在输入指示器 ==================== */
.ai-chat-typing {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 12px 16px;
  background: var(--white);
  border-radius: 4px var(--radius-md) var(--radius-md) var(--radius-md);
  box-shadow: var(--shadow-sm);
}

.ai-chat-typing span {
  width: 7px;
  height: 7px;
  background: var(--gray-400);
  border-radius: 50%;
  animation: typing-bounce 1.4s infinite ease-in-out;
}

.ai-chat-typing span:nth-child(2) {
  animation-delay: 0.2s;
}

.ai-chat-typing span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing-bounce {
  0%, 80%, 100% {
    transform: scale(0.6);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

/* ==================== 输入区域 ==================== */
.ai-chat-footer {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: var(--white);
  border-top: 1px solid var(--gray-200);
  flex-shrink: 0;
}

.ai-chat-footer__input {
  flex: 1;
  height: 40px;
  padding: 0 14px;
  font-size: 14px;
  background: var(--paper);
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-md);
  transition: border-color var(--transition-fast);
}

.ai-chat-footer__input:focus {
  border-color: var(--red);
}

.ai-chat-footer__send {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-md);
  background: var(--red);
  color: var(--white);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all var(--transition-fast);
  flex-shrink: 0;
}

.ai-chat-footer__send:hover:not(:disabled) {
  background: #8f2222;
}

.ai-chat-footer__send:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* ==================== 响应式 ==================== */
@media (max-width: 768px) {
  .ai-chat-fab {
    bottom: 16px;
    right: 16px;
  }

  .ai-chat-panel {
    position: fixed;
    inset: 0;
    width: 100%;
    height: 100%;
    border-radius: 0;
  }

  .ai-chat-quick {
    grid-template-columns: 1fr 1fr;
  }
}
</style>
