import type { Listing, UseGetListingsReturn } from "@/types/listing";
import { useEffect, useState } from "react";
import api from "./api";

export function useGetListings(): UseGetListingsReturn {
  const [listings, setListings] = useState<Listing[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<Error | null>(null);

  const fetchListings = async () => {
    try {
      setIsLoading(true);
      setError(null);

      const response = await api.get<Listing[]>("/listing");

      setListings(response.data);
    } catch (err) {
      setError(
        err instanceof Error ? err : new Error("Failed to fetch listings")
      );
      console.error("Error fetching listings:", err);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchListings();
  }, []);

  return {
    listings,
    isLoading,
    error,
    refetch: fetchListings,
  };
}
