import React from "react";
import { Link, useLocation } from "react-router-dom";
import {
  FaTachometerAlt,
  FaUserFriends,
  FaUsers,
  FaChalkboardTeacher,
  FaBook,
  FaCalendarAlt,
  FaDoorOpen,
  FaClock,
  FaUserTie,
  FaBoxes,
  FaCog,
} from "react-icons/fa";
import "../layouts/css/Sidebar.css";

export default function Sidebar() {
  const location = useLocation();

  const menuItems = [
    { name: "Dashboard", path: "/dashboard", icon: <FaTachometerAlt /> },
    { name: "Etudiants", path: "/dashboard/etudiants", icon: <FaUserFriends /> },
    { name: "Classes", path: "/dashboard/classes", icon: <FaUsers /> },
    { name: "Filieres", path: "/dashboard/filieres", icon: <FaChalkboardTeacher /> },
    { name: "Cours", path: "/dashboard/cours", icon: <FaBook /> },
    { name: "Matieres", path: "/dashboard/matieres", icon: <FaBook /> },
    { name: "Emplois-temps", path: "/dashboard/emplois-temps", icon: <FaCalendarAlt /> },
    { name: "Salles", path: "/dashboard/salles", icon: <FaDoorOpen /> },
    { name: "Seances", path: "/dashboard/seances", icon: <FaClock /> },
    { name: "Professeurs", path: "/dashboard/professeurs", icon: <FaUserTie /> },
    { name: "Specialites", path: "/dashboard/specialites", icon: <FaBoxes /> },
    { name: "Parametres", path: "/dashboard/parametres", icon: <FaCog /> },
  ];

  return (
    <nav
      className="bg-light p-3 border rounded"
      style={{ width: "250px", minHeight: "calc(100vh - 120px)", position: "sticky", top: "80px" }}
    >
      <ul className="nav flex-column">
        {menuItems.map(({ name, path, icon }) => (
          <li key={name} className="nav-item mb-2">
            <Link
              to={path}
              className={`nav-link d-flex align-items-center gap-2 ${
                location.pathname === path ? "active fw-bold text-primary" : "text-dark"
              }`}
              style={{ borderRadius: "4px" }}
            >
              <span style={{ fontSize: "1.2rem" }}>{icon}</span> {name}
            </Link>
          </li>
        ))}
      </ul>
    </nav>
  );
}
