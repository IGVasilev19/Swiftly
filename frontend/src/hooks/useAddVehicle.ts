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
      const response = await api.post("/vehicles", data);
      toast.success(response.data.message || "Vehicle added successfully");
      navigate("/vehicles");
    } catch (error: unknown) {
      const axiosError = error as AxiosError<{ message: string }>;
      console.error(axiosError);
      toast.error(
        axiosError.response?.data?.message || "Failed to add vehicle"
      );
    } finally {
      setIsPending(false);
    }
  };

  return { addVehicle, isPending };
}

