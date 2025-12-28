import type {
  Listing,
  UseListingOptions,
  UseListingReturn,
} from "@/types/listing";
import { useCallback, useEffect, useState } from "react";
import api from "./api";

export function useListing(
  options?: UseListingOptions
): UseListingReturn<Listing | Listing[]> {
  const { listingId } = options ?? {};
  const [data, setData] = useState<Listing[] | Listing | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchListing = useCallback(async () => {
    try {
      setIsLoading(true);
      setError(null);

      const url = listingId ? `/listing/${listingId}` : `/listing`;

      const response = await api.get<Listing[] | Listing>(url);

      setData(response.data);
    } catch (err) {
      setError(
        err instanceof Error ? err : new Error("Failed to fetch listings")
      );
      console.error("Error fetching listings:", err);
    } finally {
      setIsLoading(false);
    }
  }, [listingId]);

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
