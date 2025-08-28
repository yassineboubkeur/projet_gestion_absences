// frontend/src/pages/dashboard/emploi-du-temps/EmploiDuTempsList.jsx
import React, { useEffect, useState } from "react";
import { Table, Button } from "react-bootstrap";
import { getAllEmploiDuTemps, deleteEmploiDuTemps } from "../../../services/emploiDuTempsService";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../../../store/useAuthStore";

export default function EmploiDuTempsList() {
  const [emplois, setEmplois] = useState([]);
  const navigate = useNavigate();
  const { token, isLoadingAuth } = useAuthStore();

  const fetchData = async () => {
    try {
      const data = await getAllEmploiDuTemps();
      console.log("Emplois du temps fetched:", data);

      const emploisWithKey = (data || []).map((e, index) => ({
        ...e,
        key: e.id ?? index,
      }));

      setEmplois(emploisWithKey);
    } catch (err) {
      console.error(err);
      if (err.message.includes("401")) {
        alert("Session expirée. Veuillez vous reconnecter.");
        navigate("/login");
      } else {
        alert("Erreur lors du chargement des emplois du temps.");
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
    if (window.confirm("Supprimer cet emploi du temps ?")) {
      try {
        await deleteEmploiDuTemps(id);
        fetchData();
      } catch (err) {
        console.error(err);
        alert("Erreur lors de la suppression de l'emploi du temps.");
      }
    }
  };

  if (isLoadingAuth) {
    return <div>Chargement...</div>;
  }

  return (
    <div className="container mt-4">
      <h2>Liste des emplois du temps</h2>
      <Button
        className="mb-3"
        onClick={() => navigate("/dashboard/emploi-du-temps/new")}
      >
        + Ajouter
      </Button>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Intitulé</th>
            <th>Classe</th>
            <th>Date début</th>
            <th>Date fin</th>
            <th>Nb Séances</th>
            <th>Actif</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {emplois.length > 0 ? (
            emplois.map((e) => (
              <tr key={e.key}>
                <td>{e.intitule}</td>
                <td>{e.classe?.nom || "-"}</td>
                <td>{new Date(e.dateDebut).toLocaleDateString()}</td>
                <td>{new Date(e.dateFin).toLocaleDateString()}</td>
                <td>{e.seances?.length || 0}</td>
                <td>{e.actif ? "✅" : "❌"}</td>
                <td>
                  <Button
                    size="sm"
                    variant="info"
                    onClick={() => navigate(`/dashboard/emploi-du-temps/${e.id}`)}
                  >
                    Détails
                  </Button>{" "}
                  <Button
                    size="sm"
                    variant="warning"
                    onClick={() =>
                      e.id
                        ? navigate(`/dashboard/emploi-du-temps/${e.id}/edit`)
                        : alert("ID manquant pour modifier cet emploi du temps")
                    }
                  >
                    Modifier
                  </Button>{" "}
                  <Button
                    size="sm"
                    variant="danger"
                    onClick={() => handleDelete(e.id)}
                  >
                    Supprimer
                  </Button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="7" className="text-center">
                Aucun emploi du temps disponible
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </div>
  );
}
