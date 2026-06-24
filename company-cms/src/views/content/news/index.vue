<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="关键词">
          <el-input v-model="queryParams.keyword" placeholder="标题关键词" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="queryParams.categoryId" clearable placeholder="请选择" style="width: 160px;">
            <el-option v-for="cat in categoryOptions" :key="cat.id" :label="cat.categoryName" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" clearable placeholder="请选择" style="width: 120px;">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right;">
          <el-button type="primary" icon="Plus" @click="handleAdd" v-hasPermi="['content:news:add']">新增</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table v-loading="loading" :data="tableData" border stripe>
        <el-table-column label="封面图" width="100" align="center">
          <template #default="{ row }">
            <el-image v-if="row.coverImage" :src="getImageUrl(row.coverImage)" fit="cover" style="width: 60px; height: 60px;" :preview-src-list="[getImageUrl(row.coverImage)]" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="author" label="作者" width="100" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-switch v-model="row.status" :active-value="1" :inactive-value="0" @change="handleStatusChange(row)" />
          </template>
        </el-table-column>
        <el-table-column prop="isTop" label="置顶" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.isTop === 1 ? 'danger' : 'info'" size="small">
              {{ row.isTop === 1 ? '置顶' : '普通' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="80" align="center" />
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link icon="Edit" @click="handleEdit(row)" v-hasPermi="['content:news:edit']">编辑</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(row)" v-hasPermi="['content:news:delete']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        class="pagination"
        @size-change="loadData"
        @current-change="loadData"
      />
    </el-card>

    <!-- 新增/编辑大Dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="900px"
      top="5vh"
      destroy-on-close
    >
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%;">
                <el-option v-for="cat in categoryOptions" :key="cat.id" :label="cat.categoryName" :value="cat.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="封面图" prop="coverImage">
              <ImageUpload v-model="form.coverImage" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="作者" prop="author">
              <el-input v-model="form.author" placeholder="请输入作者" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="form.sort" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :value="1">启用</el-radio>
                <el-radio :value="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否置顶" prop="isTop">
              <el-switch v-model="form.isTop" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="摘要" prop="summary">
          <el-input v-model="form.summary" type="textarea" :rows="3" placeholder="请输入摘要" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <RichEditor v-model="form.content" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getNewsList, addNews, updateNews, deleteNews, updateNewsStatus } from '@/api/news'
import { getNewsCategoryAll } from '@/api/newsCategory'
import ImageUpload from '@/components/ImageUpload.vue'
import RichEditor from '@/components/RichEditor.vue'
import { getImageUrl } from '@/utils/image'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const formRef = ref(null)
const categoryOptions = ref([])

const queryParams = reactive({
  keyword: '',
  categoryId: undefined,
  status: undefined,
  pageNum: 1,
  pageSize: 10
})

const form = reactive({
  id: undefined,
  title: '',
  categoryId: undefined,
  coverImage: '',
  summary: '',
  content: '',
  author: '',
  sort: 0,
  status: 1,
  isTop: 0
})

const formRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }]
}

const dialogTitle = computed(() => form.id ? '编辑行业洞察' : '新增行业洞察')

onMounted(() => {
  loadData()
  loadCategories()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getNewsList(queryParams)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

async function loadCategories() {
  try {
    const res = await getNewsCategoryAll()
    categoryOptions.value = res.data || []
  } catch (e) {
    // ignore
  }
}

function handleSearch() {
  queryParams.pageNum = 1
  loadData()
}

function handleReset() {
  queryParams.keyword = ''
  queryParams.categoryId = undefined
  queryParams.status = undefined
  queryParams.pageNum = 1
  loadData()
}

function handleAdd() {
  resetForm()
  dialogVisible.value = true
}

function handleEdit(row) {
  resetForm()
  Object.assign(form, {
    id: row.id,
    title: row.title,
    categoryId: row.categoryId,
    coverImage: row.coverImage,
    summary: row.summary,
    content: row.content,
    author: row.author,
    sort: row.orderNum,
    status: row.status,
    isTop: row.isTop
  })
  dialogVisible.value = true
}

async function handleStatusChange(row) {
  try {
    await updateNewsStatus(row.id, row.status)
    ElMessage.success('状态更新成功')
  } catch (e) {
    row.status = row.status === 1 ? 0 : 1
  }
}

async function handleDelete(row) {
  await ElMessageBox.confirm(`确定要删除行业洞察「${row.title}」吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  await deleteNews(row.id)
  ElMessage.success('删除成功')
  loadData()
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    if (form.id) {
      await updateNews(form)
      ElMessage.success('更新成功')
    } else {
      await addNews(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    loadData()
  } finally {
    submitLoading.value = false
  }
}

function resetForm() {
  Object.assign(form, {
    id: undefined,
    title: '',
    categoryId: undefined,
    coverImage: '',
    summary: '',
    content: '',
    author: '',
    sort: 0,
    status: 1,
    isTop: 0
  })
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
