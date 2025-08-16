import React, { useEffect, useState } from "react";
import { createClasse, getClasseById, updateClasse } from "../../../services/classeService";
import { useNavigate, useParams } from "react-router-dom";
import { Form, Button } from "react-bootstrap";

export default function ClasseForm() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({ nom: "", niveau: "" });

  useEffect(() => {
    if (id) {
      getClasseById(id)
        .then((data) => setForm({ nom: data.nom || "", niveau: data.niveau || "" }))
        .catch(err => {
          console.error(err);
          alert("Erreur lors de la récupération de la classe.");
        });
    }
  }, [id]);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) await updateClasse(id, form);
      else await createClasse(form);
      navigate("/dashboard/classes");
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  return (
    <div className="container mt-4">
      <h2>{id ? "Modifier classe" : "Créer classe"}</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-2">
          <Form.Control name="nom" placeholder="Nom" value={form.nom} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control name="niveau" placeholder="Niveau" value={form.niveau} onChange={handleChange} required />
        </Form.Group>
        <Button type="submit">{id ? "Enregistrer" : "Créer"}</Button>
      </Form>
    </div>
  );
}
