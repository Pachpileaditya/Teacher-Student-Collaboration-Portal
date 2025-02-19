package com.project.lms.service;

import org.springframework.stereotype.Service;

import com.project.lms.entity.Answer;
import com.project.lms.entity.Question;
import com.project.lms.repo.AnswerRepository;

import jakarta.transaction.Transactional;

@Service
public class AnswerService 
{

    private AnswerRepository answerRepository;

    public AnswerService(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    };

    public Answer getAnswerById(int answerId)
    {
        return answerRepository.findById(answerId)
                            .orElseThrow(()->new RuntimeException("Answer not found with id = " + answerId));
    }

    
    @Transactional
    public Answer saveAnswer(Answer answer)
    {
        return answerRepository.save(answer);
    }

    @Transactional
    public void deleteAnswer(Answer answer) {
        answerRepository.delete(answer);
    }

    public Answer getAnswerByQuestion(Question question) {
        return answerRepository.findByQuestion(question);
    }
    
}
