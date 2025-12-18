import React from "react";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "../ui/form";
import { Input } from "../ui/input";
import type { AddListingFromProps } from "@/types/listing";
import { Button } from "../ui/button";
import { LoaderCircle } from "lucide-react";
import {
  InputGroup,
  InputGroupAddon,
  InputGroupTextarea,
} from "../ui/input-group";
import { Switch } from "../ui/switch";

export function AddListingForm({
  addListingFrom,
  handleSubmit,
  isPending,
}: AddListingFromProps) {
  return (
    <Form {...addListingFrom}>
      <form
        onSubmit={addListingFrom.handleSubmit(handleSubmit)}
        className="space-y-6"
      >
        <div className="flex flex-col gap-5 justify-center">
          <div className="flex gap-5 justify-between w-full">
            <FormField
              control={addListingFrom.control}
              name="title"
              render={({ field }) => (
                <FormItem className="w-full">
                  <div className="flex items-center">
                    <FormLabel>Title</FormLabel>
                    <FormMessage className="text-right" />
                  </div>
                  <FormControl>
                    <Input
                      {...field}
                      placeholder="Enter Title"
                      className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                    />
                  </FormControl>
                </FormItem>
              )}
            />

            <FormField
              control={addListingFrom.control}
              name="basePricePerDay"
              render={({ field }) => (
                <FormItem className="w-full">
                  <div className="flex items-center justify-between">
                    <FormLabel>Price per day - €</FormLabel>
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
                      placeholder="00.0€"
                      className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10"
                    />
                  </FormControl>
                </FormItem>
              )}
            />
          </div>
          <FormField
            control={addListingFrom.control}
            name="description"
            render={({ field }) => (
              <FormItem>
                <div className="flex items-center justify-between">
                  <FormLabel>Description</FormLabel>
                  <FormMessage className="text-right" />
                </div>

                <FormControl>
                  <InputGroup className="text-[#0F172A] border-[#0f172a1a] caret-[#0F172A] h-10">
                    <InputGroupTextarea
                      placeholder="Enter Description"
                      {...field}
                    />
                    <InputGroupAddon align="block-end" />
                  </InputGroup>
                </FormControl>
              </FormItem>
            )}
          />
          <FormField
            control={addListingFrom.control}
            name="instantBook"
            render={({ field }) => (
              <FormItem>
                <div className="flex items-center justify-between">
                  <FormLabel>Instant book</FormLabel>
                  <FormMessage className="text-right" />
                </div>
                <FormControl>
                  <Switch
                    checked={field.value}
                    onCheckedChange={field.onChange}
                  />
                </FormControl>
              </FormItem>
            )}
          />
        </div>

        <div className="flex pt-4 justify-center items-center">
          <Button
            disabled={isPending}
            type="submit"
            className="w-1/2 h-full bg-[#0F172A] hover:bg-[#16213b]"
            onClick={() => {
              console.log("Submit listing");
            }}
          >
            {isPending ? (
              <div className="flex items-center justify-center gap-2">
                <LoaderCircle className="animate-spin" />
                <p>Creating listing...</p>
              </div>
            ) : (
              "Create Listing"
            )}
          </Button>
        </div>
      </form>
    </Form>
  );
}
