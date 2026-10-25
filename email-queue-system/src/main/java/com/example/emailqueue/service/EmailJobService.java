package com.example.emailqueue.service;

import org.springframework.stereotype.Service;

import com.example.emailqueue.dto.CreateEmailJobRequest;
import com.example.emailqueue.entity.EmailJob;
import com.example.emailqueue.entity.EmailJobStatus;
import com.example.emailqueue.exception.EmailJobNotFoundException;
import com.example.emailqueue.producer.EmailJobProducer;
import com.example.emailqueue.repo.EmailJobRepo;

@Service
public class EmailJobService {

    private final EmailJobRepo emailJobRepo;
    private final EmailJobProducer emailJobProducer;

    public EmailJobService(EmailJobRepo emailJobRepo, EmailJobProducer emailJobProducer) {
        this.emailJobRepo = emailJobRepo;
        this.emailJobProducer = emailJobProducer;
    }

    public EmailJob createEmailJob(CreateEmailJobRequest request) {
        // Map only allowed fields — ignore any client attempt to set id/status/attempts
        EmailJob emailJob = new EmailJob();
        emailJob.setRecipient(request.getRecipient());
        emailJob.setSubject(request.getSubject());
        emailJob.setBody(request.getBody());
        emailJob.setStatus(EmailJobStatus.PENDING);
        emailJob.setAttempts(0);

        EmailJob savedJob = emailJobRepo.save(emailJob);
        emailJobProducer.sendJob(savedJob.getEmailJobId());
        return savedJob;
    }

    public EmailJob getJobById(Long id) {
        return emailJobRepo.findById(id)
                .orElseThrow(() -> new EmailJobNotFoundException(id));
    }
}
