import { AuthContext } from "@/contexts/AuthContext";
import React, { useState, type ReactNode } from "react";
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

  const login = async (data: LoginSchemaType) => {
    const response = await api.post("/auth/login", data);

    setToken(response.data.token);
    return token;
  };

  const register = async (data: RegisterSchemaType) => {
    const response = await api.post("/auth/register", data);

    return response.data.message;
  };

  const logout = () => {
    setToken(null);
  };

  return (
    <AuthContext.Provider value={{ token, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}
