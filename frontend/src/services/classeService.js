const API_URL = "http://localhost:8080/api/classes";

// GET all classes
export const getAllClasses = async () => {
  const res = await fetch(API_URL);
  if (!res.ok) throw new Error("Erreur lors de la récupération des classes");
  return res.json();
};

// GET classe by id
export const getClasseById = async (id) => {
  const res = await fetch(`${API_URL}/${id}`);
  if (!res.ok) throw new Error("Classe non trouvée");
  return res.json();
};

// GET classes by niveau
export const getClassesByNiveau = async (niveau) => {
  const res = await fetch(`${API_URL}/niveau/${niveau}`);
  if (!res.ok) throw new Error("Erreur lors de la récupération par niveau");
  return res.json();
};

// POST create classe
export const createClasse = async (classe) => {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(classe),
  });
  if (!res.ok) throw new Error("Erreur lors de la création de la classe");
  return res.json();
};

// PUT update classe
export const updateClasse = async (id, classe) => {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(classe),
  });
  if (!res.ok) throw new Error("Erreur lors de la mise à jour de la classe");
  return res.json();
};

// DELETE classe
export const deleteClasse = async (id) => {
  const res = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error("Erreur lors de la suppression de la classe");
};

// GET etudiants by classe
export const getEtudiantsByClasse = async (id) => {
  const res = await fetch(`${API_URL}/${id}/etudiants`);
  if (!res.ok) throw new Error("Erreur lors de la récupération des étudiants");
  return res.json();
};
