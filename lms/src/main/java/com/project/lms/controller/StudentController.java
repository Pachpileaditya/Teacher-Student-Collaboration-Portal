package com.project.lms.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.lms.DTO.SubjectNamesDTO;
import com.project.lms.entity.Subject;
import com.project.lms.entity.Year;
import com.project.lms.service.SubjectService;
import com.project.lms.service.YearService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private SubjectService subjectService;
    private YearService yearService;

    public StudentController(SubjectService subjectService,
            YearService yearService) {
        this.subjectService = subjectService;
        this.yearService = yearService;
    }

    @GetMapping("/student-hello")
    public String getMethodName() {
        return new String("Hello from student controller");
    }

    

}
