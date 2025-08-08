import React from 'react';
import { NavLink } from 'react-router-dom';

export default function Navbar() {
  return (
    <nav style={navStyle}>
      <div style={logoStyle}>Mon Projet</div>
      <ul style={navListStyle}>
        <li>
          <NavLink to="/" style={linkStyle} end>
            Accueil
          </NavLink>
        </li>
        <li>
          <NavLink to="/login" style={linkStyle}>
            Connexion
          </NavLink>
        </li>
        <li>
          <NavLink to="/register" style={linkStyle}>
            Inscription
          </NavLink>
        </li>
        <li>
          <NavLink to="/dashboard" style={linkStyle}>
            Tableau de bord
          </NavLink>
        </li>
        <li>
          <NavLink to="/privacy" style={linkStyle}>
            Politique
          </NavLink>
        </li>
      </ul>
    </nav>
  );
}

const navStyle = {
  display: 'flex',
  justifyContent: 'space-between',
  alignItems: 'center',
  padding: '1rem 2rem',
  backgroundColor: '#222',
  color: '#eee',
  fontWeight: 'bold',
};

const logoStyle = {
  fontSize: '1.5rem',
};

const navListStyle = {
  listStyle: 'none',
  display: 'flex',
  gap: '1.5rem',
  margin: 0,
  padding: 0,
};

const linkStyle = ({ isActive }) => ({
  color: isActive ? '#61dafb' : '#eee',
  textDecoration: 'none',
  fontWeight: isActive ? 'bold' : 'normal',
  transition: 'color 0.3s',
});
