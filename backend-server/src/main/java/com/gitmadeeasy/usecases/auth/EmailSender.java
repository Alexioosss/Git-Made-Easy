package com.gitmadeeasy.usecases.auth;

public interface EmailSender {
    void send(String to, String subject, String body);
}