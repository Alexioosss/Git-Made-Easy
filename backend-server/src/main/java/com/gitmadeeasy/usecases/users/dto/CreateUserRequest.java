package com.gitmadeeasy.usecases.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
        @NotBlank String firstName, @NotBlank String lastName,
        @Email @NotBlank String emailAddress,
        @NotBlank String password) {}