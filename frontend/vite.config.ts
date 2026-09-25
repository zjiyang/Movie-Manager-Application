import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// The browser talks to the same origin in development and in production. In
// development Vite forwards /api to the backend, so the frontend never needs to
// know the backend's address and there is no CORS configuration to get wrong.
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:8080',
        changeOrigin: false,
      },
    },
  },
});
