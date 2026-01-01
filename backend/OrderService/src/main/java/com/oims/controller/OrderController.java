package com.oims.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oims.dto.OrderDto;
import com.oims.dto.OrderResponse;
import com.oims.dto.PaymentMode;
import com.oims.model.OrderItem;
import com.oims.model.OrderTable;
import com.oims.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;
	
	@PostMapping
	public ResponseEntity<String> addOrder(@Valid @RequestBody OrderDto orderDto, @RequestHeader("X-User-Email") String email, @RequestHeader("X-User-Id") String userId){
		return orderService.addOrder(orderDto, email, userId);
	}
	
	@GetMapping
	public ResponseEntity<List<OrderTable>> getOrderByUserId(@RequestHeader("X-User-Id") String userId){
		return orderService.getOrderByUserId(userId);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<List<OrderItem>> getOrder(@PathVariable Integer id){
		return orderService.getOrder(id);
	}
	
	@GetMapping("/get")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
	
}
