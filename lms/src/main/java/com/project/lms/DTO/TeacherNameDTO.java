package com.project.lms.DTO;

public class TeacherNameDTO 
{
    private Integer teacherId;
    private String name;
    public TeacherNameDTO() {
    }
    public TeacherNameDTO(Integer teacherId, String name) {
        this.teacherId = teacherId;
        this.name = name;
    }
    public Integer getTeacherId() {
        return teacherId;
    }
    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
