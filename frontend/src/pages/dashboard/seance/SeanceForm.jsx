import React, { useEffect, useState } from "react";
import { 
  createSeance, 
  getSeanceById, 
  updateSeance,
  checkScheduleConflict 
} from "../../../services/seanceService";
import { 
  getAllProfesseurs,
  getProfesseurById 
} from "../../../services/professeurService";
import { 
  getAllSalles,
  getSalleById 
} from "../../../services/salleService";
import { 
  getAllCours,
  getCoursById 
} from "../../../services/coursService";
import { useNavigate, useParams } from "react-router-dom";
import { Form, Button, Alert, Spinner } from "react-bootstrap";
import { useAuthStore } from "../../../store/useAuthStore";

export default function SeanceForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { token, isLoadingAuth } = useAuthStore();

  const [form, setForm] = useState({
    coursId: "",
    professeurId: "",
    salleId: "",
    date: "",
    heureDebut: "",
    heureFin: "",
    statut: "PLANIFIEE"
  });

  const [professeurs, setProfesseurs] = useState([]);
  const [salles, setSalles] = useState([]);
  const [cours, setCours] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [conflictError, setConflictError] = useState(null);
  const [selectedCours, setSelectedCours] = useState("");
  const [selectedProfesseur, setSelectedProfesseur] = useState("");
  const [selectedSalle, setSelectedSalle] = useState("");

  // Charger les données initiales
  useEffect(() => {
    if (!token && !isLoadingAuth) {
      navigate("/login");
      return;
    }

    const fetchInitialData = async () => {
      try {
        setLoading(true);
        
        // Charger les listes
        const [profs, sallesData, coursData] = await Promise.all([
          getAllProfesseurs(),
          getAllSalles(),
          getAllCours()
        ]);
        
        setProfesseurs(profs);
        setSalles(sallesData);
        setCours(coursData);

        // Si c'est une modification, charger la séance existante
        if (id) {
          const seance = await getSeanceById(id);
          setForm({
            coursId: seance.cours?.id || "",
            professeurId: seance.professeur?.id || "",
            salleId: seance.salle?.id || "",
            date: seance.date || "",
            heureDebut: seance.heureDebut || "",
            heureFin: seance.heureFin || "",
            statut: seance.statut || "PLANIFIEE"
          });

          // Pré-sélectionner les valeurs dans les selects
          setSelectedCours(seance.cours?.id || "");
          setSelectedProfesseur(seance.professeur?.id || "");
          setSelectedSalle(seance.salle?.id || "");
        }
      } catch (err) {
        console.error(err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchInitialData();
  }, [id, token, isLoadingAuth, navigate]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
    setConflictError(null);

    // Mettre à jour les états de sélection
    if (name === "coursId") setSelectedCours(value);
    if (name === "professeurId") setSelectedProfesseur(value);
    if (name === "salleId") setSelectedSalle(value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setConflictError(null);

    try {
      // Vérifier d'abord les conflits d'emploi du temps
      const hasConflict = await checkScheduleConflict(form);
      if (hasConflict) {
        setConflictError("Conflit d'horaire détecté ! Vérifiez les disponibilités.");
        return;
      }

      // Si pas de conflit, sauvegarder
      if (id) {
        await updateSeance(id, form);
      } else {
        await createSeance(form);
      }
      navigate("/dashboard/seances");
    } catch (err) {
      console.error(err);
      setError(err.message);
    }
  };

  if (isLoadingAuth || loading) {
    return (
      <div className="d-flex justify-content-center mt-5">
        <Spinner animation="border" variant="primary" />
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <h2 className="mb-4">{id ? "Modifier Séance" : "Créer Séance"}</h2>
      
      {error && <Alert variant="danger">{error}</Alert>}
      {conflictError && <Alert variant="warning">{conflictError}</Alert>}

      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>Cours</Form.Label>
          <Form.Select 
            name="coursId" 
            value={selectedCours}
            onChange={handleChange} 
            required
          >
            <option value="">Sélectionner un cours</option>
            {cours.map(c => (
              <option key={c.id} value={c.id}>
                {c.intitule} - {c.matiere?.nom || 'N/A'} (Classe: {c.classe?.nom || 'N/A'})
              </option>
            ))}
          </Form.Select>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Professeur</Form.Label>
          <Form.Select 
            name="professeurId" 
            value={selectedProfesseur}
            onChange={handleChange} 
            required
          >
            <option value="">Sélectionner un professeur</option>
            {professeurs.map(p => (
              <option key={p.id} value={p.id}>
                {p.matricule} - {p.nom} {p.prenom} ({p.specialite})
              </option>
            ))}
          </Form.Select>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Salle</Form.Label>
          <Form.Select 
            name="salleId" 
            value={selectedSalle}
            onChange={handleChange} 
            required
          >
            <option value="">Sélectionner une salle</option>
            {salles.map(s => (
              <option key={s.id} value={s.id}>
                {s.batiment} - {s.numero} (Capacité: {s.capacite}, Type: {s.type})
              </option>
            ))}
          </Form.Select>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Date</Form.Label>
          <Form.Control 
            type="date" 
            name="date" 
            value={form.date} 
            onChange={handleChange} 
            required 
          />
        </Form.Group>

        <div className="row">
          <Form.Group className="mb-3 col-md-6">
            <Form.Label>Heure de début</Form.Label>
            <Form.Control 
              type="time" 
              name="heureDebut" 
              value={form.heureDebut} 
              onChange={handleChange} 
              required 
            />
          </Form.Group>

          <Form.Group className="mb-3 col-md-6">
            <Form.Label>Heure de fin</Form.Label>
            <Form.Control 
              type="time" 
              name="heureFin" 
              value={form.heureFin} 
              onChange={handleChange} 
              required 
            />
          </Form.Group>
        </div>

        <Form.Group className="mb-3">
          <Form.Label>Statut</Form.Label>
          <Form.Select 
            name="statut" 
            value={form.statut} 
            onChange={handleChange} 
            required
          >
            <option value="PLANIFIEE">Planifiée</option>
            <option value="EFFECTUEE">Effectuée</option>
            <option value="ANNULEE">Annulée</option>
            <option value="REPORTEE">Reportée</option>
          </Form.Select>
        </Form.Group>

        <div className="d-flex justify-content-end gap-2">
          <Button 
            variant="secondary" 
            onClick={() => navigate("/dashboard/seances")}
          >
            Annuler
          </Button>
          <Button variant="primary" type="submit">
            {id ? "Enregistrer" : "Créer"}
          </Button>
        </div>
      </Form>
    </div>
  );
}