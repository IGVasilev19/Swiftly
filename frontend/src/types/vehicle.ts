import type { SelectMenuOption } from "@/lib/types";
import type {
  VehicleSchemaType,
  VehicleUpdateSchemaType,
} from "@/schemas/vehicle/vehicle.schema";
import type { UseFormReturn } from "react-hook-form/dist/types/form";
import type { Profile } from "./profile";

export interface AddVehicleFormProps {
  addVehicleForm: UseFormReturn<VehicleSchemaType | VehicleUpdateSchemaType>;
  handleSubmit: (data: VehicleSchemaType | VehicleUpdateSchemaType) => void;
  isPending: boolean;
  vehicleTypesOptions: { value: string; label: string }[];
  fuelTypesOptions: { value: string; label: string }[];
  featuresOptions: { value: string; label: string }[];
  countryOpen: boolean;
  setCountryOpen: (open: boolean) => void;
  selectedCountry: SelectMenuOption | null;
  onDelete?: () => void;
  isDeleting?: boolean;
}

export interface VehicleImage {
  id?: number;
  fileName?: string;
  mimeType?: string;
  uploadedAt?: string;
  data?: string;
  [key: string]: unknown;
}

export interface Vehicle {
  id?: number;
  owner?: Profile;
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
  isRemoved?: boolean;
  images?: VehicleImage[];
  [key: string]: unknown;
}

export interface VehicleTableProps {
  vehicles: Vehicle[];
  onRowClick?: (vehicle: Vehicle) => void;
  maxHeight?: string;
  itemsPerPage?: number;
}
