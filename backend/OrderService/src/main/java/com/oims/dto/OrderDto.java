package com.oims.dto;

import java.util.List;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderDto {

    @NotNull
    private Integer userId;

    @NotBlank
    private String address;

    @Positive
    @NotNull
    private Double amount;
    
    @NotEmpty
    private List<OrderItemDto> items;
}
