import React, { useEffect, useState } from "react";
import { getCoursById } from "../../../services/coursService";
import { useParams, useNavigate } from "react-router-dom";
import { Button } from "react-bootstrap";

export default function CoursDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [cours, setCours] = useState(null);

  useEffect(() => {
    getCoursById(id).then(setCours).catch(console.error);
  }, [id]);

  if (!cours) return <div>Chargement...</div>;

  return (
    <div className="container mt-4">
      <h2>Détails du cours: {cours.intitule}</h2>
      <p>Code: {cours.code}</p>
      <p>Description: {cours.description}</p>
      <p>Coefficient: {cours.coefficient}</p>
      <p>Volume Horaire: {cours.volumeHoraire}</p>
      <p>Matière: {cours.matiere?.nom}</p>
      <Button onClick={() => navigate("/dashboard/cours")}>Retour</Button>
    </div>
  );
}
