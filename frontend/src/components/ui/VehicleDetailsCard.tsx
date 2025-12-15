import type { Vehicle, VehicleImage } from "@/types/vehicle";
import { ArrowLeft } from "lucide-react";
import React, { useState } from "react";
import { Button } from "./button";
import { VehicleDetailPlaceholder } from "./VehicleDetailPlaceholder";
import { useNavigate } from "react-router-dom";
import AddListingForm from "../listing/AddListingForm";

const formatEnumLabel = (value: string): string => {
  return value
    .split("_")
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
    .join(" ");
};

export function VehicleDetailsCard({ vehicle }: { vehicle: Vehicle }) {
  const getImageUrl = (image: VehicleImage): string | null | undefined => {
    if (!image) {
      return null;
    }

    if (image.data && typeof image.data === "string") {
      const mimeType = image.mimeType;
      return `data:${mimeType};base64,${image.data}`;
    }
  };

  const vehicleImages: VehicleImage[] =
    vehicle?.images && Array.isArray(vehicle.images) ? vehicle.images : [];

  const handleListVehicle = async () => {
    return;
  };

  const [state, setState] = useState(0);
  const navigate = useNavigate();
  const [selectedImageIndex, setSelectedImageIndex] = useState(0);

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
          </div>
        </div>
        {state === 0 ? (
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
            <div className="w-full h-full flex flex-col gap-5">
              {vehicleImages.length > 0 && (
                <div className="w-full space-y-4">
                  {vehicleImages[selectedImageIndex] &&
                    (() => {
                      const imageUrl = getImageUrl(
                        vehicleImages[selectedImageIndex]
                      );
                      const placeholderUrl =
                        "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='400' height='300'%3E%3Crect fill='%23ddd' width='400' height='300'/%3E%3Ctext fill='%23999' font-family='sans-serif' font-size='18' dy='10.5' font-weight='bold' x='50%25' y='50%25' text-anchor='middle'%3EImage not found%3C/text%3E%3C/svg%3E";
                      return (
                        <div className="relative w-full aspect-video bg-gray-100 rounded-lg overflow-hidden">
                          <img
                            src={imageUrl || placeholderUrl}
                            alt={`${vehicle.make} ${vehicle.model} - Image ${
                              selectedImageIndex + 1
                            }`}
                            className="w-full h-full object-contain"
                            onError={(e) => {
                              const target = e.target as HTMLImageElement;
                              target.src = placeholderUrl;
                            }}
                          />
                        </div>
                      );
                    })()}

                  {vehicleImages.length > 1 && (
                    <div className="grid grid-cols-4 md:grid-cols-6 gap-2">
                      {vehicleImages.map((image, index) => {
                        const imageUrl = getImageUrl(image);
                        const thumbnailPlaceholder =
                          "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='100' height='100'%3E%3Crect fill='%23ddd' width='100' height='100'/%3E%3Ctext fill='%23999' font-family='sans-serif' font-size='12' dy='.3em' x='50%25' y='50%25' text-anchor='middle'%3ENo image%3C/text%3E%3C/svg%3E";
                        return (
                          <button
                            key={image.id ?? index}
                            onClick={() => setSelectedImageIndex(index)}
                            className={`relative aspect-square rounded-lg overflow-hidden border-2 transition-all ${
                              selectedImageIndex === index
                                ? "border-[#0F172A] ring-2 ring-[#0F172A] ring-offset-2"
                                : "border-gray-200 hover:border-gray-300"
                            }`}
                            type="button"
                            aria-label={`View image ${index + 1}`}
                          >
                            <img
                              src={imageUrl || thumbnailPlaceholder}
                              alt={`Thumbnail ${index + 1} of ${vehicle.make} ${
                                vehicle.model
                              }`}
                              className="w-full h-full object-cover"
                              onError={(e) => {
                                const target = e.target as HTMLImageElement;
                                target.src = thumbnailPlaceholder;
                              }}
                            />
                          </button>
                        );
                      })}
                    </div>
                  )}

                  {vehicleImages.length > 1 && (
                    <div className="text-center text-sm text-gray-500">
                      Image {selectedImageIndex + 1} of {vehicleImages.length}
                    </div>
                  )}
                </div>
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
            <AddListingForm />
            <Button variant="default" onClick={handleListVehicle}>
              Confirm Listing
            </Button>
          </div>
        )}
      </div>
    </div>
  );
}
