<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="阅读状态">
          <el-select v-model="queryParams.readStatus" clearable placeholder="请选择" style="width: 140px;">
            <el-option label="未读" :value="0" />
            <el-option label="已读" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table v-loading="loading" :data="tableData" border stripe>
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="phone" label="电话" width="140" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="content" label="内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="readStatus" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.readStatus === 1 ? 'success' : 'warning'" size="small">
              {{ row.readStatus === 1 ? '已读' : '未读' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="170" />
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link icon="View" @click="handleView(row)">查看</el-button>
            <el-button
              v-if="row.readStatus === 0"
              type="warning"
              link
              icon="Check"
              @click="handleMarkRead(row)"
              v-hasPermi="['content:message:edit']"
            >标记已读</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(row)" v-hasPermi="['content:message:delete']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="queryParams.current"
        v-model:page-size="queryParams.size"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        class="pagination"
        @size-change="loadData"
        @current-change="loadData"
      />
    </el-card>

    <!-- 查看Dialog -->
    <el-dialog
      v-model="viewDialogVisible"
      title="留言详情"
      width="600px"
      destroy-on-close
    >
      <el-descriptions :column="1" border>
        <el-descriptions-item label="姓名">{{ viewData.name }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ viewData.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ viewData.email }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ viewData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="阅读状态">
          <el-tag :type="viewData.readStatus === 1 ? 'success' : 'warning'">
            {{ viewData.readStatus === 1 ? '已读' : '未读' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="留言内容">
          <div style="white-space: pre-wrap;">{{ viewData.content }}</div>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="viewDialogVisible = false">关闭</el-button>
        <el-button
          v-if="viewData.readStatus === 0"
          type="primary"
          @click="handleMarkRead(viewData)"
        >标记已读</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMessageList, getMessageDetail, markMessageRead, deleteMessage } from '@/api/message'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const viewDialogVisible = ref(false)
const viewData = ref({})

const queryParams = reactive({
  readStatus: undefined,
  current: 1,
  size: 10
})

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getMessageList(queryParams)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  queryParams.current = 1
  loadData()
}

function handleReset() {
  queryParams.readStatus = undefined
  queryParams.current = 1
  loadData()
}

async function handleView(row) {
  try {
    const res = await getMessageDetail(row.id)
    viewData.value = res.data || row
    viewDialogVisible.value = true
  } catch (e) {
    viewData.value = { ...row }
    viewDialogVisible.value = true
  }
}

async function handleMarkRead(row) {
  await markMessageRead(row.id)
  ElMessage.success('已标记为已读')
  row.readStatus = 1
  if (viewDialogVisible.value) {
    viewData.value.readStatus = 1
  }
  loadData()
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定要删除该留言吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  await deleteMessage(row.id)
  ElMessage.success('删除成功')
  loadData()
}
</script>

<style scoped>
.app-container {
  padding: 0;
}
.search-card {
  margin-bottom: 16px;
}
.pagination {
  margin-top: 16px;
  justify-content: flex-end;
}
</style>
