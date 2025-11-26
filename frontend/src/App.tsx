import React, { Suspense } from "react";
import Loading from "./components/ui/Loading";
import { Route, Routes } from "react-router-dom";
import { Register, Login } from "./routes/(auth)";
import Dashboard from "./routes/(root)/Dashboard/Dashboard";
import ProtectedRoute from "./components/auth/ProtectedRoute";
import Vehicles from "./routes/(root)/Vehicles/Vehicles";

const App = () => {
  return (
    <Suspense fallback={<Loading />}>
      <Routes>
        <Route path="/auth/register" element={<Register />} />
        <Route path="/" element={<Login />} />
        <Route element={<ProtectedRoute />}>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/vehicles" element={<Vehicles />} />
        </Route>
      </Routes>
    </Suspense>
  );
};

export default App;
