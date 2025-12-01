import { ReactNode, useState } from "react";
import { Button } from "./button";
import { useAuthContext } from "@/contexts/AuthContext";
import { useNavigate } from "react-router-dom";
import { Avatar, AvatarFallback, AvatarImage } from "./avatar";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "./dropdown-menu";
import { SidebarItem } from "../sidebars/SidebarItem";

interface LayoutProps {
  children: ReactNode;
}

export function Layout({ children }: LayoutProps) {
  const { logout } = useAuthContext();
  const navigate = useNavigate();

  return (
    <div className="h-screen w-screen flex">
      <div className="h-full w-[200px] flex flex-col items-center bg-[#F8FAFC]">
        <button
          className="hover:cursor-pointer"
          onClick={() => navigate("/dashboard")}
        >
          <img
            src="/images/Logo-nobg.webp"
            alt="Logo"
            className="h-[50px] w-[150px] m-4 rounded-lg object-contain select-none"
          />
        </button>
        <div className="h-full w-full flex flex-col items-center mt-20">
          <SidebarItem
            label="Dashboard"
            path="/dashboard"
            imgSrc="/images/dashboard-icon-nobg-gray.webp"
            imgSrcActive="/images/dashboard-icon-nobg.webp"
          />
          <SidebarItem
            label="Vehicles"
            path="/vehicles"
            imgSrc="/images/car-side-nobg-gray.webp"
            imgSrcActive="/images/car-side-nobg.webp"
          />
        </div>
      </div>
      <div className="w-full h-full flex flex-col">
        <div className="w-full h-[80px] flex border-b-2 border-[#F8FAFC] justify-end items-center">
          <DropdownMenu>
            <DropdownMenuTrigger>
              <Avatar className="w-[50px] h-[50px] m-4 select-none">
                <AvatarImage src="https://github.com/shadcn.png" />
                <AvatarFallback>CN</AvatarFallback>
              </Avatar>
            </DropdownMenuTrigger>
            <DropdownMenuContent className="mr-4">
              <DropdownMenuLabel className="text-[#0F172A]">
                My Account
              </DropdownMenuLabel>
              <DropdownMenuSeparator />
              <DropdownMenuItem className="text-[#0F172A]">
                Settings
              </DropdownMenuItem>
              <DropdownMenuItem className="text-[#0F172A]" onClick={logout}>
                Log out
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
        <main className="flex-1 p-6">{children}</main>
      </div>
    </div>
  );
}
