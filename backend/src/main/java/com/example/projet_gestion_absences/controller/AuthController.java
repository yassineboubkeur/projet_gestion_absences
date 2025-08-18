package com.example.projet_gestion_absences.controller;

import com.example.projet_gestion_absences.model.dto.request.AuthRequest;
import com.example.projet_gestion_absences.model.dto.request.RegisterRequest;
import com.example.projet_gestion_absences.model.dto.response.AuthResponse;
import com.example.projet_gestion_absences.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
