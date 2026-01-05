package com.oims;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oims.controller.UserController;
import com.oims.dto.LoginRequest;
import com.oims.dto.UserDTO;
import com.oims.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser_success() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("Password123");
        userDTO.setName("garvit");
        userDTO.setNumber("1234567890");

        when(userService.addUser(any(UserDTO.class)))
                .thenReturn(new ResponseEntity<>("Registration Success",HttpStatus.CREATED));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Registration Success"));
    }
    
    @Test
    void registerUser_fail() throws Exception {

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("Password123");
        userDTO.setName("garvit");
        userDTO.setNumber("12345678901");

        when(userService.addUser(any(UserDTO.class)))
                .thenReturn(new ResponseEntity<>("Registration Success",HttpStatus.CREATED));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.number").value("Invalid phone number"));
    }

    @Test
    void loginUser_success() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("Password123");

        when(userService.login(any(LoginRequest.class)))
                .thenReturn(ResponseEntity.ok("Login success"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login success"));
    }

    @Test
    void getAllUsers_success() throws Exception {

        when(userService.getAllUsers())
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(get("/auth/users"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_success() throws Exception {

        when(userService.deleteUser(any()))
                .thenReturn(ResponseEntity.ok("User deleted"));

        mockMvc.perform(delete("/auth/delete")
                        .header("X-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted"));
    }
}
