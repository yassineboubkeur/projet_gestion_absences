// src/pages/dashboard/EtudiantForm.jsx
import React, { useEffect, useState } from "react";
import {
  createEtudiant,
  getEtudiantById,
  updateEtudiant,
} from "../../../services/etudiantService";
import { useNavigate, useParams } from "react-router-dom";

export default function EtudiantForm() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    nom: "",
    prenom: "",
    email: "",
    login: "",
    password: "",
    matricule: "",
    address: "",
    classeId: "",
  });

  useEffect(() => {
    if (id) {
      getEtudiantById(id).then((data) => {
        setForm({
          nom: data.nom || "",
          prenom: data.prenom || "",
          email: data.email || "",
          login: data.login || "",
          password: "",
          matricule: data.matricule || "",
          address: data.address || "",
          classeId: data.classe?.id || "",
        });
      });
    }
  }, [id]);

  const handleChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) {
        await updateEtudiant(id, form);
      } else {
        await createEtudiant(form);
      }
      navigate("/dashboard/etudiants");
    } catch (err) {
      console.error(err);
      alert(err.message);
    }
  };

  return (
    <div className="container mt-4">
      <h2 className="mb-3">{id ? "Modifier étudiant" : "Créer étudiant"}</h2>
      <form onSubmit={handleSubmit}>
        <input
          className="form-control mb-3"
          name="nom"
          placeholder="Nom"
          value={form.nom}
          onChange={handleChange}
          required
        />
        <input
          className="form-control mb-3"
          name="prenom"
          placeholder="Prénom"
          value={form.prenom}
          onChange={handleChange}
          required
        />
        <input
          className="form-control mb-3"
          name="email"
          placeholder="Email"
          value={form.email}
          onChange={handleChange}
          required
        />
        <input
          className="form-control mb-3"
          name="login"
          placeholder="Login"
          value={form.login}
          onChange={handleChange}
          required
        />
        {!id && (
          <input
            className="form-control mb-3"
            name="password"
            placeholder="Password"
            type="password"
            value={form.password}
            onChange={handleChange}
            required
          />
        )}
        <input
          className="form-control mb-3"
          name="matricule"
          placeholder="Matricule"
          value={form.matricule}
          onChange={handleChange}
          required
        />
        <input
          className="form-control mb-3"
          name="address"
          placeholder="Address"
          value={form.address}
          onChange={handleChange}
        />
        <input
          className="form-control mb-3"
          name="classeId"
          placeholder="Classe ID"
          value={form.classeId}
          onChange={handleChange}
          required
        />
        <button className="btn btn-primary" type="submit">
          Enregistrer
        </button>
      </form>
    </div>
  );
}
