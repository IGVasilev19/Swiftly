import type { VehicleImage } from "@/types/vehicle";
import { ArrowLeft } from "lucide-react";
import { Button } from "./button";
import { VehicleDetailPlaceholder } from "./VehicleDetailPlaceholder";
import { VehicleImageGallery } from "./VehicleImageGallery";
import { useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import type { Listing } from "@/types/listing";
import {
  bookingSchema,
  type BookingSchemaType,
} from "@/schemas/booking/booking.schema";
import z from "zod";
import type { Booking } from "@/types/booking";
import { useAddBooking } from "@/hooks/useAddBooking";
import { AddBookingForm } from "../booking/AddBookingForm";
import { differenceInCalendarDays } from "date-fns/differenceInCalendarDays";

const formatEnumLabel = (value: string): string => {
  return value
    .split("_")
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
    .join(" ");
};

export function ListingDetailsCard({ listing }: { listing: Listing }) {
  const vehicleImages: VehicleImage[] =
    listing?.vehicle?.images && Array.isArray(listing.vehicle.images)
      ? listing.vehicle.images
      : [];

  const form = useForm<BookingSchemaType>({
    resolver: zodResolver(bookingSchema),
    defaultValues: {
      dateRange: {
        from: new Date(),
        to: new Date(),
      },
    },
  });

  const navigate = useNavigate();
  const { addBooking, isPending } = useAddBooking();

  const handleBookingSubmit = (data: BookingSchemaType) => {
    const days =
      differenceInCalendarDays(data.dateRange.to, data.dateRange.from) + 1;

    const totalPrice = days * listing.basePricePerDay;

    const booking: Booking = {
      listing: listing,
      startAt: data.dateRange.from,
      endAt: data.dateRange.to,
      totalPrice: totalPrice,
    };

    addBooking(booking);
  };

  return (
    <div className="flex-1 overflow-y-auto hide-scrollbar min-h-0">
      <div className="max-w-4xl mx-auto p-6 pb-8">
        <div className="mb-6">
          <Button
            variant="ghost"
            onClick={() => navigate("/app/catalogue")}
            className="mb-4"
          >
            <ArrowLeft className="h-4 w-4 mr-2" />
            Back to Catalogue
          </Button>
          <div className="flex justify-between items-center">
            <h1 className="text-3xl font-bold text-[#0F172A]">
              Listing Details
            </h1>
          </div>
        </div>
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
          <div className="w-full h-full flex flex-col gap-5">
            {vehicleImages.length > 0 && (
              <VehicleImageGallery vehicle={listing.vehicle} />
            )}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <VehicleDetailPlaceholder label="VIN">
                {listing.vehicle.vin}
              </VehicleDetailPlaceholder>
              <VehicleDetailPlaceholder label="Make">
                {listing.vehicle.make}
              </VehicleDetailPlaceholder>
              <VehicleDetailPlaceholder label="Model">
                {listing.vehicle.model}
              </VehicleDetailPlaceholder>
              <VehicleDetailPlaceholder label="Year">
                {listing.vehicle.year || "N/A"}
              </VehicleDetailPlaceholder>
              <VehicleDetailPlaceholder label="Color">
                {listing.vehicle.color || "N/A"}
              </VehicleDetailPlaceholder>

              <VehicleDetailPlaceholder label="Type">
                {listing.vehicle.type
                  ? formatEnumLabel(listing.vehicle.type)
                  : "N/A"}
              </VehicleDetailPlaceholder>
              <VehicleDetailPlaceholder label="Fuel Type">
                {listing.vehicle.fuelType
                  ? formatEnumLabel(listing.vehicle.fuelType)
                  : "N/A"}
              </VehicleDetailPlaceholder>
              <VehicleDetailPlaceholder label="Fuel Consumption">
                {listing.vehicle.fuelConsumption
                  ? `${listing.vehicle.fuelConsumption} L/100km`
                  : "N/A"}
              </VehicleDetailPlaceholder>

              <VehicleDetailPlaceholder label="Location">
                {listing.vehicle.city || "N/A"}
                {listing.vehicle.country && `, ${listing.vehicle.country}`}
              </VehicleDetailPlaceholder>
            </div>

            {listing.vehicle.features &&
              listing.vehicle.features.length > 0 && (
                <div className="mt-6 pt-6 border-t border-gray-200">
                  <h3 className="text-sm font-medium text-gray-500 mb-3">
                    Features
                  </h3>
                  <div className="flex flex-wrap gap-2">
                    {listing.vehicle.features.map((feature, index) => (
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
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
          <AddBookingForm
            addBookingForm={form}
            handleSubmit={handleBookingSubmit}
            isPending={isPending}
          />
        </div>
      </div>
    </div>
  );
}
