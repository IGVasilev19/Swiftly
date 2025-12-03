import { Button } from "@/components/ui/button";
import { Layout } from "@/components/ui/Layout";
import React from "react";
import { useNavigate } from "react-router-dom";

function Vehicles() {
  const navigate = useNavigate();

  return (
    <Layout>
      <div className="w-full h-full flex flex-col pt-4 pl-4 pr-4">
        <div className="flex justify-end">
          <Button
            variant="default"
            className="w-50px bg-[#0F172A] hover:bg-[#0f172adc] select-none"
            onClick={() => navigate("/vehicles/add")}
          >
            Add +
          </Button>
        </div>
        <div className="h-full w-full flex"></div>
      </div>
    </Layout>
  );
}

export default Vehicles;
