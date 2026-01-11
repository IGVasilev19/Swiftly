import { useState } from "react";
import type { AxiosError } from "axios";
import api from "@/hooks/api";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useDeleteVehicle() {
  const [isPending, setIsPending] = useState(false);
  const navigate = useNavigate();

  const deleteVehicle = async (vehicleId: number) => {
    try {
      setIsPending(true);

      const response = await api.delete(`/vehicle/${vehicleId}`);

      toast.success(response.data.message);
      navigate("/app/vehicles");
    } catch (error: unknown) {
      if (error && typeof error === "object" && "response" in error) {
        const axiosError = error as AxiosError<{ message?: string }>;
        toast.error(
          axiosError.response?.data?.message || "Failed to delete vehicle"
        );
      } else {
        toast.error("Failed to delete vehicle. Please try again.");
      }
    } finally {
      setIsPending(false);
    }
  };

  return { deleteVehicle, isPending };
}
