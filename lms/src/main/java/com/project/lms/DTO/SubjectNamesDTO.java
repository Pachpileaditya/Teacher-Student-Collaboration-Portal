package com.project.lms.DTO;

public class SubjectNamesDTO 
{

    private Integer id;
    private String name;

    public SubjectNamesDTO() {
    }

    public SubjectNamesDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
