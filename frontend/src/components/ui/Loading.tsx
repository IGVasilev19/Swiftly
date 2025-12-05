import React from "react";
import { LoaderCircle } from "lucide-react";

const Loading = () => {
  return (
    <div className="flex items-center justify-center w-full h-full">
      <div className="flex flex-col items-center gap-3">
        <LoaderCircle className="h-8 w-8 animate-spin text-[#0F172A]" />
      </div>
    </div>
  );
};

export default Loading;
