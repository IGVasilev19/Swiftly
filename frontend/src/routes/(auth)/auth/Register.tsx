import { RegisterForm } from "@/components/auth/RegisterForm";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  registerSchema,
  type RegisterSchemaType,
} from "@/schemas/auth/auth.schema";
import { useRegister } from "@/hooks/useRegister";

const Register = () => {
  const { register, isPending } = useRegister();

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

  return (
    <div className="min-h-screen w-screen flex flex-col justify-center items-center gap-10">
      <h1 className="text-primary font-bold text-5xl">Sign Up</h1>
      <RegisterForm
        registerForm={form}
        handleSubmit={register}
        isPending={isPending}
      />
    </div>
  );
};

export default Register;
