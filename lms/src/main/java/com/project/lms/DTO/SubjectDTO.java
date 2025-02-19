package com.project.lms.DTO;

public class SubjectDTO 
{

    private Integer subjectId;
    private String subjectName;
    public SubjectDTO() {
    }
    public SubjectDTO(Integer subjectId, String subjectName) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }
    public Integer getSubjectId() {
        return subjectId;
    }
    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }
    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    
    
}
