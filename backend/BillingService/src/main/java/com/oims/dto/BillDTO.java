package com.oims.dto;


import com.oims.model.Bill.PaymentMode;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BillDTO {

    @NotNull(message = "Order ID is required")
    private Integer orderId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private Double amount;
    
    @NotNull(message = "Payment mode is required")
    private PaymentMode paymentMode;
}