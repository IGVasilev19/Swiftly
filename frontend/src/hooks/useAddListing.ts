import React, { useState } from "react";
import api from "./api";
import { toast } from "sonner";
import { useNavigate } from "react-router-dom";
import type { AxiosError } from "axios";
import type { Listing } from "@/types/listing";

export function useAddListing() {
  const navigate = useNavigate();
  const [isPending, setIsPending] = useState(false);

  const addListing = async (data: Listing) => {
    console.log("Adding listing with data:", data);
    try {
      setIsPending(true);

      const response = await api.post("/listing", data);

      toast.success(response.data.message);
      navigate("/app/vehicles");
    } catch (error: unknown) {
      const axiosError = error as AxiosError<{ message: string }>;
      console.error(axiosError);
      toast.error(
        axiosError.response?.data?.message || "Failed to add listing"
      );
    } finally {
      setIsPending(false);
    }
  };
  return { addListing, isPending };
}
