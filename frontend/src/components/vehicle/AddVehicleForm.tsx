import { Button } from "@/components/ui/Button";
import { LoaderCircle } from "lucide-react";
import { Input } from "@/components/ui/input";
import CountrySelector from "@/components/ui/CountrySelector";
import { FileUpload } from "@/components/ui/file-upload";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "../ui/form";
import type { SelectMenuOption } from "@/lib/types";
import type { AddVehicleFormProps } from "@/types/vehicle";

export function AddVehicleForm({
  addVehicleForm,
  handleSubmit,
  isPending,
  vehicleTypesOptions,
  fuelTypesOptions,
  featuresOptions,
  countryOpen,
  setCountryOpen,
  selectedCountry,
  mode = "add",
  onDelete,
  isDeleting = false,
}: AddVehicleFormProps & { mode?: "add" | "update" }) {
  return (
    <Form {...addVehicleForm}>
      <form
        onSubmit={addVehicleForm.handleSubmit(handleSubmit)}
        className="space-y-6"
      >
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <FormField
            control={addVehicleForm.control}
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
                    placeholder={mode === "update" ? field.value || "Enter VIN" : "Enter VIN"}
                    className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                  />
                </FormControl>
              </FormItem>
            )}
          />

          <FormField
            control={addVehicleForm.control}
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
                    placeholder={mode === "update" ? field.value || "e.g., Toyota" : "e.g., Toyota"}
                    className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                  />
                </FormControl>
              </FormItem>
            )}
          />

          <FormField
            control={addVehicleForm.control}
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
                    placeholder={mode === "update" ? field.value || "e.g., Camry" : "e.g., Camry"}
                    className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                  />
                </FormControl>
              </FormItem>
            )}
          />

          <FormField
            control={addVehicleForm.control}
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
                    placeholder={mode === "update" ? field.value || "e.g., Red" : "e.g., Red"}
                    className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                  />
                </FormControl>
              </FormItem>
            )}
          />

          <FormField
            control={addVehicleForm.control}
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
                    placeholder={mode === "update" ? String(field.value || "e.g., 2024") : "e.g., 2024"}
                    className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                  />
                </FormControl>
              </FormItem>
            )}
          />

          <FormField
            control={addVehicleForm.control}
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
                    {vehicleTypesOptions.map((type) => (
                      <option key={type.value} value={type.value}>
                        {type.label}
                      </option>
                    ))}
                  </select>
                </FormControl>
              </FormItem>
            )}
          />

          <FormField
            control={addVehicleForm.control}
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
                    {fuelTypesOptions.map((fuel) => (
                      <option key={fuel.value} value={fuel.value}>
                        {fuel.label}
                      </option>
                    ))}
                  </select>
                </FormControl>
              </FormItem>
            )}
          />

          <FormField
            control={addVehicleForm.control}
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
                        e.target.value ? parseFloat(e.target.value) : undefined
                      )
                    }
                    placeholder={mode === "update" ? String(field.value || "e.g., 7.5") : "e.g., 7.5"}
                    className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                  />
                </FormControl>
              </FormItem>
            )}
          />

          <FormField
            control={addVehicleForm.control}
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
                      selectedValue={selectedCountry as SelectMenuOption | null}
                    />
                  </div>
                </FormControl>
              </FormItem>
            )}
          />

          <FormField
            control={addVehicleForm.control}
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
                    placeholder={mode === "update" ? field.value || "e.g., Amsterdam" : "e.g., Amsterdam"}
                    className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                  />
                </FormControl>
              </FormItem>
            )}
          />
        </div>

        <FormField
          control={addVehicleForm.control}
          name="features"
          render={({ field }) => (
            <FormItem>
              <div className="flex items-center justify-between">
                <FormLabel>Features</FormLabel>
                <FormMessage className="text-right" />
              </div>
              <FormControl>
                <div className="grid grid-cols-2 md:grid-cols-3 gap-3 p-4 border border-[#0f172a1a] rounded-md">
                  {featuresOptions.map((feature) => (
                    <label
                      key={feature.value}
                      className="flex items-center space-x-2 cursor-pointer"
                    >
                      <input
                        type="checkbox"
                        checked={field.value?.includes(feature.value) || false}
                        onChange={(e) => {
                          const currentFeatures = field.value || [];
                          if (e.target.checked) {
                            field.onChange([...currentFeatures, feature.value]);
                          } else {
                            field.onChange(
                              currentFeatures.filter((f) => f !== feature.value)
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
        <FormField
          control={addVehicleForm.control}
          name="images"
          render={({ field }) => (
            <FormItem>
              <div className="flex items-center justify-between">
                <FormLabel>Images</FormLabel>
                <FormMessage className="text-right" />
              </div>
              <FormControl>
                <FileUpload
                  value={field.value || []}
                  onChange={field.onChange}
                />
              </FormControl>
            </FormItem>
          )}
        />

        <div className={`flex pt-4 items-center ${mode === "update" && onDelete ? "justify-between gap-4" : "justify-center"}`}>
          {mode === "update" && onDelete && (
            <Button
              disabled={isDeleting}
              type="button"
              variant="destructive"
              onClick={onDelete}
              className="w-1/2 h-full bg-red-700 hover:bg-red-800"
            >
              {isDeleting ? "Deleting..." : "Delete Vehicle"}
            </Button>
          )}
          <Button
            disabled={isPending}
            type="submit"
            className={mode === "update" && onDelete ? "w-1/2 h-full bg-[#0F172A] hover:bg-[#16213b]" : "w-1/2 h-full bg-[#0F172A] hover:bg-[#16213b]"}
          >
            {isPending ? (
              <div className="flex items-center justify-center gap-2">
                <LoaderCircle className="animate-spin" />
                <p>{mode === "update" ? "Updating vehicle..." : "Adding vehicle..."}</p>
              </div>
            ) : (
              mode === "update" ? "Update Vehicle" : "Add Vehicle"
            )}
          </Button>
        </div>
      </form>
    </Form>
  );
}
