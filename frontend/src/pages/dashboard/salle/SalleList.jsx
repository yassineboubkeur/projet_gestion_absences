import React, { useEffect, useState } from "react";
import { Table, Button } from "react-bootstrap";
import { getAllSalles, deleteSalle, searchSalles } from "../../../services/salleService";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../../../store/useAuthStore";

export default function SalleList() {
  const [salles, setSalles] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const navigate = useNavigate();
  const { token, isLoadingAuth } = useAuthStore();

  const fetchData = async () => {
    try {
      const data = await getAllSalles();
      setSalles(data || []);
    } catch (err) {
      console.error(err);
      if (err.message.includes("401")) {
        alert("Session expirée. Veuillez vous reconnecter.");
        navigate("/login");
      } else {
        alert("Erreur lors du chargement des salles.");
      }
    }
  };

  const handleSearch = async () => {
    try {
      const results = await searchSalles(searchTerm);
      setSalles(results || []);
    } catch (err) {
      console.error(err);
      alert("Erreur lors de la recherche");
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

  if (isLoadingAuth) {
    return <div className="text-center mt-4">Chargement en cours...</div>;
  }

  return (
    <div className="container mt-4">
      <h2 className="mb-4">Liste des salles</h2>
      
      <div className="d-flex justify-content-between mb-3">
        <Button 
          variant="success" 
          onClick={() => navigate("/dashboard/salles/new")}
        >
          <i className="bi bi-plus-circle"></i> Ajouter une salle
        </Button>
        
        <div className="d-flex">
          <input
            type="text"
            placeholder="Rechercher une salle..."
            className="form-control"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            style={{ width: '300px' }}
          />
          <Button 
            variant="primary" 
            onClick={handleSearch}
            className="ms-2"
          >
            <i className="bi bi-search"></i> Rechercher
          </Button>
          <Button 
            variant="secondary" 
            onClick={fetchData}
            className="ms-2"
          >
            <i className="bi bi-arrow-clockwise"></i> Actualiser
          </Button>
        </div>
      </div>

      <Table striped bordered hover responsive>
        <thead className="table-dark">
          <tr>
            <th>Code</th>
            <th>Bâtiment</th>
            <th>Numéro</th>
            <th>Capacité</th>
            <th>Type</th>
            <th className="text-center">Actions</th>
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
                <td className="text-center">
                  <Button
                    size="sm"
                    variant="warning"
                    onClick={() => navigate(`/dashboard/salles/${s.id}/edit`)}
                    className="me-2"
                  >
                    <i className="bi bi-pencil"></i> Modifier
                  </Button>
                  <Button
                    size="sm"
                    variant="info"
                    onClick={() => navigate(`/dashboard/salles/${s.id}`)}
                    className="me-2"
                  >
                    <i className="bi bi-eye"></i> Détails
                  </Button>
                  <Button
                    size="sm"
                    variant="danger"
                    onClick={() => handleDelete(s.id)}
                  >
                    <i className="bi bi-trash"></i> Supprimer
                  </Button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="6" className="text-center text-muted py-4">
                Aucune salle disponible
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </div>
  );
}