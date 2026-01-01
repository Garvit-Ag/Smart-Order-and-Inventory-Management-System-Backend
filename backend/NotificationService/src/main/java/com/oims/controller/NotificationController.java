package com.oims.controller;

import org.springframework.web.bind.annotation.*;

import com.oims.dto.BillMailDto;
import com.oims.service.EmailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final EmailService emailService;

    @PostMapping("/welcomeMail")
    public void sendWelcomeMail(@RequestParam String mail, @RequestParam String name) {

        emailService.sendWelcomeMail(mail,name);
    }

    @PostMapping("/invoiceMail")
    public void sendBillMail(@Valid @RequestBody BillMailDto request) {

        emailService.sendBillMail(request);
    }
}
