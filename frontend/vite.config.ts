import path from "path";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";
import viteCompression from "vite-plugin-compression";
import { defineConfig } from "vite";

const isDocker = process.env.BUILD_ENV === "docker";

const corePlugins = [
  react(),
  tailwindcss(),
  viteCompression({
    algorithm: "brotliCompress",
    ext: ".br",
    threshold: 1024,
    deleteOriginFile: false,
  }),
];

const optionalPlugins = [];

if (!isDocker) {
  const { visualizer } = require("rollup-plugin-visualizer");
  const imagemin = require("vite-plugin-imagemin");

  optionalPlugins.push(
    visualizer({ open: true }),
    imagemin({
      webp: {
        quality: 80,
      },
    })
  );
}

export default defineConfig({
  plugins: [...corePlugins, ...optionalPlugins],
  resolve: {
    alias: { "@": path.resolve(__dirname, "./src") },
  },
  build: { sourcemap: true },
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
      },
    },
  },
});
