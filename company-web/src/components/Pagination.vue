<template>
  <div v-if="total > 0" class="pagination">
    <button class="page-btn" :disabled="currentPage <= 1" @click="changePage(currentPage - 1)">
      &laquo; 上一页
    </button>
    <button
      v-for="page in pageNumbers"
      :key="page"
      class="page-btn"
      :class="{ active: page === currentPage, ellipsis: page === '...' }"
      :disabled="page === '...'"
      @click="page !== '...' && changePage(page)"
    >
      {{ page }}
    </button>
    <button class="page-btn" :disabled="currentPage >= totalPages" @click="changePage(currentPage + 1)">
      下一页 &raquo;
    </button>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  total: {
    type: Number,
    default: 0
  },
  currentPage: {
    type: Number,
    default: 1
  },
  pageSize: {
    type: Number,
    default: 10
  }
})

const emit = defineEmits(['update:currentPage'])

const totalPages = computed(() => Math.ceil(props.total / props.pageSize))

const pageNumbers = computed(() => {
  const total = totalPages.value
  const current = props.currentPage
  const pages = []

  if (total <= 7) {
    for (let i = 1; i <= total; i++) pages.push(i)
    return pages
  }

  pages.push(1)
  if (current > 3) pages.push('...')

  const start = Math.max(2, current - 1)
  const end = Math.min(total - 1, current + 1)

  for (let i = start; i <= end; i++) pages.push(i)

  if (current < total - 2) pages.push('...')
  pages.push(total)

  return pages
})

function changePage(page) {
  if (page < 1 || page > totalPages.value) return
  emit('update:currentPage', page)
}
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
  padding: 30px 0;
}

.page-btn {
  min-width: 36px;
  height: 36px;
  padding: 0 10px;
  border: 1px solid var(--color-border);
  background: #ffffff;
  color: var(--color-text);
  font-size: 14px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.page-btn:hover:not(:disabled):not(.active) {
  border-color: var(--color-primary);
  color: var(--color-primary);
}

.page-btn.active {
  background-color: var(--color-primary);
  border-color: var(--color-primary);
  color: #ffffff;
}

.page-btn:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

.page-btn.ellipsis {
  border: none;
  cursor: default;
  background: transparent;
}

@media (max-width: 768px) {
  .pagination {
    flex-wrap: wrap;
  }
}
</style>
