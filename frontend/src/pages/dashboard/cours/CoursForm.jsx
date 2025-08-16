import React, { useState, useEffect } from "react";
import { Form, Button } from "react-bootstrap";
import { createCours, getCoursById, updateCours } from "../../../services/coursService";
import { getAllMatieres } from "../../../services/matiereService";
import { useNavigate, useParams } from "react-router-dom";

export default function CoursForm() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    code: "",
    intitule: "",
    description: "",
    coefficient: "",
    volumeHoraire: "",
    matiereId: ""
  });

  const [matieres, setMatieres] = useState([]);

  useEffect(() => {
    getAllMatieres()
      .then(data => setMatieres(data || []))
      .catch(err => console.error(err));
  }, []);

  useEffect(() => {
    if (!id) return; // création = pas d'id
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

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

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
        <Form.Group className="mb-2">
          <Form.Control name="code" placeholder="Code" value={form.code} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control name="intitule" placeholder="Intitulé" value={form.intitule} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control name="description" placeholder="Description" value={form.description} onChange={handleChange} />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control name="coefficient" type="number" placeholder="Coefficient" value={form.coefficient} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control name="volumeHoraire" type="number" placeholder="Volume Horaire" value={form.volumeHoraire} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Select name="matiereId" value={form.matiereId} onChange={handleChange} required>
            <option value="">-- Choisir Matière --</option>
            {matieres.map(m => (
              <option key={m.id} value={m.id}>{m.intitule}</option>
            ))}
          </Form.Select>
        </Form.Group>
        <Button type="submit">{id ? "Enregistrer" : "Créer"}</Button>
      </Form>
    </div>
  );
}
