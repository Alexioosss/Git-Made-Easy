package com.gitmadeeasy.unit.infrastructure.controllers;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.infrastructure.controllers.UserController;
import com.gitmadeeasy.infrastructure.dto.users.UserResponse;
import com.gitmadeeasy.infrastructure.mappers.users.UserResponseMapper;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.users.CreateUser;
import com.gitmadeeasy.usecases.users.GetUserByEmail;
import com.gitmadeeasy.usecases.users.GetUserById;
import com.gitmadeeasy.usecases.users.dto.CreateUserRequest;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithEmailException;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UserResponseMapper.class)
class UserControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private UserResponseMapper userResponseMapper;

    @MockitoBean private CreateUser createUser;
    @MockitoBean private GetUserById getUserById;
    @MockitoBean private GetUserByEmail getUserByEmail;


    @Test
    @DisplayName("Create User - Valid Payload Returns Expected Successful Response")
    void createUser_WhenValidPayload_ReturnsUserResponse() throws Exception {
        CreateUserRequest validRequest = new CreateUserRequest(
                "Alessio", "Cocuzza",
                "myemail1@gmail.com", "MyPassword123'"
        );

        User createdUser = new User("1", "Alessio",  "Cocuzza", "myemail1@gmail.com", "HashedMyPassword123'");
        UserResponse expectedResponse = this.userResponseMapper.toUserResponse(createdUser);

        when(this.createUser.execute(validRequest)).thenReturn(createdUser);
        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(JsonUtil.objectToJson(expectedResponse)));
    }

    @Test
    @DisplayName("Create User - Invalid Payload Returns 400")
    void createUser_WhenInvalidPayload_ReturnsBadRequest() throws Exception {
        // Arrange
        CreateUserRequest invalidRequest = new CreateUserRequest("Alessio", "Cocuzza", "", "");

        // Act & Assert
        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get User By ID - User Exists - Returns Successful Response / 200")
    void getUserById_WhenUserExists_ReturnsUserResponse() throws Exception {
        // Arrange
        User foundUser = new User("1", "Alessio", "Cocuzza", "myemail1@gmail.com", "HashedMyPassword123'");
        UserResponse expectedResponse = this.userResponseMapper.toUserResponse(foundUser);
        when(this.getUserById.execute("1")).thenReturn(foundUser);

        // Act & Assert
        this.mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(expectedResponse)));
    }

    @Test
    @DisplayName("Get User By ID - User Does Not Exist - Returns Unsuccessful Response / 404")
    void getUserById_WhenUserDoesNotExist_ReturnsNotFound() throws Exception {
        // Arrange
        when(this.getUserById.execute("1")).thenThrow(new UserNotFoundWithIdException("1"));

        // Act & Assert
        this.mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test @DisplayName("Get User By Email - User Exists - Returns Successful Response / 200")
    void getUserByEmail_WhenUserExists_ReturnsUserResponse() throws Exception {
        // Arrange
        User foundUser = new User("1", "Alessio", "Cocuzza", "myemail@gmail.com", "HashedMyPassword123'");
        UserResponse expectedResponse = userResponseMapper.toUserResponse(foundUser);
        when(getUserByEmail.execute("myemail@gmail.com")).thenReturn(foundUser);

        // Act & Assert
        mockMvc.perform(get("/users").param("emailAddress", "myemail@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtil.objectToJson(expectedResponse)));
    }

    @Test
    @DisplayName("Get User By Email - User Not Found - Returns Unsuccessful Response / 404")
    void getUserByEmail_WhenUserNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        when(getUserByEmail.execute("missing@gmail.com")).thenThrow(new UserNotFoundWithEmailException("missing@gmail.com"));

        // Act & Assert
        mockMvc.perform(get("/users").param("emailAddress", "missing@gmail.com"))
                .andExpect(status().isNotFound());
    }
}