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

    /* ===================== Lecture ===================== */

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // "sub"
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> (String) claims.get("role"));
    }

    /** Extracteur générique par nom de claim, avec conversion de type simple (Number -> Long/Integer, etc.) */
    public <T> T extractClaim(String token, String claimName, Class<T> type) {
        Claims claims = extractAllClaims(token);
        Object value = claims.get(claimName);
        if (value == null) return null;

        // Déjà du bon type
        if (type.isInstance(value)) {
            return type.cast(value);
        }

        // Conversions utiles
        if (value instanceof Number n) {
            if (type == Long.class)   return type.cast(n.longValue());
            if (type == Integer.class) return type.cast(n.intValue());
            if (type == Double.class)  return type.cast(n.doubleValue());
        }
        if (type == String.class) {
            return type.cast(String.valueOf(value));
        }

        throw new IllegalArgumentException(
                "Cannot cast claim '" + claimName + "' of type " + value.getClass().getName() + " to " + type.getName()
        );
    }

    /** Raccourci pratique si tu veux un Long directement */
    public Long extractUserId(String token) {
        Number n = extractClaim(token, "userId", Number.class);
        return (n == null) ? null : n.longValue();
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

    /* ===================== Création ===================== */

    /** Génère un token sans claims additionnels */
    public String generateToken(Utilisateur user) {
        return generateToken(Map.of(), user);
    }

    /** Génère un token avec claims additionnels */
    public String generateToken(Map<String, Object> extraClaims, Utilisateur user) {
        Instant now = Instant.now();
        Instant exp = now.plusMillis(jwtExpirationMillis);

        return Jwts.builder()
                .setClaims(extraClaims)                     // claims supplémentaires
                .setSubject(user.getLogin())                // sub
                .setIssuedAt(Date.from(now))                // iat
                .setExpiration(Date.from(exp))              // exp
                .claim("role", user.getRole().name())       // ROLE_ADMIN / ROLE_ETUDIANT / ROLE_PROFESSEUR
                .claim("userId", user.getId())
                .setIssuer("ProjetGestionAbsences")
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /* ===================== Internes ===================== */

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /** Méthode existante : extracteur via fonction */
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
