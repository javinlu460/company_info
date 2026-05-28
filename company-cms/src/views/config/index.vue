<template>
  <div class="app-container">
    <el-card shadow="never" v-loading="loading">
      <el-form ref="formRef" :model="form" label-width="120px">
        <el-divider content-position="left">基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="公司名称" prop="company_name">
              <el-input v-model="form.company_name" placeholder="请输入公司名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司Logo" prop="company_logo">
              <ImageUpload v-model="form.company_logo" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="company_phone">
              <el-input v-model="form.company_phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="company_email">
              <el-input v-model="form.company_email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="地址" prop="company_address">
              <el-input v-model="form.company_address" placeholder="请输入地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="传真" prop="company_fax">
              <el-input v-model="form.company_fax" placeholder="请输入传真" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="备案号" prop="icp_number">
              <el-input v-model="form.icp_number" placeholder="请输入备案号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">公司介绍</el-divider>
        <el-form-item label="公司简介" prop="company_description">
          <RichEditor v-model="form.company_description" />
        </el-form-item>
        <el-form-item label="企业文化" prop="company_culture">
          <RichEditor v-model="form.company_culture" />
        </el-form-item>
        <el-form-item label="资质荣誉" prop="company_honor">
          <RichEditor v-model="form.company_honor" />
        </el-form-item>
        <el-form-item label="公司愿景" prop="company_vision">
          <RichEditor v-model="form.company_vision" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="submitLoading" @click="handleSave">保存配置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getConfigList, batchUpdateConfig } from '@/api/config'
import ImageUpload from '@/components/ImageUpload.vue'
import RichEditor from '@/components/RichEditor.vue'

const loading = ref(false)
const submitLoading = ref(false)

const form = reactive({
  company_name: '',
  company_logo: '',
  company_phone: '',
  company_email: '',
  company_address: '',
  company_fax: '',
  icp_number: '',
  company_description: '',
  company_culture: '',
  company_honor: '',
  company_vision: ''
})

onMounted(() => {
  loadData()
})

async function loadData() {
  loading.value = true
  try {
    const res = await getConfigList()
    const configs = res.data || []
    configs.forEach((item) => {
      if (item.configKey && form.hasOwnProperty(item.configKey)) {
        form[item.configKey] = item.configValue
      }
    })
  } finally {
    loading.value = false
  }
}

async function handleSave() {
  submitLoading.value = true
  try {
    const configs = []
    for (const key in form) {
      configs.push({ configKey: key, configValue: form[key] || '' })
    }
    await batchUpdateConfig(configs)
    ElMessage.success('保存成功')
  } finally {
    submitLoading.value = false
  }
}
</script>

<style scoped>
.app-container {
  padding: 0;
}
</style>
