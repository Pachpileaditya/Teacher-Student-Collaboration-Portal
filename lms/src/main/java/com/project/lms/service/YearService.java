package com.project.lms.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.lms.entity.Teacher;
import com.project.lms.entity.Year;
import com.project.lms.repo.YearRepository;

@Service
public class YearService 
{

    private YearRepository yearRepository;

   
    @Autowired
    public YearService(YearRepository yearRepository) {
        this.yearRepository = yearRepository;
    }




    public List<Year> getAllYears()
    {
        return yearRepository.findAll();
    }




    public Year findYearById(Integer id) {
        return yearRepository.findById(id)
                            .orElseThrow(()->new RuntimeException("Year not found with id = " + id));
    }

    public boolean yearExits(int yearId)
    {
        Optional<Year> theYear = yearRepository.findById(yearId);
        if(!theYear.isPresent())
        {
            return false;
        }
        return true;
    }




    public List<Year> getYearsByTeacherId(int teacherId) {
        return yearRepository.findByTeachersId(teacherId);
    }




    public Year getYearByName(Integer i) {
        return yearRepository.findByName(i);
    }

    public Set<Year> getYearSet(Set<Integer> yearIds) {
        Set<Year> tempYears = new HashSet<>();
        for (Integer id : yearIds) {
            Year dbYear = yearRepository.findByName(id);
            tempYears.add(dbYear);
        }
        return tempYears;
    }


    
    
}
