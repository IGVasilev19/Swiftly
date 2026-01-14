import type { Listing, UseListingOptions } from "@/types/listing";
import { useCallback, useEffect, useState } from "react";
import api from "./api";
import type { UseQueryReturn } from "@/types/types";

export function useListing(
  options?: UseListingOptions
): UseQueryReturn<Listing | Listing[]> {
  const { id: listingId, vehicleId, role } = options ?? {};
  const [data, setData] = useState<Listing[] | Listing | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchListing = useCallback(async () => {
    try {
      setIsLoading(true);
      setError(null);

      let url = "/listing";

      if (vehicleId && role === "OWNER") {
        url = `/listing/${vehicleId}`;
      } else if (listingId && role === "RENTER") {
        url = `/listing/${listingId}`;
      } else if (!vehicleId && !listingId) {
        url = "/listing";
      } else {
        throw new Error(
          "Invalid listing query: vehicleId requires OWNER role, listingId requires RENTER role"
        );
      }

      const response = await api.get<Listing[] | Listing>(url);

      setData(response.data);
    } catch (err) {
      setError(
        err instanceof Error ? err : new Error("Failed to fetch listing/s")
      );
      console.error("Error fetching listing/s:", err);
    } finally {
      setIsLoading(false);
    }
  }, [listingId, vehicleId, role]);

  useEffect(() => {
    fetchListing();
  }, [fetchListing]);

  return {
    data,
    isLoading,
    error,
    refetch: fetchListing,
  };
}
