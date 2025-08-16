// src/pages/dashboard/SeanceList.jsx
import React, { useEffect, useState } from "react";
import { getAllSeances, deleteSeance } from "../../../services/seanceService";
import { Table, Button } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

export default function SeanceList() {
  const [seances, setSeances] = useState([]);
  const navigate = useNavigate();

  const fetchData = async () => {
    try {
      const data = await getAllSeances();
      setSeances(data);
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleDelete = async (id) => {
    if (!window.confirm("Supprimer cette séance ?")) return;
    try {
      await deleteSeance(id);
      fetchData();
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  return (
    <div className="container mt-4">
      <h2>Liste des Séances</h2>
      <Button className="mb-3" onClick={() => navigate("/dashboard/seances/new")}>
        + Ajouter
      </Button>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Cours</th>
            <th>Professeur</th>
            <th>Salle</th>
            <th>Date</th>
            <th>Heure Début</th>
            <th>Heure Fin</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {seances.map((s) => (
            <tr key={s.id}>
              <td>{s.cours?.nom}</td>
              <td>{s.professeur?.nom} {s.professeur?.prenom}</td>
              <td>{s.salle?.code}</td>
              <td>{s.date}</td>
              <td>{s.heureDebut}</td>
              <td>{s.heureFin}</td>
              <td>
                <Button
                  variant="warning"
                  size="sm"
                  onClick={() => navigate(`/dashboard/seances/${s.id}/edit`)}
                >
                  Modifier
                </Button>{" "}
                <Button
                  size="sm"
                  variant="info"
                  onClick={() => navigate(`/dashboard/seances/${s.id}`)}
                >
                  Détails
                </Button>{" "}
                <Button
                  variant="danger"
                  size="sm"
                  onClick={() => handleDelete(s.id)}
                >
                  Supprimer
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
}
