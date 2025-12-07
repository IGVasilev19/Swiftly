import React from "react";
import { SidebarItem } from "./SidebarItem";

export function RenterSidebarItems() {
  return (
    <>
      <SidebarItem
        label="Catalogue"
        path="/app/catalogue"
        imgSrc="/images/dashboard-icon-nobg-gray.webp"
        imgSrcActive="/images/dashboard-icon-nobg.webp"
      />
      <SidebarItem
        label="Bookings"
        path="/app/bookings"
        imgSrc="/images/car-side-nobg-gray.webp"
        imgSrcActive="/images/car-side-nobg.webp"
      />
    </>
  );
}
