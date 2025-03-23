package com.project.lms.service;

import org.springframework.stereotype.Service;

import com.project.lms.entity.Student;
import com.project.lms.entity.User;
import com.project.lms.repo.AnswerRepository;
import com.project.lms.repo.StudentRepository;

@Service
public class StudentService 
{

    private final AnswerRepository answerRepository;

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository, AnswerRepository answerRepository) {
        this.studentRepository = studentRepository;
        this.answerRepository = answerRepository;
    }

    public Student getStudentById(int studentId)
    {
        
        return studentRepository.findById(studentId)
                                        .orElseThrow(()-> new RuntimeException("Student not found with id = " + studentId));
    }
    

    public Student getStudentByUser(User user)
    {

        return studentRepository.findByUser(user);
        
    }

    public void saveStudent(Student student)
    {
        studentRepository.save(student);
    }
}
