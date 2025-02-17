package com.project.lms.DTO;

public class ContentDTO 
{
    private Integer id;
    private String title;
    private String description;
    private String fileURL;
    private TeacherContentDTO teacher;
    private SubjectNamesDTO subject;
    private UnitDTO unit;

    public ContentDTO() {
    }

    

    public ContentDTO(Integer id, String title, String description, String fileURL, TeacherContentDTO teacher,
            SubjectNamesDTO subject, UnitDTO unit) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fileURL = fileURL;
        this.teacher = teacher;
        this.subject = subject;
        this.unit = unit;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public TeacherContentDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherContentDTO teacher) {
        this.teacher = teacher;
    }

    public SubjectNamesDTO getSubject() {
        return subject;
    }

    public void setSubject(SubjectNamesDTO subject) {
        this.subject = subject;
    }



    public UnitDTO getUnit() {
        return unit;
    }



    public void setUnit(UnitDTO unit) {
        this.unit = unit;
    }

    
    

    

    
}
