<template>
  <div class="home-page">
    <!-- Banner轮播区 -->
    <section class="home-banner">
      <Banner :banners="homeData.banners || []" :interval="4000" />
    </section>

    <!-- 产品中心区 -->
    <section class="home-products">
      <div class="container">
        <div class="section-title">
          <h2>产品中心</h2>
          <p>Professional Products & Services</p>
          <div class="title-line"></div>
        </div>
        <div v-if="loading" class="loading-wrap">
          <div class="loading-spinner"></div>
          <span>加载中...</span>
        </div>
        <div v-else-if="homeData.products && homeData.products.length" class="products-grid">
          <ProductCard
            v-for="product in homeData.products.slice(0, 8)"
            :key="product.id"
            :product="product"
          />
        </div>
        <div v-else class="empty-state">
          <div class="empty-icon">📦</div>
          <p>暂无产品信息</p>
        </div>
        <div class="section-more" v-if="homeData.products && homeData.products.length">
          <router-link to="/products" class="btn-outline">查看更多</router-link>
        </div>
      </div>
    </section>

    <!-- 关于我们区 -->
    <section class="home-about">
      <div class="container">
        <div class="about-inner">
          <div class="about-text">
            <h2>{{ homeData.config?.siteName || '关于我们' }}</h2>
            <div class="about-line"></div>
            <div class="about-desc" v-html="decodeHtml(homeData.config?.siteDesc)"></div>
            <p v-if="!homeData.config?.siteDesc" class="about-desc">我们是一家专业的企业，致力于为客户提供优质的产品和服务，以创新驱动发展，以品质赢得信赖。</p>
            <router-link to="/about" class="btn-primary">了解更多</router-link>
          </div>
          <div class="about-stats">
            <div class="stat-item">
              <span class="stat-num">{{ homeData.config?.establishYear || '20' }}</span>
              <span class="stat-unit">年+</span>
              <span class="stat-label">成立年限</span>
            </div>
            <div class="stat-item">
              <span class="stat-num">{{ homeData.config?.registeredCapital || '5000' }}</span>
              <span class="stat-unit">万</span>
              <span class="stat-label">注册资金</span>
            </div>
            <div class="stat-item">
              <span class="stat-num">{{ homeData.config?.agentCount || '100' }}</span>
              <span class="stat-unit">+</span>
              <span class="stat-label">合作伙伴</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 新闻动态区 -->
    <section class="home-news">
      <div class="container">
        <div class="section-title">
          <h2>新闻动态</h2>
          <p>Latest News & Updates</p>
          <div class="title-line"></div>
        </div>
        <div v-if="homeData.news && homeData.news.length" class="news-list">
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
        <div class="section-more" v-if="homeData.news && homeData.news.length">
          <router-link to="/news" class="btn-outline">查看更多</router-link>
        </div>
      </div>
    </section>

    <!-- 在线咨询区 -->
    <section class="home-consult">
      <div class="container">
        <div class="consult-inner">
          <div class="consult-text">
            <h2>需要帮助？立即联系我们</h2>
            <p>专业团队为您提供全方位的咨询服务</p>
          </div>
          <div class="consult-action">
            <a v-if="homeData.config?.phone" :href="'tel:' + homeData.config.phone" class="consult-phone">
              📞 {{ homeData.config.phone }}
            </a>
            <router-link to="/contact" class="btn-accent">在线留言</router-link>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getHomeData } from '../api/home'
import Banner from '../components/Banner.vue'
import ProductCard from '../components/ProductCard.vue'
import NewsItem from '../components/NewsItem.vue'

const homeData = ref({})
const loading = ref(true)

function decodeHtml(html) {
  if (!html) return ''
  // 解码HTML实体
  let text = html
    .replace(/&lt;/g, '<')
    .replace(/&gt;/g, '>')
    .replace(/&amp;/g, '&')
    .replace(/&quot;/g, '"')
    .replace(/&#39;/g, "'")
  return text
}

onMounted(async () => {
  try {
    homeData.value = await getHomeData()
  } catch (e) {
    console.error('加载首页数据失败:', e)
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.home-banner {
  margin-top: var(--header-height);
}

.home-products {
  padding: 70px 0;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.section-more {
  text-align: center;
  margin-top: 30px;
}

.home-about {
  padding: 70px 0;
  background: var(--color-bg-gray);
}

.about-inner {
  display: flex;
  gap: 60px;
  align-items: center;
}

.about-text {
  flex: 1;
}

.about-text h2 {
  font-size: 30px;
  font-weight: 700;
  color: var(--color-primary);
  margin-bottom: 16px;
}

.about-line {
  width: 60px;
  height: 3px;
  background: var(--color-accent);
  margin-bottom: 20px;
}

.about-desc {
  font-size: 15px;
  color: var(--color-text-secondary);
  line-height: 1.8;
  margin-bottom: 24px;
}

.about-stats {
  display: flex;
  gap: 30px;
  flex-shrink: 0;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24px 30px;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);
  min-width: 140px;
}

.stat-num {
  font-size: 36px;
  font-weight: 700;
  color: var(--color-primary);
  line-height: 1;
}

.stat-unit {
  font-size: 16px;
  color: var(--color-accent);
  margin-top: 4px;
}

.stat-label {
  font-size: 13px;
  color: var(--color-text-secondary);
  margin-top: 8px;
}

.home-news {
  padding: 70px 0;
  background: var(--color-bg-gray);
}

.home-consult {
  padding: 60px 0;
  background: var(--color-primary);
  color: #ffffff;
}

.consult-inner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 40px;
}

.consult-text h2 {
  font-size: 26px;
  font-weight: 600;
  margin-bottom: 8px;
}

.consult-text p {
  font-size: 15px;
  opacity: 0.85;
}

.consult-action {
  display: flex;
  align-items: center;
  gap: 20px;
  flex-shrink: 0;
}

.consult-phone {
  font-size: 22px;
  font-weight: 700;
  color: #ffffff;
  text-decoration: none;
}

@media (max-width: 992px) {
  .products-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .about-inner {
    flex-direction: column;
    gap: 40px;
  }

  .about-stats {
    width: 100%;
    justify-content: center;
  }
}

@media (max-width: 768px) {
  .products-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }

  .home-products,
  .home-about,
  .home-news {
    padding: 50px 0;
  }

  .consult-inner {
    flex-direction: column;
    text-align: center;
  }

  .consult-action {
    flex-direction: column;
  }

  .about-stats {
    gap: 16px;
  }

  .stat-item {
    padding: 16px 20px;
    min-width: 100px;
  }

  .stat-num {
    font-size: 28px;
  }
}
</style>
