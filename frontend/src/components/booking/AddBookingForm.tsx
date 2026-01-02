import React from "react";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "../ui/form";
import { Button } from "../ui/Button";
import { LoaderCircle } from "lucide-react";
import type { AddBookingFormProps } from "@/types/booking";
import { Calendar05 } from "../ui/calendar-05";

export function AddBookingForm({
  addBookingForm,
  handleSubmit,
  isPending,
}: AddBookingFormProps) {
  return (
    <Form {...addBookingForm}>
      <form
        onSubmit={addBookingForm.handleSubmit(handleSubmit)}
        className="space-y-6"
      >
        <div className="flex flex-col gap-5 justify-center">
          <div className="flex gap-5 justify-between w-full">
            <FormField
              control={addBookingForm.control}
              name="dateRange"
              render={({ field }) => (
                <FormItem className="w-full">
                  <div className="flex items-center">
                    <FormLabel>Period</FormLabel>
                    <FormMessage className="text-right" />
                  </div>
                  <FormControl>
                    <Calendar05 value={field.value} onChange={field.onChange} />
                  </FormControl>
                </FormItem>
              )}
            />
          </div>
        </div>

        <div className="flex pt-4 justify-center items-center">
          <Button
            disabled={isPending}
            type="submit"
            className="w-1/2 h-full bg-[#0F172A] hover:bg-[#16213b]"
          >
            {isPending ? (
              <div className="flex items-center justify-center gap-2">
                <LoaderCircle className="animate-spin" />
                <p>Booking...</p>
              </div>
            ) : (
              "Book Now"
            )}
          </Button>
        </div>
      </form>
    </Form>
  );
}
