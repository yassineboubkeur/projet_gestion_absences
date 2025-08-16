import React, { useEffect, useState } from "react";
import { getAllMatieres, deleteMatiere } from "../../../services/matiereService";
import { useNavigate } from "react-router-dom";
import { Table, Button } from "react-bootstrap";

export default function MatieresList() {
  const [matieres, setMatieres] = useState([]);
  const navigate = useNavigate();

  const fetchData = async () => {
    try {
      const data = await getAllMatieres();
      setMatieres(data);
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleDelete = async (id) => {
    if (window.confirm("Supprimer cette matière ?")) {
      try {
        await deleteMatiere(id);
        fetchData();
      } catch (err) {
        console.error(err);
        alert(err.message);
      }
    }
  };

  return (
    <div className="container mt-4">
      <h2>Liste des matières</h2>
      <Button className="mb-3" onClick={() => navigate("/dashboard/matieres/new")}>+ Ajouter</Button>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Code</th>
            <th>Intitulé</th>
            <th>Domaine</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {matieres.map((m) => (
            <tr key={m.id}>
              <td>{m.code}</td>
              <td>{m.intitule}</td>
              <td>{m.domaine}</td>
              <td>
                <Button variant="warning" size="sm" onClick={() => navigate(`/dashboard/matieres/${m.id}/edit`)}>Modifier</Button>{" "}
                <Button variant="info" size="sm" onClick={() => navigate(`/dashboard/matieres/${m.id}`)}>Détails</Button>{" "}
                <Button variant="danger" size="sm" onClick={() => handleDelete(m.id)}>Supprimer</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
}
