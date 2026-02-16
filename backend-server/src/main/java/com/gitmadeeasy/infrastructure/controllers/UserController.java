package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.infrastructure.dto.users.UserResponse;
import com.gitmadeeasy.infrastructure.mappers.users.UserResponseMapper;
import com.gitmadeeasy.usecases.users.CreateUser;
import com.gitmadeeasy.usecases.users.GetUserByEmail;
import com.gitmadeeasy.usecases.users.GetUserById;
import com.gitmadeeasy.usecases.users.dto.CreateUserRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController @RequestMapping("/users")
public class UserController {
    private final CreateUser createUserUseCase;
    private final GetUserById getUserById;
    private final GetUserByEmail getUserByEmail;
    private final UserResponseMapper userResponseMapper;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    public UserController(CreateUser createUserUseCase, GetUserById getUserById, GetUserByEmail getUserByEmail,
                          UserResponseMapper userResponseMapper) {
        this.createUserUseCase = createUserUseCase;
        this.getUserById = getUserById;
        this.getUserByEmail = getUserByEmail;
        this.userResponseMapper = userResponseMapper;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("POST /users - Creating a new user with email address= {}", request.emailAddress());
        CreateUserRequest newUserRequest = new CreateUserRequest(
                request.firstName(), request.lastName(),
                request.emailAddress(), request.password()
        );
        User createdUser = this.createUserUseCase.execute(newUserRequest);
        log.info("User created successfully. UserID= {}", createdUser.getUserId());

        UserResponse userResponse = this.userResponseMapper.toUserResponse(createdUser);
        return ResponseEntity.created(URI.create("/users/" + userResponse.id())).body(userResponse);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("userId") String userId) {
        log.info("GET /users/{} - Fetching user by their ID", userId);
        User foundUser = this.getUserById.execute(userId);
        log.info("User found successfully. UserID= {}", foundUser.getUserId());
        UserResponse userResponse = this.userResponseMapper.toUserResponse(foundUser);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUserByEmailAddress(@RequestParam("emailAddress") String emailAddress) {
        log.info("GET /users/{} - Fetching user by their email address", emailAddress);
        User foundUser = this.getUserByEmail.execute(emailAddress);
        log.info("User found successfully. UserID= {}, email address= {}", foundUser.getUserId(), foundUser.getEmailAddress());
        UserResponse userResponse = this.userResponseMapper.toUserResponse(foundUser);
        return ResponseEntity.ok(userResponse);
    }
}