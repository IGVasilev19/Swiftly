import { Button } from "@/components/ui/button";
import { Layout } from "@/components/ui/Layout";
import Loading from "@/components/ui/Loading";
import React from "react";
import { useNavigate } from "react-router-dom";
import { useGetVehicles } from "@/hooks/useGetVehicles";
import { VehicleTable } from "@/components/vehicle/VehicleTable";
import { useVehicleContext } from "@/contexts/VehicleContext";

export function Vehicles() {
  const navigate = useNavigate();
  const { vehicles, isLoading, error } = useGetVehicles();
  const { setSelectedVehicleId } = useVehicleContext();

  const handleRowClick = (vehicle: { id?: number }) => {
    setSelectedVehicleId(vehicle.id ?? null);
    navigate(`/vehicles/details`);
  };

  if (isLoading) {
    return (
      <Layout>
        <div className="w-full h-full flex items-center justify-center">
          <Loading />
        </div>
      </Layout>
    );
  }

  if (error) {
    return (
      <Layout>
        <div className="w-full h-full flex items-center justify-center">
          <div className="text-red-500">
            Error loading vehicles: {error.message}
          </div>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="w-full h-full flex flex-col pt-4 pl-4 pr-4 gap-5">
        <div className="flex justify-end">
          <Button
            variant="default"
            className="w-50px bg-[#0F172A] hover:bg-[#0f172adc] select-none"
            onClick={() => navigate("/vehicles/add")}
          >
            Add +
          </Button>
        </div>
        <div className="h-full w-full flex">
          <VehicleTable vehicles={vehicles} onRowClick={handleRowClick} />
        </div>
      </div>
    </Layout>
  );
}
