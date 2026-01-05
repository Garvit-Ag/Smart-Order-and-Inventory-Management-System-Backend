package com.oims;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oims.Model.Product;
import com.oims.controller.ProductController;
import com.oims.dto.ProductDTO;
import com.oims.service.ProductService;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    // ================= ADD PRODUCT =================

    @Test
    void addProduct_success() throws Exception {

        ProductDTO dto = new ProductDTO();
        dto.setName("Mouse");
        dto.setPrice(500.0);
        dto.setStock(10);
        dto.setBrand("Logitech");
        dto.setDescription("Great mouse");
        dto.setUrl("testing url");

        when(productService.addProduct(any(ProductDTO.class)))
                .thenReturn(ResponseEntity.ok("Product added"));

        mockMvc.perform(post("/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product added"));
    }
    
    @Test
    void addProduct_fail() throws Exception {

        ProductDTO dto = new ProductDTO();
        dto.setName("Mouse");
        dto.setPrice(500.0);
        dto.setStock(10);
        dto.setDescription("Great mouse");
        dto.setUrl("testing url");
        when(productService.addProduct(any(ProductDTO.class)))
                .thenReturn(ResponseEntity.ok("Product added"));

        mockMvc.perform(post("/product/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.brand").value("Brand is required"));
    }

    // ================= GET ALL PRODUCTS =================

    @Test
    void getProduct_success() throws Exception {

        when(productService.getProduct())
                .thenReturn(ResponseEntity.ok(List.of(new Product())));

        mockMvc.perform(get("/product/get"))
                .andExpect(status().isOk());
    }

    // ================= GET STOCK =================

    @Test
    void getStock_success() throws Exception {

        when(productService.getStock(1)).thenReturn(20);

        mockMvc.perform(get("/product/stock/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("20"));
    }

    // ================= GET PRODUCT BY ID =================

    @Test
    void getProductById_success() throws Exception {

        Product product = new Product();
        product.setId(1);
        product.setName("Keyboard");

        when(productService.getProductById(1))
                .thenReturn(ResponseEntity.ok(product));

        mockMvc.perform(get("/product/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Keyboard"));
    }

    // ================= UPDATE PRICE =================

    @Test
    void updateProductPrice_success() throws Exception {

        when(productService.updateProductPrice(1, 1000.0))
                .thenReturn(ResponseEntity.ok("Price updated"));

        mockMvc.perform(put("/product/update/price/1")
                        .param("price", "1000"))
                .andExpect(status().isOk())
                .andExpect(content().string("Price updated"));
    }

    // ================= UPDATE STOCK =================

    @Test
    void updateProductStock_success() throws Exception {

        when(productService.updateProductStock(1, 50))
                .thenReturn(ResponseEntity.ok("Stock updated"));

        mockMvc.perform(put("/product/update/stock/1")
                        .param("stock", "50"))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock updated"));
    }

    // ================= DELETE PRODUCT =================

    @Test
    void deleteProduct_success() throws Exception {

        when(productService.deleteProduct(1))
                .thenReturn(ResponseEntity.ok("Product deleted"));

        mockMvc.perform(delete("/product/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted"));
    }

    // ================= GET PRODUCT NAME =================

    @Test
    void getProductName_success() throws Exception {

        when(productService.getProductName(1))
                .thenReturn(ResponseEntity.ok("Laptop"));

        mockMvc.perform(get("/product/get/name/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Laptop"));
    }
}
