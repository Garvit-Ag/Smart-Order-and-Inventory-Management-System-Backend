package com.oims.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oims.Model.User;
import com.oims.dto.UserDTO;
import com.oims.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userservice;
	
	@PostMapping("/register")
	ResponseEntity<String> addUser(@RequestBody @Valid UserDTO user){
		return  userservice.addUser(user);
	}
	
	@GetMapping("/users")
	ResponseEntity<List<User>> getAllUsers(){
		return userservice.getAllUsers();
	}
	
	@DeleteMapping("/delete/{id}")
	ResponseEntity<String> deleteUser(@PathVariable Integer id){
		return userservice.deleteUser(id);
	}
	@PutMapping("/user/password/{id}")
	ResponseEntity<String> updatePassword(@PathVariable Integer id, @RequestParam String oldPassword, @RequestParam String newPassword){
		return userservice.updatePassword(id, oldPassword, newPassword);
	}
}
