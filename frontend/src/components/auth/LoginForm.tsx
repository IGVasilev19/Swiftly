import { Button } from "@/components/ui/button";
import {
  Card,
  CardAction,
  CardContent,
  CardFooter,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Link } from "react-router-dom";

export function LoginForm() {
  return (
    <Card className="w-full max-w-sm">
      <CardContent>
        <form>
          <div className="flex flex-col gap-6">
            <div className="grid gap-2">
              <div className="flex items-baseline justify-between">
                <Label
                  htmlFor="email"
                  className="font-sans text-[#0F172A] text-md"
                >
                  Email
                </Label>
                {/* <span
                  className="text-sm text-destructive min-h-4 ml-4"
                  aria-live="polite"
                >
                  {errors.email?.message ?? ""}
                </span> */}
              </div>
              <Input
                id="email"
                //{...login("email")}
                className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A]"
              />
            </div>

            <div className="grid gap-2">
              <div className="flex items-center">
                <Label
                  htmlFor="password"
                  className="font-sans text-[#0F172A] text-md"
                >
                  Password
                </Label>
                <a
                  href="#"
                  className="ml-auto inline-block text-sm underline-offset-4 hover:underline hover:text-[#FD6123] text-[#0F172A]"
                >
                  Forgot your password?
                </a>
              </div>
              <Input id="password" type="password" required />
            </div>
          </div>
        </form>
      </CardContent>
      <CardFooter className="flex-col gap-2">
        <Button
          type="submit"
          className="w-full bg-[#0F172A] font-sans text-md hover:bg-[#0f172aec]"
        >
          Login
        </Button>
        <div className="flex justify-center items-center">
          <p className="text-[#0F172A]">Don't have an Account?</p>
          <CardAction>
            <Link
              className="text-[#0F172A] text-md p-0 pl-1 hover:text-[#FD6123]"
              to="/register"
            >
              Sign Up
            </Link>
          </CardAction>
        </div>
      </CardFooter>
    </Card>
  );
}
