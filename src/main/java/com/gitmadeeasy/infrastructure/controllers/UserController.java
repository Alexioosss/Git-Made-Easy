package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.infrastructure.dto.users.UserRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("")
    Map<String, Object> createUser(UserRequest request) {
        return Map.of();
    }

    @GetMapping("/{userId}")
    Map<String, Object> getUserById(@PathVariable("userId") Long userId) {
        return Map.of();
    }

    @GetMapping("")
    Map<String, Object> getUserByEmailAddress(@RequestParam("email") String email) {
        return Map.of();
    }

    @DeleteMapping("/{userId}")
    Map<String, Object> deleteUser(@PathVariable("userId") Long userId) {
        return Map.of();
    }
}