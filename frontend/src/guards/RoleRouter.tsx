import { useAuthContext } from "@/contexts/AuthContext";
import { Navigate, Outlet, useLocation } from "react-router-dom";
import Loading from "@/components/ui/Loading";

export function RoleRouter() {
  const { roles, isAuthenticated, loading } = useAuthContext();
  const location = useLocation();

  if (loading) {
    return <Loading />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  if (location.pathname === "/app") {
    const hasOwnerRole =
      roles &&
      Array.isArray(roles) &&
      roles.length > 0 &&
      roles.some((role) => String(role).toUpperCase() === "OWNER");
    const hasRenterRole =
      roles &&
      Array.isArray(roles) &&
      roles.length > 0 &&
      roles.some((role) => String(role).toUpperCase() === "RENTER");

    if (hasOwnerRole) {
      return <Navigate to="/app/dashboard" replace />;
    }

    if (hasRenterRole) {
      return <Navigate to="/app/catalogue" replace />;
    }

    return <Navigate to="/unauthorized" replace />;
  }

  return <Outlet />;
}
