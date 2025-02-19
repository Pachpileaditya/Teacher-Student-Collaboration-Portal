package com.project.lms.repo;


import com.project.lms.entity.Question;
import com.project.lms.entity.Student;
import com.project.lms.entity.Subject;
import com.project.lms.entity.Unit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    // Add custom methods as needed
    List<Question> findAllByStudent(Student student);

    List<Question> findAllByStudentAndSubjectAndUnit(Student student, Subject subject, Unit unit);

    List<Question> findAllBySubjectAndUnit(Subject subject, Unit unit);
}

