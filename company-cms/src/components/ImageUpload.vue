<template>
  <div class="image-upload">
    <el-upload
      :action="uploadAction"
      :headers="uploadHeaders"
      :show-file-list="false"
      :on-success="handleSuccess"
      :on-error="handleError"
      :before-upload="beforeUpload"
      accept="image/*"
    >
      <div v-if="modelValue" class="image-preview">
        <el-image :src="modelValue" fit="cover" class="preview-img" />
        <div class="image-mask">
          <el-icon class="mask-icon" @click.stop="handleRemove"><Delete /></el-icon>
        </div>
      </div>
      <div v-else class="upload-placeholder">
        <el-icon class="upload-icon"><Plus /></el-icon>
        <div class="upload-text">上传图片</div>
      </div>
    </el-upload>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  action: {
    type: String,
    default: '/api/upload'
  }
})

const emit = defineEmits(['update:modelValue'])

const uploadAction = computed(() => props.action)

const uploadHeaders = computed(() => ({
  Authorization: 'Bearer ' + (localStorage.getItem('token') || '')
}))

function beforeUpload(file) {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB!')
    return false
  }
  return true
}

function handleSuccess(response) {
  if (response.code === 200) {
    emit('update:modelValue', response.data)
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(response.msg || '上传失败')
  }
}

function handleError() {
  ElMessage.error('上传失败')
}

function handleRemove() {
  emit('update:modelValue', '')
}
</script>

<style scoped>
.image-upload :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.3s;
}
.image-upload :deep(.el-upload:hover) {
  border-color: #409eff;
}
.upload-placeholder {
  width: 148px;
  height: 148px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.upload-icon {
  font-size: 28px;
  color: #8c939d;
}
.upload-text {
  font-size: 12px;
  color: #8c939d;
  margin-top: 8px;
}
.image-preview {
  width: 148px;
  height: 148px;
  position: relative;
}
.preview-img {
  width: 148px;
  height: 148px;
}
.image-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}
.image-preview:hover .image-mask {
  opacity: 1;
}
.mask-icon {
  font-size: 24px;
  color: #fff;
  cursor: pointer;
}
</style>
