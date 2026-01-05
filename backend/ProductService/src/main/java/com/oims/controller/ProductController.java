package com.oims.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oims.Model.Product;
import com.oims.dto.ProductDTO;
import com.oims.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
	
	private final ProductService productService;
	
	@PostMapping("/add")
    public ResponseEntity<String> addProduct(@Valid @RequestBody ProductDTO product){
		return productService.addProduct(product);
	}
	
	@GetMapping("/get")
	public ResponseEntity<List<Product>> getProduct(){
		return productService.getProduct();
	}
	
	@GetMapping("/stock/{id}")
	public Integer getStock(@PathVariable Integer id) {
		return productService.getStock(id);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Integer id){
		return productService.getProductById(id);
	}
	
	@PutMapping("/update/price/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Integer id, @RequestParam Double price){
        return productService.updateProductPrice(id,price);
    }
	
	@PutMapping("/update/stock/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Integer id, @RequestParam Integer stock){
        return productService.updateProductStock(id,stock);
    }
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Integer id){
		return productService.deleteProduct(id);
	}
}
