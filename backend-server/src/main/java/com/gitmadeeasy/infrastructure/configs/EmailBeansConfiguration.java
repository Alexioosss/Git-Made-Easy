package com.gitmadeeasy.infrastructure.configs;

import com.gitmadeeasy.infrastructure.email.smtp.SmtpEmailSender;
import com.gitmadeeasy.usecases.email.EmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration @Profile("dev")
public class EmailBeansConfiguration {

    @Bean
    public EmailSender emailSender(
            @Value("${smtp.host}") String host,
            @Value("${smtp.port}") int port,
            @Value("${smtp.username}") String username,
            @Value("${smtp.password}") String password,
            @Value("${smtp.fromEmailAddress}") String fromEmailAddress) {
        return new SmtpEmailSender(host, port, username, password, fromEmailAddress);
    }
}