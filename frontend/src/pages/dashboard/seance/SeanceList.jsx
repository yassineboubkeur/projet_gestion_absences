import React, { useEffect, useState } from "react";
import { Table, Button, Spinner, Alert } from "react-bootstrap";
import {
  getAllSeances,
  deleteSeance,
  getSeancesByDate,
} from "../../../services/seanceService";
import { getAllCours } from "../../../services/coursService";
import { getAllProfesseurs } from "../../../services/professeurService";
import { getAllSalles } from "../../../services/salleService";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../../../store/useAuthStore";
import { format } from "date-fns";
import { fr } from "date-fns/locale";

export default function SeanceList() {
  const [seances, setSeances] = useState([]);
  const [cours, setCours] = useState([]);
  const [professeurs, setProfesseurs] = useState([]);
  const [salles, setSalles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedDate, setSelectedDate] = useState("");
  const navigate = useNavigate();
  const { token, isLoadingAuth } = useAuthStore();

  const fetchData = async (date = null) => {
    try {
      setLoading(true);
      setError(null);

      // parallel requests
      const [seancesData, coursData, profsData, sallesData] = await Promise.all([
        date ? getSeancesByDate(date) : getAllSeances(),
        getAllCours(),
        getAllProfesseurs(),
        getAllSalles(),
      ]);

      setCours(coursData);
      setProfesseurs(profsData);
      setSalles(sallesData);

      // enrichir seances
      const formattedData = seancesData.map((seance) => {
        const coursObj = coursData.find((c) => c.id === seance.coursId);
        const profObj = profsData.find((p) => p.id === seance.professeurId);
        const salleObj = sallesData.find((s) => s.id === seance.salleId);

        console.log("Seances:", seancesData);
        console.log("Cours:", coursData);
        console.log("Profs:", profsData);
        console.log("Salles:", sallesData);
        console.log("Seance sample:", seancesData[0]);



        return {
          ...seance,
          coursDisplay: coursObj
            ? `${coursObj.intitule} (${coursObj.matiere?.nom || "N/A"})`
            : "N/A",
          professeurDisplay: profObj
            ? `${profObj.nom} ${profObj.prenom} (${profObj.matricule})`
            : "N/A",
          salleDisplay: salleObj
            ? `${salleObj.batiment} - ${salleObj.numero}`
            : "N/A",
        };
      });

      setSeances(formattedData);
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

  useEffect(() => {
    if (!isLoadingAuth && !token) {
      navigate("/login");
    } else if (!isLoadingAuth) {
      fetchData();
    }
  }, [token, isLoadingAuth]);

  const handleDelete = async (id) => {
    if (!window.confirm("Êtes-vous sûr de vouloir supprimer cette séance ?"))
      return;
    try {
      await deleteSeance(id);
      fetchData(selectedDate || null);
    } catch (err) {
      console.error(err);
      setError(err.message);
    }
  };

  const handleDateChange = (e) => {
    const date = e.target.value;
    setSelectedDate(date);
    fetchData(date || null);
  };

  const formatDateDisplay = (dateStr) => {
    if (!dateStr) return "";
    const date = new Date(dateStr);
    return format(date, "PPPP", { locale: fr });
  };

  if (isLoadingAuth) {
    return (
      <div className="d-flex justify-content-center mt-5">
        <Spinner animation="border" variant="primary" />
      </div>
    );
  }

  return (
    <div className="container mt-4">
      {/* header */}
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Liste des Séances</h2>
        <Button
          variant="success"
          onClick={() => navigate("/dashboard/seances/new")}
        >
          <i className="bi bi-plus-lg"></i> Ajouter
        </Button>
      </div>

      {/* filtre date */}
      <div className="mb-4">
        <div className="row g-3">
          <div className="col-md-4">
            <label htmlFor="dateFilter" className="form-label">
              Filtrer par date :
            </label>
            <input
              type="date"
              id="dateFilter"
              className="form-control"
              value={selectedDate}
              onChange={handleDateChange}
            />
          </div>
          <div className="col-md-4 d-flex align-items-end">
            <Button
              variant="secondary"
              onClick={() => {
                setSelectedDate("");
                fetchData();
              }}
              disabled={!selectedDate}
            >
              <i className="bi bi-x-lg"></i> Effacer le filtre
            </Button>
          </div>
        </div>
      </div>

      {/* erreurs */}
      {error && (
        <Alert variant="danger" onClose={() => setError(null)} dismissible>
          {error}
        </Alert>
      )}

      {/* table */}
      {loading ? (
        <div className="d-flex justify-content-center mt-5">
          <Spinner animation="border" variant="primary" />
        </div>
      ) : (
        <div className="table-responsive">
          <Table striped bordered hover className="mt-3">
            <thead className="table-dark">
              <tr>
                <th>Cours</th>
                <th>Professeur</th>
                <th>Salle</th>
                <th>Date</th>
                <th>Heure Début</th>
                <th>Heure Fin</th>
                <th>Statut</th>
                <th className="text-center">Actions</th>
              </tr>
            </thead>
            <tbody>
              {seances.length > 0 ? (
                seances.map((s) => (
                  <tr key={s.id}>
                    <td>{s.coursDisplay}</td>
                    <td>{s.professeurDisplay}</td>
                    <td>{s.salleDisplay}</td>
                    <td>{formatDateDisplay(s.date)}</td>
                    <td>{s.heureDebut}</td>
                    <td>{s.heureFin}</td>
                    <td>
                      {s.statut === "PLANIFIEE" && (
                        <span className="badge bg-primary">Planifiée</span>
                      )}
                      {s.statut === "EFFECTUEE" && (
                        <span className="badge bg-success">Effectuée</span>
                      )}
                      {s.statut === "ANNULEE" && (
                        <span className="badge bg-danger">Annulée</span>
                      )}
                      {s.statut === "REPORTEE" && (
                        <span className="badge bg-warning">Reportée</span>
                      )}
                    </td>
                    <td className="text-center">
                      <Button
                        variant="warning"
                        size="sm"
                        onClick={() =>
                          navigate(`/dashboard/seances/${s.id}/edit`)
                        }
                        className="me-2"
                      >
                        <i className="bi bi-pencil"></i> Modifier
                      </Button>
                      <Button
                        size="sm"
                        variant="info"
                        onClick={() => navigate(`/dashboard/seances/${s.id}`)}
                        className="me-2"
                      >
                        <i className="bi bi-eye"></i> Détails
                      </Button>
                      <Button
                        variant="danger"
                        size="sm"
                        onClick={() => handleDelete(s.id)}
                      >
                        <i className="bi bi-trash"></i> Supprimer
                      </Button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="8" className="text-center text-muted py-4">
                    {selectedDate
                      ? `Aucune séance prévue pour le ${formatDateDisplay(
                          selectedDate
                        )}`
                      : "Aucune séance disponible"}
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
