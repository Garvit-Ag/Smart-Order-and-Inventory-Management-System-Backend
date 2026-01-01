package com.oims.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.oims.dto.BillDTO;
import com.oims.dto.BillMailRequest;
import com.oims.feign.NotificationInterface;
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
	
	private final NotificationInterface notificationInterface;
	
	public ResponseEntity<Integer> createBill(BillDTO billDto, String email) {
		PaymentStatus billStatus = billDto.getPaymentMode() == PaymentMode.CASH ? PaymentStatus.PENDING: PaymentStatus.COMPLETED;
		
		Bill bill = Bill.builder()
                .amount(billDto.getAmount())
                .paymentMode(billDto.getPaymentMode())
                .paymentStatus(billStatus)
                .billingDate(LocalDate.now())
                .billingTime(LocalTime.now())
                .build();
		
        bill = billRepository.save(bill);
        
        BillMailRequest billMailRequest = BillMailRequest.builder()
        		.email(email)
        		.billId(bill.getBillId())
        		.amount(bill.getAmount())
        		.billingDate(bill.getBillingDate())
        		.build();
        
        notificationInterface.sendBillMail(billMailRequest);
        
        return new ResponseEntity<>(bill.getBillId(), HttpStatus.CREATED);
	}

	public ResponseEntity<List<Bill>> getAllBill() {
		return new ResponseEntity<>(billRepository.findAll(), HttpStatus.OK);
	}

}
