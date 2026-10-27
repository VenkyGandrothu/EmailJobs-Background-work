package com.example.emailqueue.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.emailqueue.entity.EmailJob;
import com.example.emailqueue.entity.EmailJobStatus;

@Repository
public interface EmailJobRepo extends JpaRepository<EmailJob, Long> {

    List<EmailJob> findByStatus(EmailJobStatus status);
}
