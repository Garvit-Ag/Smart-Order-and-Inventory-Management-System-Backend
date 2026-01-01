package com.oims.dto;

import java.util.List;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderDto {
    @NotBlank
    private String address;

    @Positive
    @NotNull
    private Double amount;
    
    @NotEmpty
    private List<OrderItemDto> items;
    
    @NotNull(message = "payment mode is required")
    private PaymentMode paymentMode;
}
