import { RegisterForm } from "@/components/auth/RegisterForm";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useNavigate } from "react-router-dom";
import {
  registerSchema,
  type RegisterSchemaType,
} from "@/schemas/auth/auth.schema";
import { toast } from "sonner";
import { useState } from "react";
import type { AxiosError } from "axios";
import api from "@/hooks/api";

const Register = () => {
  const navigate = useNavigate();
  const [isPending, setIsPending] = useState(false);

  const form = useForm<RegisterSchemaType>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      name: "",
      email: "",
      password: "",
      phoneNumber: "",
      address: "",
      country: "",
      city: "",
      postalCode: "",
      role: "",
    },
  });

  const handleRegister = async (data: RegisterSchemaType) => {
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

  return (
    <div className="min-h-screen w-screen flex flex-col justify-center items-center gap-10">
      <h1 className="text-primary font-bold text-5xl">Sign Up</h1>
      <RegisterForm
        registerForm={form}
        handleSubmit={handleRegister}
        isPending={isPending}
      />
    </div>
  );
};

export default Register;
