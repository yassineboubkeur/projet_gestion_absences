// src/services/matiereService.js

const API_URL = "http://localhost:8080/api/matieres";

// Get all subjects
export const getAllMatieres = async () => {
  const res = await fetch(API_URL);
  if (!res.ok) throw new Error("Erreur lors de la récupération des matières");
  return res.json();
};

// Get subject by ID
export const getMatiereById = async (id) => {
  const res = await fetch(`${API_URL}/${id}`);
  if (!res.ok) throw new Error("Matière introuvable");
  return res.json();
};

// Get subject by code
export const getMatiereByCode = async (code) => {
  const res = await fetch(`${API_URL}/code/${code}`);
  if (!res.ok) throw new Error("Matière introuvable avec ce code");
  return res.json();
};

// Get subjects by domaine
export const getMatieresByDomaine = async (domaine) => {
  const res = await fetch(`${API_URL}/domaine/${domaine}`);
  if (!res.ok) throw new Error("Erreur lors de la récupération des matières par domaine");
  return res.json();
};

// Search subjects by title (intitule)
export const searchMatieres = async (intitule) => {
  const res = await fetch(`${API_URL}/search?intitule=${encodeURIComponent(intitule)}`);
  if (!res.ok) throw new Error("Erreur lors de la recherche des matières");
  return res.json();
};

// Create new subject
export const createMatiere = async (matiere) => {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(matiere),
  });
  if (!res.ok) throw new Error("Erreur lors de la création de la matière");
  return res.json();
};

// Update subject
export const updateMatiere = async (id, matiere) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(matiere),
  });
  if (!res.ok) throw new Error("Erreur lors de la mise à jour de la matière");
  return res.json();
};

// Delete subject
export const deleteMatiere = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
  });
  if (!res.ok) throw new Error("Erreur lors de la suppression de la matière");
};
