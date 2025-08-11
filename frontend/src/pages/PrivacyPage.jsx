// src/pages/PrivacyPage.jsx
import React from "react";

export default function PrivacyPage() {
  return (
    <div style={containerStyle}>
      <h1 style={titleStyle}>Politique de confidentialité</h1>
      <p style={textStyle}>
        Nous accordons une grande importance à la protection de vos données personnelles.
        Cette politique explique comment nous collectons, utilisons et protégeons vos informations.
      </p>
      <h2 style={subtitleStyle}>1. Collecte des informations</h2>
      <p style={textStyle}>
        Nous recueillons uniquement les informations nécessaires pour vous fournir nos services,
        telles que votre nom, adresse e-mail et informations liées à votre compte.
      </p>
      <h2 style={subtitleStyle}>2. Utilisation des données</h2>
      <p style={textStyle}>
        Les données collectées sont utilisées pour améliorer votre expérience, gérer vos absences,
        et assurer le bon fonctionnement de notre application.
      </p>
      <h2 style={subtitleStyle}>3. Protection des données</h2>
      <p style={textStyle}>
        Nous mettons en place des mesures de sécurité strictes afin d’empêcher tout accès non autorisé
        à vos données personnelles.
      </p>
    </div>
  );
}

const containerStyle = {
  padding: "2rem",
  maxWidth: "800px",
  margin: "0 auto",
};

const titleStyle = {
  fontSize: "2rem",
  marginBottom: "1rem",
  color: "#0d6efd",
};

const subtitleStyle = {
  fontSize: "1.5rem",
  marginTop: "1.5rem",
  color: "#0d6efd",
};

const textStyle = {
  fontSize: "1rem",
  color: "#555",
  lineHeight: "1.6",
};
