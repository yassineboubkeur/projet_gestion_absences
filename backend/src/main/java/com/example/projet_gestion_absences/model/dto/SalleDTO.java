package com.example.projet_gestion_absences.model.dto;

//package com.example.projet_gestion_absences.dto;

public class SalleDTO {
    private String code;
    private String batiment;
private String numero;
    private int capacite;
    private String type;

    // Constructors, Getters and Setters
    public SalleDTO() {}

    public SalleDTO(String code, String batiment, String numero,int capacite, String type) {
        this.code = code;
        this.batiment = batiment;
        this.numero = numero;
        this.capacite = capacite;
        this.type = type;
    }

    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getBatiment() { return batiment; }
    public void setBatiment(String batiment) { this.batiment = batiment; }

    public  String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}