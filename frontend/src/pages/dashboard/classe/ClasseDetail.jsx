import React, { useEffect, useState } from "react";
import { getClasseById, getEtudiantsByClasse } from "../../../services/classeService";
import { useParams, useNavigate } from "react-router-dom";
import { Table, Button } from "react-bootstrap";

export default function ClasseDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [classe, setClasse] = useState(null);
  const [etudiants, setEtudiants] = useState([]);

  useEffect(() => {
    getClasseById(id).then(setClasse).catch(console.error);
    getEtudiantsByClasse(id).then(setEtudiants).catch(console.error);
  }, [id]);

  if (!classe) return <div>Chargement...</div>;

  return (
    <div className="container mt-4">
      <h2>Détails de la classe: {classe.nom}</h2>
      <p>Niveau: {classe.niveau}</p>
      <p>Effectif: {classe.effectif}</p>

      <h3>Étudiants</h3>
      <Button className="mb-3" onClick={() => navigate("/dashboard/classes")}>Retour</Button>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Email</th>
          </tr>
        </thead>
        <tbody>
          {etudiants.map((e) => (
            <tr key={e.id}>
              <td>{e.nom}</td>
              <td>{e.prenom}</td>
              <td>{e.email}</td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
}
