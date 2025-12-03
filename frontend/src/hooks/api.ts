import axios, { type AxiosInstance, type AxiosResponse } from "axios";
import { createBrowserHistory } from "history";

// Use proxy in development, direct URL in production
export const API_URL =
  import.meta.env.VITE_API_URL || 
  (import.meta.env.DEV ? "/api/v1" : "http://localhost:8080/api/v1");

const history = createBrowserHistory();

let isRefreshing = false;
let refreshSubscribers: ((token: string) => void)[] = [];

const subscribeTokenRefresh = (cb: (token: string) => void) => {
  refreshSubscribers.push(cb);
};

const onTokenRefreshed = (token: string) => {
  refreshSubscribers.forEach((cb) => cb(token));
  refreshSubscribers = [];
};

const api: AxiosInstance = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

api.interceptors.request.use((config) => {
  const token = sessionStorage.getItem("accessToken");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response: AxiosResponse) => response,
  async (error) => {
    const originalRequest = error.config;

    // Don't intercept refresh endpoint errors - let useAuth handle them
    if (originalRequest.url?.includes("/auth/refresh")) {
      return Promise.reject(error);
    }

    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        return new Promise((resolve) => {
          subscribeTokenRefresh((token) => {
            originalRequest.headers.Authorization = `Bearer ${token}`;
            resolve(api(originalRequest));
          });
        });
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        const res = await axios.post(
          `${API_URL}/auth/refresh`,
          {},
          { withCredentials: true }
        );

        const newAccessToken = res.data.accessToken;
        sessionStorage.setItem("accessToken", newAccessToken);

        isRefreshing = false;
        onTokenRefreshed(newAccessToken);

        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        isRefreshing = false;
        sessionStorage.removeItem("accessToken");
        history.push("/");
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default api;
