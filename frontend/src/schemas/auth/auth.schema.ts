import { z } from "zod";
import {
  emailSchema,
  nameSchema,
  passwordSchema,
  phoneNumberSchema,
  countrySchema,
  citySchema,
  postalCodeSchema,
  roleSchema,
  addressSchema,
} from "../user/user.schema";

export const registerSchema = z.object({
  name: nameSchema,
  email: emailSchema,
  password: passwordSchema,
  phoneNumber: phoneNumberSchema,
  country: countrySchema,
  city: citySchema,
  postalCode: postalCodeSchema,
  role: roleSchema,
  address: addressSchema,
});

export type RegisterSchemaType = z.infer<typeof registerSchema>;

export const loginSchema = z.object({
  email: z.string().email("Invalid email address"),
  password: z.string().min(8, "Invalid password"),
});

export type LoginSchemaType = z.infer<typeof loginSchema>;
