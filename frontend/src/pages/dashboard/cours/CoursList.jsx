import React, { useEffect, useState } from "react";
import { Table, Button } from "react-bootstrap";
import { getAllCours, deleteCours } from "../../../services/coursService";
import { useNavigate } from "react-router-dom";

export default function CoursList() {
  const [cours, setCours] = useState([]);
  const navigate = useNavigate();

  const fetchData = async () => {
    try {
      const data = await getAllCours();
      setCours(data || []);
    } catch (err) {
      console.error(err);
      alert("Erreur lors du chargement des cours.");
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleDelete = async (id) => {
    if (window.confirm("Supprimer ce cours ?")) {
      try {
        await deleteCours(id);
        fetchData();
      } catch (err) {
        console.error(err);
        alert("Erreur lors de la suppression du cours.");
      }
    }
  };

  return (
    <div className="container mt-4">
      <h2>Liste des cours</h2>
      <Button className="mb-3" onClick={() => navigate("/dashboard/cours/new")}>+ Ajouter</Button>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Code</th>
            <th>Intitulé</th>
            <th>Description</th>
            <th>Coefficient</th>
            <th>Volume Horaire</th>
            <th>Matière</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {cours.length > 0 ? (
            cours.map(c => (
              <tr key={c.id}>
                <td>{c.code}</td>
                <td>{c.intitule}</td>
                <td>{c.description}</td>
                <td>{c.coefficient}</td>
                <td>{c.volumeHoraire}</td>
                <td>{c.matiere?.intitule || "N/A"}</td>
                <td>
                  <Button size="sm" variant="info" onClick={() => navigate(`/dashboard/cours/${c.id}`)}>Détails</Button>{" "}
                  <Button size="sm" variant="warning" onClick={() => navigate(`/dashboard/cours/${c.id}/edit`)}>Modifier</Button>{" "}
                  <Button size="sm" variant="danger" onClick={() => handleDelete(c.id)}>Supprimer</Button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="7" className="text-center">Aucun cours disponible</td>
            </tr>
          )}
        </tbody>
      </Table>
    </div>
  );
}
