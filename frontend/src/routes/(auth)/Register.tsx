import { RegisterForm } from "@/components/auth/RegisterForm";
import React from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  registerSchema,
  type RegisterSchemaType,
} from "@/schemas/auth/auth.schema";
import { toast } from "sonner";

const Register = () => {
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

  const handleRegister = (data: RegisterSchemaType) =>
    toast.error("Registration is not implemented yet");

  return (
    <div className="min-h-screen w-screen flex flex-col justify-center items-center gap-10">
      <h1 className="text-primary font-bold text-5xl">Sign Up</h1>
      <RegisterForm
        registerForm={form}
        handleSubmit={handleRegister}
        isPending={false}
      />
    </div>
  );
};

export default Register;
