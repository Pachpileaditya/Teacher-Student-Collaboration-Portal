package com.project.lms.controller;

import com.project.lms.entity.Teacher;
import com.project.lms.entity.Student;
import com.project.lms.entity.User;
import com.project.lms.entity.Year;
import com.project.lms.DTO.LoginRequest;
import com.project.lms.DTO.StudentRegistrationRequest;
import com.project.lms.DTO.TeacherRegistrationRequest;
import com.project.lms.entity.Role;
import com.project.lms.repo.StudentRepository;
import com.project.lms.repo.UserRepository;
import com.project.lms.service.TeacherService;
import com.project.lms.service.YearService;
import com.project.lms.utils.JwtUtil;

import java.util.Optional;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final TeacherService teacherService;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final YearService yearService;

    public AuthController(UserRepository userRepository,
                          TeacherService teacherService,
                          StudentRepository studentRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil,
                          YearService yearService) {
        this.userRepository = userRepository;
        this.teacherService = teacherService;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.yearService = yearService;
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<?> registerTeacher(@RequestBody TeacherRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password
        user.setRole(Role.TEACHER);
        user.setDob(request.getDate());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());
        user.setPincode(request.getPincode());
        user.setState(request.getState());

        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setExpertise(request.getExpertise());
        Set<Year> myYears = yearService.getYearSet(request.getYears());
        teacher.setYears(myYears);
        teacherService.saveTeacher(teacher);

        return ResponseEntity.ok("Teacher registered successfully!");
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestBody StudentRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password
        user.setRole(Role.STUDENT);
        user.setDob(request.getDate());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());
        user.setPincode(request.getPincode());
        user.setState(request.getState());

        userRepository.save(user);

        Student student = new Student();
        student.setUser(user);
        if (yearService.yearExits(request.getYear())) {
            student.setYear(request.getYear());
        } else {
            return ResponseEntity.badRequest().body("Invalid year selection.");
        }
        student.setIsPassout(false);
        studentRepository.save(student);

        return ResponseEntity.ok("Student registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                // Convert User to UserDetails
                UserDetails userDetails = org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name()) // Convert enum Role to String
                        .build();

                String token = jwtUtil.generateToken(userDetails);
                return ResponseEntity.ok("JWT Token: " + token);
            }
        }
        return ResponseEntity.badRequest().body("Invalid email or password.");
    }
}














// package com.project.lms.controller;
// import com.project.lms.entity.Teacher;
// import com.project.lms.entity.Student;
// import com.project.lms.entity.User;
// import com.project.lms.entity.Year;
// import com.project.lms.exception.YearNotFoundException;
// import com.project.lms.DTO.LoginRequest;
// import com.project.lms.entity.Role;
// import com.project.lms.repo.StudentRepository;
// import com.project.lms.repo.UserRepository;
// // import com.project.lms.utils.JwtUtil;

// import java.time.LocalDate;
// import java.util.HashSet;
// import java.util.Optional;
// import java.util.Set;

// import org.springframework.http.ResponseEntity;
// // import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.web.bind.annotation.*;

// import com.project.lms.service.TeacherService;
// import com.project.lms.service.YearService; 



// @RestController
// @RequestMapping("/api/auth")
// public class AuthController 
// {

//     private final UserRepository userRepository;
//     private final TeacherService teacherService;
//     private final StudentRepository studentRepository;
//     // private final PasswordEncoder passwordEncoder;
//     // private final JwtUtil jwtUtil;
//     private final YearService yearService;


    
//     public AuthController(UserRepository userRepository,
//                           TeacherService teacherService,
//                           StudentRepository studentRepository,
//                         //   PasswordEncoder passwordEncoder, 
//                         //   JwtUtil jwtUtil,
//                           YearService yearService) {
//         this.userRepository = userRepository;
//         this.teacherService = teacherService;
//         this.studentRepository = studentRepository;
//         // this.passwordEncoder = passwordEncoder;
//         // this.jwtUtil = jwtUtil;
//         this.yearService = yearService;
//     }

