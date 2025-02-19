package com.project.lms.service;

import org.springframework.stereotype.Service;

import com.project.lms.entity.AnswerTracking;
import com.project.lms.repo.AnswerTrackingRepository;

import jakarta.transaction.Transactional;

@Service
public class AnswerTrackingService 
{

    private AnswerTrackingRepository answerTrackingRepository;

    public AnswerTrackingService(AnswerTrackingRepository answerTrackingRepository) {
        this.answerTrackingRepository = answerTrackingRepository;
    }

    @Transactional
    public AnswerTracking saveAnswerTracking(AnswerTracking answerTracking)
    {
        return answerTrackingRepository.save(answerTracking);
    }

    
    
}
