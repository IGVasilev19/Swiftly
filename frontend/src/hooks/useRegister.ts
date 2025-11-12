import React from "react";
import type { AxiosError } from "axios";
import api from "@/hooks/api";
import type { RegisterSchemaType } from "@/schemas/auth/auth.schema";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

import { useState } from "react";

export function useRegister() {
  const [isPending, setIsPending] = useState(false);
  const navigate = useNavigate();

  const register = async (data: RegisterSchemaType) => {
    try {
      setIsPending(true);
      const response = await api.post("/auth/register", data);
      toast.success(response.data.message);
      navigate("/");
    } catch (error: unknown) {
      const axiosError = error as AxiosError<{ message: string }>;
      console.error(axiosError);
      toast.error(axiosError.response?.data?.message || "Registration failed");
    } finally {
      setIsPending(false);
    }
  };

  return { register, isPending };
}
