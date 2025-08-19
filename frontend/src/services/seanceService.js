// src/services/seanceService.js
import { useAuthStore } from '../store/useAuthStore';

const API_URL = "http://localhost:8080/api/seances";

const getAuthHeaders = () => {
  const token = useAuthStore.getState().token;
  return {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  };
};

export const createSeance = async (seanceData) => {
  const response = await fetch(`${API_URL}`, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(seanceData),
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Failed to create seance");
  }
  return response.json();
};

export const getAllSeances = async () => {
  const response = await fetch(`${API_URL}`, {
    headers: getAuthHeaders()
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Failed to fetch seances");
  }
  return response.json();
};

export const getSeanceById = async (id) => {
  const response = await fetch(`${API_URL}/${id}`, {
    headers: getAuthHeaders()
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Seance not found");
  }
  return response.json();
};

export const updateSeance = async (id, seanceData) => {
  const response = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: getAuthHeaders(),
    body: JSON.stringify(seanceData),
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Failed to update seance");
  }
  return response.json();
};

export const deleteSeance = async (id) => {
  const response = await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
    headers: getAuthHeaders()
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Failed to delete seance");
  }
  return true;
};

export const checkScheduleConflict = async (seanceData) => {
  const response = await fetch(`${API_URL}/check-conflict`, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify(seanceData),
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Failed to check schedule conflict");
  }
  return response.json();
};

// Optionnel: Filtrer les séances par date
export const getSeancesByDate = async (date) => {
  const response = await fetch(`${API_URL}/by-date?date=${date}`, {
    headers: getAuthHeaders()
  });
  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || "Failed to fetch seances by date");
  }
  return response.json();
};