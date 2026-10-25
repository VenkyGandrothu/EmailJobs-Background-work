package com.example.emailqueue.exception;

public class EmailJobNotFoundException extends RuntimeException {

    public EmailJobNotFoundException(Long id) {
        super("Email job not found with ID: " + id);
    }
}
