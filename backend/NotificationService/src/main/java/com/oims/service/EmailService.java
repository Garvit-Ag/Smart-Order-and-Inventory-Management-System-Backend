package com.oims.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.oims.dto.BillMailDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EmailService {
	
	private final JavaMailSender mailSender;
	
	public void sendWelcomeMail(String mail, String name) {
		SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail);
        message.setSubject("Welcome to OIMS!!");
        message.setText("""
                Hello %s,

                Welcome to OIMS!
                We are glad to have you onboard. We are Ready to Serve you across the global with just a click :)

                Regards,
                CEO
                Garvit Agarwal
                """.formatted(name));

        mailSender.send(message);
	}

	public void sendBillMail(@Valid BillMailDto request) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(request.getEmail());
        message.setSubject("Your Bill Details - OIMS");
        String body =
                "Hello,\n\n" +
                "Bill ID: " + request.getBillId() + "\n" +
                "Amount: Rs. " + String.format("%.2f", request.getAmount()) + "\n" +
                "Billing Date: " + request.getBillingDate() + "\n\n" +
                "Thank you for shopping with us! Hope to see you again.\n\n" +
                "Regards,\n" +
                "CEO\n" +
                "Garvit Agarwal";
        message.setText(body);

        mailSender.send(message);
	}
	
	

}
