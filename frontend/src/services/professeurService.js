const API_URL = "http://localhost:8080/api/professeurs";

export const getAllProfesseurs = async () => {
  const response = await fetch(API_URL);
  return response.json();
};

export const getProfesseurById = async (id) => {
  const response = await fetch(`${API_URL}/${id}`);
  return response.json();
};

export const createProfesseur = async (professeur) => {
  const response = await fetch(API_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(professeur),
  });
  return response.json();
};

export const updateProfesseur = async (id, professeur) => {
  const response = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(professeur),
  });
  return response.json();
};

export const deleteProfesseur = async (id) => {
  await fetch(`${API_URL}/${id}`, { method: "DELETE" });
};

export const toggleProfesseurStatus = async (id) => {
  const response = await fetch(`${API_URL}/${id}/toggle-status`, { method: "PATCH" });
  return response.json();
};

export const searchProfesseurs = async (term) => {
  const response = await fetch(`${API_URL}/search?term=${encodeURIComponent(term)}`);
  return response.json();
};

export const getProfesseursBySpecialite = async (specialite) => {
  const response = await fetch(`${API_URL}/specialites/${encodeURIComponent(specialite)}`);
  return response.json();
};

export const getAllSpecialites = async () => {
  const response = await fetch(`${API_URL}/specialites`);
  return response.json();
};

export const getProfesseursGroupedBySpecialite = async () => {
  const response = await fetch(`${API_URL}/specialitesbyprof`);
  return response.json();
};
