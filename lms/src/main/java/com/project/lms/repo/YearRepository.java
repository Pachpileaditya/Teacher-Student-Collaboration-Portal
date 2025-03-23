package com.project.lms.repo;


import com.project.lms.entity.Teacher;
import com.project.lms.entity.Year;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YearRepository extends JpaRepository<Year, Integer> {
    // Add custom methods as needed
    List<Year> findByTeachersId(Integer teacherId);
    Year findByName(Integer name);
}

