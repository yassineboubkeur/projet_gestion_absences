package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.model.entity.Salle;
import com.example.projet_gestion_absences.repository.SalleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SalleService {

    private final SalleRepository salleRepository;

    public SalleService(SalleRepository salleRepository) {
        this.salleRepository = salleRepository;
    }

    // Create
    public Salle createSalle(Salle salleDTO) {
        Salle salle = new Salle();
        salle.setCode(salleDTO.getCode());
        salle.setBatiment(salleDTO.getBatiment());
        salle.setNumero(salleDTO.getNumero());
        salle.setCapacite(salleDTO.getCapacite());
        salle.setType(salleDTO.getType());
        return salleRepository.save(salle);
    }

    // Read
    public List<Salle> getAllSalles() {
        return salleRepository.findAll();
    }

    public Optional<Salle> getSalleById(Long id) {
        return salleRepository.findById(id);
    }

    public Salle getSalleByCode(String code) {
        return salleRepository.findByCode(code);
    }

    // Update
    public Salle updateSalle(Long id, Salle salleDTO) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle not found with id: " + id));

        salle.setCode(salleDTO.getCode());
        salle.setBatiment(salleDTO.getBatiment());
        salle.setNumero(salleDTO.getNumero());
        salle.setCapacite(salleDTO.getCapacite());
        salle.setType(salleDTO.getType());

        return salleRepository.save(salle);
    }

    // Delete
    public void deleteSalle(Long id) {
        Salle salle = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle not found with id: " + id));
        salleRepository.delete(salle);
    }
}