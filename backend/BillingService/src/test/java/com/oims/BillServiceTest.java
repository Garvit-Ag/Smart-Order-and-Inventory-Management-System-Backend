package com.oims;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.oims.dto.BillDTO;
import com.oims.dto.BillMailRequest;
import com.oims.feign.NotificationInterface;
import com.oims.model.Bill;
import com.oims.model.Bill.PaymentMode;
import com.oims.model.Bill.PaymentStatus;
import com.oims.repository.BillRepository;
import com.oims.service.BillService;

@ExtendWith(MockitoExtension.class)
class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private NotificationInterface notificationInterface;

    @InjectMocks
    private BillService billService;

    // ================= CREATE BILL (NON-CASH) =================

    @Test
    void createBill_success_nonCashPayment() {

        BillDTO dto = new BillDTO();
        dto.setAmount(500.0);
        dto.setPaymentMode(PaymentMode.UPI);

        Bill savedBill = Bill.builder()
                .billId(1)
                .amount(500.0)
                .paymentMode(PaymentMode.UPI)
                .paymentStatus(PaymentStatus.COMPLETED)
                .build();

        when(billRepository.save(any(Bill.class)))
                .thenReturn(savedBill);

        ResponseEntity<Integer> response =
                billService.createBill(dto, "test@example.com");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, response.getBody());

        verify(notificationInterface)
                .sendBillMail(any(BillMailRequest.class));
    }

    // ================= CREATE BILL (CASH → PENDING) =================

    @Test
    void createBill_cashPayment_setsPendingStatus() {

        BillDTO dto = new BillDTO();
        dto.setAmount(300.0);
        dto.setPaymentMode(PaymentMode.CASH);

        Bill savedBill = Bill.builder()
                .billId(2)
                .amount(300.0)
                .paymentMode(PaymentMode.CASH)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        when(billRepository.save(any(Bill.class)))
                .thenReturn(savedBill);

        ResponseEntity<Integer> response =
                billService.createBill(dto, "cash@example.com");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(2, response.getBody());
    }

    // ================= GET ALL BILLS =================

    @Test
    void getAllBill_success() {

        when(billRepository.findAll())
                .thenReturn(List.of(new Bill()));

        ResponseEntity<List<Bill>> response =
                billService.getAllBill();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    // ================= NEGATIVE: NOTIFICATION FAILURE =================

    @Test
    void createBill_notificationFails() {

        BillDTO dto = new BillDTO();
        dto.setAmount(100.0);
        dto.setPaymentMode(PaymentMode.UPI);

        Bill savedBill = Bill.builder()
                .billId(3)
                .amount(100.0)
                .paymentMode(PaymentMode.UPI)
                .paymentStatus(PaymentStatus.COMPLETED)
                .build();

        when(billRepository.save(any(Bill.class)))
                .thenReturn(savedBill);

        doThrow(new RuntimeException("Mail service down"))
                .when(notificationInterface)
                .sendBillMail(any(BillMailRequest.class));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> billService.createBill(dto, "fail@example.com")
        );

        assertEquals("Mail service down", ex.getMessage());
    }
}
