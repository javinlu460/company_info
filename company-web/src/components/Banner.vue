<template>
  <div class="banner-carousel" @mouseenter="pause" @mouseleave="resume">
    <div class="banner-track" :style="trackStyle">
      <div
        v-for="(item, index) in banners"
        :key="index"
        class="banner-slide"
      >
        <img :src="getImageUrl(item.imageUrl)" :alt="item.title || ''" />
        <div v-if="item.title" class="banner-caption">
          <h2>{{ item.title }}</h2>
          <p v-if="item.description">{{ item.description }}</p>
        </div>
      </div>
    </div>
    <div v-if="banners.length > 1" class="banner-dots">
      <span
        v-for="(_, index) in banners"
        :key="index"
        class="dot"
        :class="{ active: currentIndex === index }"
        @click="goTo(index)"
      ></span>
    </div>
    <button v-if="banners.length > 1" class="banner-btn prev" @click="prev">&#8249;</button>
    <button v-if="banners.length > 1" class="banner-btn next" @click="next">&#8250;</button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'

const props = defineProps({
  banners: {
    type: Array,
    default: () => []
  },
  interval: {
    type: Number,
    default: 4000
  }
})

const currentIndex = ref(0)
let timer = null

const trackStyle = computed(() => ({
  transform: `translateX(-${currentIndex.value * 100}%)`,
  transition: 'transform 0.6s ease'
}))

function getImageUrl(url) {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return 'http://localhost:8080' + url
}

function next() {
  currentIndex.value = (currentIndex.value + 1) % props.banners.length
}

function prev() {
  currentIndex.value = (currentIndex.value - 1 + props.banners.length) % props.banners.length
}

function goTo(index) {
  currentIndex.value = index
}

function startAutoPlay() {
  if (props.banners.length <= 1) return
  stopAutoPlay()
  timer = setInterval(next, props.interval)
}

function stopAutoPlay() {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
}

function pause() {
  stopAutoPlay()
}

function resume() {
  startAutoPlay()
}

watch(() => props.banners, () => {
  currentIndex.value = 0
  startAutoPlay()
})

onMounted(() => {
  startAutoPlay()
})

onUnmounted(() => {
  stopAutoPlay()
})
</script>

<style scoped>
.banner-carousel {
  position: relative;
  width: 100%;
  overflow: hidden;
}

.banner-track {
  display: flex;
  will-change: transform;
}

.banner-slide {
  min-width: 100%;
  position: relative;
}

.banner-slide img {
  width: 100%;
  height: 500px;
  object-fit: cover;
}

.banner-caption {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 40px;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.6));
  color: #ffffff;
}

.banner-caption h2 {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 8px;
}

.banner-caption p {
  font-size: 16px;
  opacity: 0.9;
}

.banner-dots {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 10px;
  z-index: 2;
}

.dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.5);
  cursor: pointer;
  transition: background 0.3s, transform 0.3s;
}

.dot.active {
  background: #ffffff;
  transform: scale(1.2);
}

.banner-btn {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.3);
  color: #ffffff;
  font-size: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.3s;
  z-index: 2;
  border: none;
}

.banner-btn:hover {
  background: rgba(255, 255, 255, 0.6);
}

.banner-btn.prev {
  left: 20px;
}

.banner-btn.next {
  right: 20px;
}

@media (max-width: 768px) {
  .banner-slide img {
    height: 250px;
  }

  .banner-caption {
    padding: 20px;
  }

  .banner-caption h2 {
    font-size: 20px;
  }

  .banner-caption p {
    font-size: 14px;
  }

  .banner-btn {
    width: 36px;
    height: 36px;
    font-size: 20px;
  }
}
</style>
