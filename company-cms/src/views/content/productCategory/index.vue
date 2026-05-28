<template>
  <div class="app-container">
    <!-- 操作栏 -->
    <el-card shadow="never" class="search-card">
      <el-button type="primary" icon="Plus" @click="handleAdd()" v-hasPermi="['content:productCategory:add']">新增</el-button>
    </el-card>

    <!-- 树形表格 -->
    <el-card shadow="never">
      <el-table
        v-loading="loading"
        :data="categoryData"
        border
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        default-expand-all
      >
        <el-table-column prop="categoryName" label="分类名称" min-width="200" />
        <el-table-column prop="orderNum" label="排序" width="100" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link icon="Plus" @click="handleAdd(row)" v-hasPermi="['content:productCategory:add']">新增</el-button>
            <el-button type="primary" link icon="Edit" @click="handleEdit(row)" v-hasPermi="['content:productCategory:edit']">编辑</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(row)" v-hasPermi="['content:productCategory:delete']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑Dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      destroy-on-close
    >
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="父分类" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="categoryTreeSelect"
            :props="{ label: 'categoryName', value: 'id', children: 'children' }"
            check-strictly
            clearable
            placeholder="请选择父分类(不选则为顶级)"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="form.categoryName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="排序" prop="orderNum">
          <el-input-number v-model="form.orderNum" :min="0" />
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
import { getProductCategoryTree, addProductCategory, updateProductCategory, deleteProductCategory } from '@/api/productCategory'

const loading = ref(false)
const submitLoading = ref(false)
const categoryData = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)

const form = reactive({
  id: undefined,
  parentId: 0,
  categoryName: '',
  orderNum: 0,
  status: 1
})

const formRules = {
  categoryName: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
}

const dialogTitle = computed(() => form.id ? '编辑分类' : '新增分类')

const categoryTreeSelect = computed(() => {
  return [{ id: 0, categoryName: '顶级分类', children: categoryData.value }]
})

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getProductCategoryTree()
    categoryData.value = res.data || []
  } finally {
    loading.value = false
  }
}

function handleAdd(row) {
  resetForm()
  if (row && row.id) {
    form.parentId = row.id
  }
  dialogVisible.value = true
}

function handleEdit(row) {
  resetForm()
  Object.assign(form, {
    id: row.id,
    parentId: row.parentId || 0,
    categoryName: row.categoryName,
    orderNum: row.orderNum,
    status: row.status
  })
  dialogVisible.value = true
}

async function handleDelete(row) {
  if (row.children && row.children.length > 0) {
    ElMessage.warning('该分类下有子分类，无法删除')
    return
  }
  await ElMessageBox.confirm(`确定要删除分类「${row.categoryName}」吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  await deleteProductCategory(row.id)
  ElMessage.success('删除成功')
  loadData()
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    if (form.id) {
      await updateProductCategory(form)
      ElMessage.success('更新成功')
    } else {
      await addProductCategory(form)
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
    parentId: 0,
    categoryName: '',
    orderNum: 0,
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
</style>
