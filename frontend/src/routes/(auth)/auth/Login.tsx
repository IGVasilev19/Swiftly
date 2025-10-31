import { LoginForm } from "@/components/auth/LoginForm";
import api from "@/hooks/api";
import { loginSchema, type LoginSchemaType } from "@/schemas/auth/auth.schema";
import type { AxiosError } from "axios";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form/dist/useForm";

const Login = () => {
  const navigate = useNavigate();
  const [isPending, setIsPending] = useState(false);

  const form = useForm<LoginSchemaType>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  const handleLogin = async (data: LoginSchemaType) => {
    try {
      setIsPending(true);
      const response = await api.post("/auth/login", data);
      toast.success(response.data.message);
      navigate("/dashboard");
    } catch (error: unknown) {
      const axiosError = error as AxiosError<{ message: string }>;
      console.error(axiosError);
      toast.error(axiosError.response?.data?.message || "Something went wrong");
    } finally {
      setIsPending(false);
    }
  };

  return (
    <div className="min-h-screen w-screen flex flex-col justify-center items-center gap-10">
      <h1 className="text-primary font-bold text-5xl">Sign In</h1>
      <LoginForm
        loginForm={form}
        handleSubmit={handleLogin}
        isPending={isPending}
      />
    </div>
  );
};

export default Login;
