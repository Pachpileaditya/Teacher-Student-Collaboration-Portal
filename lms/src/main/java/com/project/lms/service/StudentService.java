package com.project.lms.service;

import org.springframework.stereotype.Service;

import com.project.lms.entity.Student;
import com.project.lms.repo.StudentRepository;

@Service
public class StudentService 
{

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student getStudentById(int studentId)
    {
        
        return studentRepository.findById(studentId)
                                        .orElseThrow(()-> new RuntimeException("Student not found with id = " + studentId));
    }
    
}
