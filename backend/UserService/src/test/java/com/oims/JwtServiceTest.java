package com.oims;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.oims.Model.User;
import com.oims.service.JwtService;

class JwtServiceTest {

    @Test
    void generateToken_shouldReturnToken() {

        JwtService jwtService = new JwtService();

        // Manually inject values
        ReflectionTestUtils.setField(
                jwtService,
                "secret",
                "b8b050cd5293136386b0f2dc09d75ef01ea4a2ab21a392dbc9952aee25d6c5d3"
        );
        ReflectionTestUtils.setField(jwtService, "expirationMs", 3600000L);

        User user = User.builder()
                .userId(1)
                .email("test@example.com")
                .role(User.Role.CUSTOMER)
                .build();

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }
}
