package com.gitmadeeasy.infrastructure.controllers;

import com.gitmadeeasy.usecases.auth.EmailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email-test")
public class EmailTestController {
    private final EmailSender emailSender;

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
        return "Email Sent!";
    }
}