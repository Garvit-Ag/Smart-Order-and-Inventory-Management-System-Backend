package com.oims.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.oims.dto.BillRequest;

@FeignClient("BILLINGSERVICE")
public interface NotificationInterface {
	
	@PostMapping("/billGenerate")
	public ResponseEntity<Integer> createBill(@RequestBody BillRequest billRequest, @RequestHeader("X-User-Email") String email);
}
