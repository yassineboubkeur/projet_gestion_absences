package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.exception.DuplicateResourceException;
import com.example.projet_gestion_absences.exception.InvalidCredentialsException;
import com.example.projet_gestion_absences.exception.ResourceNotFoundException;
import com.example.projet_gestion_absences.model.dto.request.AuthRequest;
import com.example.projet_gestion_absences.model.dto.request.RegisterRequest;
import com.example.projet_gestion_absences.model.dto.response.AuthResponse;
import com.example.projet_gestion_absences.model.entity.*;
import com.example.projet_gestion_absences.repository.UtilisateurRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UtilisateurRepository utilisateurRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validate request
        if (utilisateurRepository.existsByLogin(request.getLogin())) {
            throw new DuplicateResourceException("Login déjà utilisé");
        }

        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email déjà utilisé");
        }

        // Create user based on role
        Utilisateur utilisateur = createUserByRole(request);

        // Set common properties
        setCommonUserProperties(utilisateur, request);

        // Save user
        utilisateur = utilisateurRepository.save(utilisateur);
        log.info("Nouvel utilisateur enregistré: {}", utilisateur.getLogin());

        // Generate JWT token with additional claims
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("fullName", utilisateur.getNomComplet());
        extraClaims.put("createdAt", LocalDateTime.now());

        String jwtToken = jwtService.generateToken(extraClaims, utilisateur);

        return buildAuthResponse(utilisateur, jwtToken);
    }

    public AuthResponse authenticate(AuthRequest request) {
    Utilisateur utilisateur = utilisateurRepository.findByLogin(request.getLogin())
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

    if (!passwordEncoder.matches(request.getPassword(), utilisateur.getPassword())) {
        throw new InvalidCredentialsException("Identifiants invalides");
    }

    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("lastLogin", LocalDateTime.now());

    String jwtToken = jwtService.generateToken(extraClaims, utilisateur);

    log.info("Utilisateur authentifié (manuellement): {}", utilisateur.getLogin());

    return buildAuthResponse(utilisateur, jwtToken);
}

    private Utilisateur createUserByRole(RegisterRequest request) {
        return switch (request.getRole()) {
            case ROLE_ADMIN -> new Admin();
            case ROLE_PROFESSEUR -> {
                Professeur professeur = new Professeur();
                professeur.setMatricule(request.getMatricule());
                yield professeur;
            }
            case ROLE_ETUDIANT -> {
                Etudiant etudiant = new Etudiant();
                etudiant.setMatricule(request.getMatricule());
                yield etudiant;
            }
            default -> throw new IllegalArgumentException("Rôle invalide: " + request.getRole());
        };
    }

    private void setCommonUserProperties(Utilisateur utilisateur, RegisterRequest request) {
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setLogin(request.getLogin());
        utilisateur.setPassword(passwordEncoder.encode(request.getPassword()));
        utilisateur.setRole(request.getRole());
        utilisateur.setActive(true);
    }

    private AuthResponse buildAuthResponse(Utilisateur utilisateur, String jwtToken) {
        return AuthResponse.builder()
                .token(jwtToken)
                .role(utilisateur.getRole().name())
                .userId(utilisateur.getId())
                .nomComplet(utilisateur.getNomComplet())
                .email(utilisateur.getEmail())
                .expiresIn(jwtService.getJwtExpiration())
                .build();
    }
}