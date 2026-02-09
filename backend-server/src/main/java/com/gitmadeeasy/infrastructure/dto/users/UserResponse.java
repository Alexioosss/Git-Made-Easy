package com.gitmadeeasy.infrastructure.dto.users;

public record UserResponse(
    String id, String firstName,
    String lastName, String emailAddress) {}