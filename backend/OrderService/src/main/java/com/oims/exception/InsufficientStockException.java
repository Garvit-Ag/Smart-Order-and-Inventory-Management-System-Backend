package com.oims.exception;

import lombok.Data;

@Data
public class InsufficientStockException extends RuntimeException {
    private Integer productId;
    private Integer available;
    private Integer requested;
    
    public InsufficientStockException(Integer productId, Integer requested, Integer available) {
        super(String.format("Product ID %d: Requested quantity %d exceeds available stock %d", 
              productId, requested, available));
        this.productId = productId;
        this.requested = requested;
        this.available = available;
    }
}