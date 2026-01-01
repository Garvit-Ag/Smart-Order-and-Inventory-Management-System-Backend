package com.oims.dto;


import com.oims.model.Bill.PaymentMode;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BillDTO {

    @Positive(message = "Amount must be greater than zero")
    private Double amount;
    
    @NotNull(message = "Payment mode is required")
    private PaymentMode paymentMode;
}