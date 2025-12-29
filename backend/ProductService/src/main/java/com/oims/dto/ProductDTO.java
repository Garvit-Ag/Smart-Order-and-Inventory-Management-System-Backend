package com.oims.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductDTO {

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotNull
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull
    @PositiveOrZero(message = "Stock cannot be negative")
    private Integer stock;
}
