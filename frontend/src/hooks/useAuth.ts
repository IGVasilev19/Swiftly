import { useState, useEffect } from "react";
import api from "@/hooks/api";
import type { DecodedToken } from "@/types/types";
import { jwtDecode } from "jwt-decode";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useAuth() {
  const navigate = useNavigate();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  const getAccessToken = () => sessionStorage.getItem("accessToken");

  const isTokenValid = (token: string) => {
    try {
      const decoded = jwtDecode<DecodedToken>(token);
      return decoded.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  };

  const refreshAccessToken = async (): Promise<boolean> => {
    try {
      const res = await api.post(
        "/auth/refresh",
        {},
        { withCredentials: true }
      );
      const { accessToken } = res.data;
      if (accessToken) {
        sessionStorage.setItem("accessToken", accessToken);
        return true;
      }
      return false;
    } catch (err: any) {
      sessionStorage.removeItem("accessToken");
      return false;
    }
  };

  const logout = async () => {
    const accessToken = sessionStorage.getItem("accessToken");
    if (!accessToken) {
      sessionStorage.removeItem("accessToken");
      navigate("/");
      return;
    }

    try {
      await api.post(
        "/auth/logout",
        {},
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
          withCredentials: true,
        }
      );
    } catch (err) {
      toast.error("Failed to log out");
    } finally {
      sessionStorage.removeItem("accessToken");
      setIsAuthenticated(false);
      toast.success("Logged out successfully");
      navigate("/");
    }
  };

  useEffect(() => {
    const checkAuth = async () => {
      setLoading(true);

      const token = getAccessToken();

      if (token && isTokenValid(token)) {
        setIsAuthenticated(true);
        setLoading(false);
        return;
      }

      const refreshed = await refreshAccessToken();
      setIsAuthenticated(refreshed);
      setLoading(false);
    };

    checkAuth();
  }, []);

  const setAuthenticated = (value: boolean) => {
    setIsAuthenticated(value);
  };

  return { isAuthenticated, loading, logout, setAuthenticated };
}
