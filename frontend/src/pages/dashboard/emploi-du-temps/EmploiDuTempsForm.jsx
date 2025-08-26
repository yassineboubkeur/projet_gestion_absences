// frontend/src/pages/dashboard/emploi-du-temps/EmploiDuTempsForm.jsx
import React, { useState, useEffect } from "react";
import { Form, Button, Row, Col, Alert } from "react-bootstrap";
import {
  createEmploiDuTemps,
  getEmploiDuTempsById,
  updateEmploiDuTemps,
  getClasses,
} from "../../../services/emploiDuTempsService";
import { useNavigate, useParams } from "react-router-dom";

export default function EmploiDuTempsForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEdit = id && id !== "new";

  const [form, setForm] = useState({
    intitule: "",
    classeId: "",
    dateDebut: "",
    dateFin: "",
  });

  const [classes, setClasses] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    // Charger les classes
    getClasses()
      .then((data) => setClasses(data || []))
      .catch((err) =>
        console.error("Erreur lors du chargement des classes:", err)
      );

    if (!isEdit) return;

    getEmploiDuTempsById(id)
      .then((data) => {
        if (!data) {
          alert("Emploi du temps non trouvé");
          navigate("/dashboard/emploi-du-temps");
          return;
        }
        setForm({
          intitule: data.intitule,
          classeId: data.classe?.id || "",
          dateDebut: data.dateDebut,
          dateFin: data.dateFin,
        });
      })
      .catch(() => {
        alert("Erreur lors du chargement de l'emploi du temps");
        navigate("/dashboard/emploi-du-temps");
      });
  }, [id, isEdit, navigate]);

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (new Date(form.dateDebut) > new Date(form.dateFin)) {
      setError("La date de début doit être avant la date de fin");
      return;
    }

    try {
      const payload = {
        intitule: form.intitule,
        dateDebut: form.dateDebut,
        dateFin: form.dateFin,
        classe: { id: form.classeId }, // ✅ on envoie bien un objet classe
      };

      console.log("Payload envoyé:", payload);

      if (isEdit) {
        const result = await updateEmploiDuTemps(id, payload);
        console.log("Réponse serveur:", result);
      } else {
        const result = await createEmploiDuTemps(payload);
        console.log("Réponse serveur:", result);
      }

      navigate("/dashboard/emploi-du-temps");
    } catch (err) {
      console.error("Erreur complète:", err);
      setError(`Erreur lors de l'enregistrement: ${err.message}`);
    }
  };

  return (
    <div className="container mt-4">
      <h2>
        {isEdit ? "Modifier l'emploi du temps" : "Créer un emploi du temps"}
      </h2>

      {error && <Alert variant="danger">{error}</Alert>}

      <Form onSubmit={handleSubmit}>
        <Row>
          <Col md={6}>
            <Form.Group className="mb-3">
              <Form.Label>Intitulé</Form.Label>
              <Form.Control
                name="intitule"
                value={form.intitule}
                onChange={handleChange}
                required
                placeholder="Ex: EDT Semaine 45"
              />
            </Form.Group>
          </Col>

          <Col md={6}>
            <Form.Group className="mb-3">
              <Form.Label>Classe</Form.Label>
              <Form.Select
                name="classeId"
                value={form.classeId}
                onChange={handleChange}
                required
              >
                <option value="">Sélectionner une classe</option>
                {classes.map((classe) => (
                  <option key={classe.id} value={classe.id}>
                    {classe.nom} - {classe.niveau}
                  </option>
                ))}
              </Form.Select>
            </Form.Group>
          </Col>
        </Row>

        <Row>
          <Col md={6}>
            <Form.Group className="mb-3">
              <Form.Label>Date de début</Form.Label>
              <Form.Control
                type="date"
                name="dateDebut"
                value={form.dateDebut}
                onChange={handleChange}
                required
              />
            </Form.Group>
          </Col>

          <Col md={6}>
            <Form.Group className="mb-3">
              <Form.Label>Date de fin</Form.Label>
              <Form.Control
                type="date"
                name="dateFin"
                value={form.dateFin}
                onChange={handleChange}
                required
              />
            </Form.Group>
          </Col>
        </Row>

        <Button type="submit" variant="primary" className="me-2">
          {isEdit ? "Enregistrer" : "Créer"}
        </Button>
        <Button
          variant="secondary"
          onClick={() => navigate("/dashboard/emploi-du-temps")}
        >
          Annuler
        </Button>
      </Form>
    </div>
  );
}
