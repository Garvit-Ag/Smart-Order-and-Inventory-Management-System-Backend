package com.oims.service;

import java.io.ObjectInputFilter.Status;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.oims.dto.BillDTO;
import com.oims.model.Bill;
import com.oims.model.Bill.PaymentMode;
import com.oims.model.Bill.PaymentStatus;
import com.oims.repository.BillRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillService {

	private final BillRepository billRepository;
	
	public ResponseEntity<String> createBill(@Valid BillDTO billDto) {
		
		if(billRepository.existsByOrderId(billDto.getOrderId())) {
			return new ResponseEntity<String>("Bill For Order Already Exist", HttpStatus.BAD_REQUEST);
		}
		
		PaymentStatus billStatus = billDto.getPaymentMode() == PaymentMode.CASH ? PaymentStatus.PENDING: PaymentStatus.COMPLETED;
		
		Bill bill = Bill.builder()
                .orderId(billDto.getOrderId())
                .amount(billDto.getAmount())
                .paymentMode(billDto.getPaymentMode())
                .paymentStatus(billStatus)
                .billingDate(LocalDate.now())
                .billingTime(LocalTime.now())
                .build();
		
        billRepository.save(bill);
        return new ResponseEntity<>("Bill Generated", HttpStatus.CREATED);
	}

	public ResponseEntity<List<Bill>> getAllBill() {
		return new ResponseEntity<>(billRepository.findAll(), HttpStatus.OK);
	}

}
