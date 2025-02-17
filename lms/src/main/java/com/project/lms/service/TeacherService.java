package com.project.lms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.lms.entity.Teacher;
import com.project.lms.repo.TeacherRepository;

import jakarta.transaction.Transactional;

@Service
public class TeacherService 
{

    private TeacherRepository teacherRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public boolean teacherExits(int teacherId)
    {
        Optional<Teacher> theTeacher = teacherRepository.findById(teacherId);

        if(!theTeacher.isPresent())
        {
            return false;
        }
        else{
            return true;
        }

    }

    public Teacher findTeacherById(int teacherId) {
        return teacherRepository.findById(teacherId)
                        .orElseThrow(()->new RuntimeException("Teacher does not exits."));
    }

    @Transactional
    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public void deleteTeacherById(int teacherId) {
        teacherRepository.deleteById(teacherId);
    }

    
    
}
