import { RegisterForm } from "@/components/auth/RegisterForm";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useNavigate } from "react-router-dom";
import {
  registerSchema,
  type RegisterSchemaType,
} from "@/schemas/auth/auth.schema";
import { toast } from "sonner";
import { useApiMutation } from "@/hooks/hook";

const Register = () => {
  const navigate = useNavigate();
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

  const { mutate: register, isPending } = useApiMutation<RegisterSchemaType>(
    "POST",
    "/auth/register",
    {
      onSuccess: ({ message }) => {
        toast.success(message);
        navigate(`/`);
      },
      onError: (err) => {
        console.log(err);
        toast.error("Something went wrong");
      },
    }
  );

  const handleRegister = (data: RegisterSchemaType) => register(data);

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
