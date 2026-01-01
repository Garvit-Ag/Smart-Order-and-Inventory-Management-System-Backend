package com.oims.service;

import java.util.Date;


import org.springframework.stereotype.Service;

import com.oims.Model.User;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("b8b050cd5293136386b0f2dc09d75ef01ea4a2ab21a392dbc9952aee25d6c5d3")
    private String secret;

    @Value("${jwt.expiration-ms:360000000}")
    private long expirationMs;

    public String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", String.valueOf(user.getUserId()))
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
