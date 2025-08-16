// src/App.jsx
import React from "react";
import { Routes, Route } from "react-router-dom";

import DashboardLayout from "./pages/dashboard/DashboardLayout";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import MainLayout from "./layouts/MainLayouts";
import HomePage from "./pages/HomePage";
import { useAuthStore } from "./store/useAuthStore";
import PrivacyPage from "./pages/PrivacyPage";
import DashboardHome from "./pages/dashboard/DashboardHome";
import EtudiantsList from "./pages/dashboard/etudiant/EtudiantsList";
import EtudiantForm from "./pages/dashboard/etudiant/EtudiantForm";
import EtudiantDetails from "./pages/dashboard/etudiant/EtudiantDetails";
import ProfesseursList from "./pages/dashboard/professeur/ProfesseursList";
import ProfesseurForm from "./pages/dashboard/professeur/ProfesseurForm";
import ProfesseurDetails from "./pages/dashboard/professeur/ProfesseurDetails";
import ClassesList from "./pages/dashboard/classe/ClassesList";
import ClasseForm from "./pages/dashboard/classe/ClasseForm";
import ClasseDetail from "./pages/dashboard/classe/ClasseDetail";
import MatieresList from "./pages/dashboard/matiere/MatieresList";
import MatiereForm from "./pages/dashboard/matiere/MatiereForm";
import MatiereDetail from "./pages/dashboard/matiere/MatiereDetail";
import CoursList from "./pages/dashboard/cours/CoursList";
import CoursForm from "./pages/dashboard/cours/CoursForm";
import CoursDetail from "./pages/dashboard/cours/CoursDetail";
import SalleList from "./pages/dashboard/salle/SalleList";
import SalleForm from "./pages/dashboard/salle/SalleForm";
import SeanceList from "./pages/dashboard/seance/SeanceList";
import SeanceForm from "./pages/dashboard/seance/SeanceForm";
import SeanceDetails from "./pages/dashboard/seance/SeanceDetails";
import SalleDetail from "./pages/dashboard/salle/SalleDetail";

export default function App() {
  const token = useAuthStore((state) => state.token);

  return (
    <Routes>
      <Route element={<MainLayout />}>
        <Route path="/" element={<HomePage />} />
        {!token && (
          <>
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
          </>
        )}
        <Route path="/privacy" element={<PrivacyPage />} />
        <Route path="/dashboard" element={<DashboardLayout />}>
          <Route index element={<DashboardHome />} />
          <Route path="/dashboard/etudiants" element={<EtudiantsList />} />
          <Route path="/dashboard/etudiants/new" element={<EtudiantForm />} />
          <Route path="/dashboard/etudiants/:id/edit" element={<EtudiantForm />} />
          <Route path="/dashboard/etudiants/:id" element={<EtudiantDetails />} />
          <Route path="/dashboard/professeurs" element={<ProfesseursList />} />
          <Route path="/dashboard/professeurs/new" element={<ProfesseurForm />} />
          <Route path="/dashboard/professeurs/:id/edit" element={<ProfesseurForm />} />
          <Route path="/dashboard/professeurs/:id" element={<ProfesseurDetails />} />
          <Route path="/dashboard/classes" element={<ClassesList />} />
          <Route path="/dashboard/classes/new" element={<ClasseForm />} />
          <Route path="/dashboard/classes/:id/edit" element={<ClasseForm />} />
          <Route path="/dashboard/classes/:id" element={<ClasseDetail />} />
          <Route path="/dashboard/matieres" element={<MatieresList />} />
          <Route path="/dashboard/matieres/new" element={<MatiereForm />} />
          <Route path="/dashboard/matieres/:id/edit" element={<MatiereForm />} />
          <Route path="/dashboard/matieres/:id" element={<MatiereDetail />} />
          <Route path="/dashboard/cours" element={<CoursList />} />
          <Route path="/dashboard/cours/new" element={<CoursForm />} />
          <Route path="/dashboard/cours/:id/edit" element={<CoursForm />} />
          <Route path="/dashboard/cours/:id" element={<CoursDetail />} />
          <Route path="/dashboard/salles" element={<SalleList />} />
          <Route path="/dashboard/salles/new" element={<SalleForm />} />
          <Route path="/dashboard/salles/:id/edit" element={<SalleForm />} />
          <Route path="/dashboard/salles/:id" element={<SalleDetail />} />
          <Route path="/dashboard/seances" element={<SeanceList />} />
          <Route path="/dashboard/seances/new" element={<SeanceForm />} />
          <Route path="/dashboard/seances/:id/edit" element={<SeanceForm />} />
          <Route path="/dashboard/seances/:id" element={<SeanceDetails />} />

        </Route>
      </Route>
    </Routes>
  );
}
