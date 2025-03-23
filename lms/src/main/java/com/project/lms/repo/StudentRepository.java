package com.project.lms.repo;


import com.project.lms.entity.Student;
import com.project.lms.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    // Add custom methods as needed
    Student findByUser(User user);
}

