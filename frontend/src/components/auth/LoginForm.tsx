import { Button } from "@/components/ui/Button";
import { Card, CardAction, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import type { LoginFormProps } from "@/types/types";
import { Link } from "react-router-dom";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "../ui/form";
import { LoaderCircle } from "lucide-react";

export function LoginForm({
  loginForm,
  handleSubmit,
  isPending,
}: LoginFormProps) {
  return (
    <Card className="w-full max-w-sm">
      <CardContent>
        <Form {...loginForm}>
          <form onSubmit={loginForm.handleSubmit(handleSubmit)}>
            <div className="flex flex-col gap-6">
              <div className="grid gap-2">
                <FormField
                  control={loginForm.control}
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
              </div>
              <div className="grid gap-2">
                <FormField
                  control={loginForm.control}
                  name="password"
                  render={({ field }) => (
                    <FormItem className="grid gap-3">
                      <div className="flex items-center">
                        <FormLabel>Password</FormLabel>
                        <a
                          href="#"
                          className="ml-auto inline-block text-sm underline-offset-4 hover:underline hover:text-[#FD6123] text-[#0F172A]"
                        >
                          Forgot your password?
                        </a>
                      </div>
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
              </div>
            </div>
            <Button
              disabled={isPending}
              type="submit"
              className="flex-1 bg-[#0F172A] hover:bg-[#16213b] w-full flex gap-3 mt-6"
            >
              {isPending ? (
                <div className="flex items-center justify-center gap-2">
                  <LoaderCircle className="animate-spin" />
                  <p>Signing in...</p>
                </div>
              ) : (
                "Sign In"
              )}
            </Button>
            <div className="flex justify-center items-center mt-3">
              <p className="text-[#0F172A]">Don't have an Account?</p>
              <CardAction>
                <Link
                  className="text-[#0F172A] text-md p-0 pl-1 hover:text-[#FD6123]"
                  to="/auth/register"
                >
                  Sign Up
                </Link>
              </CardAction>
            </div>
          </form>
        </Form>
      </CardContent>
    </Card>
  );
}
