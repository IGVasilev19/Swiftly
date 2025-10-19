import { useState } from "react";
import { Button } from "@/components/ui/button";
import { LoaderCircle } from "lucide-react";
import {
  Card,
  CardAction,
  CardContent,
  CardFooter,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import PhoneNumberInput from "@/components/ui/PhoneNumberInput";
import { COUNTRIES } from "@/lib/countries";
import CountrySelector from "../ui/CountrySelector";
import { Link } from "react-router-dom";
import type { RegisterFormProps } from "@/types/types";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "../ui/form";

export function RegisterForm({
  registerForm,
  handleSubmit,
  isPending,
}: RegisterFormProps) {
  const [step, setStep] = useState(0);
  const steps = [0, 1];

  const stepFields = [
    ["name", "email", "password", "phoneNumber"],
    ["country", "city", "address", "postalCode", "role"],
  ];

  const onNext = async () => {
    if (step >= steps.length - 1) return;
    const fields = stepFields[step] ?? [];
    const ok = await registerForm.trigger(fields);
    if (ok) {
      setStep((s) => Math.min(s + 1, steps.length - 1));
    } else {
      const firstError = Object.keys(registerForm.formState.errors)[0];
      if (firstError) registerForm.setFocus(firstError as any);
    }
  };

  const onBack = () => setStep((s) => Math.max(s - 1, 0));

  const [isOpen, setIsOpen] = useState(false);

  const countryFromForm = (val?: string) => {
    if (!val) return null;
    const found = COUNTRIES.find(
      (c: any) => (c.value ?? c.code ?? c.title) === val || c.code === val
    );
    return found
      ? {
          value: found.value ?? found.code,
          title: found.title ?? found.title ?? found.label,
        }
      : { value: val, title: val };
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

        <Form {...registerForm}>
          <form onSubmit={registerForm.handleSubmit(handleSubmit)}>
            {step === 0 && (
              <div className="flex flex-col gap-6">
                <FormField
                  control={registerForm.control}
                  name="name"
                  render={({ field }) => (
                    <FormItem className="grid gap-3">
                      <FormLabel>Name</FormLabel>
                      <FormControl>
                        <Input
                          id="name"
                          {...field}
                          className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={registerForm.control}
                  name="email"
                  render={({ field }) => (
                    <FormItem className="grid gap-3">
                      <FormLabel>Email</FormLabel>
                      <FormControl>
                        <Input
                          id="email"
                          {...field}
                          className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={registerForm.control}
                  name="password"
                  render={({ field }) => (
                    <FormItem className="grid gap-3">
                      <FormLabel>Password</FormLabel>
                      <FormControl>
                        <Input
                          id="password"
                          type="password"
                          {...field}
                          className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={registerForm.control}
                  name="phoneNumber"
                  render={({ field }) => (
                    <FormItem className="grid gap-3">
                      <FormLabel>Phone</FormLabel>
                      <FormControl>
                        <PhoneNumberInput
                          id="phone"
                          value={field.value || ""}
                          onChange={(val) => field.onChange(val)}
                          defaultCountry="NL"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>
            )}

            {step === 1 && (
              <div className="flex flex-col gap-6">
                <FormField
                  control={registerForm.control}
                  name="country"
                  render={({ field }) => (
                    <FormItem className="grid gap-3">
                      <FormLabel>Country</FormLabel>
                      <FormControl>
                        <CountrySelector
                          id="country"
                          open={isOpen}
                          onToggle={() => setIsOpen(!isOpen)}
                          onChange={(v) => field.onChange(v)}
                          selectedValue={countryFromForm(field.value)}
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={registerForm.control}
                  name="city"
                  render={({ field }) => (
                    <FormItem className="grid gap-3">
                      <FormLabel>City</FormLabel>
                      <FormControl>
                        <Input
                          id="city"
                          {...field}
                          className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={registerForm.control}
                  name="address"
                  render={({ field }) => (
                    <FormItem className="grid gap-3">
                      <FormLabel>Address</FormLabel>
                      <FormControl>
                        <Input
                          id="address"
                          {...field}
                          className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <div className="flex gap-6">
                  <div className="grid gap-2 flex-1">
                    <FormField
                      control={registerForm.control}
                      name="postalCode"
                      render={({ field }) => (
                        <FormItem className="grid gap-3">
                          <FormLabel>Post Code</FormLabel>
                          <FormControl>
                            <Input
                              id="postalCode"
                              {...field}
                              className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                            />
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                  </div>

                  <div className="grid gap-2 w-36">
                    <FormField
                      control={registerForm.control}
                      name="role"
                      render={({ field }) => (
                        <FormItem className="grid gap-3">
                          <FormLabel>Role</FormLabel>
                          <FormControl>
                            <select
                              id="role"
                              {...field}
                              className="w-full h-10 rounded-md border border-[#0f172a1a] bg-white text-sm text-[#0F172A] focus:outline-none appearance-none bg-no-repeat text-center"
                            >
                              <option value="" disabled>
                                Select role
                              </option>
                              <option value="RENTER">Renter</option>
                              <option value="OWNER">Owner</option>
                            </select>
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                  </div>
                </div>
              </div>
            )}
            <div className="w-full flex gap-3 mt-6">
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
                  disabled={isPending}
                  type="submit"
                  className="flex-1 bg-[#0F172A] hover:bg-[#16213b]"
                >
                  {isPending ? (
                    <div className="flex items-center justify-center gap-2">
                      <LoaderCircle className="animate-spin" />
                      <p>Signing up...</p>
                    </div>
                  ) : (
                    "Sign Up"
                  )}
                </Button>
              )}
            </div>

            <div className="flex justify-center items-center mt-3">
              <p className="font-sans text-[#0F172A] text-md">
                Already have an Account?
              </p>
              <CardAction>
                <Link
                  className="font-sans text-[#0F172A] text-md p-0 pl-1 hover:text-[#FD6123]"
                  to="/"
                >
                  Sign In
                </Link>
              </CardAction>
            </div>
          </form>
        </Form>
      </CardContent>
      <CardFooter></CardFooter>
    </Card>
  );
}
