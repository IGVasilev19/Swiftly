import { useAuthContext } from "@/contexts/AuthContext";
import { useNavigate } from "react-router-dom";
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "../ui/dropdown-menu";
import { OwnerSidebarItems } from "../sidebars/OwnerSidebarItems";
import { RenterSidebarItems } from "../sidebars/RenterSidebarItems";
import { Switch } from "../ui/switch";

interface LayoutProps {
  children: React.ReactNode;
}

export function Layout({ children }: LayoutProps) {
  const { logout } = useAuthContext();
  const navigate = useNavigate();
  const { roles } = useAuthContext();

  const isOwner =
    roles &&
    Array.isArray(roles) &&
    roles.some((role) => String(role).toUpperCase() === "OWNER");
  const isRenter =
    roles &&
    Array.isArray(roles) &&
    roles.some((role) => String(role).toUpperCase() === "RENTER");

  return (
    <div className="h-screen w-screen flex">
      <div className="h-full w-[200px] flex flex-col items-center bg-[#F8FAFC]">
        <button
          className="hover:cursor-pointer"
          onClick={() => navigate("/app/dashboard")}
        >
          <img
            src="/images/Logo-nobg.webp"
            alt="Logo"
            className="h-[50px] w-[150px] m-4 rounded-lg object-contain select-none"
          />
        </button>
        <div className="h-full w-full flex flex-col items-center mt-20">
          {isOwner && <OwnerSidebarItems />}
          {!isOwner && isRenter && <RenterSidebarItems />}
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
              {isOwner && isRenter && (
                <DropdownMenuItem className="text-[#0F172A]">
                  <Switch />
                </DropdownMenuItem>
              )}
              <DropdownMenuItem className="text-[#0F172A]" onClick={logout}>
                Log out
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
        <main className="h-full w-full overflow-hidden flex flex-col min-h-0">
          {children}
        </main>
      </div>
    </div>
  );
}
