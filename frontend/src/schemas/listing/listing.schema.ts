import z from "zod";
import { vehicleSchema } from "../vehicle/vehicle.schema";

export const listingSchema = z.object({
  vehicle: vehicleSchema,
  title: z.string().min(1, "Title is required"),
  description: z.string().min(1, "Description is required"),
  basePricePerDay: z.number().min(1, "Base price per day must be at least 1"),
  instantBook: z.boolean(),
  availabilityStart: z.string().min(1, "Availability start date is required"),
  availabilityEnd: z.string().min(1, "Availability end date is required"),
});

export type ListingSchemaType = z.infer<typeof listingSchema>;
