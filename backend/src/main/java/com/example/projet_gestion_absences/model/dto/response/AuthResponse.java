package com.example.projet_gestion_absences.model.dto.response;

public class AuthResponse {
    private String token;
    private String role;
    private Long userId;
    private String nomComplet;
    private String email;
    private long expiresIn;

    // Private constructor for builder
    private AuthResponse() {}

    // Getters
    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public String getEmail() {
        return email;
    }
    public long getExpiresIn() {  // Added getter for expiresIn
        return expiresIn;
    }
    // Builder class
    public static class Builder {
        private final AuthResponse response = new AuthResponse();

        public Builder token(String token) {
            response.token = token;
            return this;
        }
        public Builder expiresIn(long expiresIn) {  // Added expiresIn builder method
            response.expiresIn = expiresIn;
            return this;
        }
        public Builder role(String role) {
            response.role = role;
            return this;
        }

        public Builder userId(Long userId) {
            response.userId = userId;
            return this;
        }

        public Builder nomComplet(String nomComplet) {
            response.nomComplet = nomComplet;
            return this;
        }

        public Builder email(String email) {
            response.email = email;
            return this;
        }

        public AuthResponse build() {
            // Validate required fields here if needed
            return response;
        }



    }

    // Static builder method
    public static Builder builder() {
        return new Builder();
    }
}