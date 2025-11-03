import React, { Suspense } from "react";
import Loading from "./components/ui/Loading";
import { Route, Routes } from "react-router-dom";
import { Register, Login } from "./routes/(auth)";
import Dashboard from "./routes/(root)/Dashboard/Dashboard";

const App = () => {
  return (
    <Suspense fallback={<Loading />}>
      <Routes>
        <Route path="/auth/register" element={<Register />} />
        <Route path="/" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
      </Routes>
    </Suspense>
  );
};

export default App;
