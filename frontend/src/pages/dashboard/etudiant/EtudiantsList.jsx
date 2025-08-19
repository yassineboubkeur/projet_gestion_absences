// src/pages/dashboard/EtudiantsList.jsx
import React, { useEffect, useState } from "react";
import { getEtudiants, deleteEtudiant } from "../../../services/etudiantService";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../../../store/useAuthStore";

export default function EtudiantsList() {
  const [etudiants, setEtudiants] = useState([]);
  const navigate = useNavigate();
  const { token, isLoadingAuth } = useAuthStore();

  const fetchData = async () => {
    try {
      const data = await getEtudiants();
      setEtudiants(data);
    } catch (err) {
      console.error(err);
      if (err.message.includes("401")) {
        alert("Session expirée. Veuillez vous reconnecter.");
        navigate("/login");
      } else {
        alert(err.message);
      }
    }
  };

  useEffect(() => {
    if (!isLoadingAuth) {
      if (!token) {
        navigate("/login");
      } else {
        fetchData();
      }
    }
  }, [token, isLoadingAuth]);

  const handleDelete = async (id) => {
    if (window.confirm("Supprimer cet étudiant ?")) {
      try {
        await deleteEtudiant(id);
        fetchData();
      } catch (err) {
        console.error(err);
        alert(err.message);
      }
    }
  };

  if (isLoadingAuth) {
    return <div>Chargement...</div>;
  }

  return (
    <div className="container mt-4">
      <h2 className="mb-3">Liste des étudiants</h2>
      <button
        className="btn btn-success mb-3"
        onClick={() => navigate("/dashboard/etudiants/new")}
      >
        + Ajouter
      </button>
      <table className="table table-striped table-hover table-bordered">
        <thead className="table-dark">
          <tr>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Email</th>
            <th>Classe</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {etudiants.map((et) => (
            <tr key={et.id}>
              <td>{et.nom}</td>
              <td>{et.prenom}</td>
              <td>{et.email}</td>
              <td>{et.classe?.nom}</td>
              <td>
                <button
                  className="btn btn-warning btn-sm me-2"
                  onClick={() => navigate(`/dashboard/etudiants/${et.id}/edit`)}
                >
                  Modifier
                </button>
                <button
                  className="btn btn-danger btn-sm me-2"
                  onClick={() => handleDelete(et.id)}
                >
                  Supprimer
                </button>
                <button
                  className="btn btn-info btn-sm"
                  onClick={() => navigate(`/dashboard/etudiants/${et.id}`)}
                >
                  Détails
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}