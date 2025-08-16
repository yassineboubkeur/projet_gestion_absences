import React, { useEffect, useState } from "react";
import { Table, Button } from "react-bootstrap";
import { getAllSalles, deleteSalle } from "../../../services/salleService";
import { useNavigate } from "react-router-dom";

export default function SalleList() {
  const [salles, setSalles] = useState([]);
  const navigate = useNavigate();

  const fetchData = async () => {
    try {
      const data = await getAllSalles();
      setSalles(data || []);
    } catch (err) {
      console.error(err);
      alert("Erreur lors du chargement des salles.");
    }
  };

  useEffect(() => { fetchData(); }, []);

  const handleDelete = async (id) => {
    if (window.confirm("Supprimer cette salle ?")) {
      try {
        await deleteSalle(id);
        fetchData();
      } catch (err) {
        console.error(err);
        alert("Erreur lors de la suppression de la salle.");
      }
    }
  };

  return (
    <div className="container mt-4">
      <h2>Liste des salles</h2>
      <Button className="mb-3" onClick={() => navigate("/dashboard/salles/new")}>
        + Ajouter
      </Button>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Code</th>
            <th>Batiment</th>
            <th>Numero</th>
            <th>Capacité</th>
            <th>Type</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {salles.length > 0 ? (
            salles.map(s => (
              <tr key={s.id}>
                <td>{s.code}</td>
                <td>{s.batiment}</td>
                <td>{s.numero}</td>
                <td>{s.capacite}</td>
                <td>{s.type}</td>
                <td>
                  <Button
                    size="sm"
                    variant="warning"
                    onClick={() => navigate(`/dashboard/salles/${s.id}/edit`)}
                  >
                    Modifier
                  </Button>{" "}
                  <Button
                    size="sm"
                    variant="info"
                    onClick={() => navigate(`/dashboard/salles/${s.id}`)}
                  >
                    Détails
                  </Button>{" "}
                  <Button
                    size="sm"
                    variant="danger"
                    onClick={() => handleDelete(s.id)}
                  >
                    Supprimer
                  </Button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="6" className="text-center">Aucune salle</td>
            </tr>
          )}
        </tbody>
      </Table>
    </div>
  );
}
