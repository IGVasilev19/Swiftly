import "@/styles/globals.css";

import App from "./App";

import { createRoot } from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Toaster } from "sonner";
import { AuthProvider } from "./components/auth/AuthProvider";

createRoot(document.getElementById("root")!).render(
  <QueryClientProvider client={new QueryClient()}>
    {/* <StrictMode> */}
    <BrowserRouter>
      <AuthProvider>
        <App />
        <Toaster position="top-right" />
      </AuthProvider>
    </BrowserRouter>
    {/* </StrictMode> */}
  </QueryClientProvider>
);
