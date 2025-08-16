import React, { useState, useEffect } from "react";
import { Form, Button } from "react-bootstrap";
import { createSalle, getSalleById, updateSalle } from "../../../services/salleService";
import { useNavigate, useParams } from "react-router-dom";

export default function SalleForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEdit = id && id !== "new";

  const [form, setForm] = useState({
    code: "",
    batiment: "",
    numero: "",
    capacite: "",
    type: ""
  });

  useEffect(() => {
    if (!isEdit) return;
    getSalleById(id)
      .then(data => {
        if (!data) {
          alert("Salle non trouvée");
          navigate("/dashboard/salles");
          return;
        }
        setForm(data);
      })
      .catch(() => {
        alert("Erreur lors du chargement de la salle");
        navigate("/dashboard/salles");
      });
  }, [id, isEdit, navigate]);

  const handleChange = e => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      if (isEdit) await updateSalle(id, form);
      else await createSalle(form);
      navigate("/dashboard/salles");
    } catch (err) {
      console.error(err);
      alert("Erreur lors de l'enregistrement de la salle.");
    }
  };

  return (
    <div className="container mt-4">
      <h2>{isEdit ? "Modifier Salle" : "Créer Salle"}</h2>
      <Form onSubmit={handleSubmit}>
        {["code", "batiment", "numero", "capacite", "type"].map(field => (
          <Form.Group className="mb-2" key={field}>
            <Form.Control
              name={field}
              placeholder={field}
              value={form[field]}
              onChange={handleChange}
              required
            />
          </Form.Group>
        ))}
        <Button type="submit">{isEdit ? "Enregistrer" : "Créer"}</Button>
      </Form>
    </div>
  );
}
