package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.infrastructure.gateways.security.JwtAuthenticationEntryPoint;
import com.gitmadeeasy.infrastructure.gateways.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfiguration {
    @Value("${app.cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter,
                                           JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                // Apply the custom filter, using JWT Tokens, to authenticate users
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
        .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/firestore-test").permitAll()
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/{userId}").authenticated()
                .requestMatchers(HttpMethod.GET, "/users").authenticated()
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/logout").authenticated()
                .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()
                .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()
                .requestMatchers(HttpMethod.GET, "/lessons").permitAll()
                .requestMatchers(HttpMethod.POST, "/lessons").authenticated()
                .requestMatchers(HttpMethod.GET, "/lessons/{lessonId}").permitAll()
                .requestMatchers(HttpMethod.GET, "/lessons/{lessonId}/next").permitAll()
                .requestMatchers(HttpMethod.POST, "/lessons/{lessonId}/tasks").authenticated()
                .requestMatchers(HttpMethod.GET, "/lessons/{lessonId}/tasks").permitAll()
                .requestMatchers(HttpMethod.GET, "/lessons/{lessonId}/tasks/{taskId}").permitAll()
                .requestMatchers(HttpMethod.POST, "/lessons/{lessonId}/tasks/{taskId}/progress").authenticated()
                .requestMatchers(HttpMethod.GET, "/lessons/{lessonId}/tasks/{taskId}/progress").authenticated()
                .requestMatchers(HttpMethod.POST, "/lessons/{lessonId}/tasks/progress/sync").authenticated()
                .requestMatchers(HttpMethod.GET, "/lessons/{lessonId}/progress").authenticated()
                .requestMatchers(HttpMethod.GET, "/tasks/progress").authenticated()
                .requestMatchers(HttpMethod.GET, "/dashboard").authenticated()
                .anyRequest().denyAll()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(allowedOrigins);
        configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Required setting for JWT Cookies or Authorization headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}