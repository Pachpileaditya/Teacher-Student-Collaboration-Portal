package com.project.lms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.lms.entity.Question;
import com.project.lms.entity.Student;
import com.project.lms.entity.Subject;
import com.project.lms.entity.Unit;
import com.project.lms.repo.QuestionRepository;

import jakarta.transaction.Transactional;

@Service
public class QuestionService 
{

    private QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Transactional
    public Question saveQuestion(Question question)
    {

        return questionRepository.save(question);

    }

    public Question getQuestionById(int questionId) {
        return questionRepository.findById(questionId)
                                .orElseThrow(()->new RuntimeException("Question not found with id = " + questionId));

    }

    public void deleteQuestion(Question question) {
        questionRepository.delete(question);
    }

    public List<Question> getQuestionByStudent(Student student) {
        return questionRepository.findAllByStudent(student);
    }

    public List<Question> getQuestionByStudentAndSubjectAndUnit(Student student, Subject subject, Unit unit) {
        return questionRepository.findAllByStudentAndSubjectAndUnit(student, subject, unit);
    }

    public List<Question> getQuestionBySubjectAndUnit(Subject subject, Unit unit) {
        return questionRepository.findAllBySubjectAndUnit(subject, unit);
    }
    
}
