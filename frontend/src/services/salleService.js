const API_URL = "http://localhost:8080/api/salles";

export async function getAllSalles() {
  const res = await fetch(API_URL);
  if (!res.ok) throw new Error("Erreur lors du chargement des salles");
  return res.json();
}

export async function getSalleById(id) {
  const res = await fetch(`${API_URL}/${id}`);
  if (!res.ok) return null;
  return res.json();
}

export async function createSalle(salle) {
  const res = await fetch(API_URL, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(salle)
  });
  if (!res.ok) throw new Error("Erreur lors de la création de la salle");
  return res.json();
}

export async function updateSalle(id, salle) {
  const res = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(salle)
  });
  if (!res.ok) throw new Error("Erreur lors de la mise à jour de la salle");
  return res.json();
}

export async function deleteSalle(id) {
  const res = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
  if (!res.ok) throw new Error("Erreur lors de la suppression de la salle");
  return true;
}
