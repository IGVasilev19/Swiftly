/* eslint-disable @typescript-eslint/no-unused-vars */
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
  const [roles, setRoles] = useState<string[]>([]);

  const getAccessToken = () => sessionStorage.getItem("accessToken");

  const extractRolesFromToken = (decoded: DecodedToken): string[] => {
    const normalizeRole = (role: string): string => {
      const upperRole = String(role).toUpperCase();
      return upperRole.startsWith("ROLE_") ? upperRole.replace("ROLE_", "") : upperRole;
    };

    if (decoded.roles && Array.isArray(decoded.roles)) {
      return decoded.roles.map(normalizeRole);
    }
    if (decoded.authorities && Array.isArray(decoded.authorities)) {
      return decoded.authorities.map(normalizeRole);
    }
    if (decoded.role && typeof decoded.role === "string") {
      return [normalizeRole(decoded.role)];
    }
    if (decoded.scope && typeof decoded.scope === "string") {
      return decoded.scope
        .split(" ")
        .filter((s) => s.trim().length > 0)
        .map(normalizeRole);
    }
    return [];
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
      setRoles([]);
      toast.success("Logged out successfully");
      navigate("/");
    }
  };

  useEffect(() => {
    const decodeToken = (
      token: string
    ): { valid: boolean; roles: string[]; decoded?: DecodedToken } => {
      try {
        const decoded = jwtDecode<DecodedToken>(token);
        const isValid = decoded.exp * 1000 > Date.now();
        const extractedRoles = isValid ? extractRolesFromToken(decoded) : [];
        return { valid: isValid, roles: extractedRoles, decoded };
      } catch {
        return { valid: false, roles: [] };
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
          const { roles: newRoles } = decodeToken(accessToken);
          setRoles(newRoles);
          return true;
        }
        return false;
      } catch (err) {
        sessionStorage.removeItem("accessToken");
        setRoles([]);
        return false;
      }
    };

    const checkAuth = async () => {
      setLoading(true);

      const token = getAccessToken();

      if (token) {
        const { valid, roles: tokenRoles } = decodeToken(token);
        if (valid) {
          setIsAuthenticated(true);
          setRoles(tokenRoles);
          setLoading(false);
          return;
        }
      }

      const refreshed = await refreshAccessToken();
      setIsAuthenticated(refreshed);
      setLoading(false);
    };

    checkAuth();
  }, []);

  const setAuthenticated = (value: boolean) => {
    setIsAuthenticated(value);
    if (!value) {
      setRoles([]);
    } else {
      const token = getAccessToken();
      if (token) {
        try {
          const decoded = jwtDecode<DecodedToken>(token);
          const isValid = decoded.exp * 1000 > Date.now();
          if (isValid) {
            const extractedRoles = extractRolesFromToken(decoded);
            setRoles(extractedRoles);
          }
        } catch {
          setRoles([]);
        }
      }
    }
  };

  return { isAuthenticated, loading, logout, setAuthenticated, roles };
}
