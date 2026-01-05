package com.oims.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oims.Model.User;
import com.oims.dto.LoginRequest;
import com.oims.dto.LoginResponse;
import com.oims.dto.UserDTO;
import com.oims.dto.UserProfileResponse;
import com.oims.feign.NotificationInterface;
import com.oims.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;

	private final NotificationInterface notificationInterface;
	
	private final PasswordEncoder passwordEncoder;
	
	private final JwtService jwtService;
	
	public ResponseEntity<String> addUser( UserDTO user) {
		
		if(userRepository.existsByEmail(user.getEmail())) {
			return new ResponseEntity<>("Email Already Registered", HttpStatus.BAD_REQUEST);
		}
		
		User userObj = User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .number(user.getNumber())
                .role(User.Role.CUSTOMER)
                .build();
		
		notificationInterface.sendWelcomeMail(user.getEmail(), user.getName());
		
        userRepository.save(userObj);
               
        return new ResponseEntity<>("Resgistered Success", HttpStatus.CREATED);
	}

	public ResponseEntity<List<User>> getAllUsers() {
		return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
	}

	public ResponseEntity<String> deleteUser(String id) {
		if(!userRepository.existsById(Integer.valueOf(id))) {
			return new ResponseEntity<>("No user exist by that ID", HttpStatus.BAD_REQUEST);
		}
		userRepository.deleteById(Integer.valueOf(id));
		return new ResponseEntity<>("User Removed ", HttpStatus.NO_CONTENT);
	}

	public ResponseEntity<String> login(@Valid LoginRequest request) {
		Optional<User> user = userRepository.findByEmail(request.getEmail());
        // Verify password
        if (!userRepository.existsByEmail(request.getEmail()) || !passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            return new ResponseEntity<String>("Wrong Email or Password", HttpStatus.BAD_REQUEST);
        }

        String token = jwtService.generateToken(user.get());
        return new ResponseEntity<>(token, HttpStatus.OK);
	}
	
	public UserProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return UserProfileResponse.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .role(user.getRole())
                .fullName(user.getName())
                .build();
    }
}
