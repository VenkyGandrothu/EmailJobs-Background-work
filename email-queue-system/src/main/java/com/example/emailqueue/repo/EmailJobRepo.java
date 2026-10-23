package com.example.emailqueue.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.emailqueue.entity.EmailJob;

@Repository 
public interface EmailJobRepo extends JpaRepository<Long, EmailJob> {
    
}
