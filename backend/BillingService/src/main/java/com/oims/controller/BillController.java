package com.oims.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.oims.dto.BillDTO;
import com.oims.model.Bill;
import com.oims.service.BillService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BillController {
	private final BillService billService;
	
	@PostMapping("/billGenerate")
	public ResponseEntity<Integer> createBill(@Valid @RequestBody BillDTO billDto, @RequestHeader("X-User-Email") String email){
		return billService.createBill(billDto,email);
	}
	
	@GetMapping("/bill")
	public ResponseEntity<List<Bill>> getAllBill(){
		return billService.getAllBill();
	}
}
