"use client";

import * as React from "react";

import { Calendar } from "@/components/ui/calendar";
import type { Calendar05Props } from "@/types/types";
import { useBooking } from "@/hooks/useBooking";
import { useListingContext } from "@/contexts/ListingContext";
import type { Booking } from "@/types/booking";
import { parseISO } from "date-fns";

export function Calendar05({ value, onChange }: Calendar05Props) {
  const { selectedListingId } = useListingContext();
  const { data } = useBooking({ listingId: selectedListingId });
  const bookings: Booking[] = Array.isArray(data) ? data : [];
  const today = new Date();

  const disabledDates = [
    { before: today },
    ...bookings.map((booking) => ({
      from: parseISO(booking.startAt),
      to: parseISO(booking.endAt),
    })),
  ];

  return (
    <Calendar
      mode="range"
      defaultMonth={value?.from ?? today}
      selected={value}
      onSelect={onChange}
      numberOfMonths={2}
      disabled={disabledDates}
      className="rounded-lg border shadow-sm w-full"
    />
  );
}

// Also implement showing the booking and opening the booking for renters and owners.
