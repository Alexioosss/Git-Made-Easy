package com.gitmadeeasy.infrastructure.gateways.security;

import com.gitmadeeasy.entities.security.TokenGateway;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenGateway tokenGateway;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(TokenGateway tokenGateway) {
        this.tokenGateway = tokenGateway;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if(token != null) {
            request.setAttribute("jwt", token);
            try {
                if(this.tokenGateway.isTokenValid(token)) {
                    String userId = this.tokenGateway.getUserIdFromToken(token);
                    UserPrincipal principal = new UserPrincipal(userId);

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    principal, null, principal.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch(JwtException | IllegalArgumentException e) {
                SecurityContextHolder.clearContext();
                throw new BadCredentialsException("Invalid or expired JWT Token");
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) { log.info("DEBUGGING - No cookies received on request to {}", request.getRequestURI()); }

        // Try retrieving the JWT Token via the Authorization header first
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        // Try retrieving the JWT Token via the cookies
        if(request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // Token was not passed along with the request
        return null;
    }
}