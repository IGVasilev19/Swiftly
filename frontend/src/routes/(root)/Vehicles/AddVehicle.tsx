import { Layout } from "@/components/ui/Layout";
import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  vehicleSchema,
  type VehicleSchemaType,
  type VehicleType,
  type FuelType,
  type Feature,
} from "@/schemas/vehicle/vehicle.schema";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import CountrySelector from "@/components/ui/CountrySelector";
import { COUNTRIES } from "@/lib/countries";
import { type SelectMenuOption } from "@/lib/types";
import { FileUpload } from "@/components/ui/file-upload";

const VEHICLE_TYPES: { value: VehicleType; label: string }[] = [
  { value: "SEDAN", label: "Sedan" },
  { value: "SUV", label: "SUV" },
  { value: "HATCHBACK", label: "Hatchback" },
  { value: "COUPE", label: "Coupe" },
  { value: "CONVERTIBLE", label: "Convertible" },
  { value: "WAGON", label: "Wagon" },
  { value: "PICKUP", label: "Pickup" },
  { value: "VAN", label: "Van" },
  { value: "MOTORCYCLE", label: "Motorcycle" },
];

const FUEL_TYPES: { value: FuelType; label: string }[] = [
  { value: "GASOLINE", label: "Gasoline" },
  { value: "DIESEL", label: "Diesel" },
  { value: "ELECTRIC", label: "Electric" },
  { value: "HYBRID", label: "Hybrid" },
  { value: "PLUGIN_HYBRID", label: "Plugin Hybrid" },
  { value: "HYDROGEN", label: "Hydrogen" },
];

const FEATURES: { value: Feature; label: string }[] = [
  { value: "GPS", label: "GPS" },
  { value: "BLUETOOTH", label: "Bluetooth" },
  { value: "USB_CHARGING", label: "USB Charging" },
  { value: "BACKUP_CAMERA", label: "Backup Camera" },
  { value: "PARKING_SENSORS", label: "Parking Sensors" },
  { value: "CRUISE_CONTROL", label: "Cruise Control" },
  { value: "LEATHER_SEATS", label: "Leather Seats" },
  { value: "SUNROOF", label: "Sunroof" },
  { value: "ALL_WHEEL_DRIVE", label: "All Wheel Drive" },
  { value: "AUTOMATIC_TRANSMISSION", label: "Automatic Transmission" },
];

