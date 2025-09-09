package com.example.projet_gestion_absences.service;

import com.example.projet_gestion_absences.model.entity.Utilisateur;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
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

    /* ===================== PUBLIC: LECTURE ===================== */

    /** "sub" */
    public String extractUsername(String token) {
        return extractClaim(stripBearer(token), Claims::getSubject);
    }

    /** claim "role" (ex: ROLE_ADMIN / ROLE_ETUDIANT / ROLE_PROFESSEUR) */
    public String extractRole(String token) {
        return extractClaim(stripBearer(token), "role", String.class);
    }

    /** Récupère un claim typé par son nom (avec conversions simples Number->Long/Integer, etc.) */
    public <T> T extractClaim(String token, String claimName, Class<T> type) {
        Claims claims = extractAllClaims(stripBearer(token));
        Object value = claims.get(claimName);
        if (value == null) return null;

        if (type.isInstance(value)) return type.cast(value);

        if (value instanceof Number n) {
            if (type == Long.class)    return type.cast(n.longValue());
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

    /** Raccourci pratique : récupère le claim "userId" en Long */
    public Long extractUserId(String token) {
        Number n = extractClaim(stripBearer(token), "userId", Number.class);
        return (n == null) ? null : n.longValue();
    }

    /** Validation simple (signature OK + non expiré + sub présent) */
    public boolean isTokenValid(String token) {
        try {
            final String raw = stripBearer(token);
            final String username = extractUsername(raw);
            return username != null && !isTokenExpired(raw);
        } catch (Exception e) {
            return false;
        }
    }

    public long getJwtExpirationMillis() {
        return jwtExpirationMillis;
    }

    /* ===================== PUBLIC: CREATION ===================== */

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
                .claim("userId", user.getId())              // id du compte Utilisateur
                .setIssuer("ProjetGestionAbsences")
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /* ===================== PRIVÉ: UTILS ===================== */

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(stripBearer(token), Claims::getExpiration);
    }

    /** Extracteur via fonction (jjwt) */
    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(stripBearer(token));
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
        // Tente d'abord un décodage base64, sinon utilise la chaîne brute (UTF-8)
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKeyBase64);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException ex) {
            byte[] keyBytes = secretKeyBase64.getBytes(StandardCharsets.UTF_8);
            return Keys.hmacShaKeyFor(keyBytes);
        }
    }

    /** Retire "Bearer " si présent (utile quand on passe directement l’en-tête Authorization) */
    private String stripBearer(String token) {
        if (token == null) return null;
        String t = token.trim();
        if (t.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return t.substring(7).trim();
        }
        return t;
    }
}
