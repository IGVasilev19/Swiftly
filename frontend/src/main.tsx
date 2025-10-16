import "@/styles/globals.css";

import App from "./App";

import { createRoot } from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

createRoot(document.getElementById("root")!).render(
  <QueryClientProvider client={new QueryClient()}>
    {/* <StrictMode> */}
    <BrowserRouter>
      <App />
    </BrowserRouter>
    {/* </StrictMode> */}
  </QueryClientProvider>
);
