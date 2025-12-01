import { Button } from "@/components/ui/button";
import { LoaderCircle } from "lucide-react";
import { Card, CardAction, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import PhoneNumberInput from "@/components/ui/PhoneNumberInput";
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
  return (
    <Card className="w-full max-w-sm">
      <CardContent>
        <Form {...registerForm}>
          <form onSubmit={registerForm.handleSubmit(handleSubmit)}>
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
                name="confirmPassword"
                render={({ field }) => (
                  <FormItem className="grid gap-3">
                    <FormLabel>Confirm Password</FormLabel>
                    <FormControl>
                      <Input
                        id="confirmPassword"
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

              <FormField
                control={registerForm.control}
                name="roles"
                render={({ field }) => (
                  <FormItem className="grid gap-3">
                    <FormLabel>Role</FormLabel>
                    <FormControl>
                      <select
                        id="roles"
                        value={field.value?.[0] ?? ""}
                        onChange={(e) => field.onChange([e.target.value])}
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
            <div className="w-full flex gap-3 mt-6">
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
    </Card>
  );
}
