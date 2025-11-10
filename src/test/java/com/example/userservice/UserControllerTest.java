package com.example.userservice;

import com.example.userservice.controller.UserController;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.exeption.GlobalExeptionHandler;
import com.example.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UserControllerTest {

    private MockMvc mockMvc;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService))
                .setControllerAdvice(new GlobalExeptionHandler())
                .build();
    }

    @Test
    void createUser_ShouldReturnUserWithAge() throws Exception {
        Mockito.when(userService.createUser(any(UserDTO.class)))
                .thenReturn(new UserDTO(1L, "John", "john@example.com", 25));

        String jsonRequest = """
            {
                "name": "John",
                "email": "john@example.com",
                "age": 25
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void createUser_ShouldReturnValidationError_WhenAgeMissing() throws Exception {
        String jsonRequest = """
            {
                "name": "John",
                "email": "john@example.com"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.age").value("Не должно быть пустым"));
    }

    @Test
    void createUser_ShouldReturnValidationError_WhenNameBlank() throws Exception {
        String jsonRequest = """
            {
                "name": "",
                "email": "john@example.com",
                "age": 25
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Не должно быть пустым"));
    }

    @Test
    void getUser_ShouldReturnUser() throws Exception {
        Mockito.when(userService.getUserById(1L))
                .thenReturn(new UserDTO(1L, "Alice", "alice@example.com", 30));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.age").value(30));
    }

    @Test
    void getUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        Mockito.when(userService.getUserById(99L))
                .thenThrow(new RuntimeException("Пользователь не найден"));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.ошибка").value("Пользователь не найден"));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        Mockito.when(userService.updateUser(eq(1L), any(UserDTO.class)))
                .thenReturn(new UserDTO(1L, "Bob", "bob@example.com", 40));

        String jsonRequest = """
            {
                "name": "Bob",
                "email": "bob@example.com",
                "age": 40
            }
            """;

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bob"))
                .andExpect(jsonPath("$.email").value("bob@example.com"))
                .andExpect(jsonPath("$.age").value(40));
    }

    @Test
    void updateUser_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        Mockito.when(userService.updateUser(eq(99L), any(UserDTO.class)))
                .thenThrow(new RuntimeException("Пользователь не найден"));

        String jsonRequest = """
            {
                "name": "Charlie",
                "email": "charlie@example.com",
                "age": 28
            }
            """;

        mockMvc.perform(put("/api/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.ошибка").value("Пользователь не найден"));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_ShouldReturnInternalServerError_WhenRuntimeException() throws Exception {
        Mockito.doThrow(new RuntimeException("Ошибка удаления")).when(userService).deleteUser(2L);

        mockMvc.perform(delete("/api/users/2"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.ошибка").value("Ошибка удаления"));
    }

    @Test
    void getAllUsers_ShouldReturnList() throws Exception {
        Mockito.when(userService.getAllUsers())
                .thenReturn(List.of(
                        new UserDTO(1L, "Alice", "alice@example.com", 30),
                        new UserDTO(2L, "Bob", "bob@example.com", 40)
                ));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].name").value("Bob"));
    }
}
