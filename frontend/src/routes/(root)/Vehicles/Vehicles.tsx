import { Button } from "@/components/ui/Button";
import { Layout } from "@/components/layout/Layout";
import Loading from "@/components/ui/Loading";
import React from "react";
import { useNavigate } from "react-router-dom";
import { useVehicle } from "@/hooks/useVehicle";
import { VehicleTable } from "@/components/vehicle/VehicleTable";
import { useVehicleContext } from "@/contexts/VehicleContext";
import type { Vehicle } from "@/types/vehicle";

export function Vehicles() {
  const navigate = useNavigate();
  const { data, isLoading, error } = useVehicle();
  const allVehicles: Vehicle[] = Array.isArray(data) ? data : [];
  const vehicles = allVehicles.filter((vehicle) => !vehicle.isRemoved);
  const { setSelectedVehicleId } = useVehicleContext();

  const handleRowClick = (vehicle: { id?: number }) => {
    setSelectedVehicleId(vehicle.id ?? null);
    navigate("/app/vehicles/details");
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
            onClick={() => navigate("/app/vehicles/add")}
          >
            Add +
          </Button>
        </div>
        <div className="h-full w-full flex">
          <VehicleTable
            vehicles={vehicles}
            onRowClick={handleRowClick}
          />
        </div>
      </div>
    </Layout>
  );
}
