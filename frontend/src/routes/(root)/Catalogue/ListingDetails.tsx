import { Button } from "@/components/ui/button";
import { ListingDetailsCard } from "@/components/ui/ListingDetailsCard";
import Loading from "@/components/ui/Loading";
import { useListingContext } from "@/contexts/ListingContext";
import { useListing } from "@/hooks/useListing";
import type { Listing } from "@/types/listing";
import { Layout } from "lucide-react";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export function ListingDetails() {
  const navigate = useNavigate();
  const { selectedListingId } = useListingContext();
  const { data, isLoading, error } = useListing({
    id: selectedListingId,
  });
  const listing: Listing | null = data && !Array.isArray(data) ? data : null;

  useEffect(() => {
    document.body.style.overflow = "auto";
    return () => {
      document.body.style.overflow = "auto";
    };
  }, []);

  if (isLoading) {
    return (
      <Layout>
        <div className="w-full h-full flex items-center justify-center">
          <Loading />
        </div>
      </Layout>
    );
  }

  if (error || !listing) {
    return (
      <div className="w-full h-full flex items-center justify-center">
        <div className="text-center">
          <p className="text-red-500 mb-4">
            {error?.message || "Listing not found"}
          </p>
          <Button variant="outline" onClick={() => navigate("/app/catalogue")}>
            Back to catalogue
          </Button>
        </div>
      </div>
    );
  }

  return (
    <div className="w-full h-full flex flex-col overflow-y-auto hide-scrollbar">
      <ListingDetailsCard listing={listing} />
    </div>
  );
}
