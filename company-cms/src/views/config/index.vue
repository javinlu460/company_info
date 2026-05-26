<template>
  <div class="app-container">
    <el-card shadow="never" v-loading="loading">
      <el-form ref="formRef" :model="form" label-width="120px">
        <el-divider content-position="left">基本信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="公司名称" prop="companyName">
              <el-input v-model="form.companyName" placeholder="请输入公司名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公司Logo" prop="companyLogo">
              <ImageUpload v-model="form.companyLogo" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="传真" prop="fax">
              <el-input v-model="form.fax" placeholder="请输入传真" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="备案号" prop="icp">
              <el-input v-model="form.icp" placeholder="请输入备案号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-divider content-position="left">公司介绍</el-divider>
        <el-form-item label="公司简介" prop="companyIntro">
          <RichEditor v-model="form.companyIntro" />
        </el-form-item>
        <el-form-item label="企业文化" prop="companyCulture">
          <RichEditor v-model="form.companyCulture" />
        </el-form-item>
        <el-form-item label="资质荣誉" prop="companyHonor">
          <RichEditor v-model="form.companyHonor" />
        </el-form-item>
        <el-form-item label="公司愿景" prop="companyVision">
          <RichEditor v-model="form.companyVision" />
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
  companyName: '',
  companyLogo: '',
  phone: '',
  email: '',
  address: '',
  fax: '',
  icp: '',
  companyIntro: '',
  companyCulture: '',
  companyHonor: '',
  companyVision: ''
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
