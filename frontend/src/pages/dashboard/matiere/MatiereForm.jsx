import React, { useEffect, useState } from "react";
import { createMatiere, getMatiereById, updateMatiere } from "../../../services/matiereService";
import { useNavigate, useParams } from "react-router-dom";
import { Form, Button } from "react-bootstrap";

export default function MatiereForm() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({ code: "", intitule: "", domaine: "" });

  useEffect(() => {
    if (id) {
      getMatiereById(id)
        .then((data) => setForm({
          code: data.code || "",
          intitule: data.intitule || "",
          domaine: data.domaine || ""
        }))
        .catch(err => {
          console.error(err);
          alert("Erreur lors de la récupération de la matière.");
        });
    }
  }, [id]);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) await updateMatiere(id, form);
      else await createMatiere(form);
      navigate("/dashboard/matieres");
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  return (
    <div className="container mt-4">
      <h2>{id ? "Modifier matière" : "Créer matière"}</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-2">
          <Form.Control name="code" placeholder="Code" value={form.code} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control name="intitule" placeholder="Intitulé" value={form.intitule} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control name="domaine" placeholder="Domaine" value={form.domaine} onChange={handleChange} required />
        </Form.Group>
        <Button type="submit">{id ? "Enregistrer" : "Créer"}</Button>
      </Form>
    </div>
  );
}
