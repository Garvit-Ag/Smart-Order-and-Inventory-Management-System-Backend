package com.oims.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oims.Model.Product;
import com.oims.dto.ProductDTO;
import com.oims.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	
	@PostMapping("/product")
    public ResponseEntity<String> addProduct(@Valid @RequestBody ProductDTO product){
		return productService.addProduct(product);
	}
	
	@GetMapping("/product")
	public ResponseEntity<List<Product>> getProduct(){
		return productService.getProduct();
	}
	
	@PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Integer id, @RequestParam Double price, @RequestParam Integer stock) throws Exception {
        return productService.updateProduct(id,price,stock);
    }
	
	@DeleteMapping("/product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Integer id){
		return productService.deleteProduct(id);
	}
}
