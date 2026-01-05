package com.oims;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.oims.dto.BillRequest;
import com.oims.dto.OrderDto;
import com.oims.dto.OrderItemDto;
import com.oims.feign.NotificationInterface;
import com.oims.feign.ProductInterface;
import com.oims.model.OrderItem;
import com.oims.model.OrderTable;
import com.oims.model.OrderTable.OrderStatus;
import com.oims.repository.OrderItemRepository;
import com.oims.repository.OrderRepository;
import com.oims.service.OrderService;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private NotificationInterface notificationInterface;

    @Mock
    private ProductInterface productInterface;

    @InjectMocks
    private OrderService orderService;

    // ================= ADD ORDER (SUCCESS) =================

    @Test
    void addOrder_success() {

        OrderItemDto item = new OrderItemDto();
        item.setProductId(1);
        item.setQuantity(2);
        item.setPrice(500.0);

        OrderDto dto = new OrderDto();
        dto.setAddress("Hyderabad");
        dto.setAmount(1000.0);
        dto.setItems(List.of(item));
        dto.setPaymentMode(null);

        when(productInterface.getStock(1)).thenReturn(10);

        when(notificationInterface.createBill(any(BillRequest.class), anyString()))
                .thenReturn(ResponseEntity.ok(1));

        OrderTable savedOrder = OrderTable.builder()
                .orderId(1)
                .status(OrderStatus.INITIATED)
                .build();

        when(orderRepository.save(any(OrderTable.class)))
                .thenReturn(savedOrder);

        ResponseEntity<String> response =
                orderService.addOrder(dto, "test@example.com", "1");

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Order Added", response.getBody());

        verify(productInterface).updateProduct(1, 8);
        verify(orderItemRepository).saveAll(anyList());
    }


    // ================= ADD ORDER (INSUFFICIENT STOCK) =================

    @Test
    void addOrder_insufficientStock() {

        OrderItemDto item = new OrderItemDto();
        item.setProductId(1);
        item.setQuantity(10);

        OrderDto dto = new OrderDto();
        dto.setItems(List.of(item));

        when(productInterface.getStock(1)).thenReturn(5);
        when(productInterface.getProductName(1))
                .thenReturn(ResponseEntity.ok("Mouse"));

        ResponseEntity<String> response =
                orderService.addOrder(dto, "test@example.com", "1");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Insufficient Stock"));

        verify(orderRepository, never()).save(any());
        verify(orderItemRepository, never()).saveAll(any());
    }

    // ================= GET ORDER ITEMS =================

    @Test
    void getOrder_success() {

        when(orderItemRepository.findByOrderId(1))
                .thenReturn(List.of(new OrderItem()));

        ResponseEntity<List<OrderItem>> response =
                orderService.getOrder(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    // ================= GET ORDER ITEMS (EMPTY) =================

    @Test
    void getOrder_empty() {

        when(orderItemRepository.findByOrderId(1))
                .thenReturn(List.of());

        ResponseEntity<List<OrderItem>> response =
                orderService.getOrder(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    // ================= GET ALL ORDERS =================

    @Test
    void getAllOrders_success() {

        when(orderRepository.findAll())
                .thenReturn(List.of(new OrderTable()));

        List<OrderTable> orders =
                orderService.getAllOrders();

        assertEquals(1, orders.size());
    }

    // ================= GET ORDERS BY USER =================

    @Test
    void getOrderByUserId_success() {

        when(orderRepository.findAllByUserId(1))
                .thenReturn(List.of(new OrderTable()));

        ResponseEntity<List<OrderTable>> response =
                orderService.getOrderByUserId("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
}
