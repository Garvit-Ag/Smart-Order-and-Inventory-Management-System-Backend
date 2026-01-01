package com.oims.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BillRequest {
	 	@Positive(message = "Amount must be greater than zero")
	    private Double amount;
	    
	    @NotNull(message = "Payment mode is required")
	    private PaymentMode paymentMode;
}
