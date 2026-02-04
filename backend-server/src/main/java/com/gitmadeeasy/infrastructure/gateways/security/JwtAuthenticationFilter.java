package com.gitmadeeasy.infrastructure.gateways.security;

import com.gitmadeeasy.entities.security.TokenGateway;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenGateway tokenGateway;

    public JwtAuthenticationFilter(TokenGateway tokenGateway) {
        this.tokenGateway = tokenGateway;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authenticationHeader = request.getHeader("Authorization");

        if(authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
            String token = authenticationHeader.substring(7);
            request.setAttribute("jwt", token);

            try {
                if(this.tokenGateway.isTokenValid(token)) {
                    String userId = this.tokenGateway.getUserIdFromToken(token);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch(JwtException | IllegalArgumentException e) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}