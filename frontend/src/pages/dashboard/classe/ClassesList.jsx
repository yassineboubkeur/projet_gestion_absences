import React, { useEffect, useState } from "react";
import { getAllClasses, deleteClasse } from "../../../services/classeService";
import { useNavigate } from "react-router-dom";
import { Table, Button } from "react-bootstrap";

export default function ClassesList() {
  const [classes, setClasses] = useState([]);
  const navigate = useNavigate();

  const fetchData = async () => {
    try {
      const data = await getAllClasses();
      setClasses(data);
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const handleDelete = async (id) => {
    if (window.confirm("Supprimer cette classe ?")) {
      try {
        await deleteClasse(id);
        fetchData();
      } catch (err) {
        console.error(err);
        alert(err.message);
      }
    }
  };

  return (
    <div className="container mt-4">
      <h2>Liste des classes</h2>
      <Button className="mb-3" onClick={() => navigate("/dashboard/classes/new")}>+ Ajouter</Button>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Nom</th>
            <th>Niveau</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {classes.map((cls) => (
            <tr key={cls.id}>
              <td>{cls.nom}</td>
              <td>{cls.niveau}</td>
              <td>
                <Button variant="warning" size="sm" onClick={() => navigate(`/dashboard/classes/${cls.id}/edit`)}>Modifier</Button>{" "}
                <Button variant="info" size="sm" onClick={() => navigate(`/dashboard/classes/${cls.id}`)}>Détails</Button>{" "}
                <Button variant="danger" size="sm" onClick={() => handleDelete(cls.id)}>Supprimer</Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
}
