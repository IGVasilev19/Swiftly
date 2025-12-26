"use client";

import * as React from "react";
import { type DateRange } from "react-day-picker";

import { Calendar } from "@/components/ui/calendar";

type Calendar05Props = {
  value?: DateRange;
  onChange: (range: DateRange | undefined) => void;
};

export function Calendar05({ value, onChange }: Calendar05Props) {
  return (
    <Calendar
      mode="range"
      defaultMonth={value?.from}
      selected={value}
      onSelect={onChange}
      numberOfMonths={2}
      disabled={{ before: value?.from || new Date() }}
      className="rounded-lg border shadow-sm w-full"
    />
  );
}
