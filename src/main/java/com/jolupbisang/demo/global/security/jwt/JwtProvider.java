package com.jolupbisang.demo.global.security.jwt;

import com.jolupbisang.demo.global.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private final SecretKey key;

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String generateAccessToken(Long userId, String email, String nickname) {
        return createToken(createClaims(userId, nickname), email, jwtProperties.getAccessExpiration());
    }

    public String generateRefreshToken(Long userId, String email, String nickname) {
        return createToken(createClaims(userId, nickname), email, jwtProperties.getRefreshExpiration());
    }

    public boolean isExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    public Long getUserId(String token) {
        return parseToken(token).get("userId", Long.class);
    }

    public String getEmail(String token) {
        return parseToken(token).getSubject();
    }

    public String getNickname(String token) {
        return parseToken(token).get("nickname", String.class);
    }

    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .subject(subject)
                .claims(claims)
                .issuer(jwtProperties.getIssuer())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    private Map<String, Object> createClaims(Long userId, String nickname) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("nickname", nickname);
        return claims;
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
