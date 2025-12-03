import { useState } from "react";
import { toast } from "sonner";
import api from "@/hooks/api";
import { useNavigate } from "react-router-dom";
import type { LoginSchemaType } from "@/schemas/auth/auth.schema";
import type { AxiosError } from "axios";
import { useAuthContext } from "@/contexts/AuthContext";

export function useLogin() {
  const [isPending, setIsPending] = useState(false);
  const navigate = useNavigate();
  const { setAuthenticated } = useAuthContext();

  const login = async (data: LoginSchemaType) => {
    try {
      setIsPending(true);
      const res = await api.post("/auth/login", data, {
        withCredentials: true,
      });
      const accessToken = res.data;

      if (!accessToken) {
        toast.error("Something went wrong during login");
        return;
      }

      sessionStorage.setItem("accessToken", accessToken);
      setAuthenticated(true);
      toast.success("Login successful");
      navigate("/dashboard");
    } catch (error: unknown) {
      const axiosError = error as AxiosError<{ message?: string }>;
      toast.error(
        axiosError.response?.data?.message || "Wrong email or password"
      );
    } finally {
      setIsPending(false);
    }
  };

  return { login, isPending };
}
