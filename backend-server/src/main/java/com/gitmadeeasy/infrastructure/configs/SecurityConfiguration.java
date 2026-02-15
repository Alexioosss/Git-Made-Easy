package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.infrastructure.gateways.security.JwtAuthenticationEntryPoint;
import com.gitmadeeasy.infrastructure.gateways.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter,
                                           JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.GET, "/email-test").permitAll()
            .requestMatchers(HttpMethod.GET, "/firestore-test").permitAll()
            .requestMatchers(HttpMethod.POST, "/users").permitAll()
            .requestMatchers(HttpMethod.GET, "/users/{userId}").authenticated()
            .requestMatchers(HttpMethod.GET, "/users").authenticated()
            .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/auth/logout").authenticated()
            .requestMatchers(HttpMethod.POST, "/auth/refresh").authenticated()
            .requestMatchers(HttpMethod.POST, "/lessons").authenticated()
            .requestMatchers(HttpMethod.GET, "/lessons/{lessonId}").permitAll()
            .requestMatchers(HttpMethod.POST, "/lessons/{lessonId}/tasks").authenticated()
            .requestMatchers(HttpMethod.GET, "/lessons/{lessonId}/tasks/{taskId}").permitAll()
            .requestMatchers(HttpMethod.POST, "/lessons/{lessonId}/tasks/{taskId}/progress").authenticated()
            .requestMatchers(HttpMethod.GET, "/lessons/{lessonId}/tasks/{taskId}/progress").authenticated()
            .requestMatchers(HttpMethod.GET, "/lessons/{lessonId}/progress").authenticated()
            .anyRequest().denyAll()
        )
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}