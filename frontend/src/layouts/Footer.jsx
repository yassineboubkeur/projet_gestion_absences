import React from 'react';

export default function Footer() {
  return (
    <footer style={footerStyle}>
      <div>
        &copy; {new Date().getFullYear()} Mon Projet. Tous droits réservés.
      </div>
      <div>
        <a href="/privacy" style={linkStyle}>Politique de confidentialité</a> |{" "}
        <a href="/terms" style={linkStyle}>Conditions d'utilisation</a>
      </div>
    </footer>
  );
}

const footerStyle = {
  marginTop: 'auto',
  padding: '1rem',
  backgroundColor: '#222',
  color: '#eee',
  textAlign: 'center',
  fontSize: '0.9rem',
};

const linkStyle = {
  color: '#61dafb',
  textDecoration: 'none',
};

