import type { SelectMenuOption } from "@/lib/types";
import type { VehicleSchemaType } from "@/schemas/vehicle/vehicle.schema";
import type { UseFormReturn } from "react-hook-form/dist/types/form";

export interface AddVehicleFormProps {
  addVehicleForm: UseFormReturn<VehicleSchemaType>;
  handleSubmit: (data: VehicleSchemaType) => void;
  isPending: boolean;
  vehicleTypesOptions: { value: string; label: string }[];
  fuelTypesOptions: { value: string; label: string }[];
  featuresOptions: { value: string; label: string }[];
  countryOpen: boolean;
  setCountryOpen: (open: boolean) => void;
  selectedCountry: SelectMenuOption | null;
}

export interface Vehicle {
  id?: number;
  vin: string;
  make: string;
  model: string;
  color?: string;
  year?: number;
  type?: string;
  fuelType?: string;
  fuelConsumption?: number;
  features?: string[];
  country?: string;
  city?: string;
  listed?: boolean;
  [key: string]: unknown;
}

export interface VehicleTableProps {
  vehicles: Vehicle[];
  onRowClick?: (vehicle: Vehicle) => void;
  maxHeight?: string;
  itemsPerPage?: number;
}
