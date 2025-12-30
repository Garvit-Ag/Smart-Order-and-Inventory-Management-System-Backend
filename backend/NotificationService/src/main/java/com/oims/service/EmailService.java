package com.oims.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.oims.dto.BillMailDto;
import com.oims.dto.WelcomeMailDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender mailSender;
	
	public void sendWelcomeMail(@Valid WelcomeMailDto request) {
		SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Welcome to OIMS!!");
        message.setText("""
                Hello %s,

                Welcome to OIMS!
                We are glad to have you onboard. We are Ready to Serve you across the 
                global with just a click :)

                Regards,
                CEO
                Garvit Agarwal
                """.formatted(request.getName()));

        mailSender.send(message);
	}

	public void sendBillMail(@Valid BillMailDto request) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Your Bill Details - OIMS");
        message.setText("""
                Hello,

                Bill ID: %d
                Amount: Rs. %.2f
                Payment Mode: %s
                Billing Date: %s

                Thank you for shopping with us! Hope to see you again.

                Regards,
                CEO
                Garvit Agarwal
                """.formatted(
                request.getBillId(),
                request.getAmount(),
                request.getPaymentMode(),
                request.getBillingDate()
        ));

        mailSender.send(message);
	}
	
	

}
