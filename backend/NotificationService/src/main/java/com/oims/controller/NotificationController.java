package com.oims.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.oims.dto.BillMailDto;
import com.oims.dto.WelcomeMailDto;
import com.oims.service.EmailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/welcomeMail")
    public void sendWelcomeMail(@Valid @RequestBody WelcomeMailDto request) {

        emailService.sendWelcomeMail(request);
    }

    @PostMapping("/invoiceMail")
    public void sendBillMail(@Valid @RequestBody BillMailDto request) {

        emailService.sendBillMail(request);
    }
}
