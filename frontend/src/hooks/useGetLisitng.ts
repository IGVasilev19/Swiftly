import { useEffect, useState } from "react";
import api from "@/hooks/api";
import type { Listing, UseGetListingReturn } from "@/types/listing";

export function useGetListing(
  selectedListingId: number | null
): UseGetListingReturn {
  const [listing, setListing] = useState<Listing | null>(null);
  const [isLoading, setIsLoading] = useState(selectedListingId !== null);
  const [error, setError] = useState<Error | null>(null);

  const fetchListing = async (listingId: number | null) => {
    try {
      setIsLoading(true);
      setError(null);

      const response = await api.get<Listing>(`/listing/${listingId}`);

      setListing(response.data);
    } catch (err) {
      setError(
        err instanceof Error ? err : new Error("Failed to fetch listing")
      );

      console.error("Error fetching listing:", err);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (selectedListingId === null) {
      setIsLoading(false);
      setListing(null);
      setError(new Error("Listing ID is required"));
      return;
    }
    fetchListing(selectedListingId);
  }, [selectedListingId]);

  return {
    listing,
    isLoading,
    error,
    refetch: () => fetchListing(selectedListingId),
  };
}
