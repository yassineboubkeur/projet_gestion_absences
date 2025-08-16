// src/pages/dashboard/ProfesseursList.jsx
import React, { useEffect, useState } from "react";
import { getAllProfesseurs, deleteProfesseur } from "../../../services/professeurService";
import { useNavigate } from "react-router-dom";
import { Table, Button } from "react-bootstrap";

export default function ProfesseursList() {
  const [professeurs, setProfesseurs] = useState([]);
  const navigate = useNavigate();

  const fetchData = async () => {
    try {
      const data = await getAllProfesseurs();
      setProfesseurs(data);
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleDelete = async (id) => {
    if (window.confirm("Supprimer ce professeur ?")) {
      try {
        await deleteProfesseur(id);
        fetchData();
      } catch (err) {
        console.error(err);
        alert(err.message);
      }
    }
  };

  return (
    <div className="container mt-4">
      <h2>Liste des professeurs</h2>
      <Button className="mb-3" onClick={() => navigate("/dashboard/professeurs/new")}>+ Ajouter</Button>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Email</th>
            <th>Spécialité</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {professeurs.map((prof) => (
            <tr key={prof.id}>
              <td>{prof.nom}</td>
              <td>{prof.prenom}</td>
              <td>{prof.email}</td>
              <td>{prof.specialite}</td>
              <td>{prof.active ? "Actif" : "Inactif"}</td>
              <td>
                <Button variant="warning" size="sm" onClick={() => navigate(`/dashboard/professeurs/${prof.id}/edit`)}>Modifier</Button>{" "}
                <Button variant="danger" size="sm" onClick={() => handleDelete(prof.id)}>Supprimer</Button>{" "}
                <Button variant="info" size="sm" onClick={() => navigate(`/dashboard/professeurs/${prof.id}`)}>Détails</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
}
