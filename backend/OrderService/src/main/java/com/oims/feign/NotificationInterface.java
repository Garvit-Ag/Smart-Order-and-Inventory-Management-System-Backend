package com.oims.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("NOTIFICATIONSERVICE")
public interface NotificationInterface {
	@PostMapping("/welcomeMail")
    public void sendWelcomeMail(@RequestParam String mail, @RequestParam String name);
}
