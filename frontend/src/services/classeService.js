// src/services/classeService.js
import { useAuthStore } from '../store/useAuthStore';

const API_URL = "http://localhost:8080/api/classes";

const getAuthHeaders = () => {
  const token = useAuthStore.getState().token;
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const getAllClasses = async () => {
  const res = await fetch(API_URL, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la récupération des classes");
  return res.json();
};

export const getClasseById = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Classe non trouvée");
  return res.json();
};

export const getClassesByNiveau = async (niveau) => {
  const res = await fetch(`${API_URL}/niveau/${niveau}`, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la récupération par niveau");
  return res.json();
};

export const createClasse = async (classe) => {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(classe),
  });
  if (!res.ok) throw new Error("Erreur lors de la création de la classe");
  return res.json();
};

export const updateClasse = async (id, classe) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(classe),
  });
  if (!res.ok) throw new Error("Erreur lors de la mise à jour de la classe");
  return res.json();
};

export const deleteClasse = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la suppression de la classe");
};

export const getEtudiantsByClasse = async (id) => {
  const res = await fetch(`${API_URL}/${id}/etudiants`, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la récupération des étudiants");
  return res.json();
};