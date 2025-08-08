// src/App.jsx
import React from 'react'
import { Routes, Route } from 'react-router-dom'

import DashboardLayout from "./pages/dashboard/DashboardLayout"; ;
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import MainLayout from './layouts/MainLayouts';


export default function App() {
  return (
    <Routes>
      <Route element={<MainLayout />}>

        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/" element={<LoginPage />} />


        <Route path="/dashboard" element={<DashboardLayout />}>
    {/*  <Route path="profile" element={<Profile />} />
          <Route path="clients" element={<Clients />} />
          <Route path="services" element={<Services />} />
          <Route path="services/add" element={<AddService />} />
          <Route path="services/edit/:id" element={<UpdateService />} />
          <Route path="products" element={<Products />} />
          <Route path="products/add" element={<AddProduct />} />
          <Route path="products/edit/:id" element={<UpdateProduct />} /> */}
        </Route> 
      </Route>
    </Routes>
  )
}