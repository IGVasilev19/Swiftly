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

export interface DecodedToken {
  exp: number;
  sub: string;
  email?: string;
  roles?: string[];
  authorities?: string[];
  role?: string;
  [key: string]: unknown; // Allow for other JWT claims
}
