// src/services/coursService.js
import { useAuthStore } from '../store/useAuthStore';

const API_URL = "http://localhost:8080/api/cours";

const getAuthHeaders = () => {
  const token = useAuthStore.getState().token;
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const getAllCours = async () => {
  const res = await fetch(API_URL, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors du chargement des cours");
  return res.json();
};

export const getCoursById = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Cours non trouvé");
  return res.json();
};

export const createCours = async (cours) => {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(cours),
  });
  if (!res.ok) throw new Error("Erreur lors de la création du cours");
  return res.json();
};

export const updateCoursById = async (id, cours) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(cours),
  });
  if (!res.ok) throw new Error("Erreur lors de la mise à jour du cours");
  return res.json();
};

export const deleteCours = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, { 
    method: "DELETE",
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la suppression du cours");
  return res.json();
};

export const searchCours = async (intitule) => {
  const res = await fetch(`${API_URL}/search?intitule=${encodeURIComponent(intitule)}`, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la recherche");
  return res.json();
};