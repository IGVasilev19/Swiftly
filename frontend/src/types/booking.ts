import type { UseFormReturn } from "react-hook-form/dist/types/form";
import type { Listing } from "./listing";
import type { Profile } from "./profile";
import type { BookingSchemaType } from "@/schemas/booking/booking.schema";

export interface Booking {
  id?: number;
  listing: Listing;
  renter?: Profile;
  startAt: string;
  endAt: string;
  createdAt?: Date;
  status?: string;
  totalPrice?: number;
  [key: string]: unknown;
}

export interface AddBookingFormProps {
  addBookingForm: UseFormReturn<BookingSchemaType>;
  handleSubmit: (data: BookingSchemaType) => void;
  isPending: boolean;
}

export type BookingQueryRole = "RENTER" | "OWNER";

export type UseBookingOptions = {
  id?: number | null;
  listingId?: number | null;
  role?: BookingQueryRole;
};
