<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="关键词">
          <el-input v-model="queryParams.keyword" placeholder="标题关键词" clearable @keyup.enter="handleSearch" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" clearable placeholder="请选择" style="width: 120px;">
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
        <el-form-item style="float: right;">
          <el-button type="primary" icon="Plus" @click="handleAdd" v-hasPermi="['content:solution:add']">新增</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table v-loading="loading" :data="tableData" border stripe>
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="summary" label="摘要" min-width="240" show-overflow-tooltip />
        <el-table-column label="标签" min-width="180">
          <template #default="{ row }">
            <el-tag v-for="(tag, idx) in parseTags(row.tags)" :key="idx" size="small" style="margin-right: 6px; margin-bottom: 4px;">
              {{ tag }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orderNum" label="排序" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-switch v-model="row.status" :active-value="1" :inactive-value="0" @change="handleStatusChange(row)" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link icon="Edit" @click="handleEdit(row)" v-hasPermi="['content:solution:edit']">编辑</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(row)" v-hasPermi="['content:solution:delete']">删除</el-button>
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
      width="800px"
      top="5vh"
      destroy-on-close
    >
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="16">
            <el-form-item label="标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入方案标题" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="form.sort" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio :value="1">上架</el-radio>
                <el-radio :value="0">下架</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="标签" prop="tags">
          <el-select
            v-model="form.tagsArray"
            multiple
            filterable
            allow-create
            default-first-option
            placeholder="请输入标签，按回车确认"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="摘要" prop="summary">
          <el-input v-model="form.summary" type="textarea" :rows="3" placeholder="请输入摘要" />
        </el-form-item>
        <el-form-item label="详情" prop="content">
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
import { getSolutionList, addSolution, updateSolution, deleteSolution } from '@/api/solution'
import RichEditor from '@/components/RichEditor.vue'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const formRef = ref(null)

const queryParams = reactive({
  keyword: '',
  status: undefined,
  pageNum: 1,
  pageSize: 10
})

const form = reactive({
  id: undefined,
  title: '',
  summary: '',
  content: '',
  tags: '',
  tagsArray: [],
  sort: 0,
  status: 1
})

const formRules = {
  title: [{ required: true, message: '请输入方案标题', trigger: 'blur' }]
}

const dialogTitle = computed(() => form.id ? '编辑解决方案' : '新增解决方案')

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getSolutionList(queryParams)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
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
    title: row.title,
    summary: row.summary,
    content: row.content,
    sort: row.orderNum,
    status: row.status,
    tagsArray: parseTags(row.tags)
  })
  dialogVisible.value = true
}

async function handleStatusChange(row) {
  const dto = {
    id: row.id,
    title: row.title,
    summary: row.summary,
    content: row.content,
    tags: row.tags,
    sort: row.orderNum,
    status: row.status
  }
  try {
    await updateSolution(dto)
    ElMessage.success('状态更新成功')
  } catch (e) {
    row.status = row.status === 1 ? 0 : 1
  }
}

async function handleDelete(row) {
  await ElMessageBox.confirm(`确定要删除解决方案「${row.title}」吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  await deleteSolution(row.id)
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
      title: form.title,
      summary: form.summary,
      content: form.content,
      tags: form.tagsArray.length ? JSON.stringify(form.tagsArray) : '',
      sort: form.sort,
      status: form.status
    }
    if (form.id) {
      await updateSolution(dto)
      ElMessage.success('更新成功')
    } else {
      await addSolution(dto)
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
    summary: '',
    content: '',
    tags: '',
    tagsArray: [],
    sort: 0,
    status: 1
  })
}

function parseTags(tags) {
  if (!tags) return []
  try {
    const arr = JSON.parse(tags)
    return Array.isArray(arr) ? arr : []
  } catch (e) {
    return []
  }
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
