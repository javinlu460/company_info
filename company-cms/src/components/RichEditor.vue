<template>
  <div class="rich-editor">
    <div style="border: 1px solid #ccc;">
      <Toolbar
        style="border-bottom: 1px solid #ccc;"
        :editor="editorRef"
        :defaultConfig="toolbarConfig"
        mode="simple"
      />
      <Editor
        style="height: 400px; overflow-y: hidden;"
        :modelValue="modelValue"
        :defaultConfig="editorConfig"
        mode="simple"
        @onCreated="handleCreated"
        @onChange="handleChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, shallowRef, onBeforeUnmount, watch } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])

const editorRef = shallowRef(null)

const toolbarConfig = {
  excludeKeys: [
    'headerSelect',
    'italic',
    'group-more-style',
    'codeView',
    'codeBlock',
    'fullScreen'
  ]
}

const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {
    uploadImage: {
      server: '/api/upload',
      fieldName: 'file',
      maxFileSize: 10 * 1024 * 1024,
      customInsert(res, insertFn) {
        if (res.code === 200) {
          const url = res.data
          insertFn(url, '', '')
        }
      },
      headers: {
        Authorization: 'Bearer ' + (localStorage.getItem('token') || '')
      }
    }
  }
}

function handleCreated(editor) {
  editorRef.value = editor
}

function handleChange(editor) {
  const html = editor.getHtml()
  emit('update:modelValue', html)
}

onBeforeUnmount(() => {
  if (editorRef.value) {
    editorRef.value.destroy()
  }
})

watch(
  () => props.modelValue,
  (val) => {
    if (editorRef.value && editorRef.value.getHtml() !== val) {
      editorRef.value.setHtml(val || '')
    }
  }
)
</script>

<style scoped>
.rich-editor {
  width: 100%;
}
</style>
