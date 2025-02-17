package com.project.lms.entity;


// import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "years")
public class Year {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Although the column name is "name", it is defined as INT in your SQL.
    @Column(unique = true, nullable = false)
    private Integer name;

    @ManyToMany(mappedBy = "years")
    private Set<Teacher> teachers = new HashSet<>();

    @OneToMany(mappedBy = "year")
    private Set<Subject> subjects = new HashSet<>();

    // Constructors, Getters, and Setters

    public Year() {
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public String toString() {
        return "Year [id=" + id + ", name=" + name + ", teachers=" + teachers + ", subjects=" + subjects + "]";
    }

    
}
