import React, { useState } from "react";
import type { Vehicle, VehicleImage } from "@/types/vehicle";

interface VehicleImageGalleryProps {
  vehicle: Vehicle;
}

export function VehicleImageGallery({ vehicle }: VehicleImageGalleryProps) {
  const [selectedImageIndex, setSelectedImageIndex] = useState(0);

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

  if (vehicleImages.length === 0) {
    return null;
  }

  return (
    <div className="w-full space-y-4">
      {vehicleImages[selectedImageIndex] &&
        (() => {
          const imageUrl = getImageUrl(vehicleImages[selectedImageIndex]);
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
  );
}
