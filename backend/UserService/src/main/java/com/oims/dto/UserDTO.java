package com.oims.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
	@NotBlank
	private String name;
	
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	@Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
	private String number;
		
	@NotBlank(message = "Password is required")
	@Size(min = 5, max = 15, message = "Password must be between 5 and 15 characters")
	private String password;

}
