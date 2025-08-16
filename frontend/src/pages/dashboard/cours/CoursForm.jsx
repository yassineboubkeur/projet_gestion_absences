import React, { useState, useEffect } from "react";
import { Form, Button } from "react-bootstrap";
import { createCours, getCoursById, updateCours } from "../../../services/coursService";
import { useNavigate, useParams } from "react-router-dom";

export default function CoursForm() {
  const { id } = useParams(); // undefined when creating new
  const navigate = useNavigate();

  const [form, setForm] = useState({
    code: "",
    intitule: "",
    description: "",
    coefficient: "",
    volumeHoraire: "",
    matiereId: ""
  });

  useEffect(() => {
    if (!id) return; // skip fetch for new
    getCoursById(id)
      .then(data => {
        if (!data) {
          alert("Cours non trouvé");
          navigate("/dashboard/cours");
          return;
        }
        setForm({
          code: data.code || "",
          intitule: data.intitule || "",
          description: data.description || "",
          coefficient: data.coefficient || "",
          volumeHoraire: data.volumeHoraire || "",
          matiereId: data.matiere?.id || ""
        });
      })
      .catch(() => {
        alert("Erreur lors du chargement du cours");
        navigate("/dashboard/cours");
      });
  }, [id, navigate]);

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) await updateCours(id, form);
      else await createCours(form);
      navigate("/dashboard/cours");
    } catch (err) {
      console.error(err);
      alert("Erreur lors de l'enregistrement du cours.");
    }
  };

  return (
    <div className="container mt-4">
      <h2>{id ? "Modifier Cours" : "Créer Cours"}</h2>
      <Form onSubmit={handleSubmit}>
        {["code", "intitule", "description", "coefficient", "volumeHoraire", "matiereId"].map(field => (
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
        <Button type="submit">{id ? "Enregistrer" : "Créer"}</Button>
      </Form>
    </div>
  );
}
