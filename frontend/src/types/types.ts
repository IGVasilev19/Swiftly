import type { UseFormReturn } from "react-hook-form";
import type {
  LoginSchemaType,
  RegisterSchemaType,
} from "@/schemas/auth/auth.schema";
import type { DateRange } from "react-day-picker/dist/cjs/types/shared";

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
  [key: string]: unknown;
}

export type UseByIdOptions = {
  id?: number | null;
};

export type UseQueryReturn<T> = {
  data: T | null;
  isLoading: boolean;
  error: Error | null;
  refetch: () => void;
};

export type Calendar05Props = {
  value?: DateRange;
  onChange: (range: DateRange | undefined) => void;
};
