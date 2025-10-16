import React, { Suspense } from "react";
import Loading from "./components/ui/Loading";
import { Route, Routes } from "react-router-dom";
import Register from "./routes/(auth)/Register";

const App = () => {
  return (
    <Suspense fallback={<Loading />}>
      <Routes>
        <Route path="/" element={<Register />} />
      </Routes>
    </Suspense>
  );
};

export default App;
