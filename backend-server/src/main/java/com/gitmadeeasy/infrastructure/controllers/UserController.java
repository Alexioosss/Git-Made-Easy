package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.infrastructure.dto.users.UserRequest;
import com.gitmadeeasy.infrastructure.dto.users.UserResponse;
import com.gitmadeeasy.infrastructure.mappers.users.UserResponseMapperImplementation;
import com.gitmadeeasy.usecases.users.CreateUserUseCase;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final UserResponseMapperImplementation userResponseMapper;

    public UserController(CreateUserUseCase createUserUseCase, UserResponseMapperImplementation userResponseMapper) {
        this.createUserUseCase = createUserUseCase;
        this.userResponseMapper = userResponseMapper;
    }

    @PostMapping("")
    ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        User createdUser = this.createUserUseCase.createNewUser(request);
        UserResponse userResponse = this.userResponseMapper.toUserResponse(createdUser);
        return ResponseEntity.created(URI.create("/users/" + userResponse.id())).body(userResponse);
    }

    @GetMapping("/{userId}")
    Map<String, Object> getUserById(@PathVariable("userId") String userId) {
        return Map.of();
    }

    @GetMapping("")
    Map<String, Object> getUserByEmailAddress(@RequestParam("email") String email) {
        return Map.of();
    }

    @DeleteMapping("/{userId}")
    Map<String, Object> deleteUser(@PathVariable("userId") String userId) {
        return Map.of();
    }
}