import { Outlet } from "react-router-dom";
import { ListingProvider } from "@/contexts/ListingContext";

export function ListingLayout() {
  return (
    <ListingProvider>
      <Outlet />
    </ListingProvider>
  );
}
