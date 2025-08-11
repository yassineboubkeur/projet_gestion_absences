// src/pages/HomePage.jsx
import React from "react";

export default function HomePage() {
  return (
    <div style={containerStyle}>
      <h1 style={titleStyle}>Bienvenue sur notre application de gestion d'absence pour les étudiants</h1>
      <p style={textStyle}>
        Suivez et gérez facilement vos absences, retards et présences dans vos cours.
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
