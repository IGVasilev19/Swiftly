import React from "react";
import { DataTable, type Column } from "../ui/DataTable";
import type { Vehicle, VehicleTableProps } from "@/types/vehicle";

export function VehicleTable({
  vehicles,
  onRowClick,
  maxHeight = "600px",
  itemsPerPage = 10,
}: VehicleTableProps) {
  const columns: Column<Vehicle>[] = [
    {
      header: "VIN",
      render: (vehicle) => vehicle.vin,
      className: "w-[100px]",
    },
    {
      header: "Make",
      render: (vehicle) => vehicle.make,
    },
    {
      header: "Model",
      render: (vehicle) => vehicle.model,
    },
    {
      header: "Year",
      render: (vehicle) => vehicle.year || "-",
    },
    {
      header: "Color",
      render: (vehicle) => vehicle.color || "-",
    },
    {
      header: "Listed",
      render: (vehicle) => (vehicle.listed ? "Yes" : "No"),
      className: "text-right",
    },
  ];

  return (
    <DataTable
      data={vehicles}
      columns={columns}
      getKey={(vehicle, index) => vehicle.id || vehicle.vin || index}
      emptyMessage="No vehicles found"
      itemName="vehicles"
      onRowClick={onRowClick}
      maxHeight={maxHeight}
      itemsPerPage={itemsPerPage}
    />
  );
}
