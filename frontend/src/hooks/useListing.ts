import type { Listing } from "@/types/listing";
import { useCallback, useEffect, useState } from "react";
import api from "./api";
import type { UseByIdOptions, UseQueryReturn } from "@/types/types";

export function useListing(
  options?: UseByIdOptions
): UseQueryReturn<Listing | Listing[]> {
  const { id: listingId } = options ?? {};
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
        err instanceof Error ? err : new Error("Failed to fetch listing/s")
      );
      console.error("Error fetching listing/s:", err);
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
