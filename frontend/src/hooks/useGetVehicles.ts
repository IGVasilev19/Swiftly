import { useState, useEffect } from "react";
import api from "@/hooks/api";
import type { UseGetVehiclesReturn, Vehicle } from "@/types/vehicle";

export function useGetVehicles(): UseGetVehiclesReturn {
  const [vehicles, setVehicles] = useState<Vehicle[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchVehicles = async () => {
    try {
      setIsLoading(true);
      setError(null);

      const response = await api.get<Vehicle[]>("/vehicle/owned");

      setVehicles(response.data);
    } catch (err) {
      setError(
        err instanceof Error ? err : new Error("Failed to fetch vehicles")
      );
      console.error("Error fetching vehicles:", err);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchVehicles();
  }, []);

  return {
    vehicles,
    isLoading,
    error,
    refetch: fetchVehicles,
  };
}
