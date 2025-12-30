package com.oims.dto;

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

    @NotBlank
    private String billingDate;

    @NotBlank
    private String paymentMode;
}
