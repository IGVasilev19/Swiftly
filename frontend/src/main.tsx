import "@/styles/globals.css";

import App from "./App";

import { createRoot } from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Toaster } from "sonner";

createRoot(document.getElementById("root")!).render(
  <QueryClientProvider client={new QueryClient()}>
    {/* <StrictMode> */}
    <BrowserRouter>
      <App />
      <Toaster position="top-right" />
    </BrowserRouter>
    {/* </StrictMode> */}
  </QueryClientProvider>
);
