import { config } from "@/config/config";
import { z } from "zod";

export const emailSchema = z
  .string()
  .min(1, "Required")
  .email("Enter a valid email address");

const rules = config.user.passwordRules;

export const passwordSchema = z
  .string()
  .min(
    rules.minLength,
    `Password must be at least ${rules.minLength} characters long`
  )
  .refine((val) => !rules.requireUppercase || /[A-Z]/.test(val), {
    message: "Password must contain a uppercase letter",
  })
  .refine((val) => !rules.requireLowercase || /[a-z]/.test(val), {
    message: "Password must contain a lowercase letter",
  })
  .refine((val) => !rules.requireNumber || /\d/.test(val), {
    message: "Password must contain a number",
  })
  .refine((val) => !rules.requireSymbol || /[!@#$%^&*(),.?":{}|<>]/.test(val), {
    message: "Password must contain a symbol",
  });

export const confirmPasswordSchema = z.string().min(1, "Required");

export const nameSchema = z
  .string()
  .min(1, "Name is required")
  .max(100, "Name must be at most 100 characters long");

export const roleSchema = z.enum(["OWNER", "RENTER"]);

export const phoneNumberSchema = z
  .string()
  .min(1, "Required")
  .regex(
    /^\+[1-9]\d{6,14}$/,
    "Phone must be in international E.164 format (e.g. +31612345678)"
  );

export const userUpdateSchema = z
  .object({
    name: nameSchema.optional(),
    email: emailSchema.optional(),
    password: passwordSchema.optional(),
  })
  .refine((data) => Object.keys(data).length > 0, {
    message: "At least one field must be provided for update",
  });

export type updateUserSchemaType = z.infer<typeof userUpdateSchema>;
