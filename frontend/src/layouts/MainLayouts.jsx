// src/layouts/MainLayout.jsx
import React from 'react'
import Navbar from '../components/Navbar'
import Footer from './Footer'
import { Outlet } from 'react-router-dom'

export default function MainLayout() {
  return (
    <div className="d-flex flex-column min-vh-100">
      <Navbar />
      <main className="flex-grow-1 container mt-5">
        <Outlet />
      </main>
      <Footer />
    </div>
  )
}