<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="关键词">
          <el-input v-model="queryParams.keyword" placeholder="问题关键词" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" clearable placeholder="请选择" style="width: 120px;">
            <el-option label="显示" :value="1" />
            <el-option label="隐藏" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right;">
          <el-button type="primary" icon="Plus" @click="handleAdd" v-hasPermi="['content:faq:add']">新增</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table v-loading="loading" :data="tableData" border stripe>
        <el-table-column prop="question" label="问题" min-width="240" show-overflow-tooltip />
        <el-table-column prop="answer" label="答案" min-width="300" show-overflow-tooltip />
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="orderNum" label="排序" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-switch v-model="row.status" :active-value="1" :inactive-value="0" @change="handleStatusChange(row)" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link icon="Edit" @click="handleEdit(row)" v-hasPermi="['content:faq:edit']">编辑</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(row)" v-hasPermi="['content:faq:delete']">删除</el-button>
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

    <!-- 新增/编辑Dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      top="5vh"
      destroy-on-close
    >
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="问题" prop="question">
          <el-input v-model="form.question" placeholder="请输入问题" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="分类" prop="category">
              <el-select v-model="form.category" clearable allow-create filterable default-first-option placeholder="请选择或输入分类" style="width: 100%;">
                <el-option v-for="cat in categoryOptions" :key="cat" :label="cat" :value="cat" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="form.sort" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">显示</el-radio>
            <el-radio :value="0">隐藏</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="答案" prop="answer">
          <el-input v-model="form.answer" type="textarea" :rows="6" placeholder="请输入答案" />
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
import { getFaqList, addFaq, updateFaq, deleteFaq } from '@/api/faq'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const formRef = ref(null)
const categoryOptions = ref([])

const queryParams = reactive({
  keyword: '',
  status: undefined,
  pageNum: 1,
  pageSize: 10
})

const form = reactive({
  id: undefined,
  question: '',
  answer: '',
  category: '',
  sort: 0,
  status: 1
})

const formRules = {
  question: [{ required: true, message: '请输入问题', trigger: 'blur' }],
  answer: [{ required: true, message: '请输入答案', trigger: 'blur' }]
}

const dialogTitle = computed(() => form.id ? '编辑常见问题' : '新增常见问题')

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getFaqList(queryParams)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
    extractCategories(tableData.value)
  } finally {
    loading.value = false
  }
}

function extractCategories(list) {
  const set = new Set()
  list.forEach(item => {
    if (item.category) set.add(item.category)
  })
  categoryOptions.value = Array.from(set)
}

function handleSearch() {
  queryParams.pageNum = 1
  loadData()
}

function handleReset() {
  queryParams.keyword = ''
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
    question: row.question,
    answer: row.answer,
    category: row.category,
    sort: row.orderNum,
    status: row.status
  })
  dialogVisible.value = true
}

async function handleStatusChange(row) {
  const dto = {
    id: row.id,
    question: row.question,
    answer: row.answer,
    category: row.category,
    sort: row.orderNum,
    status: row.status
  }
  try {
    await updateFaq(dto)
    ElMessage.success('状态更新成功')
  } catch (e) {
    row.status = row.status === 1 ? 0 : 1
  }
}

async function handleDelete(row) {
  await ElMessageBox.confirm(`确定要删除常见问题「${row.question}」吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  await deleteFaq(row.id)
  ElMessage.success('删除成功')
  loadData()
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    const dto = {
      id: form.id,
      question: form.question,
      answer: form.answer,
      category: form.category || '通用',
      sort: form.sort,
      status: form.status
    }
    if (form.id) {
      await updateFaq(dto)
      ElMessage.success('更新成功')
    } else {
      await addFaq(dto)
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
    question: '',
    answer: '',
    category: '',
    sort: 0,
    status: 1
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
