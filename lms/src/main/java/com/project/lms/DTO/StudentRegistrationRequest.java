package com.project.lms.DTO;

import java.time.LocalDate;

public class StudentRegistrationRequest {
            private String name;
            private String email;
            private String password;
            private Integer year;
            private LocalDate date;
            private String gender;
            private String address;
            private int pincode;
            private String state;
        
            // Getters and Setters
            public String getName() { return name; }
            public void setName(String name) { this.name = name; }
        
            public String getEmail() { return email; }
            public void setEmail(String email) { this.email = email; }
        
            public String getPassword() { return password; }
            public void setPassword(String password) { this.password = password; }
        
            
        
            public LocalDate getDate() { return date; }
            public void setDate(LocalDate date) { this.date = date; }
        
            public String getGender() { return gender; }
            public void setGender(String gender) { this.gender = gender; }
        
            public String getAddress() { return address; }
            public void setAddress(String address) { this.address = address; }
        
            public int getPincode() { return pincode; }
            public void setPincode(int pincode) { this.pincode = pincode; }
        
            public String getState() { return state; }
            public void setState(String state) { this.state = state; }
    
            public Integer getYear() {
                return year;
            }
            public void setYear(Integer year) {
                this.year = year;
            }
            
        }
