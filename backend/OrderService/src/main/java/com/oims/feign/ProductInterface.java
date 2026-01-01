package com.oims.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("PRODUCTSERVICE")
public interface ProductInterface {
	
	@GetMapping("/product/stock/{id}")
	public Integer getStock(@PathVariable Integer id);
	
	@PutMapping("/product/update/stock/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable Integer id, @RequestParam Integer stock);
	
}
