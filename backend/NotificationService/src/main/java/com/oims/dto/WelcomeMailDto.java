package com.oims.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WelcomeMailDto {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;
}
