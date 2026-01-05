package com.oims;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.oims.Model.User;
import com.oims.dto.LoginRequest;
import com.oims.dto.UserDTO;
import com.oims.repository.UserRepository;
import com.oims.service.JwtService;
import com.oims.service.UserService;
import com.oims.feign.NotificationInterface;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationInterface notificationInterface;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void addUser_success() {

        UserDTO dto = new UserDTO();
        dto.setName("John");
        dto.setEmail("test@example.com");
        dto.setPassword("pass123");
        dto.setNumber("9999999999");

        when(userRepository.existsByEmail(dto.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(dto.getPassword()))
                .thenReturn("encodedPassword");

        ResponseEntity<String> response = userService.addUser(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Registration Success", response.getBody());

        verify(notificationInterface)
                .sendWelcomeMail(dto.getEmail(), dto.getName());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void login_success() {

        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("pass123");

        User user = User.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .build();

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        when(passwordEncoder.matches(request.getPassword(), "encodedPassword"))
                .thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("jwt-token");

        ResponseEntity<String> response = userService.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwt-token", response.getBody());
    }


    @Test
    void getAllUsers_success() {

        when(userRepository.findAll())
                .thenReturn(List.of(new User()));

        ResponseEntity<List<User>> response =
                userService.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void deleteUser_success() {

        when(userRepository.existsById(1))
                .thenReturn(true);

        ResponseEntity<String> response =
                userService.deleteUser("1");

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("User Removed ", response.getBody());

        verify(userRepository).deleteById(1);
    }


    @Test
    void getProfile_success() {

        User user = User.builder()
                .userId(1)
                .email("test@example.com")
                .name("John Doe")
                .role(User.Role.CUSTOMER)
                .build();

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        var response =
                userService.getProfile("test@example.com");

        assertEquals("test@example.com", response.getEmail());
        assertEquals("John Doe", response.getFullName());
        assertEquals(User.Role.CUSTOMER, response.getRole());
    }
}
