import { useState } from "react";
import { useForm, Controller } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardAction,
  CardContent,
  CardFooter,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import PhoneNumberInput from "@/components/ui/PhoneNumberInput";

const registerSchema = z.object({
  name: z.string().min(1, "Required"),
  email: z.string().min(1, "Required").email("Enter a valid email address"),
  password: z
    .string()
    .min(8, "Password must be at least 8 characters")
    .regex(
      /(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\da-zA-Z])/,
      "Password must contain upper and lower case letters, a number and a symbol"
    ),
  phone: z
    .string()
    .min(1, "Required")
    .regex(
      /^\+[1-9]\d{6,14}$/,
      "Phone must be in international E.164 format (e.g. +31612345678)"
    ),
  country: z.string().min(1, "Required"),
  city: z.string().min(1, "Required"),
  postalCode: z.string().min(1, "Required"),
  role: z.string().min(1, "Required"),
  address: z.string().min(1, "Required"),
});

type RegisterFormValues = z.infer<typeof registerSchema>;

export function RegisterForm() {
  const [step, setStep] = useState(0);
  const steps = ["First", "Second"];

  const {
    register,
    handleSubmit,
    trigger,
    formState: { errors, isSubmitting },
    control,
  } = useForm<RegisterFormValues>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      name: "",
      email: "",
      password: "",
      phone: "",
      country: "",
      city: "",
      postalCode: "",
      role: "",
      address: "",
    },
    mode: "onTouched",
  });

  const fieldsByStep: Record<number, (keyof RegisterFormValues)[]> = {
    0: ["name", "email", "password", "phone"],
    1: ["country", "city", "postalCode", "role", "address"],
  };

  const onNext = async () => {
    const ok = await trigger(fieldsByStep[step]);
    if (ok) setStep((s) => Math.min(s + 1, steps.length - 1));
  };

  const onBack = () => setStep((s) => Math.max(s - 1, 0));

  const onSubmit = (data: RegisterFormValues) => {
    console.log("register submit", data);
  };

  return (
    <Card className="w-full max-w-sm">
      <CardContent>
        <div className="mb-4">
          <div className="mt-2 flex gap-2">
            {steps.map((_, i) => (
              <div
                key={i}
                className={`flex-1 h-1 rounded-full ${
                  i <= step ? "bg-[#FD6123]" : "bg-gray-200"
                }`}
              />
            ))}
          </div>
        </div>

        <form onSubmit={handleSubmit(onSubmit)}>
          {step === 0 && (
            <div className="flex flex-col gap-6">
              {/* Name */}
              <div className="grid gap-2">
                <div className="flex items-baseline justify-between">
                  <Label
                    htmlFor="name"
                    className="font-sans text-[#0F172A] text-md"
                  >
                    Name
                  </Label>
                  <span
                    className="text-sm text-destructive min-h-4 ml-4"
                    aria-live="polite"
                  >
                    {errors.name?.message ?? ""}
                  </span>
                </div>
                <Input
                  id="name"
                  {...register("name")}
                  className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A]"
                />
              </div>

              {/* Email */}
              <div className="grid gap-2">
                <div className="flex items-baseline justify-between">
                  <Label
                    htmlFor="email"
                    className="font-sans text-[#0F172A] text-md"
                  >
                    Email
                  </Label>
                  <span
                    className="text-sm text-destructive min-h-4 ml-4"
                    aria-live="polite"
                  >
                    {errors.email?.message ?? ""}
                  </span>
                </div>
                <Input
                  id="email"
                  {...register("email")}
                  className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A]"
                />
              </div>

              {/* Password */}
              <div className="grid gap-2">
                <div className="flex items-baseline justify-between">
                  <Label
                    htmlFor="password"
                    className="font-sans text-[#0F172A] text-md"
                  >
                    Password
                  </Label>
                  <span
                    className="text-sm text-destructive min-h-4 ml-4"
                    aria-live="polite"
                  >
                    {errors.password?.message ?? ""}
                  </span>
                </div>
                <Input
                  id="password"
                  type="password"
                  {...register("password")}
                  className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A]"
                />
              </div>

              {/* Phone */}
              <div className="grid gap-2">
                <div className="flex items-baseline justify-between">
                  <Label
                    htmlFor="phone"
                    className="font-sans text-[#0F172A] text-md"
                  >
                    Phone
                  </Label>
                  <span
                    className="text-sm text-destructive min-h-4 ml-4"
                    aria-live="polite"
                  >
                    {errors.phone?.message ?? ""}
                  </span>
                </div>

                <div className="flex gap-2">
                  <div className="flex-1">
                    <Controller
                      name="phone"
                      control={control}
                      defaultValue=""
                      render={({ field }) => (
                        <PhoneNumberInput
                          id="phone"
                          value={field.value || ""}
                          onChange={(val) => field.onChange(val)}
                          defaultCountry="NL"
                          className=""
                        />
                      )}
                    />
                  </div>
                </div>
              </div>
            </div>
          )}

          {step === 1 && (
            <div className="flex flex-col gap-6">
              <div className="grid gap-2">
                <div className="flex items-baseline justify-between">
                  <Label
                    htmlFor="city"
                    className="font-sans text-[#0F172A] text-md"
                  >
                    Country
                  </Label>
                  <span
                    className="text-sm text-destructive min-h-4 ml-4"
                    aria-live="polite"
                  >
                    {errors.country?.message ?? ""}
                  </span>
                </div>
                <Input
                  id="country"
                  {...register("country")}
                  className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A]"
                />
              </div>
              <div className="grid gap-2">
                <div className="flex items-baseline justify-between">
                  <Label
                    htmlFor="city"
                    className="font-sans text-[#0F172A] text-md"
                  >
                    City
                  </Label>
                  <span
                    className="text-sm text-destructive min-h-4 ml-4"
                    aria-live="polite"
                  >
                    {errors.city?.message ?? ""}
                  </span>
                </div>
                <Input
                  id="city"
                  {...register("city")}
                  className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A]"
                />
              </div>
              <div className="grid gap-2">
                <div className="flex items-baseline justify-between">
                  <Label
                    htmlFor="address"
                    className="font-sans text-[#0F172A] text-md"
                  >
                    Address
                  </Label>
                  <span
                    className="text-sm text-destructive min-h-4 ml-4"
                    aria-live="polite"
                  >
                    {errors.address?.message ?? ""}
                  </span>
                </div>
                <Input
                  id="address"
                  {...register("address")}
                  className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A]"
                />
              </div>

              <div className="flex gap-4">
                <div className="grid gap-2 flex-1">
                  <div className="flex items-baseline justify-between">
                    <Label
                      htmlFor="postalCode"
                      className="font-sans text-[#0F172A] text-md"
                    >
                      Post Code
                    </Label>
                    <span
                      className="text-sm text-destructive min-h-4 ml-4"
                      aria-live="polite"
                    >
                      {errors.postalCode?.message ?? ""}
                    </span>
                  </div>
                  <Input
                    id="postalCode"
                    {...register("postalCode")}
                    className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A]"
                  />
                </div>

                <div className="grid gap-2 w-36">
                  <div className="flex items-baseline justify-between">
                    <Label
                      htmlFor="role"
                      className="font-sans text-[#0F172A] text-md"
                    >
                      Role
                    </Label>
                    <span
                      className="text-sm text-destructive min-h-4 ml-4"
                      aria-live="polite"
                    >
                      {errors.role?.message ?? ""}
                    </span>
                  </div>
                  <select
                    id="role"
                    {...register("role")}
                    className="w-full h-10 rounded-md border border-[#0f172a1a] bg-white px-3 py-2 text-sm text-[#0F172A] focus:outline-none"
                  >
                    <option value="user">Renter</option>
                    <option value="owner">Owner</option>
                  </select>
                </div>
              </div>
            </div>
          )}
        </form>
      </CardContent>

      <CardFooter className="flex-col gap-2">
        <div className="w-full flex gap-3">
          <Button
            onClick={onBack}
            className="flex-1 text-[#0F172A] border-[#0F172A]"
            type="button"
            variant="outline"
            disabled={step === 0}
          >
            Back
          </Button>

          {step < steps.length - 1 ? (
            <Button
              onClick={onNext}
              className="flex-1 bg-[#0F172A] hover:bg-[#16213b]"
              type="button"
            >
              Next
            </Button>
          ) : (
            <Button
              onClick={handleSubmit(onSubmit)}
              className="flex-1 bg-[#0F172A] hover:bg-[#16213b]"
              type="submit"
              disabled={isSubmitting}
            >
              Sign up
            </Button>
          )}
        </div>

        <div className="flex justify-center items-center">
          <p className="font-sans text-[#0F172A] text-md">
            Already have an Account?
          </p>
          <CardAction>
            <Button
              variant="link"
              className="font-sans text-[#0F172A] text-md p-0 pl-1 hover:text-[#FD6123]"
            >
              Sign In
            </Button>
          </CardAction>
        </div>
      </CardFooter>
    </Card>
  );
}
