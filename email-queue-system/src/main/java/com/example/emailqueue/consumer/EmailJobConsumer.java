package com.example.emailqueue.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.emailqueue.config.RabbitMq;
import com.example.emailqueue.entity.EmailJob;
import com.example.emailqueue.entity.EmailJobStatus;
import com.example.emailqueue.repo.EmailJobRepo;
import com.example.emailqueue.service.EmailService;



@Component
public class EmailJobConsumer {
    
    private final EmailJobRepo emailJobRepo;
    private final EmailService emailService;

    public EmailJobConsumer(EmailJobRepo emailJobRepo, EmailService emailService){
        this.emailJobRepo = emailJobRepo;
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMq.EMAIL_QUEUE)
    public void processEmailJob(Long emailJobId) {

        EmailJob job = emailJobRepo.findById(emailJobId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Email job not found: " + emailJobId));

        try {
            // 1. Mark the job as processing
            job.setStatus(EmailJobStatus.PROCESSING);
            emailJobRepo.save(job);

            // 2. Send the email
            emailService.sendEmail(
                    job.getRecipient(),
                    job.getSubject(),
                    job.getBody()
            );

            // 3. Mark the job as sent
            job.setStatus(EmailJobStatus.SENT);

        } catch (Exception e) {
            // 4. Mark the job as failed
            job.setAttempts(job.getAttempts() + 1);
            job.setStatus(EmailJobStatus.FAILED);

            System.err.println(
                    "Email job " + emailJobId
                            + " failed: " + e.getMessage()
            );
        }

        emailJobRepo.save(job);
    }
}
