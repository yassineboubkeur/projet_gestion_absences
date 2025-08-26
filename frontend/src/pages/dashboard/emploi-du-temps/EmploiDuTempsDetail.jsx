// frontend/src/pages/dashboard/emploi-du-temps/EmploiDuTempsDetail.jsx
import React, { useEffect, useState } from "react";
import { getEmploiDuTempsById } from "../../../services/emploiDuTempsService";
import { useParams, useNavigate } from "react-router-dom";
import { Button, Card, Badge, Table } from "react-bootstrap";

export default function EmploiDuTempsDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [emploi, setEmploi] = useState(null);

  useEffect(() => {
    getEmploiDuTempsById(id)
      .then((data) => {
        if (!data) {
          alert("Emploi du temps non trouvé");
          navigate("/dashboard/emploi-du-temps");
          return;
        }
        setEmploi(data);
      })
      .catch((err) => {
        console.error(err);
        alert("Erreur lors du chargement de l'emploi du temps");
        navigate("/dashboard/emploi-du-temps");
      });
  }, [id, navigate]);

  if (!emploi) return <div className="container mt-4">Chargement...</div>;

  const getStatusBadge = (statut) => {
    const variants = {
      PLANIFIEE: "primary",
      EN_COURS: "warning",
      TERMINEE: "success",
      ANNULEE: "danger"
    };
    return <Badge bg={variants[statut] || "secondary"}>{statut}</Badge>;
  };

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Détails de l'emploi du temps: {emploi.intitule}</h2>
        <div>
          <Button 
            variant="secondary" 
            onClick={() => navigate("/dashboard/emploi-du-temps")}
            className="me-2"
          >
            Retour
          </Button>
          <Button
            variant="warning"
            onClick={() => navigate(`/dashboard/emploi-du-temps/${emploi.id}/edit`)}
          >
            Modifier
          </Button>
        </div>
      </div>

      <Card className="mb-4">
        <Card.Body>
          <Row>
            <Col md={6}>
              <p><strong>Classe:</strong> {emploi.classe?.nom} ({emploi.classe?.niveau})</p>
              <p><strong>Date de début:</strong> {new Date(emploi.dateDebut).toLocaleDateString()}</p>
            </Col>
            <Col md={6}>
              <p><strong>Statut:</strong> {emploi.actif ? "Actif" : "Inactif"}</p>
              <p><strong>Date de fin:</strong> {new Date(emploi.dateFin).toLocaleDateString()}</p>
            </Col>
          </Row>
        </Card.Body>
      </Card>

      <h4>Séances ({emploi.seances?.length || 0})</h4>
      
      {emploi.seances && emploi.seances.length > 0 ? (
        <Table striped bordered hover responsive>
          <thead className="table-dark">
            <tr>
              <th>Date</th>
              <th>Heure</th>
              <th>Cours</th>
              <th>Professeur</th>
              <th>Salle</th>
              <th>Statut</th>
            </tr>
          </thead>
          <tbody>
            {emploi.seances.map(seance => (
              <tr key={seance.id}>
                <td>{new Date(seance.date).toLocaleDateString()}</td>
                <td>{seance.heureDebut} - {seance.heureFin}</td>
                <td>{seance.cours?.intitule} ({seance.cours?.code})</td>
                <td>{seance.professeur?.prenom} {seance.professeur?.nom}</td>
                <td>{seance.salle?.code || seance.salle?.nom}</td>
                <td>{getStatusBadge(seance.statut)}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      ) : (
        <Card>
          <Card.Body className="text-center text-muted py-4">
            Aucune séance planifiée
          </Card.Body>
        </Card>
      )}
    </div>
  );
}