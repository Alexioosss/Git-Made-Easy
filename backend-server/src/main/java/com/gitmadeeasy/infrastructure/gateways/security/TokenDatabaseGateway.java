package com.gitmadeeasy.infrastructure.gateways.security;

import com.gitmadeeasy.entities.security.TokenGateway;
import com.gitmadeeasy.entities.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TokenDatabaseGateway implements TokenGateway {
    @Value("${jwt.secret}") private String secret;
    private Key key;
    private static final Set<String> tokensBlacklist = new HashSet<>();

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(User user) {
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(user.getUserId())
                .claim("email", user.getEmailAddress())
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
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Refresh the token for another 1 hour
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
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}