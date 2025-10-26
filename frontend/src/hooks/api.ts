import axios, { type AxiosInstance, type AxiosResponse } from "axios";
import { createBrowserHistory } from "history";

export const API_URL =
  import.meta.env.VITE_API_URL || "http://localhost:8080/api/v1";

const history = createBrowserHistory();

const api: AxiosInstance = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

api.interceptors.response.use(
  (response: AxiosResponse) => {
    if (response.data && response.data.success === false) {
      return Promise.reject(
        new Error(response.data.message || "Request failed")
      );
    }
    return response;
  },
  (error) => {
    if (error.response?.status === 401) {
      history.push("/");
    }

    return Promise.reject(error);
  }
);

export default api;
