import { Layout } from "@/components/layout/Layout";
import React from "react";
import CatalogueGrid from "@/components/ui/CatalogueGrid";

export function Catalogue() {
  return (
    <Layout>
      <div className="w-full h-full p-4">
        <CatalogueGrid />
      </div>
    </Layout>
  );
}
