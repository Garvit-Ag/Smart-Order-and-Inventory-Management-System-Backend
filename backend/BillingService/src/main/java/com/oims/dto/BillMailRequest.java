package com.oims.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BillMailRequest {
	
	@NotBlank
    private String email;

    @NotNull
    private Integer billId;

    @NotNull
    private Double amount;

    @NotNull
    private LocalDate billingDate;
}
