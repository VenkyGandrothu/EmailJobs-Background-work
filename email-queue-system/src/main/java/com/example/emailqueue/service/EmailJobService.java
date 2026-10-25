
package com.example.emailqueue.service;

import org.springframework.stereotype.Service;

import com.example.emailqueue.entity.EmailJob;
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

    public EmailJob createEmailJob(EmailJob emailJob) {
        
        EmailJob savedJob = emailJobRepo.save(emailJob);

        emailJobProducer.sendJob(savedJob.getEmailJobId());

        return savedJob;
    }

    public EmailJob getJobById(Long id) {
        return emailJobRepo.findById(id)
                .orElseThrow(() ->
                    new RuntimeException(
                        "Email job not found with ID: " + id
                    )
                );
    }
}
