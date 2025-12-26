import z from "zod";

export const bookingSchema = z.object({
  dateRange: z
    .object({
      from: z.date({
        required_error: "Start date is required",
      }),
      to: z.date({
        required_error: "End date is required",
      }),
    })
    .refine((range) => !range.to || !range.from || range.to >= range.from, {
      message: "End date must be the same or after the start date",
      path: ["to"],
    }),
});

export type BookingSchemaType = z.infer<typeof bookingSchema>;
