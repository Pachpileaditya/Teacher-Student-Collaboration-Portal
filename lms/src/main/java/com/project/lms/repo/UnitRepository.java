package com.project.lms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.lms.entity.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Integer> {
    
}
