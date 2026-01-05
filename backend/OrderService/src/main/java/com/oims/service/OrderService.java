package com.oims.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.oims.dto.BillRequest;
import com.oims.dto.OrderDto;
import com.oims.dto.OrderItemDto;
import com.oims.exception.InsufficientStockException;
import com.oims.feign.NotificationInterface;
import com.oims.feign.ProductInterface;
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
	private final NotificationInterface notificationInterface;
	private final ProductInterface productInterface;
	
	@Transactional
	public ResponseEntity<String> addOrder(OrderDto orderDto, String email, String userId) {
		Map<Integer, Integer> stockMap = new HashMap<>();
		for (OrderItemDto item : orderDto.getItems()) {
            Integer availableStock = productInterface.getStock(item.getProductId());
            if (availableStock < item.getQuantity()) {
            	return new ResponseEntity<>("Insufficient Stock for "+productInterface.getProductName(item.getProductId()).getBody(), HttpStatus.BAD_REQUEST);
            }
            stockMap.put(item.getProductId(), availableStock);
		}
		
		for (OrderItemDto item : orderDto.getItems()) {
            productInterface.updateProduct(item.getProductId(), stockMap.get(item.getProductId())-item.getQuantity());           
		}
		
		BillRequest billRequest = BillRequest.builder()
				.amount(orderDto.getAmount())
				.paymentMode(orderDto.getPaymentMode())
				.build();
		
		Integer billId = notificationInterface.createBill(billRequest, email).getBody();
		
		OrderTable order = OrderTable.builder()
                .userId(Integer.valueOf(userId))
                .paymentId(billId)
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
        	return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
		
	}

    public List<OrderTable> getAllOrders() {
        return orderRepository.findAll();               
    }  
    
    public ResponseEntity<List<OrderTable>> getOrderByUserId(String userId) {
		List<OrderTable> listOrder = orderRepository.findAllByUserId(Integer.valueOf(userId));
		return new ResponseEntity<>(listOrder,HttpStatus.OK);
	}
}
