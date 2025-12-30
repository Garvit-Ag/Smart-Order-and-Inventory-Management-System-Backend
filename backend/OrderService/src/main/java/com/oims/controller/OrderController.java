package com.oims.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oims.dto.OrderDto;
import com.oims.dto.OrderResponse;
import com.oims.model.OrderItem;
import com.oims.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;
	
	@PostMapping
	public ResponseEntity<String> addOrder(@Valid @RequestBody OrderDto orderDto){
		return orderService.addOrder(orderDto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<List<OrderItem>> getOrder(@PathVariable Integer id){
		return orderService.getOrder(id);
	}
	
	@GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
	
}
