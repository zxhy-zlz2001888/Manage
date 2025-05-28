import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'




export default defineConfig({
  server: {
    open: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  // 后端端口
        changeOrigin: true,
        rewrite: path => path // 如果你的Spring Boot接口本身是 /api/... 不要替换
      }
    }
  },
  plugins: [vue()]
})