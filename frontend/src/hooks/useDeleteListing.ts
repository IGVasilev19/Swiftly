import { useState } from "react";
import type { AxiosError } from "axios";
import api from "@/hooks/api";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useDeleteListing() {
  const [isPending, setIsPending] = useState(false);
  const navigate = useNavigate();

  const deleteListing = async (listingId: number) => {
    try {
      setIsPending(true);

      const response = await api.delete(`/listing/${listingId}`);

      toast.success(response.data.message);
      navigate("/app/vehicles");
    } catch (error: unknown) {
      if (error && typeof error === "object" && "response" in error) {
        const axiosError = error as AxiosError<{ message?: string }>;
        toast.error(
          axiosError.response?.data?.message || "Failed to delete listing"
        );
      } else {
        toast.error("Failed to delete listing. Please try again.");
      }
    } finally {
      setIsPending(false);
    }
  };

  return { deleteListing, isPending };
}
