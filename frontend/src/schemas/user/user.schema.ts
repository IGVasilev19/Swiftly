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

export const nameSchema = z
  .string()
  .min(1, "Name is required")
  .max(100, "Name must be at most 100 characters long");

export const countrySchema = z.string().min(1, "Country is required");

export const citySchema = z
  .string()
  .nonempty("City is required")
  .min(2, { message: "City name must be at least 2 characters long" })
  .max(50, { message: "City name must be at most 50 characters long" })
  .regex(/^[A-Za-zÀ-ÿ\u00f1\u00d1\s\-]+$/, {
    message: "City name can only contain letters, spaces, and hyphens",
  })
  .trim();

export const postalCodeSchema = z
  .string()
  .nonempty("Postal code is required")
  .min(3, { message: "Postal code must be at least 3 characters long" })
  .max(12, { message: "Postal code must be at most 12 characters long" })
  .regex(/^[A-Za-z0-9\s\-]+$/, {
    message:
      "Postal code can only contain letters, numbers, spaces, and hyphens",
  })
  .trim();

export const roleSchema = z.string().min(1, "Role is required");

export const phoneNumberSchema = z
  .string()
  .min(1, "Required")
  .regex(
    /^\+[1-9]\d{6,14}$/,
    "Phone must be in international E.164 format (e.g. +31612345678)"
  );

export const addressSchema = z
  .string()
  .nonempty({ message: "Address is required" })
  .min(5, { message: "Address must be at least 5 characters long" })
  .max(100, { message: "Address must be at most 100 characters long" })
  .regex(/^[A-Za-z0-9À-ÿ\u00f1\u00d1\s.,'\-#/\\()]+$/, {
    message: "Address contains invalid characters",
  })
  .trim();

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
