package com.project.lms.service;

import org.springframework.stereotype.Service;

import com.project.lms.entity.Unit;
import com.project.lms.repo.UnitRepository;

@Service
public class UnitService 
{

    private UnitRepository unitRepository;

    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public Unit getUnit(Integer unitId)
    {
        return unitRepository.findById(unitId)
                            .orElseThrow(()->new RuntimeException("Unit not found for id = " + unitId));
    }
    
}
