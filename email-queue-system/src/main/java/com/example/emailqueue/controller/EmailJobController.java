package com.example.emailqueue.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.emailqueue.dto.CreateEmailJobRequest;
import com.example.emailqueue.entity.EmailJob;
import com.example.emailqueue.entity.EmailJobStatus;
import com.example.emailqueue.service.EmailJobService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/email-jobs")
public class EmailJobController {

    private final EmailJobService emailJobService;

    public EmailJobController(EmailJobService emailJobService) {
        this.emailJobService = emailJobService;
    }

    @PostMapping
    public ResponseEntity<EmailJob> createJobs(
            @Valid @RequestBody CreateEmailJobRequest request) {

        EmailJob savedJob = emailJobService.createEmailJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedJob);
    }

    @GetMapping
    public ResponseEntity<List<EmailJob>> getJobs(
            @RequestParam(required = false) EmailJobStatus status) {
        return ResponseEntity.ok(emailJobService.getJobs(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailJob> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(emailJobService.getJobById(id));
    }
}
