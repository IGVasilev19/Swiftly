import { DataTable, type Column } from "../ui/DataTable";
import type { Booking } from "@/types/booking";

export interface BookingTableProps {
  bookings: Booking[];
  onRowClick?: (booking: Booking) => void;
  maxHeight?: string;
  itemsPerPage?: number;
}

const formatDate = (dateString: string) => {
  try {
    return new Date(dateString).toLocaleDateString("en-GB", {
      dateStyle: "medium",
    });
  } catch {
    return dateString;
  }
};

const formatCurrency = (amount?: number) => {
  if (amount === undefined || amount === null) return "-";
  return `€${amount.toFixed(2)}`;
};

export function BookingTable({
  bookings,
  onRowClick,
  maxHeight = "600px",
  itemsPerPage = 10,
}: BookingTableProps) {
  const columns: Column<Booking>[] = [
    {
      header: "Listing Title",
      render: (booking) => booking.listing?.title || "-",
    },
    {
      header: "Vehicle",
      render: (booking) =>
        booking.listing?.vehicle
          ? `${booking.listing.vehicle.make} ${booking.listing.vehicle.model} ${
              booking.listing.vehicle.year || ""
            }`.trim()
          : "-",
    },
    {
      header: "Start Date",
      render: (booking) => formatDate(booking.startAt),
    },
    {
      header: "End Date",
      render: (booking) => formatDate(booking.endAt),
    },
    {
      header: "Status",
      render: (booking) => (
        <span
          className={`inline-block px-2 py-1 rounded-full text-xs font-medium ${
            booking.status === "CONFIRMED"
              ? "bg-green-100 text-green-800"
              : booking.status === "PENDING"
              ? "bg-yellow-100 text-yellow-800"
              : booking.status === "CANCELLED"
              ? "bg-red-100 text-red-800"
              : "bg-gray-100 text-gray-800"
          }`}
        >
          {booking.status || "PENDING"}
        </span>
      ),
    },
    {
      header: "Total Price",
      render: (booking) => formatCurrency(booking.totalPrice),
      className: "text-right",
    },
  ];

  return (
    <DataTable
      data={bookings}
      columns={columns}
      getKey={(booking, index) => booking.id || index}
      emptyMessage="No bookings found"
      itemName="bookings"
      onRowClick={onRowClick}
      maxHeight={maxHeight}
      itemsPerPage={itemsPerPage}
    />
  );
}
