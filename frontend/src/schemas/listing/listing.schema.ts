import z from "zod";

export const listingSchema = z.object({
  title: z.string().min(1, "Title is required"),
  description: z.string().min(1, "Description is required"),
  basePricePerDay: z.number().min(1, "Base price per day must be at least 1"),
  instantBook: z.boolean(),
});

export const listingUpdateSchema = listingSchema;

export type ListingSchemaType = z.infer<typeof listingSchema>;
export type ListingUpdateSchemaType = z.infer<typeof listingUpdateSchema>;