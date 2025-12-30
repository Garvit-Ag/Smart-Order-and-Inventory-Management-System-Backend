package com.oims.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @Column(nullable = false)
    private Integer userId;

    private Integer paymentId;
    
    private Double amount;
    
    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDate orderDate;

    public enum OrderStatus {
        INITIATED,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }

}
