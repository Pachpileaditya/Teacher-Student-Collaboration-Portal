package com.project.lms.DTO;

public class AnswerTrackingDTO {

    private Integer answerTrackingId;
    private Integer likes;
    private Integer views;
    public AnswerTrackingDTO() {
    }
    public AnswerTrackingDTO(Integer answerTrackingId, Integer likes, Integer views) {
        this.answerTrackingId = answerTrackingId;
        this.likes = likes;
        this.views = views;
    }
    public Integer getAnswerTrackingId() {
        return answerTrackingId;
    }
    public void setAnswerTrackingId(Integer answerTrackingId) {
        this.answerTrackingId = answerTrackingId;
    }
    public Integer getLikes() {
        return likes;
    }
    public void setLikes(Integer likes) {
        this.likes = likes;
    }
    public Integer getViews() {
        return views;
    }
    public void setViews(Integer views) {
        this.views = views;
    }

    

}
