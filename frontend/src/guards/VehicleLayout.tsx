import { Outlet } from "react-router-dom";
import { VehicleProvider } from "@/contexts/VehicleContext";

export function VehicleLayout() {
  return (
    <VehicleProvider>
      <Outlet />
    </VehicleProvider>
  );
}

