import type { ListingSchemaType } from "@/schemas/listing/listing.schema";
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
}

export interface AddListingFromProps {
  addListingFrom: UseFormReturn<ListingSchemaType>;
  handleSubmit: (data: ListingSchemaType) => void;
  isPending: boolean;
}

export interface UseGetListingsReturn {
  listings: Listing[];
  isLoading: boolean;
  error: Error | null;
  refetch: () => void;
}

export interface UseGetListingReturn {
  listing: Listing | null;
  isLoading: boolean;
  error: Error | null;
  refetch: () => void;
}
