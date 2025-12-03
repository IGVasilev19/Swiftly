import { z } from "zod";

export const createEnumSchema = (values: string[], fieldName: string) =>
  z
    .string()
    .min(1, `${fieldName} is required`)
    .refine(
      (val) => values.includes(val),
      (val) => ({
        message: `Invalid ${fieldName.toLowerCase()}: ${val}`,
      })
    );

export const createEnumArraySchema = (values: string[], fieldName: string) =>
  z
    .array(z.string())
    .refine((arr) => arr.every((val) => values.includes(val)), {
      message: `One or more ${fieldName.toLowerCase()} are invalid`,
    });

export const vehicleSchema = z.object({
  vin: z
    .string()
    .min(1, "VIN is required")
    .max(17, "VIN must be at most 17 characters"),
  make: z.string().min(1, "Make is required"),
  model: z.string().min(1, "Model is required"),
  color: z.string().min(1, "Color is required"),
  year: z
    .number()
    .int("Year must be an integer")
    .min(1900, "Year must be at least 1900")
    .max(new Date().getFullYear() + 1, "Year cannot be in the future"),
  type: z.string().min(1, "Vehicle type is required"),
  fuelType: z.string().min(1, "Fuel type is required"),
  fuelConsumption: z
    .number()
    .positive("Fuel consumption must be positive")
    .min(0.1, "Fuel consumption must be at least 0.1")
    .optional(),
  features: z.array(z.string()),
  country: z.string().min(1, "Country is required"),
  city: z.string().min(1, "City is required"),
});

export type VehicleSchemaType = z.infer<typeof vehicleSchema>;

export const createVehicleSchema = (
  vehicleTypes: string[],
  fuelTypes: string[],
  features: string[]
) => {
  const vehicleTypeSchema = createEnumSchema(vehicleTypes, "Vehicle type");
  const fuelTypeSchema = createEnumSchema(fuelTypes, "Fuel type");
  const featureSchema = createEnumArraySchema(features, "Feature");

  return z.object({
    vin: z
      .string()
      .min(1, "VIN is required")
      .max(17, "VIN must be at most 17 characters"),
    make: z.string().min(1, "Make is required"),
    model: z.string().min(1, "Model is required"),
    color: z.string().min(1, "Color is required"),
    year: z
      .number()
      .int("Year must be an integer")
      .min(1900, "Year must be at least 1900")
      .max(new Date().getFullYear() + 1, "Year cannot be in the future"),
    type: vehicleTypeSchema,
    fuelType: fuelTypeSchema,
    fuelConsumption: z
      .number()
      .positive("Fuel consumption must be positive")
      .min(0.1, "Fuel consumption must be at least 0.1")
      .optional(),
    features: featureSchema,
    country: z.string().min(1, "Country is required"),
    city: z.string().min(1, "City is required"),
  });
};

export type VehicleType = string;
export type FuelType = string;
export type Feature = string;
