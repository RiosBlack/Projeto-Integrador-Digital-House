import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080/',
        changeOrigin: true,
        secure: false,
        onProxyReq(proxyReq) {
          proxyReq.setHeader('Origin', 'http://localhost:5173/');
          proxyReq.setHeader('Access-Control-Allow-Origin', 'http://localhost:5173');
          proxyReq.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
          proxyReq.setHeader('Access-Control-Allow-Headers', ['Content-Type', 'Authorization']);
          proxyReq.setHeader('Access-Control-Expose-Headers', '*');
        },
      },
    },
  },
});
