import { Button } from "@/components/ui/button";
import { useAuthContext } from "@/contexts/AuthContext";
import React from "react";

const Dashboard = () => {
  const { logout } = useAuthContext();
  return (
    <>
      <h1>Dashboard</h1>
      <Button onClick={logout}>Log out</Button>
    </>
  );
};

export default Dashboard;
