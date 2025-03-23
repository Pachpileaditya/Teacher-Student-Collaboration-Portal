package com.project.lms.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.lms.DTO.SubjectNamesDTO;
import com.project.lms.entity.Subject;
import com.project.lms.entity.Year;
import com.project.lms.service.SubjectService;
import com.project.lms.service.TeacherService;
import com.project.lms.service.YearService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/subjects")
@SecurityRequirement(name = "bearerAuth")
public class SubjectController 
{

    private SubjectService subjectService;
    private YearService yearService;
    private TeacherService teacherService;


    public SubjectController(SubjectService subjectService,
                            YearService yearService,
                            TeacherService teacherService) {
        this.subjectService = subjectService;
        this.yearService = yearService;
        this.teacherService = teacherService;
    }

    // get subject by year
    @GetMapping("/year/{yearId}")
    public ResponseEntity<List<SubjectNamesDTO>> getSubjectByYear(@PathVariable int yearId) {
        Year year = yearService.findYearById(yearId);

        if (year == null) {
            return ResponseEntity.notFound().build();
        }

        List<Subject> subjects = subjectService.getAllSubjectsByYear(year);

        // Convert Subject entities to SubjectNamesDTO
        List<SubjectNamesDTO> subjectDTOs = subjects.stream()
                .map(subject -> new SubjectNamesDTO(subject.getId(), subject.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(subjectDTOs);
    }

    // // get subjects by year
    // @GetMapping("/names/{yearId}")    
    // public ResponseEntity<?> getSubjectsByYear(@PathVariable int yearId) {
    //     if(!yearService.yearExits(yearId))
    //     {
    //         return ResponseEntity.badRequest().body("Year does not exits with yearId = " + yearId);
    //     }
    //     Year theYear = yearService.findYearById(yearId);
    //     List<Subject> theSubjects = subjectService.getAllSubjectsByYear(theYear);

    //     List<SubjectNamesDTO> subjectNames = toSubjectNamesDTOList(theSubjects);

    //     return ResponseEntity.ok(subjectNames);
    // }

}