//     // DTO for teacher registration
//     public static class TeacherRegistrationRequest {
//         private String name;
//         private String email;
//         private String password;
//         private String expertise;
//         private LocalDate date;
//         private String gender;
//         private String address;
//         private int pincode;
//         private String state;
//         Set<Integer> years;

        
//         // Getters and Setters
        
        
//         public String getName() { return name; }
//         public void setName(String name) { this.name = name; }
    
//         public String getEmail() { return email; }
//         public void setEmail(String email) { this.email = email; }
    
//         public String getPassword() { return password; }
//         public void setPassword(String password) { this.password = password; }
    
//         public String getExpertise() { return expertise; }
//         public void setExpertise(String expertise) { this.expertise = expertise; }
    
//         public LocalDate getDate() { return date; }
//         public void setDate(LocalDate date) { this.date = date; }
    
//         public String getGender() { return gender; }
//         public void setGender(String gender) { this.gender = gender; }
    
//         public String getAddress() { return address; }
//         public void setAddress(String address) { this.address = address; }
    
//         public int getPincode() { return pincode; }
//         public void setPincode(int pincode) { this.pincode = pincode; }
    
//         public String getState() { return state; }
//         public void setState(String state) { this.state = state; }
//         public Set<Integer> getYears() {
//             return years;
//         }
//         public void setYears(Set<Integer> years) {
//             this.years = years;
//         }
        
//     }
    
//     public Set<Year> GetYearSet(Set<Integer> years)
//     {
//         Set<Year> tempYears = new HashSet<>();
//         for(Integer i : years)
//         {
//             Year dbYear = yearService.getYearByName(i);
//             tempYears.add(dbYear);
//         }

//         return tempYears;
//     }
//     public static class StudentRegistrationRequest {
//         private String name;
//         private String email;
//         private String password;
//         private Integer year;
//         private LocalDate date;
//         private String gender;
//         private String address;
//         private int pincode;
//         private String state;
    
//         // Getters and Setters
//         public String getName() { return name; }
//         public void setName(String name) { this.name = name; }
    
//         public String getEmail() { return email; }
//         public void setEmail(String email) { this.email = email; }
    
//         public String getPassword() { return password; }
//         public void setPassword(String password) { this.password = password; }
    
        
    
//         public LocalDate getDate() { return date; }
//         public void setDate(LocalDate date) { this.date = date; }
    
//         public String getGender() { return gender; }
//         public void setGender(String gender) { this.gender = gender; }
    
//         public String getAddress() { return address; }
//         public void setAddress(String address) { this.address = address; }
    
//         public int getPincode() { return pincode; }
//         public void setPincode(int pincode) { this.pincode = pincode; }
    
//         public String getState() { return state; }
//         public void setState(String state) { this.state = state; }

//         public Integer getYear() {
//             return year;
//         }
//         public void setYear(Integer year) {
//             this.year = year;
//         }
        
//     }
    
//     public static class AdminRegistrationRequest {
//         private String name;
//         private String email;
//         private String password;
//         private LocalDate date;
//         private String gender;
//         private String address;
//         private int pincode;
//         private String state;
    
//         // Getters and Setters
//         public String getName() { return name; }
//         public void setName(String name) { this.name = name; }
    
//         public String getEmail() { return email; }
//         public void setEmail(String email) { this.email = email; }
    
//         public String getPassword() { return password; }
//         public void setPassword(String password) { this.password = password; }
    
//         public LocalDate getDate() { return date; }
//         public void setDate(LocalDate date) { this.date = date; }
    
//         public String getGender() { return gender; }
//         public void setGender(String gender) { this.gender = gender; }
    
//         public String getAddress() { return address; }
//         public void setAddress(String address) { this.address = address; }
    
//         public int getPincode() { return pincode; }
//         public void setPincode(int pincode) { this.pincode = pincode; }
    
//         public String getState() { return state; }
//         public void setState(String state) { this.state = state; }
//     }

