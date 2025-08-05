package com.example.projet_gestion_absences.model.dto.request;

import com.example.projet_gestion_absences.model.enums.Role;

public class RegisterRequest {
    private String nom;
    private String prenom;
    private String email;
    private String login;
    private String password;
    private Role role;

    // Pour étudiant/professeur
    private String matricule;
    private Long classeId; // Pour étudiant

    // Getters & Setters

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getMatricule() {
        return matricule;
    }

    public Long getClasseId() {
        return classeId;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public void setClasseId(Long classeId) {
        this.classeId = classeId;
    }
}
