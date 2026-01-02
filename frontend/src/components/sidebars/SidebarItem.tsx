import { useLocation, useNavigate } from "react-router-dom";
import { Button } from "../ui/Button";
import type { SidebarItemProps } from "@/schemas/component/sidebar.schema";

export const SidebarItem = ({
  label,
  path,
  imgSrc,
  imgSrcActive,
}: SidebarItemProps) => {
  const navigate = useNavigate();
  const location = useLocation();

  const isSelected = location.pathname === path;

  return (
    <Button
      onClick={() => navigate(path)}
      variant="ghost"
      className="w-full p-0"
    >
      <div className="w-full h-full flex items-center gap-3">
        <div
          className={`h-full w-[5px] rounded-4xl transition-all
            ${isSelected ? "bg-[#00A0A0]" : "bg-transparent"}
          `}
        />
        <img
          src={isSelected ? imgSrcActive : imgSrc}
          className={"h-[24px] w-[24px] text-xl transition-colors"}
        />

        <div className="w-full flex items-center">
          <p
            className={`text-lg transition-colors
              ${isSelected ? "text-[#0F172A]" : "text-gray-400"}
            `}
          >
            {label}
          </p>
        </div>
      </div>
    </Button>
  );
};
