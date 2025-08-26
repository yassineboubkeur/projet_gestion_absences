// frontend/src/services/emploiDuTempsService.js
import { useAuthStore } from '../store/useAuthStore';

const API_URL = "http://localhost:8080/api/emploi-du-temps";

const getAuthHeaders = () => {
  const token = useAuthStore.getState().token;
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const getAllEmploiDuTemps = async () => {
  const res = await fetch(API_URL, {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors du chargement des emplois du temps");
  return res.json();
};

export const getEmploiDuTempsById = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, {
    headers: getAuthHeaders()
  });
  if (!res.ok) return null;
  return res.json();
};

export const createEmploiDuTemps = async (emploi) => {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(emploi)
  });
  if (!res.ok) throw new Error("Erreur lors de la création de l'emploi du temps");
  return res.json();
};

export const updateEmploiDuTemps = async (id, emploi) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(emploi)
  });
  if (!res.ok) throw new Error("Erreur lors de la mise à jour de l'emploi du temps");
  return res.json();
};

export const deleteEmploiDuTemps = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la suppression de l'emploi du temps");
  return true;
};

export const getClasses = async () => {
  const res = await fetch("http://localhost:8080/api/classes", {
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors du chargement des classes");
  return res.json();
};

export const generateWeekly16 = async (classeId, weekStart) => {
  const res = await fetch(`${API_URL}/generate-weekly-16?classeId=${classeId}&weekStart=${weekStart}`, {
    method: "POST",
    headers: getAuthHeaders()
  });
  if (!res.ok) throw new Error("Erreur lors de la génération automatique");
  return res.json();
};