import { AuthContext } from "@/contexts/AuthContext";
import React, { useEffect, useState, type ReactNode } from "react";
import type {
  LoginSchemaType,
  RegisterSchemaType,
} from "@/schemas/auth/auth.schema";
import api from "@/hooks/api";

interface Props {
  children: ReactNode;
}

export function AuthProvider({ children }: Props) {
  const [token, setToken] = useState<string | null>(null);

  useEffect(() => {
    const savedToken = localStorage.getItem("accessToken");
    if (savedToken) {
      setToken(savedToken);
    }
  }, []);

  const login = async (data: LoginSchemaType) => {
    const response = await api.post("/auth/login", data);

    const newToken = response.data.token;

    localStorage.setItem("accessToken", newToken);
    setToken(newToken);

    return newToken;
  };

  const register = async (data: RegisterSchemaType) => {
    const response = await api.post("/auth/register", data);

    return response.data.message;
  };

  const logout = () => {
    localStorage.removeItem("accessToken");
    setToken(null);
  };

  return (
    <AuthContext.Provider value={{ token, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}