export function AddVehicle() {
  const [countryOpen, setCountryOpen] = useState(false);

  const form = useForm<VehicleSchemaType>({
    resolver: zodResolver(vehicleSchema),
    defaultValues: {
      vin: "",
      make: "",
      model: "",
      color: "",
      year: new Date().getFullYear(),
      type: "SEDAN",
      fuelType: "GASOLINE",
      fuelConsumption: undefined,
      features: [],
      country: "NL",
      city: "",
    },
  });

  const onSubmit = (data: VehicleSchemaType) => {
    console.log("Vehicle data:", data);
    // TODO: Implement API call to add vehicle
  };

  const selectedCountry =
    COUNTRIES.find((c) => c.value === form.watch("country")) ||
    COUNTRIES.find((c) => c.value === "NL");

  return (
    <Layout>
      <div className="w-full h-full overflow-hidden flex flex-col min-h-0">
        <div className="flex-1 overflow-y-auto hide-scrollbar min-h-0">
          <div className="max-w-2xl mx-auto p-6 pb-8">
          <h1 className="text-3xl font-bold text-[#0F172A] mb-6">Add Vehicle</h1>

          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <FormField
                control={form.control}
                name="vin"
                render={({ field }) => (
                  <FormItem>
                    <div className="flex items-center justify-between">
                      <FormLabel>VIN</FormLabel>
                      <FormMessage className="text-right" />
                    </div>
                    <FormControl>
                      <Input
                        {...field}
                        placeholder="Enter VIN"
                        className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                      />
                    </FormControl>
                  </FormItem>
                )}
              />

              {/* Make */}
              <FormField
                control={form.control}
                name="make"
                render={({ field }) => (
                  <FormItem>
                    <div className="flex items-center justify-between">
                      <FormLabel>Make</FormLabel>
                      <FormMessage className="text-right" />
                    </div>
                    <FormControl>
                      <Input
                        {...field}
                        placeholder="e.g., Toyota"
                        className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                      />
                    </FormControl>
                  </FormItem>
                )}
              />

              {/* Model */}
              <FormField
                control={form.control}
                name="model"
                render={({ field }) => (
                  <FormItem>
                    <div className="flex items-center justify-between">
                      <FormLabel>Model</FormLabel>
                      <FormMessage className="text-right" />
                    </div>
                    <FormControl>
                      <Input
                        {...field}
                        placeholder="e.g., Camry"
                        className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                      />
                    </FormControl>
                  </FormItem>
                )}
              />

              {/* Color */}
              <FormField
                control={form.control}
                name="color"
                render={({ field }) => (
                  <FormItem>
                    <div className="flex items-center justify-between">
                      <FormLabel>Color</FormLabel>
                      <FormMessage className="text-right" />
                    </div>
                    <FormControl>
                      <Input
                        {...field}
                        placeholder="e.g., Red"
                        className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                      />
                    </FormControl>
                  </FormItem>
                )}
              />

              {/* Year */}
              <FormField
                control={form.control}
                name="year"
                render={({ field }) => (
                  <FormItem>
                    <div className="flex items-center justify-between">
                      <FormLabel>Year</FormLabel>
                      <FormMessage className="text-right" />
                    </div>
                    <FormControl>
                      <Input
                        type="number"
                        {...field}
                        onChange={(e) =>
                          field.onChange(parseInt(e.target.value) || 0)
                        }
                        placeholder="e.g., 2024"
                        className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                      />
                    </FormControl>
                  </FormItem>
                )}
              />

              {/* Vehicle Type */}
              <FormField
                control={form.control}
                name="type"
                render={({ field }) => (
                  <FormItem>
                    <div className="flex items-center justify-between">
                      <FormLabel>Vehicle Type</FormLabel>
                      <FormMessage className="text-right" />
                    </div>
                    <FormControl>
                      <select
                        {...field}
                        className="w-full h-10 rounded-md border border-[#0f172a1a] bg-white text-sm text-[#0F172A] focus:outline-none focus:ring-2 focus:ring-[#0F172A] focus:border-transparent px-3"
                      >
                        {VEHICLE_TYPES.map((type) => (
                          <option key={type.value} value={type.value}>
                            {type.label}
                          </option>
                        ))}
                      </select>
                    </FormControl>
                  </FormItem>
                )}
              />

              {/* Fuel Type */}
              <FormField
                control={form.control}
                name="fuelType"
                render={({ field }) => (
                  <FormItem>
                    <div className="flex items-center justify-between">
                      <FormLabel>Fuel Type</FormLabel>
                      <FormMessage className="text-right" />
                    </div>
                    <FormControl>
                      <select
                        {...field}
                        className="w-full h-10 rounded-md border border-[#0f172a1a] bg-white text-sm text-[#0F172A] focus:outline-none focus:ring-2 focus:ring-[#0F172A] focus:border-transparent px-3"
                      >
                        {FUEL_TYPES.map((fuel) => (
                          <option key={fuel.value} value={fuel.value}>
                            {fuel.label}
                          </option>
                        ))}
                      </select>
                    </FormControl>
                  </FormItem>
                )}
              />

              {/* Fuel Consumption */}
              <FormField
                control={form.control}
                name="fuelConsumption"
                render={({ field }) => (
                  <FormItem>
                    <div className="flex items-center justify-between">
                      <FormLabel>Fuel Consumption (L/100km)</FormLabel>
                      <FormMessage className="text-right" />
                    </div>
                    <FormControl>
                      <Input
                        type="number"
                        step="0.1"
                        {...field}
                        onChange={(e) =>
                          field.onChange(
                            e.target.value
                              ? parseFloat(e.target.value)
                              : undefined
                          )
                        }
                        placeholder="e.g., 7.5"
                        className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                      />
                    </FormControl>
                  </FormItem>
                )}
              />

              {/* Country */}
              <FormField
                control={form.control}
                name="country"
                render={({ field }) => (
                  <FormItem>
                    <div className="flex items-center justify-between">
                      <FormLabel>Country</FormLabel>
                      <FormMessage className="text-right" />
                    </div>
                    <FormControl>
                      <div className="relative">
                        <CountrySelector
                          id="country-selector"
                          open={countryOpen}
                          onToggle={() => setCountryOpen(!countryOpen)}
                          onChange={(value) => {
                            field.onChange(value);
                            setCountryOpen(false);
                          }}
                          selectedValue={
                            selectedCountry as SelectMenuOption | null
                          }
                        />
                      </div>
                    </FormControl>
                  </FormItem>
                )}
              />

              {/* City */}
              <FormField
                control={form.control}
                name="city"
                render={({ field }) => (
                  <FormItem>
                    <div className="flex items-center justify-between">
                      <FormLabel>City</FormLabel>
                      <FormMessage className="text-right" />
                    </div>
                    <FormControl>
                      <Input
                        {...field}
                        placeholder="e.g., Amsterdam"
                        className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                      />
                    </FormControl>
                  </FormItem>
                )}
              />
            </div>

            {/* Features */}
            <FormField
              control={form.control}
              name="features"
              render={({ field }) => (
                <FormItem>
                  <div className="flex items-center justify-between">
                    <FormLabel>Features</FormLabel>
                    <FormMessage className="text-right" />
                  </div>
                  <FormControl>
                    <div className="grid grid-cols-2 md:grid-cols-3 gap-3 p-4 border border-[#0f172a1a] rounded-md">
                      {FEATURES.map((feature) => (
                        <label
                          key={feature.value}
                          className="flex items-center space-x-2 cursor-pointer"
                        >
                          <input
                            type="checkbox"
                            checked={field.value?.includes(feature.value)}
                            onChange={(e) => {
                              const currentFeatures = field.value || [];
                              if (e.target.checked) {
                                field.onChange([
                                  ...currentFeatures,
                                  feature.value,
                                ]);
                              } else {
                                field.onChange(
                                  currentFeatures.filter(
                                    (f) => f !== feature.value
                                  )
                                );
                              }
                            }}
                            className="w-4 h-4 text-[#0F172A] border-[#0f172a1a] rounded focus:ring-[#0F172A]"
                          />
                          <span className="text-sm text-[#0F172A]">
                            {feature.label}
                          </span>
                        </label>
                      ))}
                    </div>
                  </FormControl>
                </FormItem>
              )}
            />
            <FileUpload />

            <div className="flex gap-3 pt-4">
              <Button
                type="submit"
                className="flex-1 bg-[#0F172A] hover:bg-[#16213b]"
              >
                Add Vehicle
              </Button>
              <Button
                type="button"
                variant="outline"
                onClick={() => form.reset()}
                className="flex-1"
              >
                Reset
              </Button>
            </div>
          </form>
        </Form>
          </div>
        </div>
      </div>
    </Layout>
  );
}
