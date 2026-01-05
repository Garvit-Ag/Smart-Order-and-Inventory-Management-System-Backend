package com.oims;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oims.controller.OrderController;
import com.oims.dto.OrderDto;
import com.oims.dto.PaymentMode;
import com.oims.model.OrderItem;
import com.oims.model.OrderTable;
import com.oims.service.OrderService;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    // ================= ADD ORDER (POSITIVE) =================

    @Test
    void addOrder_fail() throws Exception {

        OrderDto dto = new OrderDto();
        dto.setAddress("Hyderabad");
        dto.setAmount(1000.0);
        dto.setItems(new ArrayList<>()); // simple dummy list
        dto.setPaymentMode(PaymentMode.CASH); // validation not tested here

        when(orderService.addOrder(any(OrderDto.class), anyString(), anyString()))
                .thenReturn(ResponseEntity.ok("Order placed"));

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Email", "test@example.com")
                        .header("X-User-Id", "1")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.items").value("must not be empty"));
    }

    // ================= ADD ORDER (NEGATIVE – MISSING HEADER) =================

    @Test
    void addOrder_missingHeader() throws Exception {

        OrderDto dto = new OrderDto();
        dto.setAddress("Hyderabad");
        dto.setAmount(500.0);
        dto.setItems(List.of());

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    // ================= GET ORDERS BY USER ID =================

    @Test
    void getOrderByUserId_success() throws Exception {

        when(orderService.getOrderByUserId("1"))
                .thenReturn(ResponseEntity.ok(List.of(new OrderTable())));

        mockMvc.perform(get("/order")
                        .header("X-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ================= GET ORDER ITEMS BY ORDER ID =================

    @Test
    void getOrder_success() throws Exception {

        when(orderService.getOrder(1))
                .thenReturn(ResponseEntity.ok(List.of(new OrderItem())));

        mockMvc.perform(get("/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ================= GET ALL ORDERS =================

    @Test
    void getAllOrders_success() throws Exception {

        when(orderService.getAllOrders())
                .thenReturn(List.of(new OrderTable()));

        mockMvc.perform(get("/order/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
