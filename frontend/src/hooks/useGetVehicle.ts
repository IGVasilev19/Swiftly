import { useEffect, useState } from "react";
import type { UseGetVehicleReturn } from "@/types/vehicle";
import type { Vehicle } from "@/types/vehicle";
import api from "@/hooks/api";

export function useGetVehicle(
  selectedVehicleId: number | null
): UseGetVehicleReturn {
  const [vehicle, setVehicle] = useState<Vehicle | null>(null);
  const [isLoading, setIsLoading] = useState(selectedVehicleId !== null);
  const [error, setError] = useState<Error | null>(null);

  const fetchVehicle = async (vehicleId: number | null) => {
    try {
      setIsLoading(true);
      setError(null);

      const response = await api.get<Vehicle>(`/vehicle/${vehicleId}`);

      setVehicle(response.data);
    } catch (err) {
      setError(
        err instanceof Error ? err : new Error("Failed to fetch vehicle")
      );

      console.error("Error fetching vehicle:", err);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (selectedVehicleId === null) {
      setIsLoading(false);
      setVehicle(null);
      setError(new Error("Vehicle ID is required"));
      return;
    }
    fetchVehicle(selectedVehicleId);
  }, [selectedVehicleId]);

  return {
    vehicle,
    isLoading,
    error,
    refetch: () => fetchVehicle(selectedVehicleId),
  };
}
