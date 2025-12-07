import { Navigate } from "react-router-dom";
import { useAuthContext } from "@/contexts/AuthContext";
import Loading from "@/components/ui/Loading";

export function RequireOwner({ children }: { children: React.ReactNode }) {
  const { roles, loading, isAuthenticated } = useAuthContext();

  if (loading) {
    return <Loading />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  const hasOwnerRole =
    roles &&
    Array.isArray(roles) &&
    roles.length > 0 &&
    roles.some((role) => String(role).toUpperCase() === "OWNER");

  if (!hasOwnerRole) {
    return <Navigate to="/unauthorized" replace />;
  }

  return <>{children}</>;
}
