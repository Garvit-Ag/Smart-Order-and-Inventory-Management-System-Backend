package com.oims.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderItemDto {

    @NotNull
    private Integer productId;

    @Min(1)
    private Integer quantity;

    @NotNull
    private Double price;
}
