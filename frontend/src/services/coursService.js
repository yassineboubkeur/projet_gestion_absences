const API_URL = "http://localhost:8080/api/cours";

export const getAllCours = async () => {
  const res = await fetch(API_URL);
  if (!res.ok) throw new Error("Erreur lors du chargement des cours");
  return res.json();
};

export const getCoursById = async (id) => {
  const res = await fetch(`${API_URL}/${id}`);
  if (!res.ok) throw new Error("Cours non trouvé");
  return res.json();
};

export const getCoursByMatiere = async (matiereId) => {
  const res = await fetch(`${API_URL}/matiere/${matiereId}`);
  if (!res.ok) throw new Error("Erreur lors du chargement des cours par matière");
  return res.json();
};

export const createCours = async (cours) => {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(cours),
  });
  if (!res.ok) throw new Error("Erreur lors de la création du cours");
  return res.json();
};

export const updateCours = async (id, cours) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(cours),
  });
  if (!res.ok) throw new Error("Erreur lors de la mise à jour du cours");
  return res.json();
};

export const deleteCours = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error("Erreur lors de la suppression du cours");
};

export const searchCours = async (intitule) => {
  const res = await fetch(`${API_URL}/search?intitule=${encodeURIComponent(intitule)}`);
  if (!res.ok) throw new Error("Erreur lors de la recherche");
  return res.json();
};
