import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  base: '/admin/',
  build: {
    // 产物输出到 dist/admin，与线上 Nginx 的 /admin/ 子路径部署目录结构对齐
    outDir: 'dist/admin'
  },
  server: {
    port: 3001,
    host: '0.0.0.0',
    allowedHosts: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  }
})
