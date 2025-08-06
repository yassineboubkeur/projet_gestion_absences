-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : jeu. 07 août 2025 à 00:08
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
(4);

-- --------------------------------------------------------

--
-- Structure de la table `classe`
--

CREATE TABLE `classe` (
  `id` bigint(20) NOT NULL,
  `niveau` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
  `matiere_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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

-- --------------------------------------------------------

--
-- Structure de la table `etudiant`
--

CREATE TABLE `etudiant` (
  `matricule` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `classe_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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

-- --------------------------------------------------------

--
-- Structure de la table `professeur`
--

CREATE TABLE `professeur` (
  `matricule` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `salle`
--

CREATE TABLE `salle` (
  `id` bigint(20) NOT NULL,
  `batiment` varchar(255) DEFAULT NULL,
  `capacite` int(11) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
  `salle_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
(4, 'admin@school.com', 'admin', 'Admin', '$2a$10$KpEFEAbQIIWz.EjjXj7EZOhalzmca1ZVHsjpYZ0EqaA.RCuVXqc.m', 'User', 'ROLE_ADMIN', b'1');

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
  ADD KEY `FKmle5drslk5nx2kwu3rxpsyh50` (`matiere_id`);

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
  ADD KEY `FKhupu0bkkkinurpu14xeyjcuep` (`salle_id`);

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
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `cours`
--
ALTER TABLE `cours`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `emploi_du_temps`
--
ALTER TABLE `emploi_du_temps`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `justificatif`
--
ALTER TABLE `justificatif`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `matiere`
--
ALTER TABLE `matiere`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `salle`
--
ALTER TABLE `salle`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `seance`
--
ALTER TABLE `seance`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `utilisateur`
--
ALTER TABLE `utilisateur`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

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
  ADD CONSTRAINT `FKo2ykldllli0ds3ngesc0ajwna` FOREIGN KEY (`professeur_id`) REFERENCES `professeur` (`id`),
  ADD CONSTRAINT `FKrrc1k4hpxsmh2hau4gl5wjgod` FOREIGN KEY (`cours_id`) REFERENCES `cours` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
