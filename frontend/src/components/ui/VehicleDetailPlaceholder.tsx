import React from "react";

export function VehicleDetailPlaceholder({
  label,
  children,
}: {
  label: string;
  children: React.ReactNode;
}) {
  return (
    <>
      <div>
        <h3 className="text-sm font-medium text-gray-500 mb-1">{label}</h3>
        <p className="text-lg text-gray-900">{children}</p>
      </div>
    </>
  );
}
