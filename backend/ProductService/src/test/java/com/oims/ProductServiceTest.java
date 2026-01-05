package com.oims;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.oims.Model.Product;
import com.oims.dto.ProductDTO;
import com.oims.repository.ProductRepository;
import com.oims.service.ProductService;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    // ================= ADD PRODUCT =================

    @Test
    void addProduct_success() {

        ProductDTO dto = new ProductDTO();
        dto.setName("Mouse");
        dto.setPrice(500.0);
        dto.setStock(10);

        ResponseEntity<String> response =
                productService.addProduct(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Added new Product", response.getBody());

        verify(productRepository).save(any(Product.class));
    }

    // ================= GET PRODUCTS =================

    @Test
    void getProduct_success() {

        when(productRepository.findAll())
                .thenReturn(List.of(new Product()));

        ResponseEntity<List<Product>> response =
                productService.getProduct();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    // ================= UPDATE PRICE =================

    @Test
    void updateProductPrice_success() {

        Product product = new Product();
        product.setId(1);
        product.setPrice(100.0);

        when(productRepository.existsById(1)).thenReturn(true);
        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        ResponseEntity<String> response =
                productService.updateProductPrice(1, 200.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Properties Updated", response.getBody());

        verify(productRepository).save(product);
    }

    // ================= UPDATE STOCK =================

    @Test
    void updateProductStock_success() {

        Product product = new Product();
        product.setId(1);
        product.setStock(10);

        when(productRepository.existsById(1)).thenReturn(true);
        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        ResponseEntity<String> response =
                productService.updateProductStock(1, 50);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Properties Updated", response.getBody());
    }

    // ================= DELETE PRODUCT =================

    @Test
    void deleteProduct_success() {

        when(productRepository.existsById(1)).thenReturn(true);

        ResponseEntity<String> response =
                productService.deleteProduct(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Removed Successfully", response.getBody());

        verify(productRepository).deleteById(1);
    }

    // ================= GET STOCK =================

    @Test
    void getStock_success() {

        Product product = new Product();
        product.setStock(25);

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        Integer stock = productService.getStock(1);

        assertEquals(25, stock);
    }

    // ================= GET PRODUCT NAME =================

    @Test
    void getProductName_success() {

        Product product = new Product();
        product.setName("Keyboard");

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        ResponseEntity<String> response =
                productService.getProductName(1);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Keyboard", response.getBody());
    }
    
    @Test
    void updateProductPrice_productNotFound() {

        when(productRepository.existsById(1)).thenReturn(false);

        ResponseEntity<String> response =
                productService.updateProductPrice(1, 500.0);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Product Doesnt Exist", response.getBody());

        verify(productRepository, never()).save(any());
    }

    // ================= UPDATE PRICE - INVALID PRICE =================

    @Test
    void updateProductPrice_invalidPrice() {

        when(productRepository.existsById(1)).thenReturn(true);

        ResponseEntity<String> response =
                productService.updateProductPrice(1, 0.0);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Price can't be negative or 0", response.getBody());

        verify(productRepository, never()).save(any());
    }

    // ================= UPDATE STOCK - PRODUCT NOT FOUND =================

    @Test
    void updateProductStock_productNotFound() {

        when(productRepository.existsById(1)).thenReturn(false);

        ResponseEntity<String> response =
                productService.updateProductStock(1, 10);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Product Doesnt Exist", response.getBody());
    }

    // ================= UPDATE STOCK - NEGATIVE STOCK =================

    @Test
    void updateProductStock_negativeStock() {

        when(productRepository.existsById(1)).thenReturn(true);

        ResponseEntity<String> response =
                productService.updateProductStock(1, -5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Stock can't be negative", response.getBody());

        verify(productRepository, never()).save(any());
    }

    // ================= DELETE PRODUCT - NOT FOUND =================

    @Test
    void deleteProduct_productNotFound() {

        when(productRepository.existsById(1)).thenReturn(false);

        ResponseEntity<String> response =
                productService.deleteProduct(1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Product Doesnt Exist", response.getBody());

        verify(productRepository, never()).deleteById(any());
    }
}
