package com.project.lms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.lms.entity.Student;
import com.project.lms.entity.User;
import com.project.lms.repo.UserRepository;

@Service
public class UserService 
{
    private UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User findUserById(Integer id) {
        return userRepository.findById(id)
                            .orElseThrow(()->new RuntimeException("User details does not exits with uderid = " + id));
    }

    
    
}
