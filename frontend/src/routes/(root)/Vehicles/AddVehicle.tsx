import { Layout } from "@/components/layout/Layout";
import { AddVehicleForm } from "@/components/vehicle/AddVehicleForm";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import {
  vehicleSchema,
  type VehicleSchemaType,
} from "@/schemas/vehicle/vehicle.schema";
import { useVehicleEnums } from "@/hooks/useVehicleEnums";
import { useAddVehicle } from "@/hooks/useAddVehicle";
import { COUNTRIES } from "@/lib/countries";
import { useState, useEffect, useMemo } from "react";
import Loading from "@/components/ui/Loading";

const formatEnumLabel = (value: string): string => {
  return value
    .split("_")
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
    .join(" ");
};

export function AddVehicle() {
  const [countryOpen, setCountryOpen] = useState(false);
  const { vehicleTypes, fuelTypes, features, isLoading, error } =
    useVehicleEnums();
  const { addVehicle, isPending } = useAddVehicle();

  const vehicleTypesOptions = useMemo(
    () =>
      vehicleTypes.map((value) => ({
        value,
        label: formatEnumLabel(value),
      })),
    [vehicleTypes]
  );

  const fuelTypesOptions = useMemo(
    () =>
      fuelTypes.map((value) => ({
        value,
        label: formatEnumLabel(value),
      })),
    [fuelTypes]
  );

  const featuresOptions = useMemo(
    () =>
      features.map((value) => ({
        value,
        label: formatEnumLabel(value),
      })),
    [features]
  );

  const form = useForm<VehicleSchemaType>({
    resolver: zodResolver(vehicleSchema),
    defaultValues: {
      vin: "",
      make: "",
      model: "",
      color: "",
      year: new Date().getFullYear(),
      type: vehicleTypes[0] || "",
      fuelType: fuelTypes[0] || "",
      fuelConsumption: undefined,
      features: [],
      country: "NL",
      city: "",
      images: [],
    },
  });

  useEffect(() => {
    if (vehicleTypes.length > 0 && fuelTypes.length > 0) {
      form.reset({
        ...form.getValues(),
        type: vehicleTypes[0],
        fuelType: fuelTypes[0],
        images: [],
      });
    }
  }, [vehicleTypes, fuelTypes, form]);

  const selectedCountry =
    COUNTRIES.find((c) => c.value === form.watch("country")) ||
    COUNTRIES.find((c) => c.value === "NL");

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
          <div className="text-center">
            <p className="text-red-500 mb-4">Failed to load vehicle options</p>
            <p className="text-sm text-gray-600">{error.message}</p>
          </div>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="w-full h-full overflow-hidden flex flex-col min-h-0">
        <div className="flex-1 overflow-y-auto hide-scrollbar min-h-0">
          <div className="max-w-2xl mx-auto p-6 pb-8">
            <h1 className="text-3xl font-bold text-[#0F172A] mb-6 text-center">
              Add Vehicle
            </h1>
            <AddVehicleForm
              addVehicleForm={form}
              handleSubmit={addVehicle}
              isPending={isPending}
              vehicleTypesOptions={vehicleTypesOptions}
              fuelTypesOptions={fuelTypesOptions}
              featuresOptions={featuresOptions}
              countryOpen={countryOpen}
              setCountryOpen={setCountryOpen}
              selectedCountry={selectedCountry}
            />
          </div>
        </div>
      </div>
    </Layout>
  );
}
