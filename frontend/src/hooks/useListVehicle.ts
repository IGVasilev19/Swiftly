import { useState } from "react";
import type { AxiosError } from "axios";
import api from "@/hooks/api";
import { toast } from "sonner";

export function useListVehicle() {
  const [isPending, setIsPending] = useState(false);

  const listVehicle = async (vehicleId: number) => {
    try {
      setIsPending(true);

      const response = await api.patch(`/vehicle/${vehicleId}/list`, {});

      toast.success(response.data.message || "Vehicle listed successfully");
      return true;
    } catch (error: unknown) {
      const axiosError = error as AxiosError<{ message?: string }>;
      toast.error(
        axiosError.response?.data?.message || "Failed to list vehicle"
      );
      return false;
    } finally {
      setIsPending(false);
    }
  };

  return { listVehicle, isPending };
}
