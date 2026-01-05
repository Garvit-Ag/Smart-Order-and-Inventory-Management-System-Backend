package com.oims.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oims.Model.User;
import com.oims.dto.LoginRequest;
import com.oims.dto.LoginResponse;
import com.oims.dto.UserDTO;
import com.oims.dto.UserProfileResponse;
import com.oims.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
	
	private final UserService userservice;
	
	@PostMapping("/register")
	ResponseEntity<String> addUser(@RequestBody @Valid UserDTO user){
		return  userservice.addUser(user);
	}
	
	@PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        return userservice.login(request);
    }

	@GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@RequestHeader("X-User-Email") String email) {
        return ResponseEntity.ok(userservice.getProfile(email));
    }
	
	@GetMapping("/users")
	ResponseEntity<List<User>> getAllUsers(){
		return userservice.getAllUsers();
	}
	
	@DeleteMapping("/delete")
	ResponseEntity<String> deleteUser(@RequestHeader("X-User-Id") String id){
		return userservice.deleteUser(id);
	}
}
