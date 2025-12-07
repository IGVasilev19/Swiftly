import React from "react";
import { SidebarItem } from "./SidebarItem";

export function OwnerSidebarItems() {
  return (
    <>
      <SidebarItem
        label="Dashboard"
        path="/app/dashboard"
        imgSrc="/images/dashboard-icon-nobg-gray.webp"
        imgSrcActive="/images/dashboard-icon-nobg.webp"
      />
      <SidebarItem
        label="Vehicles"
        path="/app/vehicles"
        imgSrc="/images/car-side-nobg-gray.webp"
        imgSrcActive="/images/car-side-nobg.webp"
      />
    </>
  );
}
