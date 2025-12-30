package com.oims.service;

import java.time.LocalDate;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.oims.dto.OrderDto;
import com.oims.dto.OrderResponse;
import com.oims.model.OrderTable;
import com.oims.model.OrderTable.OrderStatus;
import com.oims.model.OrderItem;
import com.oims.repository.OrderItemRepository;
import com.oims.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	
	@Transactional
	public ResponseEntity<String> addOrder(OrderDto orderDto) {
		OrderTable order = OrderTable.builder()
                .userId(orderDto.getUserId())
                .address(orderDto.getAddress())
                .status(OrderStatus.INITIATED)
                .amount(orderDto.getAmount())
                .orderDate(LocalDate.now())
                .build();

		OrderTable savedOrder = orderRepository.save(order);

        List<OrderItem> items = orderDto.getItems()
                .stream()
                .map(i -> OrderItem.builder()
                		
                        .orderId(savedOrder.getOrderId())
                        .productId(i.getProductId())
                        .quantity(i.getQuantity())
                        .priceAtPurchase(i.getPrice())
                        .build())
                .toList();
		
        orderItemRepository.saveAll(items);

        
        return new ResponseEntity<>("Order Added", HttpStatus.ACCEPTED);
	}

	public ResponseEntity<List<OrderItem>> getOrder(Integer id) {
        List<OrderItem> items = orderItemRepository.findByOrderId(id);

        if(items.isEmpty()) {
        	throw new RuntimeException("Order Doesn't Exist");
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
		
	}

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(o -> mapToResponse(o, orderItemRepository.findByOrderId(o.getOrderId())))
                .toList();
    }

    private OrderResponse mapToResponse(OrderTable order, List<OrderItem> items) {
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .orderStatus(order.getStatus())
                .address(order.getAddress())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate())
                .items(items)
                .build();
    }

}
