import type {
  LoginSchemaType,
  RegisterSchemaType,
} from "@/schemas/auth/auth.schema";
import { createContext } from "react";

export interface AuthContextType {
  token: string | null;
  login: (data: LoginSchemaType) => Promise<string>;
  register: (data: RegisterSchemaType) => Promise<string>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextType | undefined>(
  undefined
);
