import { Layout } from "@/components/layout/Layout";
import Loading from "@/components/ui/Loading";
import { VehicleDetailsCard } from "@/components/ui/VehicleDetailsCard";
import { Button } from "@/components/ui/button";
import { useVehicleContext } from "@/contexts/VehicleContext";
import { useGetVehicle } from "@/hooks/useGetVehicle";
import { useNavigate } from "react-router-dom";

export function VehicleDetails() {
  const navigate = useNavigate();
  const { selectedVehicleId } = useVehicleContext();
  const { vehicle, isLoading, error } = useGetVehicle(selectedVehicleId);

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
