package com.project.lms.DTO;

public class ContentSubjectDTO 
{
    private UnitDTO unitNo;
    private String title;
    private String description;
    private String fileURL;
    public ContentSubjectDTO() {
    }
    

    public ContentSubjectDTO(UnitDTO unitNo, String title, String description, String fileURL) {
        this.unitNo = unitNo;
        this.title = title;
        this.description = description;
        this.fileURL = fileURL;
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

    public UnitDTO getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(UnitDTO unitNo) {
        this.unitNo = unitNo;
    }

}
