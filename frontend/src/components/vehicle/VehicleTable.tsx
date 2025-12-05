import React, { useState, useMemo } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "../ui/table";
import { Button } from "../ui/button";
import type { Vehicle, VehicleTableProps } from "@/types/vehicle";
import { ChevronLeft, ChevronRight } from "lucide-react";

export function VehicleTable({
  vehicles,
  onRowClick,
  maxHeight = "600px",
  itemsPerPage = 10,
}: VehicleTableProps) {
  const [currentPage, setCurrentPage] = useState(1);

  const totalPages = Math.ceil(vehicles.length / itemsPerPage);
  const startIndex = (currentPage - 1) * itemsPerPage;
  const endIndex = startIndex + itemsPerPage;
  const paginatedVehicles = useMemo(
    () => vehicles.slice(startIndex, endIndex),
    [vehicles, startIndex, endIndex]
  );

  const handleRowClick = (vehicle: Vehicle) => {
    if (onRowClick) {
      onRowClick(vehicle);
    }
  };

  const handlePreviousPage = () => {
    setCurrentPage((prev) => Math.max(1, prev - 1));
  };

  const handleNextPage = () => {
    setCurrentPage((prev) => Math.min(totalPages, prev + 1));
  };

  return (
    <div className="w-full h-full flex flex-col">
      <div
        className="w-full overflow-auto"
        style={{ maxHeight, minHeight: "400px" }}
      >
        <Table>
          <TableHeader className="sticky top-0 bg-white z-10">
            <TableRow className="hover:bg-transparent">
              <TableHead className="w-[100px] select-none">VIN</TableHead>
              <TableHead className="select-none">Make</TableHead>
              <TableHead className="select-none">Model</TableHead>
              <TableHead className="select-none">Year</TableHead>
              <TableHead className="select-none">Color</TableHead>
              <TableHead className="text-right select-none">Listed</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {paginatedVehicles.length === 0 ? (
              <TableRow>
                <TableCell
                  colSpan={6}
                  className="text-center text-muted-foreground"
                >
                  No vehicles found
                </TableCell>
              </TableRow>
            ) : (
              paginatedVehicles.map((vehicle, index) => (
                <TableRow
                  key={vehicle.id || vehicle.vin || index}
                  className="cursor-pointer hover:bg-muted/50 transition-colors"
                  onClick={() => handleRowClick(vehicle)}
                >
                  <TableCell>{vehicle.vin}</TableCell>
                  <TableCell>{vehicle.make}</TableCell>
                  <TableCell>{vehicle.model}</TableCell>
                  <TableCell>{vehicle.year || "-"}</TableCell>
                  <TableCell>{vehicle.color || "-"}</TableCell>
                  <TableCell className="text-right">
                    {vehicle.listed ? "Yes" : "No"}
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </div>
      {totalPages > 1 && (
        <div className="flex items-center justify-between px-2 py-4 border-t">
          <div className="text-sm text-muted-foreground">
            Showing {startIndex + 1} to {Math.min(endIndex, vehicles.length)} of{" "}
            {vehicles.length} vehicles
          </div>
          <div className="flex items-center gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={handlePreviousPage}
              disabled={currentPage === 1}
            >
              <ChevronLeft className="h-4 w-4" />
              Previous
            </Button>
            <div className="text-sm">
              Page {currentPage} of {totalPages}
            </div>
            <Button
              variant="outline"
              size="sm"
              onClick={handleNextPage}
              disabled={currentPage === totalPages}
            >
              Next
              <ChevronRight className="h-4 w-4" />
            </Button>
          </div>
        </div>
      )}
    </div>
  );
}
