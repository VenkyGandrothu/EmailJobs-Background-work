package com.example.emailqueue.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.Value;

@Service
public class EmailService{

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }
    
    @Value("${app.mail.from}")
    private String fromAddress;

    public void sendEmail(
        String recipient,
        String subject,
        String body) {

    SimpleMailMessage message = new SimpleMailMessage();

    message.setFrom(fromAddress);
    message.setTo(recipient);
    message.setSubject(subject);
    message.setText(body);
    mailSender.send(message);
    
    }
}