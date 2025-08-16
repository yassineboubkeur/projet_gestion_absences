import React, { useEffect, useState } from "react";
import { getMatiereById } from "../../../services/matiereService";
import { useParams, useNavigate } from "react-router-dom";
import { Button } from "react-bootstrap";

export default function MatiereDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [matiere, setMatiere] = useState(null);

  useEffect(() => {
    getMatiereById(id).then(setMatiere).catch(console.error);
  }, [id]);

  if (!matiere) return <div>Chargement...</div>;

  return (
    <div className="container mt-4">
      <h2>Détails de la matière: {matiere.intitule}</h2>
      <p><b>Code:</b> {matiere.code}</p>
      <p><b>Domaine:</b> {matiere.domaine}</p>

      <Button className="mb-3" onClick={() => navigate("/dashboard/matieres")}>Retour</Button>
    </div>
  );
}
