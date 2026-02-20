package com.gitmadeeasy.infrastructure.gateways.security;

import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.entities.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TokenDatabaseGateway implements TokenGateway {
    private final String secret;
    private static Key key;
    private static final Set<String> tokensBlacklist = new HashSet<>();

    public TokenDatabaseGateway(String secret) { this.secret = secret; }

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(User user) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(user.getUserId())
                .claim("email", user.getEmailAddress())
                .claim("firebaseUid", user.getFirebaseUid())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Token is valid for 1 Hour
                .signWith(key)
                .compact();
    }

    @Override
    public void invalidateToken(String token) {
        tokensBlacklist.add(token);
    }

    @Override
    public String refreshToken(User user) {
        return  Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(user.getUserId())
                .claim("email", user.getEmailAddress())
                .claim("firebaseUid", user.getFirebaseUid())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            if(tokensBlacklist.contains(token)) { return false; }
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    @Override
    public String getUserIdFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch(ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }
}