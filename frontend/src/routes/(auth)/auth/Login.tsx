import { LoginForm } from "@/components/auth/LoginForm";
import React from "react";

const Login = () => {
  return (
    <div className="min-h-screen w-screen flex flex-col justify-center items-center gap-10">
      <h1 className="text-primary font-bold text-5xl">Sign In</h1>
      <LoginForm />
    </div>
  );
};

export default Login;
