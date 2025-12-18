"use client";

import * as React from "react";
import { type DateRange } from "react-day-picker";

import { Calendar } from "@/components/ui/calendar";

export function Calendar05() {
  const today = new Date();
  const tomorrow = new Date();

  tomorrow.setDate(today.getDate() + 1);

  const [dateRange, setDateRange] = React.useState<DateRange | undefined>({
    from: today,
    to: tomorrow,
  });

  return (
    <Calendar
      mode="range"
      defaultMonth={dateRange?.from}
      selected={dateRange}
      onSelect={setDateRange}
      numberOfMonths={2}
      disabled={{ before: today }}
      className="rounded-lg border shadow-sm w-full"
    />
  );
}
