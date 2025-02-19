package com.project.lms.DTO;

import java.time.LocalDateTime;


public class AnswerDTO 
{

    private Integer answerId;
    private String text;
    private TeacherNameDTO teacherNameDTO;
    private QuestionDTO questionDTO;
    private AnswerTrackingDTO answerTrackingDTO;
    private LocalDateTime createdAt;


    public AnswerDTO() {
    }

    public AnswerDTO(Integer answerId, String text, TeacherNameDTO teacherNameDTO, QuestionDTO questionDTO,
            AnswerTrackingDTO answerTrackingDTO, LocalDateTime createdAt) {
        this.answerId = answerId;
        this.text = text;
        this.teacherNameDTO = teacherNameDTO;
        this.questionDTO = questionDTO;
        this.answerTrackingDTO = answerTrackingDTO;
        this.createdAt = createdAt;
    }

    public Integer getAnswerId() {
        return answerId;
    }
    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    
    public QuestionDTO getQuestionDTO() {
        return questionDTO;
    }
    public void setQuestionDTO(QuestionDTO questionDTO) {
        this.questionDTO = questionDTO;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public TeacherNameDTO getTeacherNameDTO() {
        return teacherNameDTO;
    }
    public void setTeacherNameDTO(TeacherNameDTO teacherNameDTO) {
        this.teacherNameDTO = teacherNameDTO;
    }

    public AnswerTrackingDTO getAnswerTrackingDTO() {
        return answerTrackingDTO;
    }

    public void setAnswerTrackingDTO(AnswerTrackingDTO answerTrackingDTO) {
        this.answerTrackingDTO = answerTrackingDTO;
    }
    
}
