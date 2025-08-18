package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.model.entity.Utilisateur;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKeyBase64;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpirationMillis;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // sub
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> (String) claims.get("role"));
    }

    public boolean isTokenValid(String token) {
        try {
            final String username = extractUsername(token);
            return username != null && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public long getJwtExpirationMillis() {
        return jwtExpirationMillis;
    }

    /* ===================== Token creation ===================== */

    /** Génère un token sans claims additionnels */
    public String generateToken(Utilisateur user) {
        return generateToken(Map.of(), user);
    }

    /** Génère un token avec claims additionnels */
    public String generateToken(Map<String, Object> extraClaims, Utilisateur user) {
        Instant now = Instant.now();
        Instant exp = now.plusMillis(jwtExpirationMillis);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getLogin())                 // sub
                .setIssuedAt(Date.from(now))                 // iat
                .setExpiration(Date.from(exp))               // exp
                .claim("role", user.getRole().name())        // ROLE_ADMIN / ROLE_ETUDIANT / ROLE_PROFESSEUR
                .claim("userId", user.getId())
                .setIssuer("ProjetGestionAbsences")
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /* ===================== Internals ===================== */

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyBase64);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
