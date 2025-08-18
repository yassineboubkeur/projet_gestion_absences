// src/main/java/com/example/projet_gestion_absences/service/AuthService.java
package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.exception.DuplicateResourceException;
import com.example.projet_gestion_absences.exception.InvalidCredentialsException;
import com.example.projet_gestion_absences.model.dto.request.AuthRequest;
import com.example.projet_gestion_absences.model.dto.request.RegisterRequest;
import com.example.projet_gestion_absences.model.dto.response.AuthResponse;
import com.example.projet_gestion_absences.model.entity.Admin;
import com.example.projet_gestion_absences.model.entity.Etudiant;
import com.example.projet_gestion_absences.model.entity.Professeur;
import com.example.projet_gestion_absences.model.entity.Utilisateur;
import com.example.projet_gestion_absences.repository.UtilisateurRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UtilisateurRepository utilisateurRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (utilisateurRepository.existsByLogin(request.getLogin())) {
            throw new DuplicateResourceException("Login déjà utilisé");
        }
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email déjà utilisé");
        }

        Utilisateur utilisateur = createUserByRole(request);
        setCommonUserProperties(utilisateur, request);
        utilisateur = utilisateurRepository.save(utilisateur);

        Map<String, Object> extra = new HashMap<>();
        // IMPORTANT: String ISO-8601 (ou System.currentTimeMillis())
        extra.put("createdAt", Instant.now().toString());

        String token = jwtService.generateToken(extra, utilisateur);
        log.info("Nouvel utilisateur enregistré: {}", utilisateur.getLogin());

        return buildAuthResponse(utilisateur, token);
    }

    public AuthResponse authenticate(AuthRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(request.getPassword(), utilisateur.getPassword())) {
            throw new InvalidCredentialsException("Identifiants invalides");
        }

        Map<String, Object> extra = new HashMap<>();
        // IMPORTANT: String ISO-8601 (ou System.currentTimeMillis())
        extra.put("lastLogin", Instant.now().toString());

        String token = jwtService.generateToken(extra, utilisateur);
        log.info("Utilisateur authentifié: {}", utilisateur.getLogin());

        return buildAuthResponse(utilisateur, token);
    }

    private Utilisateur createUserByRole(RegisterRequest request) {
        return switch (request.getRole()) {
            case ROLE_ADMIN -> new Admin();
            case ROLE_PROFESSEUR -> {
                Professeur p = new Professeur();
                p.setMatricule(request.getMatricule());
                yield p;
            }
            case ROLE_ETUDIANT -> {
                Etudiant e = new Etudiant();
                e.setMatricule(request.getMatricule());
                yield e;
            }
        };
    }

    private void setCommonUserProperties(Utilisateur u, RegisterRequest r) {
        u.setNom(r.getNom());
        u.setPrenom(r.getPrenom());
        u.setEmail(r.getEmail());
        u.setLogin(r.getLogin());
        u.setPassword(passwordEncoder.encode(r.getPassword()));
        u.setRole(r.getRole());
        u.setActive(true);
    }

    private AuthResponse buildAuthResponse(Utilisateur u, String jwtToken) {
        return AuthResponse.builder()
                .token(jwtToken)
                .role(u.getRole().name())
                .userId(u.getId())
                .nomComplet(u.getNomComplet())
                .email(u.getEmail())
                .expiresIn(jwtService.getJwtExpirationMillis())
                .build();
    }
}
