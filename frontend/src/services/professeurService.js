// src/services/professeurService.js
import { useAuthStore } from '../store/useAuthStore';

const API_URL = "http://localhost:8080/api/professeurs";

const getAuthHeaders = () => {
  const token = useAuthStore.getState().token;
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const getAllProfesseurs = async () => {
  const response = await fetch(API_URL, {
    headers: getAuthHeaders()
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Erreur lors de la récupération des professeurs");
  }
  return response.json();
};

export const getProfesseurById = async (id) => {
  const response = await fetch(`${API_URL}/${id}`, {
    headers: getAuthHeaders()
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Professeur non trouvé");
  }
  return response.json();
};

export const createProfesseur = async (professeur) => {
  const response = await fetch(API_URL, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(professeur),
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Erreur lors de la création du professeur");
  }
  return response.json();
};

export const updateProfesseur = async (id, professeur) => {
  const response = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(professeur),
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Erreur lors de la mise à jour du professeur");
  }
  return response.json();
};

export const deleteProfesseur = async (id) => {
  const response = await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
    headers: getAuthHeaders()
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Erreur lors de la suppression du professeur");
  }
};

export const toggleProfesseurStatus = async (id) => {
  const response = await fetch(`${API_URL}/${id}/toggle-status`, {
    method: "PATCH",
    headers: getAuthHeaders()
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Erreur lors du changement de statut");
  }
  return response.json();
};

export const searchProfesseurs = async (term) => {
  const response = await fetch(`${API_URL}/search?term=${encodeURIComponent(term)}`, {
    headers: getAuthHeaders()
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Erreur lors de la recherche");
  }
  return response.json();
};

export const getProfesseursBySpecialite = async (specialite) => {
  const response = await fetch(`${API_URL}/specialites/${encodeURIComponent(specialite)}`, {
    headers: getAuthHeaders()
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Erreur lors de la récupération par spécialité");
  }
  return response.json();
};

export const getAllSpecialites = async () => {
  const response = await fetch(`${API_URL}/specialites`, {
    headers: getAuthHeaders()
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Erreur lors de la récupération des spécialités");
  }
  return response.json();
};

export const getProfesseursGroupedBySpecialite = async () => {
  const response = await fetch(`${API_URL}/specialitesbyprof`, {
    headers: getAuthHeaders()
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Erreur lors du regroupement par spécialité");
  }
  return response.json();
};