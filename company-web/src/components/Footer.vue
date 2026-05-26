<template>
  <footer class="site-footer">
    <div class="footer-main container">
      <div class="footer-col">
        <h3 class="footer-col-title">{{ siteConfig.siteName || '企业官网' }}</h3>
        <p class="footer-desc">{{ siteConfig.siteDesc || '致力于为客户提供优质的产品和服务' }}</p>
      </div>
      <div class="footer-col">
        <h3 class="footer-col-title">快速导航</h3>
        <nav class="footer-nav">
          <router-link to="/">网站首页</router-link>
          <router-link to="/about">关于我们</router-link>
          <router-link to="/products">产品中心</router-link>
          <router-link to="/news">新闻中心</router-link>
          <router-link to="/business">业务领域</router-link>
          <router-link to="/contact">联系我们</router-link>
        </nav>
      </div>
      <div class="footer-col">
        <h3 class="footer-col-title">联系方式</h3>
        <div class="footer-contact">
          <p v-if="contactInfo.address"><strong>地址：</strong>{{ contactInfo.address }}</p>
          <p v-if="contactInfo.phone"><strong>电话：</strong>{{ contactInfo.phone }}</p>
          <p v-if="contactInfo.email"><strong>邮箱：</strong>{{ contactInfo.email }}</p>
          <p v-if="contactInfo.fax"><strong>传真：</strong>{{ contactInfo.fax }}</p>
        </div>
      </div>
    </div>
    <div class="footer-bottom">
      <div class="container">
        <p>{{ siteConfig.copyright || 'Copyright © 企业官网 All Rights Reserved.' }}
          <span v-if="siteConfig.icp" style="margin-left: 10px;">{{ siteConfig.icp }}</span>
        </p>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getSiteConfig } from '../api/home'
import { getContactInfo } from '../api/contact'

const siteConfig = ref({})
const contactInfo = ref({})

onMounted(async () => {
  try {
    const [config, contact] = await Promise.allSettled([
      getSiteConfig(),
      getContactInfo()
    ])
    if (config.status === 'fulfilled') siteConfig.value = config.value
    if (contact.status === 'fulfilled') contactInfo.value = contact.value
  } catch (e) {
    console.warn('加载底部信息失败:', e)
  }
})
</script>

<style scoped>
.site-footer {
  background-color: var(--color-primary);
  color: #ffffff;
}

.footer-main {
  display: flex;
  justify-content: space-between;
  padding: 50px 20px 40px;
  gap: 40px;
}

.footer-col {
  flex: 1;
  min-width: 0;
}

.footer-col-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
  position: relative;
  padding-bottom: 12px;
}

.footer-col-title::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 40px;
  height: 2px;
  background: var(--color-accent);
}

.footer-desc {
  font-size: 14px;
  line-height: 1.8;
  opacity: 0.8;
}

.footer-nav {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.footer-nav a {
  font-size: 14px;
  opacity: 0.8;
  transition: opacity 0.3s, padding-left 0.3s;
}

.footer-nav a:hover {
  opacity: 1;
  padding-left: 4px;
}

.footer-contact p {
  font-size: 14px;
  line-height: 2;
  opacity: 0.8;
}

.footer-contact strong {
  opacity: 1;
}

.footer-bottom {
  border-top: 1px solid rgba(255, 255, 255, 0.15);
  padding: 16px 0;
  text-align: center;
  font-size: 13px;
  opacity: 0.7;
}

@media (max-width: 768px) {
  .footer-main {
    flex-direction: column;
    gap: 30px;
    padding: 30px 20px;
  }
}
</style>
