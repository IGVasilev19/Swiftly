import type { UseFormReturn } from "react-hook-form";
import type {
  LoginSchemaType,
  RegisterSchemaType,
} from "@/schemas/auth/auth.schema";

export interface LoginFormProps {
  loginForm: UseFormReturn<LoginSchemaType>;
  handleSubmit: (data: LoginSchemaType) => void;
  isPending: boolean;
}

export interface RegisterFormProps {
  registerForm: UseFormReturn<RegisterSchemaType>;
  handleSubmit: (data: RegisterSchemaType) => void;
  isPending: boolean;
}
