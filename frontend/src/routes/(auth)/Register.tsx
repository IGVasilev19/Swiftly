import { RegisterForm } from "@/components/ui/RegisterForm";

const Register = () => {
  return (
    <div className="min-h-screen w-screen flex flex-col justify-center items-center gap-10">
      <h1 className="text-primary font-bold text-5xl">Sign Up</h1>
      <RegisterForm />
    </div>
  );
};

export default Register;
