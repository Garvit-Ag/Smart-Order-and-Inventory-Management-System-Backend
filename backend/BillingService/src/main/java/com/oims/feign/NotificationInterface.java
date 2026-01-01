package com.oims.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.oims.dto.BillMailRequest;


@FeignClient("NOTIFICATIONSERVICE")
public interface NotificationInterface {
	
	 @PostMapping("/invoiceMail")
	 public void sendBillMail(@RequestBody BillMailRequest request);
}
