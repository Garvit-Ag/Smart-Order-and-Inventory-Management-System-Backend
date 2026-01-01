package com.oims.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer billId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @Column(nullable = false)
    private LocalDate billingDate;
    
    @Column(nullable = false)
    private LocalTime billingTime;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    
    public enum PaymentMode {
        UPI,
        CREDIT_CARD,
        DEBIT_CARD,
        NET_BANKING,
        CASH
    }
    
    public enum PaymentStatus{
    	COMPLETED,
    	PENDING
    }
}
