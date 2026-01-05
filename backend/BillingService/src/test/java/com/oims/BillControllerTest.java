package com.oims;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oims.controller.BillController;
import com.oims.dto.BillDTO;
import com.oims.model.Bill;
import com.oims.model.Bill.PaymentMode;
import com.oims.service.BillService;

@WebMvcTest(BillController.class)
class BillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BillService billService;

    @Autowired
    private ObjectMapper objectMapper;

    // ================= CREATE BILL (POSITIVE) =================

    @Test
    void createBill_success() throws Exception {

        BillDTO billDTO = new BillDTO();
        billDTO.setAmount(500.0);
        billDTO.setPaymentMode(PaymentMode.UPI);

        when(billService.createBill(any(BillDTO.class), anyString()))
                .thenReturn(ResponseEntity.ok(1));

        mockMvc.perform(post("/billGenerate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Email", "test@example.com")
                        .content(objectMapper.writeValueAsString(billDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    // ================= CREATE BILL (NEGATIVE – VALIDATION FAIL) =================

    @Test
    void createBill_invalidAmount() throws Exception {

        BillDTO billDTO = new BillDTO();
        billDTO.setAmount(-100.0); // invalid
        billDTO.setPaymentMode(PaymentMode.CASH);

        mockMvc.perform(post("/billGenerate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Email", "test@example.com")
                        .content(objectMapper.writeValueAsString(billDTO)))
                .andExpect(status().isBadRequest());
    }

    // ================= CREATE BILL (NEGATIVE – MISSING HEADER) =================

    @Test
    void createBill_missingEmailHeader() throws Exception {

        BillDTO billDTO = new BillDTO();
        billDTO.setAmount(300.0);
        billDTO.setPaymentMode(PaymentMode.UPI);

        mockMvc.perform(post("/billGenerate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(billDTO)))
                .andExpect(status().is5xxServerError());
    }

    // ================= GET ALL BILLS (POSITIVE) =================

    @Test
    void getAllBill_success() throws Exception {

        when(billService.getAllBill())
                .thenReturn(ResponseEntity.ok(List.of(new Bill())));

        mockMvc.perform(get("/bill"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ================= GET ALL BILLS (EMPTY LIST) =================

    @Test
    void getAllBill_empty() throws Exception {

        when(billService.getAllBill())
                .thenReturn(ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/bill"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
