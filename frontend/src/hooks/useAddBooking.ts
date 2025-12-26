import type { Booking } from "@/types/booking";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "./api";
import { toast } from "sonner";
import type { AxiosError } from "axios";

export function useAddBooking() {
  const navigate = useNavigate();
  const [isPending, setIsPending] = useState(false);

  const addBooking = async (data: Booking) => {
    try {
      setIsPending(true);

      const response = await api.post("/booking", data);

      toast.success(response.data.message);
      navigate("/app/bookings");
    } catch (error: unknown) {
      const axiosError = error as AxiosError<{ message: string }>;
      console.error(axiosError);
      toast.error(
        axiosError.response?.data?.message || "Failed to add booking"
      );
    } finally {
      setIsPending(false);
    }
  };
  return { addBooking, isPending };
}
