import { useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthContext } from "@/contexts/AuthContext";

export function useAuthRedirect(redirectTo: string) {
  const { isAuthenticated, loading } = useAuthContext();
  const navigate = useNavigate();
  const hasRedirected = useRef(false);

  useEffect(() => {
    if (!loading && isAuthenticated && !hasRedirected.current) {
      hasRedirected.current = true;
      navigate(redirectTo, { replace: true });
    }

    if (!loading && !isAuthenticated) {
      hasRedirected.current = false;
    }
  }, [isAuthenticated, loading, navigate, redirectTo]);
}
