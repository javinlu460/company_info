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
          <el-button type="primary" icon="Plus" @click="handleAdd" v-hasPermi="['content:case:add']">新增</el-button>
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
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="clientName" label="客户名称" width="140" />
        <el-table-column prop="industry" label="所属行业" width="120" />
        <el-table-column prop="description" label="案例描述" min-width="200" show-overflow-tooltip />
        <el-table-column prop="orderNum" label="排序" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-switch v-model="row.status" :active-value="1" :inactive-value="0" @change="handleStatusChange(row)" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="160" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link icon="Edit" @click="handleEdit(row)" v-hasPermi="['content:case:edit']">编辑</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(row)" v-hasPermi="['content:case:delete']">删除</el-button>
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
              <el-input v-model="form.title" placeholder="请输入案例标题" />
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
            <el-form-item label="客户名称" prop="clientName">
              <el-input v-model="form.clientName" placeholder="请输入客户名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属行业" prop="industry">
              <el-input v-model="form.industry" placeholder="请输入所属行业" />
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
        <el-form-item label="封面图" prop="coverImage">
          <ImageUpload v-model="form.coverImage" />
        </el-form-item>
        <el-form-item label="案例描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入案例描述" />
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
import { getCaseList, addCase, updateCase, deleteCase } from '@/api/case'
import ImageUpload from '@/components/ImageUpload.vue'
import RichEditor from '@/components/RichEditor.vue'
import { getImageUrl } from '@/utils/image'

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
  description: '',
  coverImage: '',
  clientName: '',
  industry: '',
  content: '',
  sort: 0,
  status: 1
})

const formRules = {
  title: [{ required: true, message: '请输入案例标题', trigger: 'blur' }]
}

const dialogTitle = computed(() => form.id ? '编辑客户案例' : '新增客户案例')

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getCaseList(queryParams)
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
    description: row.description,
    coverImage: row.coverImage,
    clientName: row.clientName,
    industry: row.industry,
    content: row.content,
    sort: row.orderNum,
    status: row.status
  })
  dialogVisible.value = true
}

async function handleStatusChange(row) {
  const dto = {
    id: row.id,
    title: row.title,
    description: row.description,
    coverImage: row.coverImage,
    clientName: row.clientName,
    industry: row.industry,
    content: row.content,
    sort: row.orderNum,
    status: row.status
  }
  try {
    await updateCase(dto)
    ElMessage.success('状态更新成功')
  } catch (e) {
    row.status = row.status === 1 ? 0 : 1
  }
}

async function handleDelete(row) {
  await ElMessageBox.confirm(`确定要删除客户案例「${row.title}」吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  await deleteCase(row.id)
  ElMessage.success('删除成功')
  loadData()
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    if (form.id) {
      await updateCase(form)
      ElMessage.success('更新成功')
    } else {
      await addCase(form)
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
    description: '',
    coverImage: '',
    clientName: '',
    industry: '',
    content: '',
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
