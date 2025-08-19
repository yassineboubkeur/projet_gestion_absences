// src/services/salleService.js
import { useAuthStore } from '../store/useAuthStore';

const API_URL = "http://localhost:8080/api/salles";

const getAuthHeaders = () => {
  const token = useAuthStore.getState().token;
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const getAllSalles = async () => {
  const res = await fetch(API_URL, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors du chargement des salles");
  return res.json();
};

export const getSalleById = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, {
    headers: getAuthHeaders()
  });
  if (!res.ok) return null;
  return res.json();
};

export const createSalle = async (salle) => {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(salle)
  });
  if (!res.ok) throw new Error("Erreur lors de la création de la salle");
  return res.json();
};

export const updateSalle = async (id, salle) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(salle)
  });
  if (!res.ok) throw new Error("Erreur lors de la mise à jour de la salle");
  return res.json();
};

export const deleteSalle = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la suppression de la salle");
  return true;
};

// Optionnel: Recherche de salles
export const searchSalles = async (criteria) => {
  const res = await fetch(`${API_URL}/search?q=${encodeURIComponent(criteria)}`, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la recherche des salles");
  return res.json();
};