package com.gitmadeeasy.integration.controllers;

import com.gitmadeeasy.infrastructure.gateways.users.repositories.jpa.JpaUserRepository;
import com.gitmadeeasy.testConfig.FirebaseTestConfig;
import com.gitmadeeasy.testUtil.JsonUtil;
import com.gitmadeeasy.usecases.users.dto.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Import(FirebaseTestConfig.class)
class UserControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private JpaUserRepository userRepository;

    @BeforeEach
    public void setUp() { this.userRepository.deleteAll(); }


    @Test
    @DisplayName("Create User - Valid Request")
    void createUser_WhenValidRequestData_ReturnsCreatedUser() throws Exception {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("John", "Doe",
                "myemail1@gmail.com", "MyPassword123'");

        // Act & Assert
        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.emailAddress").value("myemail1@gmail.com"));
    }

    @Test
    @DisplayName("Create User - Duplicate Email")
    void createUser_WhenEmailAlreadyExists_ReturnsConflict() throws Exception {
        // Arrange
        createUserAndReturnUserId();
        CreateUserRequest request = new CreateUserRequest(
                "John", "Doe", "myemail1@gmail.com", "MyPassword123'");

        // Act & Assert
        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(request)))
                .andExpect(status().isConflict());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideInvalidUserRequest")
    @DisplayName("Create User - Missing Required Fields")
    void createUser_WhenMissingRequiredFields_ReturnsBadRequest(String displayName, CreateUserRequest request) throws Exception {
        // Act & Assert
        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get User By ID - User Exists")
    void getUserById_WhenUserExists_ReturnsUser() throws Exception {
        // Arrange
        String userId = createUserAndReturnUserId();

        // Act & Assert
        this.mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.emailAddress").value("myemail1@gmail.com"));
    }

    @Test
    @DisplayName("Get User By ID - User Does Not Exist")
    void getUserById_WhenUserDoesNotExist_ReturnsNotFound() throws Exception {
        // Act & Assert
        this.mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get User By Email - User Exists")
    void getUserByEmailAddress_WhenUserExists_ReturnsUser() throws Exception {
        // Arrange
        createUserAndReturnUserId();

        // Act & Assert
        this.mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "emailAddress": "myemail1@gmail.com"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get User By Email - User Does Not Exist")
    void getUserByEmailAddress_WhenUserDoesNotExist_ReturnsNotFound() throws Exception {
        // Act & Assert
        this.mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "emailAddress": "myemail1@gmail.com"
                                }
                                """))
                .andExpect(status().isNotFound());
    }



    // ----- HELPER METHODS ----- //


    private String createUserAndReturnUserId() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "John", "Doe", "myemail1@gmail.com", "MyPassword123'");

        String response = this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.objectToJson(request)))
                .andReturn().getResponse().getContentAsString();
        return JsonUtil.readJson(response, "id");
    }

    private static Stream<Arguments> provideInvalidUserRequest() {
        return Stream.of(
                Arguments.of(
                "Missing First Name",
                        new CreateUserRequest("", "Doe", "myemail1@gmail.com", "MyPassword123'")),
                Arguments.of("Missing Last Name",
                        new CreateUserRequest("John", "", "myemail1@gmail.com", "MyPassword123'")),
                Arguments.of("Missing Email Address",
                        new CreateUserRequest("John", "Doe", "", "MyPassword123'")),
                Arguments.of("Missing Password",
                        new CreateUserRequest("John", "Doe", "myemail1@gmail.com", "")),
                Arguments.of("Invalid Email Format",
                        new CreateUserRequest("John", "Doe", "myemail1gmailcom", "MyPassword123'"))
        );
    }
}