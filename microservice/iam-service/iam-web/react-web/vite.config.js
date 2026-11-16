import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    outDir: 'dist',
    emptyOutDir: true,
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:39000',
        changeOrigin: true,
      },
      '/oauth2': {
        target: 'http://localhost:39000',
        changeOrigin: true,
      },
    },
  },
})
