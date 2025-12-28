import { useCallback, useEffect, useState } from "react";
import api from "./api";
import type { UseQueryReturn } from "@/types/types";
import type { Booking, UseBookingOptions } from "@/types/booking";

export function useBooking(
  options?: UseBookingOptions
): UseQueryReturn<Booking | Booking[]> {
  const { id: bookingId, listingId, role } = options ?? {};
  const [data, setData] = useState<Booking[] | Booking | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchBooking = useCallback(async () => {
    try {
      setIsLoading(true);
      setError(null);

      let url = "/bookings";

      if (bookingId) {
        url = `/booking/${bookingId}`;
      } else if (listingId) {
        url = `/booking/listing/${listingId}`;
      } else if (role === "RENTER") {
        url = "/booking/me";
      } else if (role === "OWNER") {
        url = "/booking/owned";
      }

      const response = await api.get<Booking[] | Booking>(url);

      setData(response.data);
    } catch (err) {
      console.log(err);
      setError(
        err instanceof Error ? err : new Error("Failed to fetch booking/s")
      );
      console.error("Error fetching bookings:", err);
    } finally {
      setIsLoading(false);
    }
  }, [bookingId, listingId, role]);

  useEffect(() => {
    fetchBooking();
  }, [fetchBooking]);
  return {
    data,
    isLoading,
    error,
    refetch: fetchBooking,
  };
}
