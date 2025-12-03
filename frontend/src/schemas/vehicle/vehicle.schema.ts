import { z } from "zod";

export const vehicleTypeSchema = z.enum([
  "SEDAN",
  "SUV",
  "HATCHBACK",
  "COUPE",
  "CONVERTIBLE",
  "WAGON",
  "PICKUP",
  "VAN",
  "MOTORCYCLE",
]);

export const fuelTypeSchema = z.enum([
  "GASOLINE",
  "DIESEL",
  "ELECTRIC",
  "HYBRID",
  "PLUGIN_HYBRID",
  "HYDROGEN",
]);

export const featureSchema = z.enum([
  "GPS",
  "BLUETOOTH",
  "USB_CHARGING",
  "BACKUP_CAMERA",
  "PARKING_SENSORS",
  "CRUISE_CONTROL",
  "LEATHER_SEATS",
  "SUNROOF",
  "ALL_WHEEL_DRIVE",
  "AUTOMATIC_TRANSMISSION",
]);

export const vehicleSchema = z.object({
  vin: z.string().min(1, "VIN is required").max(17, "VIN must be at most 17 characters"),
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
  features: z.array(featureSchema).default([]),
  country: z.string().min(1, "Country is required"),
  city: z.string().min(1, "City is required"),
});

export type VehicleSchemaType = z.infer<typeof vehicleSchema>;
export type VehicleType = z.infer<typeof vehicleTypeSchema>;
export type FuelType = z.infer<typeof fuelTypeSchema>;
export type Feature = z.infer<typeof featureSchema>;

