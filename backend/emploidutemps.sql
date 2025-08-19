-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : lun. 18 août 2025 à 22:13
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `emploidutemps`
--

-- --------------------------------------------------------

--
-- Structure de la table `absence`
--

CREATE TABLE `absence` (
  `id` bigint(20) NOT NULL,
  `date_declaration` datetime(6) DEFAULT NULL,
  `justifiee` bit(1) NOT NULL,
  `motif` varchar(255) DEFAULT NULL,
  `etudiant_id` bigint(20) NOT NULL,
  `seance_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `admin`
--

CREATE TABLE `admin` (
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `admin`
--

INSERT INTO `admin` (`id`) VALUES
(4),
(5);

-- --------------------------------------------------------

--
-- Structure de la table `classe`
--

CREATE TABLE `classe` (
  `id` bigint(20) NOT NULL,
  `niveau` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `classe`
--

INSERT INTO `classe` (`id`, `niveau`, `nom`) VALUES
(1, '6emme', 'classe11'),
(2, '5eme', 'classe23'),
(3, '6eme', 'classe3'),
(4, '6eme', 'classe4'),
(5, '6eme', 'classe5'),
(6, '6eme', 'classe6'),
(7, '6eme', 'classe7'),
(8, '6eme', 'classe8');

-- --------------------------------------------------------

--
-- Structure de la table `cours`
--

CREATE TABLE `cours` (
  `id` bigint(20) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `coefficient` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `intitule` varchar(255) DEFAULT NULL,
  `volume_horaire` int(11) NOT NULL,
  `matiere_id` bigint(20) DEFAULT NULL,
  `professeur_id` bigint(20) DEFAULT NULL,
  `classe_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `cours`
--

INSERT INTO `cours` (`id`, `code`, `coefficient`, `description`, `intitule`, `volume_horaire`, `matiere_id`, `professeur_id`, `classe_id`) VALUES
(1, 'MATH101', 3, 'Basic mathematics course', 'Mathematics Fundamentals', 60, 1, NULL, NULL),
(2, 'MATH101', 3, 'Basic mathematics course', 'Mathematics Fundamentals', 60, 1, NULL, NULL),
(3, 'MATH1012', 3, 'Basic mathematics course2', 'Mathematics Fundamentals2', 60, 1, NULL, NULL),
(4, 'MATH10123', 3, 'Basic mathematics course2', 'Mathematics Fundamentals22', 60, 2, NULL, NULL),
(5, 'MATH10123', 3, 'Basic mathematics course2', 'Mathematics Fundamentals22', 60, 2, NULL, NULL),
(7, '3e3e', 2, 'sdsdsdsdssdsds', 'sdsdsd', 2, 2, 24, 2),
(8, 'e3ee4r4r44', 2, 'physics', 'physics avancee', 2, 2, NULL, 1),
(9, 'PHY103', 2, 'Mécanique, thermodynamique et électromagnétisme.', 'Physique Générale', 45, 3, NULL, 2),
(10, 'INF102', 3, 'Concepts de base de la POO avec Java.', 'Programmation Orientée Objet', 50, 2, NULL, 1),
(11, 'PHY103', 2, 'Mécanique, thermodynamique et électromagnétisme.', 'Physique Générale', 45, 3, NULL, 2),
(12, 'CHM104', 2, 'Bases de la chimie organique et réactions fondamentales.', 'Chimie Organique', 40, 4, NULL, 2),
(13, 'INF105', 3, 'Conception et manipulation des bases de données relationnelles.', 'Bases de Données', 55, 5, NULL, 1),
(14, 'MAT106', 3, 'Vecteurs, matrices, déterminants et systèmes d’équations.', 'Algèbre Linéaire', 50, 6, NULL, 1),
(15, 'INF107', 4, 'Listes, piles, files, arbres, graphes et complexité.', 'Structures de Données et Algorithmes', 60, 7, NULL, 3),
(16, 'ECO108', 2, 'Principes de la microéconomie et de la macroéconomie.', 'Économie Générale', 40, 8, NULL, 3),
(17, 'INF109', 3, 'HTML, CSS, JavaScript et introduction aux frameworks.', 'Développement Web', 55, 9, NULL, 2);

-- --------------------------------------------------------

--
-- Structure de la table `emploi_du_temps`
--

CREATE TABLE `emploi_du_temps` (
  `id` bigint(20) NOT NULL,
  `actif` bit(1) NOT NULL,
  `date_debut` date DEFAULT NULL,
  `date_fin` date DEFAULT NULL,
  `intitule` varchar(255) DEFAULT NULL,
  `classe_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `emploi_du_temps`
--

INSERT INTO `emploi_du_temps` (`id`, `actif`, `date_debut`, `date_fin`, `intitule`, `classe_id`) VALUES
(3, b'1', '2025-09-01', '2025-09-07', 'EDT classe23 (2025-09-01 → 2025-09-07)', 2),
(4, b'1', '2025-09-01', '2025-09-07', 'EDT classe23 (2025-09-01 → 2025-09-07)', 2),
(5, b'1', '2025-09-01', '2025-09-07', 'EDT classe23 (2025-09-01 → 2025-09-07)', 2),
(6, b'1', '2025-09-01', '2025-09-07', 'EDT classe11 (2025-09-01 → 2025-09-07)', 1),
(7, b'1', '2025-08-25', '2025-08-31', 'EDT classe23 (2025-08-25 → 2025-08-31)', 2),
(8, b'1', '2025-08-25', '2025-08-31', 'EDT classe3 (2025-08-25 → 2025-08-31)', 3),
(9, b'1', '2025-08-25', '2025-08-31', 'EDT classe11 (2025-08-25 → 2025-08-31)', 1),
(10, b'1', '2025-08-25', '2025-08-31', 'EDT classe3 (2025-08-25 → 2025-08-31)', 3);

-- --------------------------------------------------------

--
-- Structure de la table `etudiant`
--

CREATE TABLE `etudiant` (
  `matricule` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `classe_id` bigint(20) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `etudiant`
--

INSERT INTO `etudiant` (`matricule`, `id`, `classe_id`, `address`) VALUES
('ETU20230012', 17, 2, '123 Student Street, Paris'),
('jean.dupo@univ.edu', 18, 1, '123 Student Street, Paris'),
('ET2023001', 25, 1, '123 Rue de Paris, 75001 Paris'),
('ET2023003', 27, 1, '789 Boulevard Maritime, 13003 Marseille'),
('ET2023004', 28, 2, '101 Rue de la République, 31000 Toulouse'),
('ET2023005', 29, 2, '202 Avenue de la Liberté, 59000 Lille'),
('ET2023006', 30, 2, '303 Rue du Port, 33000 Bordeaux'),
('ET2023007', 31, 3, '404 Boulevard des Roses, 06000 Nice'),
('ET2023008', 32, 3, '505 Avenue du Soleil, 34000 Montpellier'),
('ET2023009', 33, 3, '606 Rue des Écoles, 67000 Strasbourg'),
('ET2023010', 34, 4, '707 Boulevard des Alpes, 38000 Grenoble'),
('ET2023011', 35, 4, '808 Avenue de la Gare, 44000 Nantes'),
('ET2023012', 36, 4, '909 Rue du Commerce, 35000 Rennes'),
('ET2023013', 37, 5, '1010 Boulevard Victor Hugo, 06000 Nice'),
('ET2023014', 38, 5, '1111 Avenue Gambetta, 69003 Lyon'),
('ET2023015', 39, 5, '1212 Rue de la Paix, 75002 Paris');

-- --------------------------------------------------------

--
-- Structure de la table `justificatif`
--

CREATE TABLE `justificatif` (
  `id` bigint(20) NOT NULL,
  `chemin_fichier` varchar(255) DEFAULT NULL,
  `commentaire` varchar(255) DEFAULT NULL,
  `date_depot` datetime(6) DEFAULT NULL,
  `statut` enum('EN_ATTENTE','REJETE','VALIDE') DEFAULT NULL,
  `type_fichier` varchar(255) DEFAULT NULL,
  `absence_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `matiere`
--

CREATE TABLE `matiere` (
  `id` bigint(20) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `domaine` varchar(255) DEFAULT NULL,
  `intitule` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `matiere`
--

INSERT INTO `matiere` (`id`, `code`, `domaine`, `intitule`) VALUES
(1, 'MATH', 'Advanced Science', 'Advanced Mathematics'),
(2, 'MATH', 'Science', 'Mathematics'),
(3, 'MAT', 'Sciences Fondamentales', 'Mathématiques'),
(4, 'INF', 'Sciences et Technologies', 'Informatique'),
(5, 'PHY', 'Sciences Fondamentales', 'Physique'),
(6, 'CHM', 'Sciences Fondamentales', 'Chimie'),
(7, 'CHM', 'Sciences Fondamentales', 'Chimie'),
(8, 'ECO', 'Sciences Sociales', 'Économie'),
(9, 'ENG', 'Langues et Communication', 'Anglais');

-- --------------------------------------------------------

--
-- Structure de la table `professeur`
--

CREATE TABLE `professeur` (
  `matricule` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `date_naissance` date DEFAULT NULL,
  `specialite` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `professeur`
--

INSERT INTO `professeur` (`matricule`, `id`, `adresse`, `date_naissance`, `specialite`) VALUES
('PROF123', 9, 'kenitra', NULL, 'mathematics'),
('92982', 19, 'Casa', '2000-08-15', 'Philo'),
('PROF78901', 20, '123 University Ave, Cityville', '1980-05-15', 'Computer Science'),
('PROF23456', 21, '456 College Street, Townsville', '1975-11-22', 'Mathematics'),
('PROF34567', 22, '789 Science Road, Knowledge City', '1982-03-08', 'Physics'),
('faridda433', 23, 'kenitra', '2000-08-18', 'sport'),
('faridda433', 24, 'kenitra', '2000-08-18', 'sport');

-- --------------------------------------------------------

--
-- Structure de la table `salle`
--

CREATE TABLE `salle` (
  `id` bigint(20) NOT NULL,
  `batiment` varchar(255) DEFAULT NULL,
  `capacite` int(11) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `numero` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `salle`
--

INSERT INTO `salle` (`id`, `batiment`, `capacite`, `code`, `type`, `numero`) VALUES
(1, 'Batiment A', 35, 'A101', 'Salle TD', NULL),
(2, 'Batiment A', 25, 'A102', 'Salle TD', '101'),
(3, 'A', 40, 'A101', 'Salle de cours', '101'),
(4, 'A', 60, 'A102', 'Amphithéâtre', '102'),
(5, 'B', 25, 'B201', 'Salle de TP', '201'),
(6, 'B', 30, 'B202', 'Salle informatique', '202'),
(7, 'C', 100, 'C301', 'Amphithéâtre', '301'),
(8, 'C', 20, 'C302', 'Salle de réunion', '302'),
(9, 'D', 15, 'D401', 'Salle de séminaire', '401'),
(10, 'D', 50, 'D402', 'Salle de cours', '402'),
(11, 'E', 35, 'E501', 'Salle informatique', '501'),
(12, 'E', 80, 'E502', 'Amphithéâtre', '502');

-- --------------------------------------------------------

--
-- Structure de la table `seance`
--

CREATE TABLE `seance` (
  `id` bigint(20) NOT NULL,
  `date` date DEFAULT NULL,
  `heure_debut` time(6) DEFAULT NULL,
  `heure_fin` time(6) DEFAULT NULL,
  `statut` enum('ANNULEE','EFFECTUEE','PLANIFIEE','REPORTEE') DEFAULT NULL,
  `cours_id` bigint(20) NOT NULL,
  `emploi_du_temps_id` bigint(20) DEFAULT NULL,
  `professeur_id` bigint(20) NOT NULL,
  `salle_id` bigint(20) NOT NULL,
  `edt_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `seance`
--

INSERT INTO `seance` (`id`, `date`, `heure_debut`, `heure_fin`, `statut`, `cours_id`, `emploi_du_temps_id`, `professeur_id`, `salle_id`, `edt_id`) VALUES
(2, '2025-08-18', '15:00:00.000000', '17:00:00.000000', 'REPORTEE', 3, NULL, 19, 2, NULL),
(3, '2025-08-19', '15:00:00.000000', '18:00:00.000000', 'PLANIFIEE', 3, NULL, 19, 1, NULL),
(4, '2025-08-18', '10:00:00.000000', '12:00:00.000000', 'PLANIFIEE', 3, NULL, 19, 2, NULL),
(5, '2025-09-01', '08:00:00.000000', '10:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 3),
(6, '2025-09-01', '10:00:00.000000', '12:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 3),
(7, '2025-09-01', '14:00:00.000000', '16:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 3),
(8, '2025-09-01', '16:00:00.000000', '18:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 3),
(9, '2025-09-02', '08:00:00.000000', '10:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 3),
(10, '2025-09-02', '10:00:00.000000', '12:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 3),
(11, '2025-09-02', '14:00:00.000000', '16:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 3),
(12, '2025-09-03', '08:00:00.000000', '10:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 3),
(13, '2025-09-03', '10:00:00.000000', '12:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 3),
(14, '2025-09-03', '14:00:00.000000', '16:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 3),
(15, '2025-09-04', '08:00:00.000000', '10:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 3),
(16, '2025-09-04', '10:00:00.000000', '12:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 3),
(17, '2025-09-04', '14:00:00.000000', '16:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 3),
(18, '2025-09-05', '08:00:00.000000', '10:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 3),
(19, '2025-09-05', '10:00:00.000000', '12:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 3),
(20, '2025-09-05', '14:00:00.000000', '16:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 3),
(21, '2025-09-02', '16:00:00.000000', '18:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 4),
(22, '2025-09-03', '16:00:00.000000', '18:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 4),
(23, '2025-09-04', '16:00:00.000000', '18:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 4),
(24, '2025-09-05', '16:00:00.000000', '18:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 1, 4),
(25, '2025-08-18', '08:00:00.000000', '10:00:00.000000', 'PLANIFIEE', 1, NULL, 9, 1, NULL),
(26, '2025-09-01', '08:00:00.000000', '10:00:00.000000', 'PLANIFIEE', 1, NULL, 19, 2, NULL),
(27, '2025-09-02', '09:00:00.000000', '11:00:00.000000', 'PLANIFIEE', 3, NULL, 22, 3, NULL),
(28, '2025-09-02', '09:00:00.000000', '11:00:00.000000', 'PLANIFIEE', 3, NULL, 23, 4, NULL),
(29, '2025-09-02', '11:15:00.000000', '13:15:00.000000', 'PLANIFIEE', 4, NULL, 24, 4, NULL),
(30, '2025-09-03', '08:30:00.000000', '10:30:00.000000', 'PLANIFIEE', 5, NULL, 21, 5, NULL),
(31, '2025-09-03', '10:45:00.000000', '12:00:00.000000', 'PLANIFIEE', 10, NULL, 20, 10, NULL),
(32, '2025-09-04', '09:00:00.000000', '11:00:00.000000', 'PLANIFIEE', 7, NULL, 22, 7, NULL),
(33, '2025-08-25', '08:00:00.000000', '10:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 7, 7),
(34, '2025-08-26', '10:00:00.000000', '12:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 7, 7),
(35, '2025-08-27', '14:00:00.000000', '16:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 7, 7),
(36, '2025-08-28', '16:00:00.000000', '18:00:00.000000', 'PLANIFIEE', 7, NULL, 24, 7, 7);

-- --------------------------------------------------------

--
-- Structure de la table `utilisateur`
--

CREATE TABLE `utilisateur` (
  `id` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `login` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `role` enum('ROLE_ADMIN','ROLE_ETUDIANT','ROLE_PROFESSEUR') DEFAULT NULL,
  `active` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `utilisateur`
--

INSERT INTO `utilisateur` (`id`, `email`, `login`, `nom`, `password`, `prenom`, `role`, `active`) VALUES
(4, 'admin@school.com', 'admin', 'Admin', '$2a$10$KpEFEAbQIIWz.EjjXj7EZOhalzmca1ZVHsjpYZ0EqaA.RCuVXqc.m', 'User', 'ROLE_ADMIN', b'1'),
(5, 'admin@school2.com', 'admin2', 'Admin', '$2a$10$jVl2LFGxiITATFurHxI4PuqTp.I/Ki.r1oNox.MIiIfTZQqGcJr6C', 'User', 'ROLE_ADMIN', b'1'),
(9, 'jean.dupont2@ecole.com', 'jdupont2', 'Dupont', '$2a$10$/H2aBJWVLL0iUCrx1oMK7uGICaV47m7JLNLkq1jkd7L9pfmf0bxrG', 'Jean', 'ROLE_PROFESSEUR', b'1'),
(16, 'jean.dupont22@univ.edu', 'jdupont22', 'Dupont', 'jdupont22', 'Jean', 'ROLE_ETUDIANT', b'0'),
(17, 'jean.dupont242@univ.edu', 'jean.dupont242@univ.edu', 'Dupont', '$2a$10$ixqU8wflQ/oVV.JlK8bq3uuvy0bNpmsMehZ.DNW9ui.BduQtMoRvC', 'Jean', 'ROLE_ETUDIANT', b'0'),
(18, 'jean.dupo@univ.edu', 'jean.dupo@univ.edu', 'Dupont', '$2a$10$XWAsD3XwOx9.agaOjwVktO7CEhfuH5M4WgDQPJIrRptVIAw4lxG2e', 'Jean', 'ROLE_ETUDIANT', b'0'),
(19, 'farid@farid.farid', 'farid@farid.farid', 'FARID', '$2a$10$kTP61.KQxy5TB2GpUNg0KufcPdW/yHSlxH2r27q7VAfpZQ3py5xdW', 'farid', 'ROLE_PROFESSEUR', b'1'),
(20, 'sophie.martin@univ.edu', 'smartin', 'Martin', '$2a$10$XVNIDKj1rb0uVw/m0sxD/O6YeZUGwfvbg82S2UZsWm3VrCweZz2/S', 'Sophie', 'ROLE_PROFESSEUR', b'1'),
(21, 'robert.johnson@univ.edu', 'rjohnson', 'Johnson', '$2a$10$iycrcuE/6lZPSjgqjK78bOiyMuDX4/WxlnFZfiXsNklDbc9rQskQO', 'Robert', 'ROLE_PROFESSEUR', b'1'),
(22, 'wei.chen@univ.edu', 'wchen', 'Chen', '$2a$10$.Aj.GloVhyGvCEYvXDjxRuts8MIc5D6nGp2fmPpta9GPYu9sWhed6', 'Wei', 'ROLE_PROFESSEUR', b'1'),
(23, 'faridda2@faridda.faridda', 'faridda2', 'faridda', '$2a$10$uVm088bILE.IQ4TvE6LwWODrXVC18aK7fSrplJlfNpr8u4Gq61Jne', 'faridda', 'ROLE_PROFESSEUR', b'1'),
(24, 'faridda@faridda.faridda', 'faridda', 'faridda', '$2a$10$V4YKDAqCpnOS5a9yMgW3fusxTOfB8E2wgTrAnibt4tQwzGoBL7B3e', 'faridda', 'ROLE_PROFESSEUR', b'1'),
(25, 'jean.dupont@email.com', 'jdupont', 'Dupont', '$2a$10$aWY4SiR6WpF7CMPTLguHgucg4ebaEsyUC8uTmeokIulgpIaJSy8j6', 'Jean', 'ROLE_ETUDIANT', b'0'),
(27, 'pierre.bernard@email.com', 'pbernard', 'Bernard', '$2a$10$j1ZYW1weAl7H4Mpe6behoe41yYN4pNNcwe0R.xD.y.7oiWy3Gbwpa', 'Pierre', 'ROLE_ETUDIANT', b'0'),
(28, 'marie.petit@email.com', 'mpetit', 'Petit', '$2a$10$oDxOU0BqsMj9dcsgy/o6hOsUV.RRrYOnAn8LXmF.zGKzlFmoLVGXC', 'Marie', 'ROLE_ETUDIANT', b'0'),
(29, 'thomas.leroy@email.com', 'tleroy', 'Leroy', '$2a$10$8.Lc0liWaDNqt2cXAI2a5ugSohATLzVlQjbvlv8vjTqJNwCcEfI4y', 'Thomas', 'ROLE_ETUDIANT', b'0'),
(30, 'julie.moreau@email.com', 'jmoreau', 'Moreau', '$2a$10$dVbgWYcZq4s/Zq9AXmvXceIyveiBjIMhDUr.oWGr/a6m4t6nn6Keu', 'Julie', 'ROLE_ETUDIANT', b'0'),
(31, 'luc.simon@email.com', 'lsimon', 'Simon', '$2a$10$FxZhZiiW2CG7e76z.Ej/F.Ndph1csnNmeWnHrWMDoveR41BcrTTQG', 'Luc', 'ROLE_ETUDIANT', b'0'),
(32, 'emma.laurent@email.com', 'elaurent', 'Laurent', '$2a$10$AG3ssXnWa028UteB5y1x6uj8qxlPztaZQw4GQyMoadyrs7LUsK8Iq', 'Emma', 'ROLE_ETUDIANT', b'0'),
(33, 'antoine.lefebvre@email.com', 'alefebvre', 'Lefebvre', '$2a$10$Ogq75K1VCJtJmjWhmV6Zp.XgCcCcSiWcO45XQHgpZ4UZVsuB9TjJu', 'Antoine', 'ROLE_ETUDIANT', b'0'),
(34, 'camille.michel@email.com', 'cmichel', 'Michel', '$2a$10$SVldhc8k5Je24AkyS4Y/0uhLk9wzE/mlSkLoyKwA2.2TzoAp56m4K', 'Camille', 'ROLE_ETUDIANT', b'0'),
(35, 'nicolas.garcia@email.com', 'ngarcia', 'Garcia', '$2a$10$fbWRCly1t5OJBNmadFfM..wZ6AKSsrAqL3Mnz8gRMKvQC1has/6We', 'Nicolas', 'ROLE_ETUDIANT', b'0'),
(36, 'laura.david@email.com', 'ldavid', 'David', '$2a$10$nO3jOwZNbtkcJ6P8SJ51Y.EQ7zUre4LEMFQWEQYqWyEY//wD0YdRK', 'Laura', 'ROLE_ETUDIANT', b'0'),
(37, 'alexandre.bertrand@email.com', 'abertrand', 'Bertrand', '$2a$10$wh.1kIFwUN6DBBARNcGQ1OqfFnPRtW0Xh0/KjoJGyYkpZzmyRDxt.', 'Alexandre', 'ROLE_ETUDIANT', b'0'),
(38, 'sarah.roux@email.com', 'sroux', 'Roux', '$2a$10$pDGegD3F1HH00kmL9jZOVOQorG6ulO8hyVlgIj.h4MO/Jv0zqA2DS', 'Sarah', 'ROLE_ETUDIANT', b'0'),
(39, 'paul.vincent@email.com', 'pvincent', 'Vincent', '$2a$10$31W57287t4Agns1BDSh8z.5YpWqoGQzMKuWGG2pIw8.dAd9U9BqgS', 'Paul', 'ROLE_ADMIN', b'0');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `absence`
--
ALTER TABLE `absence`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKt8yk6f8uker9w8s1ec60a3a4p` (`etudiant_id`),
  ADD KEY `FKiqc5qah2eedl4ben0gkjcna05` (`seance_id`);

--
-- Index pour la table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `classe`
--
ALTER TABLE `classe`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `cours`
--
ALTER TABLE `cours`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKmle5drslk5nx2kwu3rxpsyh50` (`matiere_id`),
  ADD KEY `FK5vnwklshnvc9w2qjsorbdv03b` (`professeur_id`),
  ADD KEY `FK5kx0iux5mtt4jng03wa2j57n9` (`classe_id`);

--
-- Index pour la table `emploi_du_temps`
--
ALTER TABLE `emploi_du_temps`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKmw6o3sbc5i1pt8aug4k35one3` (`classe_id`);

--
-- Index pour la table `etudiant`
--
ALTER TABLE `etudiant`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK4f9xvhnh2rx4gl5l1sbrtflmh` (`classe_id`);

--
-- Index pour la table `justificatif`
--
ALTER TABLE `justificatif`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_t5snfc4gjxk9ip6qyx9mpfoxu` (`absence_id`);

--
-- Index pour la table `matiere`
--
ALTER TABLE `matiere`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `professeur`
--
ALTER TABLE `professeur`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `salle`
--
ALTER TABLE `salle`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `seance`
--
ALTER TABLE `seance`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKrrc1k4hpxsmh2hau4gl5wjgod` (`cours_id`),
  ADD KEY `FKc7wj666ypniqwg385sq9mxv6f` (`emploi_du_temps_id`),
  ADD KEY `FKo2ykldllli0ds3ngesc0ajwna` (`professeur_id`),
  ADD KEY `FKhupu0bkkkinurpu14xeyjcuep` (`salle_id`),
  ADD KEY `FKiragdrotl0u3cx41f1o9swenw` (`edt_id`);

--
-- Index pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_rma38wvnqfaf66vvmi57c71lo` (`email`),
  ADD UNIQUE KEY `UK_18vwp4resqussqmlpqnymfqxk` (`login`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `absence`
--
ALTER TABLE `absence`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `classe`
--
ALTER TABLE `classe`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `cours`
--
ALTER TABLE `cours`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT pour la table `emploi_du_temps`
--
ALTER TABLE `emploi_du_temps`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT pour la table `justificatif`
--
ALTER TABLE `justificatif`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `matiere`
--
ALTER TABLE `matiere`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT pour la table `salle`
--
ALTER TABLE `salle`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT pour la table `seance`
--
ALTER TABLE `seance`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `absence`
--
ALTER TABLE `absence`
  ADD CONSTRAINT `FKiqc5qah2eedl4ben0gkjcna05` FOREIGN KEY (`seance_id`) REFERENCES `seance` (`id`),
  ADD CONSTRAINT `FKt8yk6f8uker9w8s1ec60a3a4p` FOREIGN KEY (`etudiant_id`) REFERENCES `etudiant` (`id`);

--
-- Contraintes pour la table `admin`
--
ALTER TABLE `admin`
  ADD CONSTRAINT `FKgodqjbbtwk30kf3s0xuxklkr3` FOREIGN KEY (`id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `cours`
--
ALTER TABLE `cours`
  ADD CONSTRAINT `FK5kx0iux5mtt4jng03wa2j57n9` FOREIGN KEY (`classe_id`) REFERENCES `classe` (`id`),
  ADD CONSTRAINT `FK5vnwklshnvc9w2qjsorbdv03b` FOREIGN KEY (`professeur_id`) REFERENCES `professeur` (`id`),
  ADD CONSTRAINT `FKmle5drslk5nx2kwu3rxpsyh50` FOREIGN KEY (`matiere_id`) REFERENCES `matiere` (`id`);

--
-- Contraintes pour la table `emploi_du_temps`
--
ALTER TABLE `emploi_du_temps`
  ADD CONSTRAINT `FKmw6o3sbc5i1pt8aug4k35one3` FOREIGN KEY (`classe_id`) REFERENCES `classe` (`id`);

--
-- Contraintes pour la table `etudiant`
--
ALTER TABLE `etudiant`
  ADD CONSTRAINT `FK4f9xvhnh2rx4gl5l1sbrtflmh` FOREIGN KEY (`classe_id`) REFERENCES `classe` (`id`),
  ADD CONSTRAINT `FK8r3ygi23xihnm6jbtxoyrmlrj` FOREIGN KEY (`id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `justificatif`
--
ALTER TABLE `justificatif`
  ADD CONSTRAINT `FK3gcfueoj92xas5vespb0qe09l` FOREIGN KEY (`absence_id`) REFERENCES `absence` (`id`);

--
-- Contraintes pour la table `professeur`
--
ALTER TABLE `professeur`
  ADD CONSTRAINT `FKamxqj5y3n8hjn5visgfush36s` FOREIGN KEY (`id`) REFERENCES `utilisateur` (`id`);

--
-- Contraintes pour la table `seance`
--
ALTER TABLE `seance`
  ADD CONSTRAINT `FKc7wj666ypniqwg385sq9mxv6f` FOREIGN KEY (`emploi_du_temps_id`) REFERENCES `emploi_du_temps` (`id`),
  ADD CONSTRAINT `FKhupu0bkkkinurpu14xeyjcuep` FOREIGN KEY (`salle_id`) REFERENCES `salle` (`id`),
  ADD CONSTRAINT `FKiragdrotl0u3cx41f1o9swenw` FOREIGN KEY (`edt_id`) REFERENCES `emploi_du_temps` (`id`),
  ADD CONSTRAINT `FKo2ykldllli0ds3ngesc0ajwna` FOREIGN KEY (`professeur_id`) REFERENCES `professeur` (`id`),
  ADD CONSTRAINT `FKrrc1k4hpxsmh2hau4gl5wjgod` FOREIGN KEY (`cours_id`) REFERENCES `cours` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
