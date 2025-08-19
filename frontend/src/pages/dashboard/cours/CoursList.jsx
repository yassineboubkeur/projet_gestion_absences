import React, { useEffect, useState } from "react";
import { Table, Button } from "react-bootstrap";
import { getAllCours, deleteCours } from "../../../services/coursService";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../../../store/useAuthStore";

export default function CoursList() {
  const [cours, setCours] = useState([]);
  const navigate = useNavigate();
  const { token, isLoadingAuth } = useAuthStore();

  const fetchData = async () => {
    try {
      const data = await getAllCours();
      console.log("Cours fetched:", data);

      const coursWithKey = data.map((c, index) => ({
        ...c,
        key: c.id ?? index,
      }));

      setCours(coursWithKey);
    } catch (err) {
      console.error(err);
      if (err.message.includes("401")) {
        alert("Session expirée. Veuillez vous reconnecter.");
        navigate("/login");
      } else {
        alert("Erreur lors du chargement des cours.");
      }
    }
  };

  useEffect(() => {
    if (!isLoadingAuth) {
      if (!token) {
        navigate("/login");
      } else {
        fetchData();
      }
    }
  }, [token, isLoadingAuth]);

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

  if (isLoadingAuth) {
    return <div>Chargement...</div>;
  }

  return (
    <div className="container mt-4">
      <h2>Liste des cours</h2>
      <Button
        className="mb-3"
        onClick={() => navigate("/dashboard/cours/new")}
      >
        + Ajouter
      </Button>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Code</th>
            <th>Intitulé</th>
            <th>Description</th>
            <th>Coefficient</th>
            <th>Volume Horaire</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {cours.length > 0 ? (
            cours.map((c) => (
              <tr key={c.key}>
                <td>{c.code}</td>
                <td>{c.intitule}</td>
                <td>{c.description}</td>
                <td>{c.coefficient}</td>
                <td>{c.volumeHoraire}</td>
                <td>
                  <Button
                    size="sm"
                    variant="info"
                    onClick={() => navigate(`/dashboard/cours/${c.id}`)}
                  >
                    Détails
                  </Button>{" "}
                  <Button
                    size="sm"
                    variant="warning"
                    onClick={() =>
                      c.id
                        ? navigate(`/dashboard/cours/${c.id}/edit`)
                        : alert("ID manquant pour modifier ce cours")
                    }
                  >
                    Modifier
                  </Button>{" "}
                  <Button
                    size="sm"
                    variant="danger"
                    onClick={() => handleDelete(c.id)}
                  >
                    Supprimer
                  </Button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="6" className="text-center">
                Aucun cours disponible
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </div>
  );
}