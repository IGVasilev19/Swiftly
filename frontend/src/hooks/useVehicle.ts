import { useState, useEffect, useCallback } from "react";
import api from "@/hooks/api";
import type {
  UseVehicleReturn,
  UseVehiclesOptions,
  Vehicle,
} from "@/types/vehicle";

export function useVehicle(
  options?: UseVehiclesOptions
): UseVehicleReturn<Vehicle | Vehicle[]> {
  const { vehicleId } = options ?? {};
  const [data, setData] = useState<Vehicle[] | Vehicle | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchVehicle = useCallback(async () => {
    try {
      setIsLoading(true);
      setError(null);

      const url = vehicleId ? `/vehicle/${vehicleId}` : `/vehicle/owned`;

      const response = await api.get<Vehicle[] | Vehicle>(url);

      setData(response.data);
    } catch (err) {
      setError(
        err instanceof Error ? err : new Error("Failed to fetch vehicle/s")
      );
      console.error("Error fetching vehicles:", err);
    } finally {
      setIsLoading(false);
    }
  }, [vehicleId]);

  useEffect(() => {
    fetchVehicle();
  }, [fetchVehicle]);

  return {
    data,
    isLoading,
    error,
    refetch: fetchVehicle,
  };
}
