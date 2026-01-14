import { useState } from "react";
import type { AxiosError } from "axios";
import api from "@/hooks/api";
import type { ListingUpdateSchemaType } from "@/schemas/listing/listing.schema";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useUpdateListing() {
  const [isPending, setIsPending] = useState(false);
  const navigate = useNavigate();

  const updateListing = async (
    listingId: number,
    data: ListingUpdateSchemaType
  ) => {
    try {
      setIsPending(true);

      const response = await api.put(`/listing/${listingId}`, data);

      toast.success(response.data.message);
      navigate("/app/vehicles");
    } catch (error: unknown) {
      if (error && typeof error === "object" && "response" in error) {
        const axiosError = error as AxiosError<{ message?: string }>;
        toast.error(
          axiosError.response?.data?.message || "Failed to update listing"
        );
      } else {
        toast.error("Failed to update listing. Please try again.");
      }
    } finally {
      setIsPending(false);
    }
  };

  const reactivateListing = async (listingId: number) => {
    try {
      setIsPending(true);

      const response = await api.patch(`/listing/${listingId}`);

      toast.success(
        response.data?.message || "Listing reactivated successfully"
      );
      navigate("/app/vehicles");
    } catch (error: unknown) {
      if (error && typeof error === "object" && "response" in error) {
        const axiosError = error as AxiosError<{
          success?: boolean;
          message?: string;
        }>;

        if (
          axiosError.response?.status === 400 &&
          axiosError.response?.data?.success === true
        ) {
          const message =
            axiosError.response.data.message ||
            "Listing reactivated successfully";
          toast.success(message);
          navigate("/app/vehicles");
        } else {
          toast.error(
            axiosError.response?.data?.message || "Failed to reactivate listing"
          );
        }
      } else {
        toast.error("Failed to reactivate listing. Please try again.");
      }
    } finally {
      setIsPending(false);
    }
  };

  return { updateListing, reactivateListing, isPending };
}
