package com.oims.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.oims.Model.User;
import com.oims.dto.UserDTO;
import com.oims.exception.GlobalExceptionHandler;
import com.oims.repository.UserRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {

    private final GlobalExceptionHandler globalExceptionHandler;

	private final UserRepository userRepository;

	public ResponseEntity<String> addUser( UserDTO user) {
		
		if(userRepository.existsByEmail(user.getEmail())) {
			return new ResponseEntity<String>("Email Already Registered", HttpStatus.BAD_REQUEST);
		}
		
		User userObj = User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .number(user.getNumber())
                .role(User.Role.CUSTOMER)
                .build();

        userRepository.save(userObj);
        return new ResponseEntity<>("You Are Successfully Registered", HttpStatus.CREATED);
	}

	public ResponseEntity<List<User>> getAllUsers() {
		return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
	}

	public ResponseEntity<String> deleteUser(Integer id) {
		if(!userRepository.existsById(id)) {
			return new ResponseEntity<>("No user exist by that ID", HttpStatus.BAD_REQUEST);
		}
		userRepository.deleteById(id);
		return new ResponseEntity<>("User Removed ", HttpStatus.ACCEPTED);
	}

	public ResponseEntity<String> updatePassword(Integer id, String oldPassword, String newPassword) {
		if(!userRepository.existsById(id)) {
			return new ResponseEntity<>("No user exist by that ID", HttpStatus.BAD_REQUEST);
		}
		User user = userRepository.findById(id).get();
		if(!user.getPassword().equals(oldPassword)) {
			return new ResponseEntity<>("Wrong Old Password", HttpStatus.BAD_REQUEST);
		}
		user.setPassword(newPassword);
		userRepository.save(user);
		return new ResponseEntity<>("Password updated", HttpStatus.CREATED);
	}
}