//     @PostMapping("/register/teacher")
//     public ResponseEntity<?> registerTeacher(@RequestBody TeacherRegistrationRequest request) {
//         // Check if email already exists
//         if (userRepository.existsByEmail(request.getEmail())) {
//             return ResponseEntity.badRequest().body("Email is already taken.");
//         }

//         // Create and save the user with role TEACHER
//         User user = new User();
//         user.setName(request.getName());
//         user.setEmail(request.getEmail());
//         // user.setPassword(passwordEncoder.encode(request.getPassword()));
//         user.setPassword(request.getPassword());// Encrypt password
//         user.setRole(Role.TEACHER);
//         user.setDob(request.date);
//         user.setGender(request.getGender());
//         user.setAddress(request.getAddress());
//         user.setPincode(request.getPincode());
//         user.setState(request.getState());

//         // Create and save the teacher entity linked to the user
//         Teacher teacher = new Teacher();
//         teacher.setUser(user);
//         teacher.setExpertise(request.getExpertise());
//         Set<Year> myYears = yearService.getYearSet(request.getYears());
//         System.out.println(myYears);
//         System.out.println();
//         teacher.setYears(myYears);
//         System.out.println(teacher);
//         teacherService.saveTeacher(teacher);

//         return ResponseEntity.ok(teacher);
//     }

//     @PostMapping("/register/student")
//     public ResponseEntity<?> registerStudent(@RequestBody StudentRegistrationRequest request) {
//         // Check if email already exists
//         if (userRepository.existsByEmail(request.getEmail())) {
//             return ResponseEntity.badRequest().body("Email is already taken.");
//         }

//         // Create and save the user with role TEACHER
//         User user = new User();
//         user.setName(request.getName());
//         user.setEmail(request.getEmail());
//         // user.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password
//         user.setPassword(request.getPassword());
//         user.setRole(Role.STUDENT);
//         user.setDob(request.date);
//         user.setGender(request.getGender());
//         user.setAddress(request.getAddress());
//         user.setPincode(request.getPincode());
//         user.setState(request.getState());

//         userRepository.save(user);

//         // Create and save the student entity linked to the user
//         Student student = new Student();
//         student.setUser(user);
//         Integer myYear = request.getYear();
        
//         if(yearService.yearExits(myYear))
//         {
//             student.setYear(myYear);
//         }
//         else{
//             throw new YearNotFoundException("Year not found with id = " + myYear);
//         }
//         student.setIsPassout(false);
//         studentRepository.save(student);

//         return ResponseEntity.ok(student);
//     }

//     @PostMapping("/register/admin")
//     public ResponseEntity<?> registerAdmin(@RequestBody AdminRegistrationRequest request) {
//         // Check if email already exists
//         if (userRepository.existsByEmail(request.getEmail())) {
//             return ResponseEntity.badRequest().body("Email is already taken.");
//         }

//         // Create and save the user with role TEACHER
//         User user = new User();
//         user.setName(request.getName());
//         user.setEmail(request.getEmail());
//         // user.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password
//         user.setPassword(request.getPassword());
//         user.setRole(Role.ADMIN);
//         user.setDob(request.date);
//         user.setGender(request.getGender());
//         user.setAddress(request.getAddress());
//         user.setPincode(request.getPincode());
//         user.setState(request.getState());

//         // If you have an Admin entity, create and save it here.
//         // For this example, we're using the User entity for admin registration.

//         return ResponseEntity.ok(user);
//     }


//     @PostMapping("/login")
//     public String login(@RequestBody LoginRequest request) {
//         Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

//         if (userOptional.isPresent()) {
//             User user = userOptional.get();
            
//             // // Verify password
//             // if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//             //     // Generate JWT token
//             //     Teacher theTeacher = teacherService.getTeacherByUser(user);
//             //     return jwtUtil.generateToken(user);
//             // }

//             // send teacherDTO
//         }
//         return "Invalid email or password";
//     }
    

// }
