import { useState } from "react";
import type { AxiosError } from "axios";
import api from "@/hooks/api";
import type { VehicleUpdateSchemaType } from "@/schemas/vehicle/vehicle.schema";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useUpdateVehicle() {
  const [isPending, setIsPending] = useState(false);
  const navigate = useNavigate();

  const updateVehicle = async (
    vehicleId: number,
    data: VehicleUpdateSchemaType
  ) => {
    try {
      setIsPending(true);

      const formData = new FormData();

      const { images, ...vehicleData } = data;

      const vehicleDataBlob = new Blob([JSON.stringify(vehicleData)], {
        type: "application/json",
      });
      formData.append("vehicleData", vehicleDataBlob);

      if (images && Array.isArray(images)) {
        images.forEach((file) => {
          formData.append("images", file);
        });
      }

      const response = await api.put(`/vehicle/${vehicleId}`, formData);

      toast.success(response.data.message);
      navigate("/app/vehicles");
    } catch (error: unknown) {
      if (error && typeof error === "object" && "response" in error) {
        const axiosError = error as AxiosError<{ message?: string }>;
        toast.error(
          axiosError.response?.data?.message || "Failed to update vehicle"
        );
      } else {
        toast.error("Failed to update vehicle. Please try again.");
      }
    } finally {
      setIsPending(false);
    }
  };

  return { updateVehicle, isPending };
}
