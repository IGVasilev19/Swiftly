import { LoginForm } from "@/components/auth/LoginForm";
import { loginSchema, type LoginSchemaType } from "@/schemas/auth/auth.schema";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useLogin } from "@/hooks/useLogin";
import { useAuthRedirect } from "@/hooks/useAuthRedirect";

const Login = () => {
  const { login, isPending } = useLogin();
  const form = useForm<LoginSchemaType>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  useAuthRedirect("/dashboard");

  return (
    <div className="min-h-screen w-screen flex flex-col justify-center items-center gap-10">
      <h1 className="text-primary font-bold text-5xl">Sign In</h1>
      <LoginForm loginForm={form} handleSubmit={login} isPending={isPending} />
    </div>
  );
};

export default Login;
