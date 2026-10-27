package com.example.emailqueue.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.emailqueue.config.RabbitMq;
import com.example.emailqueue.entity.EmailJob;
import com.example.emailqueue.entity.EmailJobStatus;
import com.example.emailqueue.exception.EmailJobNotFoundException;
import com.example.emailqueue.producer.EmailJobProducer;
import com.example.emailqueue.repo.EmailJobRepo;
import com.example.emailqueue.service.EmailService;

@Component
public class EmailJobConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmailJobConsumer.class);

    private final EmailJobRepo emailJobRepo;
    private final EmailService emailService;
    private final EmailJobProducer emailJobProducer;
    private final int maxAttempts;

    public EmailJobConsumer(
            EmailJobRepo emailJobRepo,
            EmailService emailService,
            EmailJobProducer emailJobProducer,
            @Value("${app.mail.max-attempts:3}") int maxAttempts) {
        this.emailJobRepo = emailJobRepo;
        this.emailService = emailService;
        this.emailJobProducer = emailJobProducer;
        this.maxAttempts = maxAttempts;
    }

    @RabbitListener(queues = RabbitMq.EMAIL_QUEUE)
    public void processEmailJob(Long emailJobId) {
        EmailJob job = emailJobRepo.findById(emailJobId)
                .orElseThrow(() -> new EmailJobNotFoundException(emailJobId));

        // Idempotency: ignore jobs already completed
        if (job.getStatus() == EmailJobStatus.SENT) {
            log.info("Skipping already-sent email job {}", emailJobId);
            return;
        }

        try {
            job.setStatus(EmailJobStatus.PROCESSING);
            job.setLastError(null);
            emailJobRepo.save(job);

            emailService.sendEmail(
                    job.getRecipient(),
                    job.getSubject(),
                    job.getBody()
            );

            job.setStatus(EmailJobStatus.SENT);
            job.setLastError(null);
            emailJobRepo.save(job);
            log.info("Email job {} sent successfully", emailJobId);

        } catch (Exception e) {
            handleFailure(job, e);
        }
    }

    private void handleFailure(EmailJob job, Exception e) {
        job.setAttempts(job.getAttempts() + 1);
        job.setLastError(truncate(e.getMessage(), 500));

        if (job.getAttempts() < maxAttempts) {
            // Soft fail: keep PENDING and republish for another try
            job.setStatus(EmailJobStatus.PENDING);
            emailJobRepo.save(job);
            emailJobProducer.sendJob(job.getEmailJobId());
            log.warn(
                    "Email job {} failed (attempt {}/{}): {}. Requeued.",
                    job.getEmailJobId(),
                    job.getAttempts(),
                    maxAttempts,
                    e.getMessage()
            );
        } else {
            job.setStatus(EmailJobStatus.FAILED);
            emailJobRepo.save(job);
            log.error(
                    "Email job {} permanently failed after {} attempts: {}",
                    job.getEmailJobId(),
                    job.getAttempts(),
                    e.getMessage()
            );
        }
    }

    private String truncate(String value, int maxLen) {
        if (value == null) {
            return null;
        }
        return value.length() <= maxLen ? value : value.substring(0, maxLen);
    }
}
