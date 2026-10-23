package com.example.emailqueue.service;

import org.springframework.stereotype.Service;

import com.example.emailqueue.repo.EmailJobRepo;

@Service 
public class EmailJobService {
    

    private final EmailJobRepo emailJobRepo;

    public EmailJobService(EmailJobRepo emailJobRepo){
        this.emailJobRepo = emailJobRepo;
    }


    

}
