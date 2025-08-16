// src/pages/dashboard/SeanceDetails.jsx
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getSeanceById, deleteSeance } from "../../../services/seanceService";
import { Button } from "react-bootstrap";

export default function SeanceDetails() {
  const { id } = useParams();
  const [seance, setSeance] = useState(null);

  const fetchData = async () => {
    try {
      const data = await getSeanceById(id);
      setSeance(data);
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  useEffect(() => {
    fetchData();
  }, [id]);

  const handleDelete = async () => {
    if (!window.confirm("Voulez-vous vraiment supprimer cette séance ?")) return;
    try {
      await deleteSeance(id);
      alert("Séance supprimée");
      window.location.href = "/dashboard/seances";
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  if (!seance) return <div>Chargement...</div>;

  return (
    <div className="container mt-4">
      <h2>Détails de la Séance</h2>
      <p><strong>Cours:</strong> {seance.cours?.nom}</p>
      <p><strong>Professeur:</strong> {seance.professeur?.nom} {seance.professeur?.prenom}</p>
      <p><strong>Salle:</strong> {seance.salle?.code}</p>
      <p><strong>Date:</strong> {seance.date}</p>
      <p><strong>Heure Début:</strong> {seance.heureDebut}</p>
      <p><strong>Heure Fin:</strong> {seance.heureFin}</p>
      <p><strong>Statut:</strong> {seance.statut}</p>
      <Button variant="danger" onClick={handleDelete}>Supprimer</Button>
    </div>
  );
}
