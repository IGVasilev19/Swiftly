import { Navigate } from "react-router-dom";
import { useAuthContext } from "@/contexts/AuthContext";
import Loading from "@/components/ui/Loading";

export function RequireRenter({ children }: { children: React.ReactNode }) {
  const { roles, loading } = useAuthContext();

  if (loading) {
    return <Loading />;
  }

  const hasRenterRole =
    roles &&
    Array.isArray(roles) &&
    roles.some((role) => String(role).toUpperCase() === "RENTER");

  if (!hasRenterRole) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <>{children}</>;
}
