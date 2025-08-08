import React from 'react'
import Navbar from '../layouts/Navbar'
import Footer from './Footer'
import { Outlet } from 'react-router-dom'

export default function MainLayout() {
  return (
    <div style={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      <Navbar />
      <main style={{ flexGrow: 1, flexShrink: 0, flexBasis: 'auto', minHeight: 0 }}>
        <Outlet />
      </main>
      <Footer />
    </div>
  )
}
