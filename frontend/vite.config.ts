import path from "path";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";
import viteImagemin from "vite-plugin-imagemin";
import viteCompression from "vite-plugin-compression";

import { defineConfig } from "vite";
import viteImagemin from "vite-plugin-imagemin";

const isDocker = process.env.BUILD_ENV === "docker";

export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
    viteCompression({
      algorithm: "brotliCompress",
      ext: ".br",
      threshold: 1024,
      deleteOriginFile: false,
    }),

    // Optional imagemin plugin for local builds only
    !isDocker &&
      viteImagemin({
        webp: { quality: 80 },
      }),
  ],

  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },

  build: {
    sourcemap: true,
  },

  optimizeDeps: {
    include: [
      "react",
      "react-dom",
      "react-router-dom",
      "@tanstack/react-query",
      "react-hook-form",
      "zod",
    ],
  },

  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, "/api"),
      },
    },
  },
});
