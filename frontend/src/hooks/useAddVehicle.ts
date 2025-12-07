import { useState } from "react";
import type { AxiosError } from "axios";
import api from "@/hooks/api";
import type { VehicleSchemaType } from "@/schemas/vehicle/vehicle.schema";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useAddVehicle() {
  const [isPending, setIsPending] = useState(false);
  const navigate = useNavigate();

  const addVehicle = async (data: VehicleSchemaType) => {
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

      const response = await api.post("/vehicle/add", formData);

      toast.success(response.data.message);
      navigate("/app/vehicles");
    } catch (error: unknown) {
      if (error && typeof error === "object" && "response" in error) {
        const axiosError = error as AxiosError<{ message?: string }>;
        toast.error(
          axiosError.response?.data?.message || "Failed to add vehicle"
        );
      } else {
        toast.error("Failed to add vehicle. Please try again.");
      }
    } finally {
      setIsPending(false);
    }
  };

  return { addVehicle, isPending };
}
