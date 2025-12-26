import type { UseFormReturn } from "react-hook-form/dist/types/form";
import type { Listing } from "./listing";
import type { Profile } from "./profile";
import type { BookingSchemaType } from "@/schemas/booking/booking.schema";

export interface Booking {
  id?: number;
  listing: Listing;
  renter?: Profile;
  startAt: Date;
  endAt: Date;
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

export interface UseGetBookingsReturn {
  bookings: Booking[];
  isLoading: boolean;
  error: Error | null;
  refetch: () => void;
}

export interface UseGetBookingReturn {
  booking: Booking | null;
  isLoading: boolean;
  error: Error | null;
  refetch: () => void;
}
