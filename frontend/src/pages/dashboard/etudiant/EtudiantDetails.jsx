// src/pages/dashboard/EtudiantDetails.jsx
import React, { useEffect, useState } from "react";
import {
  getEtudiantById,
  changeClasse,
} from "../../../services/etudiantService";
import { useParams } from "react-router-dom";

export default function EtudiantDetails() {
  const { id } = useParams();
  const [etudiant, setEtudiant] = useState(null);
  const [newClasseId, setNewClasseId] = useState("");

  const fetchData = async () => {
    try {
      const data = await getEtudiantById(id);
      setEtudiant(data);
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  useEffect(() => {
    fetchData();
  }, [id]);

  const handleChangeClasse = async () => {
    try {
      await changeClasse(id, newClasseId);
      fetchData();
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  if (!etudiant) return <div>Chargement...</div>;

  return (
    <div className="container mt-4">
      <h2>
        {etudiant.nom} {etudiant.prenom}
      </h2>
      <p>
        <strong>Email:</strong> {etudiant.email}
      </p>
      <p>
        <strong>Matricule:</strong> {etudiant.matricule}
      </p>
      <p>
        <strong>Adresse:</strong> {etudiant.address}
      </p>
      <p>
        <strong>Classe:</strong> {etudiant.classe?.nom}
      </p>

      <h3 className="mt-4">Changer de classe</h3>
      <div className="input-group mb-3">
        <input
          className="form-control"
          placeholder="Nouvelle Classe ID"
          value={newClasseId}
          onChange={(e) => setNewClasseId(e.target.value)}
        />
        <button className="btn btn-primary" onClick={handleChangeClasse}>
          Changer
        </button>
      </div>
    </div>
  );
}
