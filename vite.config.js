import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173, // React 前端端口
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // Spring Boot 后端
        changeOrigin: true,
        secure: false, // 如果是 http，不验证 SSL
        rewrite: (path) => path.replace(/^\/api/, '') // 可选：去掉 /api 前缀
      }
    }
  }
})


