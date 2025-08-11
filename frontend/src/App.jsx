// src/App.jsx
import React from "react";
import { Routes, Route } from "react-router-dom";

import DashboardLayout from "./pages/dashboard/DashboardLayout";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import MainLayout from "./layouts/MainLayouts";
import HomePage from "./pages/HomePage"; 
import { useAuthStore } from "./store/useAuthStore";
import PrivacyPage from "./pages/PrivacyPage";
import DashboardHome from "./pages/dashboard/DashboardHome";

export default function App() {
  const token = useAuthStore((state) => state.token);

  return (
    <Routes>
      <Route element={<MainLayout />}>
        <Route path="/" element={<HomePage />} />
        {!token && (
          <>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
          </>
        )}
        <Route path="/privacy" element={<PrivacyPage />} />
          <Route path="/dashboard" element={<DashboardLayout />}>
          <Route index element={<DashboardHome />} />
            {/* Add other dashboard routes here */}
          </Route>
      </Route>
    </Routes>
  );
}
