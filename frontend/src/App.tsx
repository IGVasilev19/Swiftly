import React, { Suspense } from "react";
import Loading from "./components/ui/Loading";
import { Route, Routes } from "react-router-dom";
import { Register, Login } from "./routes/(auth)";
import Dashboard from "./routes/(root)/Dashboard/Dashboard";
import ProtectedRoute from "./guards/ProtectedRoute";
import { Vehicles } from "./routes/(root)/Vehicles/Vehicles";
import { AddVehicle } from "./routes/(root)/Vehicles/AddVehicle";
import { VehicleDetails } from "./routes/(root)/Vehicles/VehicleDetails";
import { VehicleLayout } from "./guards/VehicleLayout";
import { RoleRouter } from "./guards/RoleRouter";
import { RequireOwner } from "./guards/RequireOwner";
import { RequireRenter } from "./guards/RequireRenter";
import { Catalogue } from "./routes/(root)/Catalogue/Catalogue";
import { Bookings } from "./routes/(root)/Bookings/Bookings";
import { Unauthorized } from "./routes/(root)/Unauthorized/Unauthorized";

const App = () => {
  return (
    <Suspense fallback={<Loading />}>
      <Routes>
        <Route path="/auth/register" element={<Register />} />
        <Route path="/" element={<Login />} />
        <Route path="/unauthorized" element={<Unauthorized />} />
        <Route element={<ProtectedRoute />}>
          <Route element={<VehicleLayout />}>
            <Route path="/app" element={<RoleRouter />}>
              <Route
                path="dashboard"
                element={
                  <RequireOwner>
                    <Dashboard />
                  </RequireOwner>
                }
              />
              <Route
                path="vehicles"
                element={
                  <RequireOwner>
                    <Vehicles />
                  </RequireOwner>
                }
              />
              <Route
                path="vehicles/add"
                element={
                  <RequireOwner>
                    <AddVehicle />
                  </RequireOwner>
                }
              />
              <Route
                path="vehicles/details"
                element={
                  <RequireOwner>
                    <VehicleDetails />
                  </RequireOwner>
                }
              />
              <Route
                path="catalogue"
                element={
                  <RequireRenter>
                    <Catalogue />
                  </RequireRenter>
                }
              />
              <Route
                path="bookings"
                element={
                  <RequireRenter>
                    <Bookings />
                  </RequireRenter>
                }
              />
            </Route>
          </Route>
        </Route>
      </Routes>
    </Suspense>
  );
};

export default App;
