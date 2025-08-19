// src/services/etudiantService.js
import { useAuthStore } from '../store/useAuthStore';

const API_URL = "http://localhost:8080/api/etudiants";

const getAuthHeaders = () => {
  const token = useAuthStore.getState().token;
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const getEtudiants = async () => {
  const res = await fetch(API_URL, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la récupération des étudiants");
  return res.json();
};

export const getEtudiantById = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la récupération de l'étudiant");
  return res.json();
};

export const createEtudiant = async (data) => {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Erreur lors de la création de l'étudiant");
  return res.json();
};

export const updateEtudiant = async (id, data) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(data),
  });
  if (!res.ok) throw new Error("Erreur lors de la mise à jour de l'étudiant");
  return res.json();
};

export const deleteEtudiant = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, { 
    method: "DELETE",
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la suppression de l'étudiant");
};

export const getEtudiantsByClasse = async (classeId) => {
  const res = await fetch(`${API_URL}/by-classe/${classeId}`, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la récupération des étudiants par classe");
  return res.json();
};

export const changeClasse = async (etudiantId, newClasseId) => {
  const res = await fetch(`${API_URL}/${etudiantId}/change-classe/${newClasseId}`, {
    method: "PATCH",
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors du changement de classe");
  return res.json();
};