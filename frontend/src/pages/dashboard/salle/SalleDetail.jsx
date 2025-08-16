// src/pages/dashboard/salles/SalleDetail.jsx
import React, { useEffect, useState } from "react";
import { getSalleById } from "../../../services/salleService";
import { useParams, useNavigate } from "react-router-dom";
import { Button, Card } from "react-bootstrap";

export default function SalleDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [salle, setSalle] = useState(null);

  useEffect(() => {
    getSalleById(id)
      .then((data) => {
        if (!data) {
          alert("Salle non trouvée");
          navigate("/dashboard/salles");
          return;
        }
        setSalle(data);
      })
      .catch((err) => {
        console.error(err);
        alert("Erreur lors du chargement de la salle");
        navigate("/dashboard/salles");
      });
  }, [id, navigate]);

  if (!salle) return <div className="container mt-4">Chargement...</div>;

  return (
    <div className="container mt-4">
      <h2>Détails de la Salle</h2>
      <Card className="p-3 shadow-sm">
        <p><strong>Code:</strong> {salle.code}</p>
        <p><strong>Bâtiment:</strong> {salle.batiment}</p>
        <p><strong>Numéro:</strong> {salle.numero}</p>
        <p><strong>Capacité:</strong> {salle.capacite}</p>
        <p><strong>Type:</strong> {salle.type}</p>
      </Card>
      <div className="mt-3">
        <Button variant="secondary" onClick={() => navigate("/dashboard/salles")}>
          Retour
        </Button>{" "}
        <Button
          variant="warning"
          onClick={() => navigate(`/dashboard/salles/${salle.id}/edit`)}
        >
          Modifier
        </Button>
      </div>
    </div>
  );
}
