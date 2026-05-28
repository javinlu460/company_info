<template>
  <div class="app-container">
    <!-- 操作栏 -->
    <el-card shadow="never" class="search-card">
      <el-button type="primary" icon="Plus" @click="handleAdd" v-hasPermi="['content:banner:add']">新增</el-button>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table v-loading="loading" :data="tableData" border stripe>
        <el-table-column label="缩略图" width="120" align="center">
          <template #default="{ row }">
            <el-image
              v-if="row.imageUrl"
              :src="getImageUrl(row.imageUrl)"
              :preview-src-list="[getImageUrl(row.imageUrl)]"
              fit="cover"
              style="width: 80px; height: 45px;"
            />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="160" show-overflow-tooltip />
        <el-table-column prop="linkUrl" label="链接" min-width="200" show-overflow-tooltip />
        <el-table-column prop="orderNum" label="排序" width="100" align="center">
          <template #default="{ row }">
            <el-input-number
              v-model="row.orderNum"
              :min="0"
              size="small"
              controls-position="right"
              @change="handleSortChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link icon="Edit" @click="handleEdit(row)" v-hasPermi="['content:banner:edit']">编辑</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(row)" v-hasPermi="['content:banner:delete']">删除</el-button>
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
      width="550px"
      destroy-on-close
    >
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="图片" prop="imageUrl">
          <ImageUpload v-model="form.imageUrl" />
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="链接" prop="linkUrl">
          <el-input v-model="form.linkUrl" placeholder="请输入链接" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
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
import { getBannerList, addBanner, updateBanner, deleteBanner, updateBannerStatus } from '@/api/banner'
import ImageUpload from '@/components/ImageUpload.vue'
import { getImageUrl } from '@/utils/image'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const formRef = ref(null)

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10
})

const form = reactive({
  id: undefined,
  imageUrl: '',
  title: '',
  linkUrl: '',
  sort: 0,
  status: 1
})

const formRules = {
  imageUrl: [{ required: true, message: '请上传图片', trigger: 'change' }]
}

const dialogTitle = computed(() => form.id ? '编辑轮播图' : '新增轮播图')

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getBannerList(queryParams)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } finally {
    loading.value = false
  }
}

function handleAdd() {
  resetForm()
  dialogVisible.value = true
}

function handleEdit(row) {
  resetForm()
  Object.assign(form, {
    id: row.id,
    imageUrl: row.imageUrl,
    title: row.title,
    linkUrl: row.linkUrl,
    sort: row.orderNum,
    status: row.status
  })
  dialogVisible.value = true
}

async function handleStatusChange(row) {
  try {
    await updateBannerStatus(row.id, row.status)
    ElMessage.success('状态更新成功')
  } catch (e) {
    row.status = row.status === 1 ? 0 : 1
  }
}

async function handleSortChange(row) {
  try {
    await updateBanner({ id: row.id, sort: row.orderNum })
  } catch (e) {
    loadData()
  }
}

async function handleDelete(row) {
  await ElMessageBox.confirm('确定要删除该轮播图吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  await deleteBanner(row.id)
  ElMessage.success('删除成功')
  loadData()
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    if (form.id) {
      await updateBanner(form)
      ElMessage.success('更新成功')
    } else {
      await addBanner(form)
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
    imageUrl: '',
    title: '',
    linkUrl: '',
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
