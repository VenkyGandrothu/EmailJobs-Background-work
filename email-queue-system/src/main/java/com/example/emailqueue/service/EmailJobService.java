
package com.example.emailqueue.service;

import org.springframework.stereotype.Service;

import com.example.emailqueue.entity.EmailJob;
import com.example.emailqueue.repo.EmailJobRepo;

@Service
public class EmailJobService {

    private final EmailJobRepo emailJobRepo;

    public EmailJobService(EmailJobRepo emailJobRepo) {
        this.emailJobRepo = emailJobRepo;
    }

    public EmailJob createEmailJob(EmailJob emailJob) {
        return emailJobRepo.save(emailJob);
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
