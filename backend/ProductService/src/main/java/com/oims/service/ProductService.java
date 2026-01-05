package com.oims.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.oims.Model.Product;
import com.oims.dto.ProductDTO;
import com.oims.repository.ProductRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	
	String msg = "Product Doesnt Exist";
	
	public ResponseEntity<String> addProduct(@Valid ProductDTO product) {
		Product productObj = Product.builder()
                .name(product.getName())
                .description(product.getDescription())
                .brand(product.getBrand())
                .price(product.getPrice())
                .stock(product.getStock())
                .url(product.getUrl())
                .build();
		productRepository.save(productObj);
		return new ResponseEntity<>("Added new Product", HttpStatus.CREATED);
	}

	public ResponseEntity<List<Product>> getProduct() {
		return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
	}

	public ResponseEntity<String> updateProductPrice(Integer id, Double price){
		if(!productRepository.existsById(id)) {
			return new ResponseEntity<>(msg,HttpStatus.BAD_REQUEST) ;
		}
		if (price <= 0) {
        	return new ResponseEntity<>("Price can't be negative or 0", HttpStatus.BAD_REQUEST);
        }
		Product product = productRepository.findById(id).get();
		product.setPrice(price);
		productRepository.save(product);
		return new ResponseEntity<>("Product Properties Updated", HttpStatus.OK);
	}
	
	public ResponseEntity<String> updateProductStock(Integer id, Integer stock){
		if(!productRepository.existsById(id)) {
			return new ResponseEntity<>(msg,HttpStatus.BAD_REQUEST) ;
		}

        if (stock < 0) {
        	return new ResponseEntity<>("Stock can't be negative", HttpStatus.BAD_REQUEST);
        }
		Product product = productRepository.findById(id).get();
		product.setStock(stock);
		productRepository.save(product);
		return new ResponseEntity<>("Product Properties Updated", HttpStatus.OK);
	}

	public ResponseEntity<String> deleteProduct(Integer id) {
		if(!productRepository.existsById(id)) {
			return new ResponseEntity<>(msg,HttpStatus.BAD_REQUEST) ;
		}
		productRepository.deleteById(id);
		return new ResponseEntity<>("Product Removed Successfully", HttpStatus.OK);
	}

	public ResponseEntity<Product> getProductById(Integer id) {
		Optional<Product> product = productRepository.findById(id);
		if(product.isEmpty()) {
			return new ResponseEntity<>(new Product(), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(product.get(), HttpStatus.OK);
	}

	public Integer getStock(Integer id) {
		return productRepository.findById(id).get().getStock();
	}

	public ResponseEntity<String> getProductName(Integer id) {
		Product product = productRepository.findById(id).get();
		return new ResponseEntity<>(product.getName(),HttpStatus.ACCEPTED);
	}
	
	
}
