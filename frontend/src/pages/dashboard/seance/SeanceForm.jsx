// src/pages/dashboard/SeanceForm.jsx
import React, { useEffect, useState } from "react";
import { createSeance, getSeanceById, updateSeance } from "../../../services/seanceService";
import { useNavigate, useParams } from "react-router-dom";
import { Form, Button } from "react-bootstrap";

export default function SeanceForm() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    coursId: "",
    professeurId: "",
    salleId: "",
    date: "",
    heureDebut: "",
    heureFin: "",
    statut: "prévu"
  });

  useEffect(() => {
    if (id) {
      getSeanceById(id)
        .then((data) => setForm({
          coursId: data.cours?.id || "",
          professeurId: data.professeur?.id || "",
          salleId: data.salle?.id || "",
          date: data.date || "",
          heureDebut: data.heureDebut || "",
          heureFin: data.heureFin || "",
          statut: data.statut || "prévu"
        }))
        .catch(err => {
          console.error(err);
          alert("Erreur lors de la récupération de la séance.");
        });
    }
  }, [id]);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) await updateSeance(id, form);
      else await createSeance(form);
      navigate("/dashboard/seances");
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  return (
    <div className="container mt-4">
      <h2>{id ? "Modifier Séance" : "Créer Séance"}</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-2">
          <Form.Control name="coursId" placeholder="ID du cours" value={form.coursId} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control name="professeurId" placeholder="ID du professeur" value={form.professeurId} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control name="salleId" placeholder="ID de la salle" value={form.salleId} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control type="date" name="date" value={form.date} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control type="time" name="heureDebut" value={form.heureDebut} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control type="time" name="heureFin" value={form.heureFin} onChange={handleChange} required />
        </Form.Group>
        <Button type="submit">{id ? "Enregistrer" : "Créer"}</Button>
      </Form>
    </div>
  );
}
