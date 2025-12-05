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

      images.forEach((file) => {
        formData.append("images", file);
      });

      const response = await api.post("/vehicle/add", formData, {
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem("accessToken")}`,
        },
        withCredentials: true,
      });

      toast.success(response.data.message);

      navigate("/vehicles");
    } catch (error: unknown) {
      const axiosError = error as AxiosError<{ message: string }>;

      toast.error(
        axiosError.response?.data?.message || "Failed to add vehicle"
      );
    } finally {
      setIsPending(false);
    }
  };

  return { addVehicle, isPending };
}
