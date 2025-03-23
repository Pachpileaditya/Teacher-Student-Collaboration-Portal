package com.project.lms.DTO;

public class YearDTO 
{
    private Integer id;
    private Integer year;
    public YearDTO() {
    }
    public YearDTO(Integer id, Integer year) {
        this.id = id;
        this.year = year;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    
    
    
}
