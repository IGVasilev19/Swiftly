import React from "react";
import { SidebarItem } from "./SidebarItem";

export function RenterSidebarItems() {
  return (
    <>
      <SidebarItem
        label="Catalogue"
        path="/app/catalogue"
        imgSrc="/images/layout-grid-gray.webp"
        imgSrcActive="/images/layout-grid.webp"
      />
      <SidebarItem
        label="Bookings"
        path="/app/bookings"
        imgSrc="/images/notebook-pen-gray.webp"
        imgSrcActive="/images/notebook-pen.webp"
      />
    </>
  );
}
