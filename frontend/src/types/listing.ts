import type {
  ListingSchemaType,
  ListingUpdateSchemaType,
} from "@/schemas/listing/listing.schema";
import type { Vehicle } from "./vehicle";
import type { UseFormReturn } from "react-hook-form/dist/types/form";

export interface Listing {
  id?: number;
  vehicle: Vehicle;
  title: string;
  description: string;
  creationDate?: string;
  basePricePerDay: number;
  instantBook: boolean;
  isRemoved?: boolean;
}

export interface AddListingFromProps {
  addListingFrom: UseFormReturn<ListingSchemaType | ListingUpdateSchemaType>;
  handleSubmit: (data: ListingSchemaType | ListingUpdateSchemaType) => void;
  isPending: boolean;
  mode?: "add" | "update";
  onDelete?: () => void;
  isDeleting?: boolean;
}

export type ListingQueryRole = "RENTER" | "OWNER";

export type UseListingOptions = {
  id?: number | null;
  vehicleId?: number | null;
  role?: ListingQueryRole;
};
