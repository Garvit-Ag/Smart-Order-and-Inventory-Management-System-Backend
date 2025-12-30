package com.oims.dto;

import java.time.LocalDate;
import java.util.List;

import com.oims.model.OrderItem;
import com.oims.model.OrderTable.OrderStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
	
    private Integer userId;

    private String address;

    private Double amount;
    
    private Integer orderId;
    
    private OrderStatus orderStatus;
    
    private LocalDate orderDate;
    
    private List<OrderItem> items;
}
