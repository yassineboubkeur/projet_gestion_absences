// src/pages/dashboard/ProfesseurForm.jsx
import React, { useEffect, useState } from "react";
import { createProfesseur, getProfesseurById, updateProfesseur } from "../../../services/professeurService";
import { useNavigate, useParams } from "react-router-dom";
import { Form, Button } from "react-bootstrap";

export default function ProfesseurForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [form, setForm] = useState({
    nom: "",
    prenom: "",
    email: "",
    login: "",
    password: "",
    matricule: "",
    adresse: "",
    specialite: "",
    dateNaissance: ""
  });

  useEffect(() => {
    if (id) {
      getProfesseurById(id).then((data) => {
        // Bach kol field yb9a string w ma yb9ach undefined
        const safeData = {
          nom: data.nom || "",
          prenom: data.prenom || "",
          email: data.email || "",
          login: data.login || "",
          password: "",
          matricule: data.matricule || "",
          adresse: data.adresse || "",
          specialite: data.specialite || "",
          dateNaissance: data.dateNaissance || ""
        };
        setForm(safeData);
      });
    }
  }, [id]);

  const handleChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) {
        await updateProfesseur(id, form);
      } else {
        await createProfesseur(form);
      }
      navigate("/dashboard/professeurs");
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  return (
    <div className="container mt-4">
      <h2>{id ? "Modifier professeur" : "Créer professeur"}</h2>
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-2">
          <Form.Control name="nom" placeholder="Nom" value={form.nom} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control name="prenom" placeholder="Prénom" value={form.prenom} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control name="email" placeholder="Email" value={form.email} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control name="login" placeholder="Login" value={form.login} onChange={handleChange} required />
        </Form.Group>
        {!id && (
          <Form.Group className="mb-2">
            <Form.Control name="password" placeholder="Password" type="password" value={form.password} onChange={handleChange} required />
          </Form.Group>
        )}
        <Form.Group className="mb-2">
          <Form.Control name="matricule" placeholder="Matricule" value={form.matricule} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control name="adresse" placeholder="Adresse" value={form.adresse} onChange={handleChange} />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control name="specialite" placeholder="Spécialité" value={form.specialite} onChange={handleChange} required />
        </Form.Group>
        <Form.Group className="mb-2">
          <Form.Control type="date" name="dateNaissance" placeholder="Date de naissance" value={form.dateNaissance} onChange={handleChange} />
        </Form.Group>
        <Button type="submit">{id ? "Enregistrer" : "Créer"}</Button>
      </Form>
    </div>
  );
}
