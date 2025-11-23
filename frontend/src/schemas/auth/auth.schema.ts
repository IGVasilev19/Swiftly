import { z } from "zod";
import {
  emailSchema,
  nameSchema,
  passwordSchema,
  confirmPasswordSchema,
  phoneNumberSchema,
  roleSchema,
} from "../user/user.schema";

export const registerSchema = z
  .object({
    name: nameSchema,
    email: emailSchema,
    password: passwordSchema,
    confirmPassword: confirmPasswordSchema,
    phoneNumber: phoneNumberSchema,
    roles: z.array(roleSchema).min(1, "Role is required"),
  })
  .superRefine((values, ctx) => {
    if (values.password !== values.confirmPassword) {
      ctx.addIssue({
        path: ["confirmPassword"],
        message: "Passwords don't match",
        code: "custom",
      });
    }
  });

export type RegisterSchemaType = z.infer<typeof registerSchema>;

export const loginSchema = z.object({
  email: z.string().email("Invalid email address"),
  password: z.string().min(8, "Invalid password"),
});

export type LoginSchemaType = z.infer<typeof loginSchema>;
