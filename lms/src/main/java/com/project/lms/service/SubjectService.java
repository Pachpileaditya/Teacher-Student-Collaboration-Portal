package com.project.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.lms.entity.Subject;
import com.project.lms.entity.Teacher;
import com.project.lms.entity.Year;
import com.project.lms.repo.SubjectRepository;

@Service
public class SubjectService 
{

    private SubjectRepository subjectRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getAllSubjectsByYear(Year theYear)
    {
        List<Subject> subjects = subjectRepository.findByYear(theYear);

        return subjects;
        
    }

    public Subject getSubjectById(int subjectId)
    {
        return subjectRepository.findById(subjectId)
                        .orElseThrow(()->new RuntimeException("Subject not found eith id = " + subjectId));
    }

    public List<Subject> getAllSubjectsByTeacherAndYear(Teacher teacher, Year year)
    {

        return subjectRepository.findAllByTeachersContainingAndYear(teacher, year);

    }

    
    
}
