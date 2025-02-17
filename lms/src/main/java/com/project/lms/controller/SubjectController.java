package com.project.lms.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.lms.DTO.SubjectNamesDTO;
import com.project.lms.entity.Subject;
import com.project.lms.entity.Teacher;
import com.project.lms.entity.Year;
import com.project.lms.service.SubjectService;
import com.project.lms.service.TeacherService;
import com.project.lms.service.YearService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/subjects")
public class SubjectController 
{

    private SubjectService subjectService;
    private YearService yearService;
    private TeacherService teacherService;


    @Autowired
    public SubjectController(SubjectService subjectService,
                            YearService yearService,
                            TeacherService teacherService) {
        this.subjectService = subjectService;
        this.yearService = yearService;
        this.teacherService = teacherService;
    }

    // get subjects by year
    @GetMapping("/names/{yearId}")    
    public ResponseEntity<?> getSubjectsByYear(@PathVariable int yearId) {
        if(!yearService.yearExits(yearId))
        {
            return ResponseEntity.badRequest().body("Year does not exits with yearId = " + yearId);
        }
        Year theYear = yearService.findYearById(yearId);
        List<Subject> theSubjects = subjectService.getAllSubjectsByYear(theYear);

        List<SubjectNamesDTO> subjectNames = toSubjectNamesDTOList(theSubjects);

        return ResponseEntity.ok(subjectNames);
    }

    public static List<SubjectNamesDTO> toSubjectNamesDTOList(List<Subject> subjects) {
        return subjects.stream()
            .map(subject -> new SubjectNamesDTO(subject.getId(), subject.getName()))
            .collect(Collectors.toList());
    }

    @GetMapping("/{teacherId}/{yearId}")
    public ResponseEntity<?> getSubjectsByTeachersAndYears(@PathVariable int teacherId, @PathVariable int yearId) {
        
        Teacher teacher = teacherService.findTeacherById(teacherId);
        Year year = yearService.findYearById(yearId);
        if(teacher == null || year == null)
        {
            return ResponseEntity.badRequest().body("Either teacher or Year does not match");
        }

        List<Subject> subjects = subjectService.getAllSubjectsByTeacherAndYear(teacher, year);

        List<SubjectNamesDTO> subjectNamesDTOs = toSubjectNamesDTOList(subjects);

        return ResponseEntity.ok(subjectNamesDTOs);
    }

    // delete subject for teacher
    @DeleteMapping("/teachers/{teacherId}")
    public ResponseEntity<?> deleteSubjectByTeacher(@PathVariable int teacherId, @RequestBody List<SubjectNamesDTO> subjectNamesDTO)
    {
        Teacher teacher = teacherService.findTeacherById(teacherId);
        Set<Subject> subjects = teacher.getSubjects();
        for(SubjectNamesDTO sn : subjectNamesDTO)
        {
            int subjectId = sn.getId();
            Subject theSubject = subjectService.getSubjectById(subjectId);
            subjects.remove(theSubject);
        }
        teacher.setSubjects(subjects);
        teacherService.saveTeacher(teacher);
        return ResponseEntity.ok("Subjects deleted successfully");
    }
    
    
    

    
    

}
