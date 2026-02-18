package com.gitmadeeasy.usecases.email;

public interface EmailSender {
    void send(String toEmailAddress, String subject, String body);
}