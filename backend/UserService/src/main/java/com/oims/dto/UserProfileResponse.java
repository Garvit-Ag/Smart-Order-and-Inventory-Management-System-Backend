package com.oims.dto;

import com.oims.Model.User.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Integer id;
    private String email;
    private Role role;
    private String fullName; // For future use, can be null for now
}