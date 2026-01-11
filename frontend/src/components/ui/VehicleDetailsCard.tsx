import type { Vehicle, VehicleImage } from "@/types/vehicle";
import { ArrowLeft } from "lucide-react";
import React, { useState } from "react";
import { Button } from "./Button";
import { VehicleDetailPlaceholder } from "./VehicleDetailPlaceholder";
import { VehicleImageGallery } from "./VehicleImageGallery";
import { useNavigate } from "react-router-dom";
import { AddListingForm } from "../listing/AddListingForm";
import { useAddListing } from "@/hooks/useAddListing";
import {
  listingSchema,
  type ListingSchemaType,
} from "@/schemas/listing/listing.schema";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import type { Listing } from "@/types/listing";

const formatEnumLabel = (value: string): string => {
  return value
    .split("_")
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
    .join(" ");
};

export function VehicleDetailsCard({ vehicle }: { vehicle: Vehicle }) {
  const vehicleImages: VehicleImage[] =
    vehicle?.images && Array.isArray(vehicle.images) ? vehicle.images : [];

  const form = useForm<ListingSchemaType>({
    resolver: zodResolver(listingSchema),
    defaultValues: {
      title: "",
      description: "",
      basePricePerDay: 0,
      instantBook: true,
    },
  });

  const [state, setState] = useState(0);
  const navigate = useNavigate();
  const { addListing, isPending } = useAddListing();

  const handleListingSubmit = (data: ListingSchemaType) => {
    const listing: Listing = {
      vehicle: vehicle,
      title: data.title,
      description: data.description,
      basePricePerDay: data.basePricePerDay,
      instantBook: data.instantBook,
    };

    addListing(listing);
  };

  return (
    <div className="flex-1 overflow-y-auto hide-scrollbar min-h-0">
      <div className="max-w-4xl mx-auto p-6 pb-8">
        <div className="mb-6">
          <Button
            variant="ghost"
            onClick={() => navigate("/app/vehicles")}
            className="mb-4"
          >
            <ArrowLeft className="h-4 w-4 mr-2" />
            Back to Vehicles
          </Button>
          <div className="flex justify-between items-center">
            <h1 className="text-3xl font-bold text-[#0F172A]">
              {state === 0 ? "Vehicle Details" : "Listing Details"}
            </h1>
            {!vehicle.listed ? (
              <Button
                variant="default"
                className={
                  state === 0
                    ? "bg-[#0F172A] hover:bg-[#0f172adc]"
                    : "bg-red-700 hover:bg-red-800"
                }
                onClick={() => setState(state === 0 ? 1 : 0)}
              >
                {state === 0 ? "List Vehicle +" : "Cancel 𐌗"}
              </Button>
            ) : (
              <div className="flex gap-2">
                <Button
                  variant="default"
                  className="bg-[#0F172A] hover:bg-[#0f172adc]"
                >
                  Edit Vehicle
                </Button>
                <Button
                  variant="default"
                  className="bg-[#0F172A] hover:bg-[#0f172adc]"
                >
                  Edit Listing
                </Button>
              </div>
            )}
          </div>
        </div>
        {state === 0 ? (
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <div className="w-full h-full flex flex-col gap-5">
              {vehicleImages.length > 0 && (
                <VehicleImageGallery vehicle={vehicle} />
              )}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <VehicleDetailPlaceholder label="VIN">
                  {vehicle.vin}
                </VehicleDetailPlaceholder>
                <VehicleDetailPlaceholder label="Make">
                  {vehicle.make}
                </VehicleDetailPlaceholder>
                <VehicleDetailPlaceholder label="Model">
                  {vehicle.model}
                </VehicleDetailPlaceholder>
                <VehicleDetailPlaceholder label="Year">
                  {vehicle.year || "N/A"}
                </VehicleDetailPlaceholder>
                <VehicleDetailPlaceholder label="Color">
                  {vehicle.color || "N/A"}
                </VehicleDetailPlaceholder>

                <VehicleDetailPlaceholder label="Type">
                  {vehicle.type ? formatEnumLabel(vehicle.type) : "N/A"}
                </VehicleDetailPlaceholder>
                <VehicleDetailPlaceholder label="Fuel Type">
                  {vehicle.fuelType ? formatEnumLabel(vehicle.fuelType) : "N/A"}
                </VehicleDetailPlaceholder>
                <VehicleDetailPlaceholder label="Fuel Consumption">
                  {vehicle.fuelConsumption
                    ? `${vehicle.fuelConsumption} L/100km`
                    : "N/A"}
                </VehicleDetailPlaceholder>

                <VehicleDetailPlaceholder label="Location">
                  {vehicle.city || "N/A"}
                  {vehicle.country && `, ${vehicle.country}`}
                </VehicleDetailPlaceholder>

                <VehicleDetailPlaceholder label="Status">
                  <span
                    className={`inline-block px-3 py-1 rounded-full text-sm font-medium ${
                      vehicle.listed
                        ? "bg-green-100 text-green-800"
                        : "bg-gray-100 text-gray-800"
                    }`}
                  >
                    {vehicle.listed ? "Listed" : "Not Listed"}
                  </span>
                </VehicleDetailPlaceholder>
              </div>

              {vehicle.features && vehicle.features.length > 0 && (
                <div className="mt-6 pt-6 border-t border-gray-200">
                  <h3 className="text-sm font-medium text-gray-500 mb-3">
                    Features
                  </h3>
                  <div className="flex flex-wrap gap-2">
                    {vehicle.features.map((feature, index) => (
                      <span
                        key={index}
                        className="inline-block px-3 py-1 bg-blue-100 text-blue-800 rounded-full text-sm"
                      >
                        {formatEnumLabel(feature)}
                      </span>
                    ))}
                  </div>
                </div>
              )}
            </div>
          </div>
        ) : (
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <AddListingForm
              addListingFrom={form}
              handleSubmit={handleListingSubmit}
              isPending={isPending}
            />
          </div>
        )}
      </div>
    </div>
  );
}
