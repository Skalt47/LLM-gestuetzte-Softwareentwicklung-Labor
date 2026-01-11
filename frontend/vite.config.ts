import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: { 
      "/api": process.env.VITE_API_BASE_URL || "http://localhost:8080"
    },
    host: "0.0.0.0",
  },
  preview: {
    host: "0.0.0.0",
    port: 5173,
  },
});
