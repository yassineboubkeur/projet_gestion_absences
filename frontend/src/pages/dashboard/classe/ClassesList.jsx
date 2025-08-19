import React, { useEffect, useState } from "react";
import { getAllClasses, deleteClasse } from "../../../services/classeService";
import { useNavigate } from "react-router-dom";
import { Table, Button } from "react-bootstrap";
import { useAuthStore } from "../../../store/useAuthStore";

export default function ClassesList() {
  const [classes, setClasses] = useState([]);
  const navigate = useNavigate();
  const { token, isLoadingAuth } = useAuthStore();

  const fetchData = async () => {
    try {
      const data = await getAllClasses();
      setClasses(data);
    } catch (err) {
      console.error(err);
      if (err.message.includes("401")) {
        alert("Session expirée. Veuillez vous reconnecter.");
        navigate("/login");
      } else {
        alert(err.message);
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

  if (isLoadingAuth) {
    return <div>Chargement...</div>;
  }

  return (
    <div className="container mt-4">
      <h2>Liste des classes</h2>
      <Button 
        className="mb-3" 
        onClick={() => navigate("/dashboard/classes/new")}
      >
        + Ajouter
      </Button>
      
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Nom</th>
            <th>Niveau</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {classes.length > 0 ? (
            classes.map((cls) => (
              <tr key={cls.id}>
                <td>{cls.nom}</td>
                <td>{cls.niveau}</td>
                <td>
                  <Button 
                    variant="warning" 
                    size="sm" 
                    onClick={() => navigate(`/dashboard/classes/${cls.id}/edit`)}
                  >
                    Modifier
                  </Button>{" "}
                  <Button 
                    variant="info" 
                    size="sm" 
                    onClick={() => navigate(`/dashboard/classes/${cls.id}`)}
                  >
                    Détails
                  </Button>{" "}
                  <Button 
                    variant="danger" 
                    size="sm" 
                    onClick={() => handleDelete(cls.id)}
                  >
                    Supprimer
                  </Button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="3" className="text-center">
                Aucune classe disponible
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </div>
  );
}