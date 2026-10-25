
package com.example.emailqueue.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.emailqueue.entity.EmailJob;
import com.example.emailqueue.service.EmailJobService;

@RestController
@RequestMapping("/api/email-jobs")
public class EmailJobController {

    private final EmailJobService emailJobService;

    public EmailJobController(EmailJobService emailJobService) {
        this.emailJobService = emailJobService;
    }

    @PostMapping
    public ResponseEntity<EmailJob> createJobs(
            @RequestBody EmailJob emailJob) {

        EmailJob savedJob =
                emailJobService.createEmailJob(emailJob);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedJob);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailJob> getJobById(
            @PathVariable Long id) {

        EmailJob job = emailJobService.getJobById(id);

        return ResponseEntity.ok(job);
    }
}
