// src/pages/dashboard/DashboardHome.jsx
import React from "react";
import { useAuthStore } from "../../store/useAuthStore";

export default function DashboardHome() {
  const user = useAuthStore((state) => state.user);

  if (!user) {
    return <h2>Chargement...</h2>;
  }

  let message = "";

  switch (user.role) {
    case "ROLE_ADMIN":
      message = `Bienvenue admin ${user.nomComplet}`;
      break;
    case "ROLE_ETUDIANT":
      message = `Bienvenue étudiant ${user.nomComplet}`;
      break;
    case "ROLE_PROFESSEUR":
      message = `Bienvenue professeur ${user.nomComplet}`;
      break;
    default:
      message = `Bienvenue ${user.nomComplet}`;
      break;
  }

  return (
    <div style={containerStyle}>
      <h1 style={titleStyle}>{message}</h1>
      <p style={textStyle}>
        Vous êtes connecté à votre tableau de bord.
      </p>
    </div>
  );
}

const containerStyle = {
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
  justifyContent: "center",
  minHeight: "70vh",
  textAlign: "center",
  padding: "2rem",
};

const titleStyle = {
  fontSize: "2rem",
  marginBottom: "1rem",
  color: "#0d6efd",
};

const textStyle = {
  fontSize: "1.2rem",
  color: "#555",
};
