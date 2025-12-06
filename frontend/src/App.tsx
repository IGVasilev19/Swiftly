import React, { Suspense } from "react";
import Loading from "./components/ui/Loading";
import { Route, Routes } from "react-router-dom";
import { Register, Login } from "./routes/(auth)";
import Dashboard from "./routes/(root)/Dashboard/Dashboard";
import ProtectedRoute from "./components/auth/ProtectedRoute";
import { Vehicles } from "./routes/(root)/Vehicles/Vehicles";
import { AddVehicle } from "./routes/(root)/Vehicles/AddVehicle";
import VehicleDetails from "./routes/(root)/Vehicles/VehicleDetails";
import { VehicleLayout } from "./components/layout/VehicleLayout";

const App = () => {
  return (
    <Suspense fallback={<Loading />}>
      <Routes>
        <Route path="/auth/register" element={<Register />} />
        <Route path="/" element={<Login />} />
        <Route element={<ProtectedRoute />}>
          <Route element={<VehicleLayout />}>
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/vehicles" element={<Vehicles />} />
            <Route path="/vehicles/add" element={<AddVehicle />} />
            <Route path="/vehicles/details" element={<VehicleDetails />} />
          </Route>
        </Route>
      </Routes>
    </Suspense>
  );
};

export default App;
