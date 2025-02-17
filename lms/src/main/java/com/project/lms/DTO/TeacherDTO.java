package com.project.lms.DTO;

import java.util.Set;

public class TeacherDTO {
    private Integer id;
    private String expertise;
    private Integer totalPoints;
    private UserDTO user;
    private Set<Integer> yearIds;

    // Default constructor
    public TeacherDTO() {
    }

    // Parameterized constructor
    public TeacherDTO(Integer id, String expertise, Integer totalPoints, UserDTO user, Set<Integer> yearIds) {
        this.id = id;
        this.expertise = expertise;
        this.totalPoints = totalPoints;
        this.user = user;
        this.yearIds = yearIds;
    }

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Set<Integer> getYearIds() {
        return yearIds;
    }

    public void setYearIds(Set<Integer> yearIds) {
        this.yearIds = yearIds;
    }
}
