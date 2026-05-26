<template>
  <div class="app-container">
    <!-- 操作栏 -->
    <el-card shadow="never" class="search-card">
      <el-button type="primary" icon="Plus" @click="handleAdd()" v-hasPermi="['system:menu:add']">新增</el-button>
    </el-card>

    <!-- 树形表格 -->
    <el-card shadow="never">
      <el-table
        v-loading="loading"
        :data="menuData"
        border
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        default-expand-all
      >
        <el-table-column prop="name" label="菜单名称" min-width="180" />
        <el-table-column prop="icon" label="图标" width="80" align="center">
          <template #default="{ row }">
            <el-icon v-if="row.icon"><component :is="row.icon" /></el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" width="90" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.type === 0" type="">目录</el-tag>
            <el-tag v-else-if="row.type === 1" type="success">菜单</el-tag>
            <el-tag v-else-if="row.type === 2" type="warning">按钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由路径" min-width="140" show-overflow-tooltip />
        <el-table-column prop="permission" label="权限标识" min-width="160" show-overflow-tooltip />
        <el-table-column prop="sort" label="排序" width="80" align="center" />
        <el-table-column prop="visible" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.visible === 0 ? 'success' : 'info'">
              {{ row.visible === 0 ? '显示' : '隐藏' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right" align="center">
          <template #default="{ row }">
            <el-button
              v-if="row.type !== 2"
              type="primary"
              link
              icon="Plus"
              @click="handleAdd(row)"
              v-hasPermi="['system:menu:add']"
            >新增</el-button>
            <el-button type="primary" link icon="Edit" @click="handleEdit(row)" v-hasPermi="['system:menu:edit']">编辑</el-button>
            <el-button type="danger" link icon="Delete" @click="handleDelete(row)" v-hasPermi="['system:menu:delete']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑Dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="650px"
      destroy-on-close
    >
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="父菜单" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="menuTreeSelect"
            :props="{ label: 'name', value: 'id', children: 'children' }"
            check-strictly
            clearable
            placeholder="请选择父菜单(不选则为顶级)"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio :value="0">目录</el-radio>
            <el-radio :value="1">菜单</el-radio>
            <el-radio :value="2">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item v-if="form.type !== 2" label="路由路径" prop="path">
          <el-input v-model="form.path" placeholder="请输入路由路径" />
        </el-form-item>
        <el-form-item v-if="form.type === 1" label="组件路径" prop="component">
          <el-input v-model="form.component" placeholder="如: system/user/index" />
        </el-form-item>
        <el-form-item v-if="form.type === 2" label="权限标识" prop="permission">
          <el-input v-model="form.permission" placeholder="如: system:user:add" />
        </el-form-item>
        <el-form-item v-if="form.type !== 2" label="图标" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" />
        </el-form-item>
        <el-form-item v-if="form.type !== 2" label="是否可见" prop="visible">
          <el-radio-group v-model="form.visible">
            <el-radio :value="0">显示</el-radio>
            <el-radio :value="1">隐藏</el-radio>
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
import { getMenuTree, addMenu, updateMenu, deleteMenu } from '@/api/menu'

const loading = ref(false)
const submitLoading = ref(false)
const menuData = ref([])
const dialogVisible = ref(false)
const formRef = ref(null)

const form = reactive({
  id: undefined,
  parentId: 0,
  name: '',
  type: 0,
  path: '',
  component: '',
  permission: '',
  icon: '',
  sort: 0,
  visible: 0
})

const formRules = {
  name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }]
}

const dialogTitle = computed(() => form.id ? '编辑菜单' : '新增菜单')

const menuTreeSelect = computed(() => {
  return [{ id: 0, name: '顶级菜单', children: menuData.value }]
})

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getMenuTree()
    menuData.value = res.data || []
  } finally {
    loading.value = false
  }
}

function handleAdd(row) {
  resetForm()
  if (row && row.id) {
    form.parentId = row.id
    form.type = row.type === 0 ? 0 : 1
  }
  dialogVisible.value = true
}

function handleEdit(row) {
  resetForm()
  Object.assign(form, {
    id: row.id,
    parentId: row.parentId || 0,
    name: row.name,
    type: row.type,
    path: row.path,
    component: row.component,
    permission: row.permission,
    icon: row.icon,
    sort: row.sort,
    visible: row.visible
  })
  dialogVisible.value = true
}

async function handleDelete(row) {
  if (row.children && row.children.length > 0) {
    ElMessage.warning('该菜单下有子菜单，无法删除')
    return
  }
  await ElMessageBox.confirm(`确定要删除菜单「${row.name}」吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  await deleteMenu(row.id)
  ElMessage.success('删除成功')
  loadData()
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  submitLoading.value = true
  try {
    if (form.id) {
      await updateMenu(form)
      ElMessage.success('更新成功')
    } else {
      await addMenu(form)
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
    name: '',
    type: 0,
    path: '',
    component: '',
    permission: '',
    icon: '',
    sort: 0,
    visible: 0
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
