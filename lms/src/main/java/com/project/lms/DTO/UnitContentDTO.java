package com.project.lms.DTO;

public class UnitContentDTO {

    private Integer id;
    private String title;
    private String url;

    public UnitContentDTO()
    {
    }

    public UnitContentDTO(Integer id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    
    
}
