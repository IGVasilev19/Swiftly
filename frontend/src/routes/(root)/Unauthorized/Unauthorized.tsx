import { Link } from "react-router-dom";
import { Button } from "@/components/ui/Button";

export function Unauthorized() {
  return (
    <div className="min-h-screen w-screen flex flex-col justify-center items-center gap-6 bg-gray-50">
      <div className="text-center space-y-4">
        <h1 className="text-6xl font-bold text-gray-900">403</h1>
        <h2 className="text-2xl font-semibold text-gray-700">
          Unauthorized Access
        </h2>
        <p className="text-gray-600 max-w-md">
          You don't have permission to access this page. Please contact your
          administrator if you believe this is an error.
        </p>
        <div className="pt-4">
          <Link to="/">
            <Button variant="default">Go to Login</Button>
          </Link>
        </div>
      </div>
    </div>
  );
}

