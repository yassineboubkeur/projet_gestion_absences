// src/services/matiereService.js
import { useAuthStore } from '../store/useAuthStore';

const API_URL = "http://localhost:8080/api/matieres";

const getAuthHeaders = () => {
  const token = useAuthStore.getState().token;
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const getAllMatieres = async () => {
  const res = await fetch(API_URL, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la récupération des matières");
  return res.json();
};

export const getMatiereById = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Matière introuvable");
  return res.json();
};

export const getMatiereByCode = async (code) => {
  const res = await fetch(`${API_URL}/code/${code}`, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Matière introuvable avec ce code");
  return res.json();
};

export const getMatieresByDomaine = async (domaine) => {
  const res = await fetch(`${API_URL}/domaine/${domaine}`, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la récupération des matières par domaine");
  return res.json();
};

export const searchMatieres = async (intitule) => {
  const res = await fetch(`${API_URL}/search?intitule=${encodeURIComponent(intitule)}`, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la recherche des matières");
  return res.json();
};

export const createMatiere = async (matiere) => {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(matiere),
  });
  if (!res.ok) throw new Error("Erreur lors de la création de la matière");
  return res.json();
};

export const updateMatiere = async (id, matiere) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(matiere),
  });
  if (!res.ok) throw new Error("Erreur lors de la mise à jour de la matière");
  return res.json();
};

export const deleteMatiere = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la suppression de la matière");
};