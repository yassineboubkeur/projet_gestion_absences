import React, { useEffect } from 'react'
import { Outlet, useNavigate } from 'react-router-dom'
import Sidebar from "../../layouts/Sidebar";
import { useAuthStore } from "../../store/useAuthStore";

export default function DashboardLayout() {
  const navigate = useNavigate();
  const user = useAuthStore(state => state.user);
  const token = useAuthStore(state => state.token);
  const isLoadingAuth = useAuthStore(state => state.isLoadingAuth);
  const loadAuthFromStorage = useAuthStore(state => state.loadAuthFromStorage);

  useEffect(() => {
    loadAuthFromStorage();
  }, [loadAuthFromStorage]);

  useEffect(() => {
    if (!isLoadingAuth && (!user || !token)) {
      navigate("/login");
    }
  }, [isLoadingAuth, user, token, navigate]);

  if (isLoadingAuth) {
    return <div>Loading authentication...</div>;
  }

  return (
    <div className="d-flex">
      <Sidebar />
      <main className="flex-grow-1 p-4">
        <Outlet />
      </main>
    </div>
  );
}