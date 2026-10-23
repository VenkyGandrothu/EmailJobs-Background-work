package com.example.emailqueue.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EmailJob{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailJobId;
    private String recipient;
    private String subject;
    private String body;
    private int attempts = 0;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private EmailJobStatus status = EmailJobStatus.PENDING;
    public Long getEmailJobId() {
        return emailJobId;
    }
    public void setEmailJobId(Long emailJobId) {
        this.emailJobId = emailJobId;
    }
    public String getRecipient() {
        return recipient;
    }
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public int getAttempts() {
        return attempts;
    }
    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public EmailJobStatus getStatus() {
        return status;
    }
    public void setStatus(EmailJobStatus status) {
        this.status = status;
    }

    public EmailJob(Long emailJobId, String recipient, String subject, String body, int attempts,
            LocalDateTime createdAt, LocalDateTime updatedAt, EmailJobStatus status) {
        this.emailJobId = emailJobId;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.attempts = attempts;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
    }

    public EmailJob() {
    }
}