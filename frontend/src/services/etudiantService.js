// src/services/etudiantService.js

const API_URL = "http://localhost:8080/api/etudiants";

export const getEtudiants = async () => {
  const res = await fetch(API_URL);
  if (!res.ok) throw new Error("Erreur lors de la récupération des étudiants");
  return res.json();
};

export const getEtudiantById = async (id) => {
  const res = await fetch(`${API_URL}/${id}`);
  if (!res.ok) throw new Error("Erreur lors de la récupération de l'étudiant");
  return res.json();
};

export const createEtudiant = async (data) => {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Erreur lors de la création de l'étudiant");
  return res.json();
};

export const updateEtudiant = async (id, data) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Erreur lors de la mise à jour de l'étudiant");
  return res.json();
};

export const deleteEtudiant = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error("Erreur lors de la suppression de l'étudiant");
};

export const getEtudiantsByClasse = async (classeId) => {
  const res = await fetch(`${API_URL}/by-classe/${classeId}`);
  if (!res.ok) throw new Error("Erreur lors de la récupération des étudiants par classe");
  return res.json();
};

export const changeClasse = async (etudiantId, newClasseId) => {
  const res = await fetch(`${API_URL}/${etudiantId}/change-classe/${newClasseId}`, {
    method: "PATCH",
  });
  if (!res.ok) throw new Error("Erreur lors du changement de classe");
  return res.json();
};
