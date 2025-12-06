import { Layout } from "@/components/ui/Layout";
import Loading from "@/components/ui/Loading";
import { Button } from "@/components/ui/button";
import { useVehicleContext } from "@/contexts/VehicleContext";
import { useGetVehicle } from "@/hooks/useGetVehicle";
import { useListVehicle } from "@/hooks/useListVehicle";
import { useNavigate } from "react-router-dom";
import { ArrowLeft } from "lucide-react";
import { useState } from "react";
import type { VehicleImage } from "@/types/vehicle";

const formatEnumLabel = (value: string): string => {
  return value
    .split("_")
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
    .join(" ");
};

function VehicleDetails() {
  const navigate = useNavigate();
  const { selectedVehicleId } = useVehicleContext();
  const { vehicle, isLoading, error, refetch } =
    useGetVehicle(selectedVehicleId);
  const { listVehicle, isPending: isListPending } = useListVehicle();
  const [selectedImageIndex, setSelectedImageIndex] = useState(0);

  const getImageUrl = (image: VehicleImage): string | null | undefined => {
    if (!image) {
      return null;
    }

    if (image.data && typeof image.data === "string") {
      if (image.data.startsWith("data:")) {
        return image.data;
      }
      const mimeType = image.mimeType || "image/jpeg";
      return `data:${mimeType};base64,${image.data}`;
    }
  };

  const vehicleImages: VehicleImage[] =
    vehicle?.images && Array.isArray(vehicle.images) ? vehicle.images : [];

  const handleListVehicle = async () => {
    if (!vehicle?.id) return;

    const success = await listVehicle(vehicle.id);
    if (success) {
      refetch();
    }
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

  if (error || !vehicle) {
    return (
      <Layout>
        <div className="w-full h-full flex items-center justify-center">
          <div className="text-center">
            <p className="text-red-500 mb-4">
              {error?.message || "Vehicle not found"}
            </p>
            <Button variant="outline" onClick={() => navigate("/vehicles")}>
              Back to Vehicles
            </Button>
          </div>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="w-full h-full overflow-hidden flex flex-col min-h-0">
        <div className="flex-1 overflow-y-auto hide-scrollbar min-h-0">
          <div className="max-w-4xl mx-auto p-6 pb-8">
            <div className="mb-6">
              <Button
                variant="ghost"
                onClick={() => navigate("/vehicles")}
                className="mb-4"
              >
                <ArrowLeft className="h-4 w-4 mr-2" />
                Back to Vehicles
              </Button>
              <div className="flex justify-between items-center">
                <h1 className="text-3xl font-bold text-[#0F172A]">
                  Vehicle Details
                </h1>
                <Button
                  variant="default"
                  className="bg-[#0F172A] hover:bg-[#0f172adc]"
                  onClick={handleListVehicle}
                  disabled={isListPending || vehicle.listed}
                >
                  {isListPending
                    ? "Processing..."
                    : vehicle.listed
                    ? "Already Listed"
                    : "List Vehicle"}
                </Button>
              </div>
            </div>

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
                                alt={`Thumbnail ${index + 1} of ${
                                  vehicle.make
                                } ${vehicle.model}`}
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
                  <div>
                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                      VIN
                    </h3>
                    <p className="text-lg text-gray-900">{vehicle.vin}</p>
                  </div>
                  <div>
                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                      Make
                    </h3>
                    <p className="text-lg text-gray-900">{vehicle.make}</p>
                  </div>

                  <div>
                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                      Model
                    </h3>
                    <p className="text-lg text-gray-900">{vehicle.model}</p>
                  </div>

                  <div>
                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                      Year
                    </h3>
                    <p className="text-lg text-gray-900">
                      {vehicle.year || "N/A"}
                    </p>
                  </div>

                  <div>
                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                      Color
                    </h3>
                    <p className="text-lg text-gray-900">
                      {vehicle.color || "N/A"}
                    </p>
                  </div>

                  <div>
                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                      Type
                    </h3>
                    <p className="text-lg text-gray-900">
                      {vehicle.type ? formatEnumLabel(vehicle.type) : "N/A"}
                    </p>
                  </div>

                  <div>
                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                      Fuel Type
                    </h3>
                    <p className="text-lg text-gray-900">
                      {vehicle.fuelType
                        ? formatEnumLabel(vehicle.fuelType)
                        : "N/A"}
                    </p>
                  </div>

                  <div>
                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                      Fuel Consumption
                    </h3>
                    <p className="text-lg text-gray-900">
                      {vehicle.fuelConsumption
                        ? `${vehicle.fuelConsumption} L/100km`
                        : "N/A"}
                    </p>
                  </div>

                  <div>
                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                      Location
                    </h3>
                    <p className="text-lg text-gray-900">
                      {vehicle.city || "N/A"}
                      {vehicle.country && `, ${vehicle.country}`}
                    </p>
                  </div>

                  <div>
                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                      Status
                    </h3>
                    <p className="text-lg text-gray-900">
                      <span
                        className={`inline-block px-3 py-1 rounded-full text-sm font-medium ${
                          vehicle.listed
                            ? "bg-green-100 text-green-800"
                            : "bg-gray-100 text-gray-800"
                        }`}
                      >
                        {vehicle.listed ? "Listed" : "Not Listed"}
                      </span>
                    </p>
                  </div>
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
          </div>
        </div>
      </div>
    </Layout>
  );
}

export default VehicleDetails;
