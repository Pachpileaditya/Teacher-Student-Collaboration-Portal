package com.project.lms.controller;


import com.project.lms.entity.Teacher;
import com.project.lms.entity.Student;
import com.project.lms.entity.User;
import com.project.lms.entity.Role;
import com.project.lms.repo.TeacherRepository;
import com.project.lms.repo.StudentRepository;
import com.project.lms.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public AuthController(UserRepository userRepository,
                          TeacherRepository teacherRepository,
                          StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    // DTO for teacher registration
    public static class TeacherRegistrationRequest {
        private String name;
        private String email;
        private String password;
        private String expertise;

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public String getExpertise() { return expertise; }
        public void setExpertise(String expertise) { this.expertise = expertise; }
    }

    // DTO for student registration
    public static class StudentRegistrationRequest {
        private String name;
        private String email;
        private String password;
        private Integer year; // student's academic year

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public Integer getYear() { return year; }
        public void setYear(Integer year) { this.year = year; }
    }

    // DTO for admin registration
    public static class AdminRegistrationRequest {
        private String name;
        private String email;
        private String password;

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<?> registerTeacher(@RequestBody TeacherRegistrationRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken.");
        }

        // Create and save the user with role TEACHER
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());  // In production, encrypt the password!
        user.setRole(Role.TEACHER);
        userRepository.save(user);

        // Create and save the teacher entity linked to the user
        Teacher teacher = new Teacher();
        teacher.setUser(user);
        teacher.setExpertise(request.getExpertise());
        teacherRepository.save(teacher);

        return ResponseEntity.ok(teacher);
    }

    @PostMapping("/register/student")
    public ResponseEntity<?> registerStudent(@RequestBody StudentRegistrationRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken.");
        }

        // Create and save the user with role STUDENT
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(Role.STUDENT);
        userRepository.save(user);

        // Create and save the student entity linked to the user
        Student student = new Student();
        student.setUser(user);
        student.setYear(request.getYear());
        student.setIsPassout(false);
        studentRepository.save(student);

        return ResponseEntity.ok(student);
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminRegistrationRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email is already taken.");
        }

        // Create and save the user with role ADMIN
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        // If you have an Admin entity, create and save it here.
        // For this example, we're using the User entity for admin registration.

        return ResponseEntity.ok(user);
    }
}
