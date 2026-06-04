<template>
  <div class="about-page">
    <div class="page-banner">
      <h1>关于我们</h1>
      <p>ABOUT US</p>
    </div>

    <div class="container">
      <div class="about-tabs">
        <button
          v-for="tab in tabs"
          :key="tab.key"
          class="tab-btn"
          :class="{ active: activeTab === tab.key }"
          @click="switchTab(tab.key)"
        >
          {{ tab.name }}
        </button>
      </div>

      <div v-if="loading" class="loading-wrap">
        <div class="loading-spinner"></div>
        <span>加载中...</span>
      </div>
      <div v-else-if="tabContent" class="about-content" v-html="tabContent"></div>
      <div v-else class="empty-state">
        <div class="empty-icon">📄</div>
        <p>暂无相关内容</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getSiteConfig } from '../api/home'

const tabs = [
  { key: 'about', name: '公司简介' },
  { key: 'culture', name: '企业文化' },
  { key: 'honor', name: '资质荣誉' },
  { key: 'vision', name: '公司愿景' }
]

const activeTab = ref('about')
const tabContent = ref('')
const loading = ref(false)
const configData = ref({})

async function loadConfig() {
  loading.value = true
  try {
    configData.value = await getSiteConfig()
    updateTabContent()
  } catch (e) {
    console.error('加载配置失败:', e)
  } finally {
    loading.value = false
  }
}

function decodeHtml(html) {
  if (!html) return ''
  return html
    .replace(/&lt;/g, '<')
    .replace(/&gt;/g, '>')
    .replace(/&amp;/g, '&')
    .replace(/&quot;/g, '"')
    .replace(/&#39;/g, "'")
}

function updateTabContent() {
  const keyMap = {
    about: 'aboutContent',
    culture: 'cultureContent',
    honor: 'honorContent',
    vision: 'visionContent'
  }
  const field = keyMap[activeTab.value]
  tabContent.value = decodeHtml(configData.value[field] || '')
}

function switchTab(key) {
  activeTab.value = key
  updateTabContent()
}

onMounted(() => {
  loadConfig()
})
</script>

<style scoped>
.about-tabs {
  display: flex;
  justify-content: center;
  gap: 8px;
  padding: 40px 0 30px;
  border-bottom: 1px solid var(--color-border);
  margin-bottom: 30px;
}

.tab-btn {
  padding: 10px 28px;
  font-size: 15px;
  color: var(--color-text-secondary);
  background: transparent;
  border: 1px solid transparent;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.tab-btn:hover {
  color: var(--color-primary);
}

.tab-btn.active {
  color: #ffffff;
  background: var(--color-primary);
  border-color: var(--color-primary);
}

.about-content {
  padding: 20px 0 60px;
  line-height: 1.8;
  font-size: 15px;
  color: var(--color-text);
}

.about-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 4px;
  margin: 16px 0;
}

.about-content :deep(p) {
  margin-bottom: 12px;
}

.about-content :deep(h1),
.about-content :deep(h2),
.about-content :deep(h3) {
  font-weight: 600;
  color: var(--color-primary);
  margin: 24px 0 12px;
}

.about-content :deep(ul),
.about-content :deep(ol) {
  padding-left: 20px;
  margin-bottom: 12px;
}

.about-content :deep(li) {
  margin-bottom: 6px;
}
</style>
