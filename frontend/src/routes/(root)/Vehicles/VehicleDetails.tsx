import { Layout } from "@/components/layout/Layout";
import { Button } from "@/components/ui/Button";
import Loading from "@/components/ui/Loading";
import { VehicleDetailsCard } from "@/components/ui/VehicleDetailsCard";
import { useVehicleContext } from "@/contexts/VehicleContext";
import { useVehicle } from "@/hooks/useVehicle";
import type { Vehicle } from "@/types/vehicle";
import { useNavigate } from "react-router-dom";

export function VehicleDetails() {
  const navigate = useNavigate();
  const { selectedVehicleId } = useVehicleContext();
  const { data, isLoading, error } = useVehicle({
    id: selectedVehicleId,
  });
  const vehicle: Vehicle | null = data && !Array.isArray(data) ? data : null;

  if (isLoading) {
    return (
      <Layout>
        <div className="w-full h-full flex items-center justify-center">
          <Loading />
        </div>
      </Layout>
    );
  }

  if (error || !vehicle) {
    return (
      <div className="w-full h-full flex items-center justify-center">
        <div className="text-center">
          <p className="text-red-500 mb-4">
            {error?.message || "Vehicle not found"}
          </p>
          <Button variant="outline" onClick={() => navigate("/app/vehicles")}>
            Back to Vehicles
          </Button>
        </div>
      </div>
    );
  }

  return (
    <div className="w-full h-full flex flex-col overflow-y-auto hide-scrollbar">
      <VehicleDetailsCard vehicle={vehicle} />
    </div>
  );
}
