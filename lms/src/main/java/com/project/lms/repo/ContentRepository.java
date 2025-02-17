package com.project.lms.repo;


import com.project.lms.entity.Content;
import com.project.lms.entity.Subject;
import com.project.lms.entity.Teacher;
import com.project.lms.entity.Unit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {
    // Add custom methods as needed
    Content findByTeacherAndSubjectAndUnit(Teacher teacher, Subject subject, Unit unit);
}

