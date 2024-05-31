package com.dblibproject.dblibproject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User("user1", "user1@example.com");
        User user2 = new User("user2", "user2@example.com");

        List<User> users = Arrays.asList(user1, user2);

        given(userRepository.findAll()).willReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is(user1.getUsername())))
                .andExpect(jsonPath("$[1].username", is(user2.getUsername())));
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User("user1", "user1@example.com");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User("user1", "user1@example.com");
        given(userRepository.save(user)).willReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"user1\", \"email\": \"user1@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User existingUser = new User("user1", "user1@example.com");
        given(userRepository.findById(1L)).willReturn(Optional.of(existingUser));

        User updatedUser = new User("updatedUser", "updated@example.com");
        given(userRepository.save(existingUser)).willReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"updatedUser\", \"email\": \"updated@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(updatedUser.getUsername())))
                .andExpect(jsonPath("$.email", is(updatedUser.getEmail())));
    }

    @Test
    public void testUpdateUserNotFound() throws Exception {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"updatedUser\", \"email\": \"updated@example.com\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUser() throws Exception {
        given(userRepository.existsById(1L)).willReturn(true);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        given(userRepository.existsById(1L)).willReturn(false);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound());
    }
}
