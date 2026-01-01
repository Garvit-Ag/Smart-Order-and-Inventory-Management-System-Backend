package com.oims.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BillMailDto {

    @Email
    @NotBlank
    private String email;

    @NotNull
    private Integer billId;

    @NotNull
    private Double amount;

    @NotNull
    private LocalDate billingDate;
}
