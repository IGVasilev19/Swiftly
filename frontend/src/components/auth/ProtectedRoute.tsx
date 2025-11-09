import { Navigate, Outlet } from "react-router-dom";
import { useAuthContext } from "@/contexts/AuthContext";
import Loading from "../ui/Loading";

export default function ProtectedRoute() {
  const { isAuthenticated, loading } = useAuthContext();

  if (loading) return <Loading />;

  if (!isAuthenticated) return <Navigate to="/" replace />;
  return <Outlet />;
}
