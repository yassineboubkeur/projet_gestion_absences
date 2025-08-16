// src/services/seanceService.js
const API_URL = "http://localhost:8080/api/seances"; // changer l'URL si besoin

// Create Seance
export const createSeance = async (seanceData) => {
  const response = await fetch(`${API_URL}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(seanceData),
  });
  if (!response.ok) throw new Error("Failed to create seance");
  return response.json();
};

// Get all Seances
export const getAllSeances = async () => {
  const response = await fetch(`${API_URL}`);
  if (!response.ok) throw new Error("Failed to fetch seances");
  return response.json();
};

// Get Seance by ID
export const getSeanceById = async (id) => {
  const response = await fetch(`${API_URL}/${id}`);
  if (!response.ok) throw new Error("Seance not found");
  return response.json();
};

// Update Seance
export const updateSeance = async (id, seanceData) => {
  const response = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(seanceData),
  });
  if (!response.ok) throw new Error("Failed to update seance");
  return response.json();
};

// Delete Seance
export const deleteSeance = async (id) => {
  const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
  if (!response.ok) throw new Error("Failed to delete seance");
  return true;
};

// Check schedule conflict
export const checkScheduleConflict = async (seanceData) => {
  const response = await fetch(`${API_URL}/check-conflict`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(seanceData),
  });
  if (!response.ok) throw new Error("Failed to check schedule conflict");
  return response.json(); // returns true or false
};
