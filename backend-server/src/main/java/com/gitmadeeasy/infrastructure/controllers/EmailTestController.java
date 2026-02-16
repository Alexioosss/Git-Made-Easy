package com.gitmadeeasy.infrastructure.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/email-test")
public class EmailTestController {
    private final EmailSender emailSender;
    private static final Logger log = LoggerFactory.getLogger(EmailTestController.class);

    public EmailTestController(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @GetMapping
    public String sendTestEmail() {
        this.emailSender.send(
                "gitmadeeasy.noreply@gmail.com",
                "Test Email From GitMadeEasy",
                "Hello, World!"
        );
        log.info("GET /email-test - Test verification email sent to={}", "gitmadeeasy.noreply@gmail.com");
        return "An Email Has Been Sent!";
    }
}