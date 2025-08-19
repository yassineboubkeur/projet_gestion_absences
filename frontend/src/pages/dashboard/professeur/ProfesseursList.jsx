import React, { useEffect, useState } from "react";
import { 
  getAllProfesseurs, 
  deleteProfesseur,
  toggleProfesseurStatus,
  searchProfesseurs,
  getAllSpecialites
} from "../../../services/professeurService";
import { useNavigate } from "react-router-dom";
import { Table, Button, Badge, Spinner, Alert, Form } from "react-bootstrap";
import { useAuthStore } from "../../../store/useAuthStore";

export default function ProfesseursList() {
  const [professeurs, setProfesseurs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [specialites, setSpecialites] = useState([]);
  const [selectedSpecialite, setSelectedSpecialite] = useState("");
  const navigate = useNavigate();
  const { token, isLoadingAuth } = useAuthStore();

  const fetchData = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // Charger les spécialités
      const specs = await getAllSpecialites();
      setSpecialites(specs);
      
      // Charger les professeurs
      const data = await getAllProfesseurs();
      setProfesseurs(data);
    } catch (err) {
      console.error(err);
      setError(err.message);
      if (err.message.includes("401")) {
        navigate("/login");
      }
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    try {
      setLoading(true);
      const results = await searchProfesseurs(searchTerm);
      setProfesseurs(results);
    } catch (err) {
      console.error(err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleFilterBySpecialite = async () => {
    try {
      setLoading(true);
      const results = selectedSpecialite 
        ? await getProfesseursBySpecialite(selectedSpecialite)
        : await getAllProfesseurs();
      setProfesseurs(results);
    } catch (err) {
      console.error(err);
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleToggleStatus = async (id) => {
    try {
      await toggleProfesseurStatus(id);
      fetchData();
    } catch (err) {
      console.error(err);
      setError(err.message);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Êtes-vous sûr de vouloir supprimer ce professeur ?")) return;
    try {
      await deleteProfesseur(id);
      fetchData();
    } catch (err) {
      console.error(err);
      setError(err.message);
    }
  };

  useEffect(() => {
    if (!isLoadingAuth && !token) {
      navigate("/login");
    } else if (!isLoadingAuth) {
      fetchData();
    }
  }, [token, isLoadingAuth]);

  if (isLoadingAuth) {
    return (
      <div className="d-flex justify-content-center mt-5">
        <Spinner animation="border" variant="primary" />
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Liste des professeurs</h2>
        <Button 
          variant="success" 
          onClick={() => navigate("/dashboard/professeurs/new")}
        >
          <i className="bi bi-plus-lg"></i> Ajouter un professeur
        </Button>
      </div>

      <div className="row mb-4 g-3">
        <div className="col-md-4">
          <Form.Control
            type="text"
            placeholder="Rechercher un professeur..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
        <div className="col-md-2">
          <Button variant="primary" onClick={handleSearch}>
            <i className="bi bi-search"></i> Rechercher
          </Button>
        </div>
      </div>

      {error && (
        <Alert variant="danger" onClose={() => setError(null)} dismissible>
          {error}
        </Alert>
      )}

      {loading ? (
        <div className="d-flex justify-content-center mt-5">
          <Spinner animation="border" variant="primary" />
        </div>
      ) : (
        <div className="table-responsive">
          <Table striped bordered hover>
            <thead className="table-dark">
              <tr>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Email</th>
                <th>Spécialité</th>
                <th>Status</th>
                <th className="text-center">Actions</th>
              </tr>
            </thead>
            <tbody>
              {professeurs.length > 0 ? (
                professeurs.map((prof) => (
                  <tr key={prof.id}>
                    <td>{prof.nom}</td>
                    <td>{prof.prenom}</td>
                    <td>{prof.email}</td>
                    <td>{prof.specialite}</td>
                    <td>
                      <Badge 
                        pill 
                        bg={prof.active ? "success" : "secondary"}
                        style={{ cursor: 'pointer' }}
                        onClick={() => handleToggleStatus(prof.id)}
                      >
                        {prof.active ? "Actif" : "Inactif"}
                      </Badge>
                    </td>
                    <td className="text-center">
                      <Button
                        variant="warning"
                        size="sm"
                        onClick={() => navigate(`/dashboard/professeurs/${prof.id}/edit`)}
                        className="me-2"
                      >
                        <i className="bi bi-pencil"></i> Modifier
                      </Button>
                      <Button
                        variant="info"
                        size="sm"
                        onClick={() => navigate(`/dashboard/professeurs/${prof.id}`)}
                        className="me-2"
                      >
                        <i className="bi bi-eye"></i> Détails
                      </Button>
                      <Button
                        variant="danger"
                        size="sm"
                        onClick={() => handleDelete(prof.id)}
                      >
                        <i className="bi bi-trash"></i> Supprimer
                      </Button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="6" className="text-center text-muted py-4">
                    Aucun professeur trouvé
                  </td>
                </tr>
              )}
            </tbody>
          </Table>
        </div>
      )}
    </div>
  );
}