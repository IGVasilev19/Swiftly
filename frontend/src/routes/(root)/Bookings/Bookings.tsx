import { Layout } from "@/components/layout/Layout";
import Loading from "@/components/ui/Loading";
import { useBooking } from "@/hooks/useBooking";
import { BookingTable } from "@/components/booking/BookingTable";
import type { Booking } from "@/types/booking";

export function Bookings() {
  const { data, isLoading, error } = useBooking({ role: "RENTER" });
  const bookings: Booking[] = Array.isArray(data) ? data : [];

  if (isLoading) {
    return (
      <Layout>
        <div className="w-full h-full flex items-center justify-center">
          <Loading />
        </div>
      </Layout>
    );
  }

  if (error) {
    return (
      <Layout>
        <div className="w-full h-full flex items-center justify-center">
          <div className="text-red-500">
            Error loading bookings: {error.message}
          </div>
        </div>
      </Layout>
    );
  }
  //test
  return (
    <Layout>
      <div className="w-full h-full flex flex-col pt-4 pl-4 pr-4 gap-5">
        <div className="h-full w-full flex">
          <BookingTable bookings={bookings} />
        </div>
      </div>
    </Layout>
  );
}
