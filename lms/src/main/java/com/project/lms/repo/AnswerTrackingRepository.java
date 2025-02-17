package com.project.lms.repo;


import com.project.lms.entity.AnswerTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerTrackingRepository extends JpaRepository<AnswerTracking, Integer> {
    // Add custom methods as needed
}

