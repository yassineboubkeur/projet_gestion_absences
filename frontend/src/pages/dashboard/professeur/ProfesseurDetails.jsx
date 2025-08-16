// src/pages/dashboard/ProfesseurDetails.jsx
import React, { useEffect, useState } from "react";
import { getProfesseurById, toggleProfesseurStatus } from "../../../services/professeurService";
import { useParams } from "react-router-dom";
import { Button } from "react-bootstrap";

export default function ProfesseurDetails() {
  const { id } = useParams();
  const [professeur, setProfesseur] = useState(null);

  const fetchData = async () => {
    try {
      const data = await getProfesseurById(id);
      setProfesseur(data);
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  useEffect(() => {
    fetchData();
  }, [id]);

  const handleToggleStatus = async () => {
    try {
      await toggleProfesseurStatus(id);
      fetchData();
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  if (!professeur) return <div>Chargement...</div>;

  return (
    <div className="container mt-4">
      <h2>{professeur.nom} {professeur.prenom}</h2>
      <p>Email: {professeur.email}</p>
      <p>Matricule: {professeur.matricule}</p>
      <p>Adresse: {professeur.adresse}</p>
      <p>Spécialité: {professeur.specialite}</p>
      <p>Status: {professeur.active ? "Actif" : "Inactif"}</p>
      <Button onClick={handleToggleStatus} className="mt-2">
        {professeur.active ? "Désactiver" : "Activer"}
      </Button>
    </div>
  );
}
