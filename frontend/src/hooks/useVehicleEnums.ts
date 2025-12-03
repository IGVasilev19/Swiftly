import { useState, useEffect } from "react";
import api from "@/hooks/api";

export type VehicleTypeEnum = string;
export type FuelTypeEnum = string;
export type FeatureEnum = string;

interface VehicleEnums {
  vehicleTypes: VehicleTypeEnum[];
  fuelTypes: FuelTypeEnum[];
  features: FeatureEnum[];
  isLoading: boolean;
  error: Error | null;
}

export function useVehicleEnums(): VehicleEnums {
  const [vehicleTypes, setVehicleTypes] = useState<VehicleTypeEnum[]>([]);
  const [fuelTypes, setFuelTypes] = useState<FuelTypeEnum[]>([]);
  const [features, setFeatures] = useState<FeatureEnum[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  useEffect(() => {
    const fetchEnums = async () => {
      try {
        setIsLoading(true);
        setError(null);

        const [vehicleTypesRes, fuelTypesRes, featuresRes] = await Promise.all([
          api.get<VehicleTypeEnum[]>("/enums/vehicle-types"),
          api.get<FuelTypeEnum[]>("/enums/fuel-types"),
          api.get<FeatureEnum[]>("/enums/features"),
        ]);

        setVehicleTypes(vehicleTypesRes.data);
        setFuelTypes(fuelTypesRes.data);
        setFeatures(featuresRes.data);
      } catch (err) {
        setError(
          err instanceof Error ? err : new Error("Failed to fetch enums")
        );
        console.error("Error fetching vehicle enums:", err);
      } finally {
        setIsLoading(false);
      }
    };

    fetchEnums();
  }, []);

  return {
    vehicleTypes,
    fuelTypes,
    features,
    isLoading,
    error,
  };
}
