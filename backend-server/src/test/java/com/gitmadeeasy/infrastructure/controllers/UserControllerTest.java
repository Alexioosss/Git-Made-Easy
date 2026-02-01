package com.gitmadeeasy.infrastructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.infrastructure.configs.SecurityConfiguration;
import com.gitmadeeasy.infrastructure.dto.users.UserResponse;
import com.gitmadeeasy.infrastructure.mappers.users.UserResponseMapper;
import com.gitmadeeasy.testUtil.JsonConverterUtil;
import com.gitmadeeasy.usecases.users.CreateUser;
import com.gitmadeeasy.usecases.users.CreateUserRequest;
import com.gitmadeeasy.usecases.users.GetUserByEmail;
import com.gitmadeeasy.usecases.users.GetUserById;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@Import({SecurityConfiguration.class, UserResponseMapper.class})
class UserControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private CreateUser createUser;
    @MockitoBean private GetUserById getUserById;
    @MockitoBean private GetUserByEmail getUserByEmail;
    @Autowired private UserResponseMapper userResponseMapper;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Create A User - Valid Request Data")
    void createUser_WhenValidData_ReturnsUserResponse() throws Exception {
        CreateUserRequest userRequest = new CreateUserRequest(
                "Alessio", "Cocuzza",
                "myemail1@gmail.com", "MyPassword123'"
        );

        User createdUser = new User("1", "Alessio",  "Cocuzza", "myemail1@gmail.com", "MyPassword123'");
        UserResponse expectedResponse = this.userResponseMapper.toUserResponse(createdUser);

        when(this.createUser.execute(userRequest)).thenReturn(createdUser);
        this.mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonConverterUtil.objectToJson(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(JsonConverterUtil.objectToJson(expectedResponse)));
    }
}