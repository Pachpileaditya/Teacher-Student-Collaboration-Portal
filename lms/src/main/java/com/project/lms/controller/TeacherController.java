package com.project.lms.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.lms.DTO.SubjectNamesDTO;
import com.project.lms.DTO.TeacherDTO;
import com.project.lms.DTO.UserDTO;
import com.project.lms.entity.Subject;
import com.project.lms.entity.Teacher;
import com.project.lms.entity.User;
import com.project.lms.entity.Year;
import com.project.lms.service.SubjectService;
import com.project.lms.service.TeacherService;
import com.project.lms.service.UserService;
import com.project.lms.service.YearService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/teachers")
public class TeacherController 
{

    private YearService yearService;
    private TeacherService teacherService;
    private UserService userService;
    private SubjectService subjectService;

    @Autowired
    public TeacherController(YearService yearService,
                            TeacherService teacherService,
                            UserService userService,
                            SubjectService subjectService) {
        this.yearService = yearService;
        this.teacherService = teacherService;
        this.userService = userService;
        this.subjectService = subjectService;
    }



    @GetMapping("/teacher-hello")
    public String getMethodName() {
        return new String( "Hello from teacher controller");
    }


    // CRUD operation on teacher
    // get all teacher
    @GetMapping("/")
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return ResponseEntity.ok(teacherService.getAllTeachers());
    }
    
    
    // get teacher by id
    @GetMapping("/{id}")
    public ResponseEntity<TeacherDTO> getTeacherById(@PathVariable Integer id) {
        Teacher teacher = teacherService.findTeacherById(id);
        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }
        TeacherDTO teacherDTO = convertToTeacherDTO(teacher);
        return ResponseEntity.ok(teacherDTO);
    }

    // Helper method to convert Teacher entity to TeacherDTO
    private TeacherDTO convertToTeacherDTO(Teacher teacher) {
        User user = teacher.getUser();
        UserDTO userDTO = new UserDTO(user.getId(), user.getName(), user.getEmail());
        Set<Integer> yearIds = teacher.getYears()
                                      .stream()
                                      .map(Year::getId)
                                      .collect(Collectors.toSet());
        return new TeacherDTO(teacher.getId(), teacher.getExpertise(), teacher.getTotalPoints(), userDTO, yearIds);
    }

    // update teacher

    @PutMapping("/{id}")
    public ResponseEntity<TeacherDTO> updateTeacher(@PathVariable Integer id, 
                                                    @RequestBody TeacherDTO teacherDTO) {
        // Retrieve existing teacher by id
        Teacher existingTeacher = teacherService.findTeacherById(id);
        if (existingTeacher == null) {
            return ResponseEntity.notFound().build();
        }

        // Update basic teacher fields
        existingTeacher.setExpertise(teacherDTO.getExpertise());
        existingTeacher.setTotalPoints(teacherDTO.getTotalPoints());

        // Update nested User details
        if (teacherDTO.getUser() != null) {
            User user = existingTeacher.getUser();
            if (user == null) {
                user = new User();
            }
            user.setName(teacherDTO.getUser().getName());
            user.setEmail(teacherDTO.getUser().getEmail());
            // Optionally update other fields like password if needed.
            existingTeacher.setUser(user);
        }

        // Update associated Years
        Set<Year> updatedYears = new HashSet<>();
        if (teacherDTO.getYearIds() != null) {
            for (Integer yearId : teacherDTO.getYearIds()) {
                Year year = yearService.findYearById(yearId);
                if (year != null) {
                    updatedYears.add(year);
                } else {
                    // Optionally, handle missing Year (e.g., log error or return a bad request)
                    return ResponseEntity.badRequest()
                        .body(null);
                }
            }
        }
        existingTeacher.setYears(updatedYears);

        // Save the updated teacher entity
        Teacher savedTeacher = teacherService.saveTeacher(existingTeacher);

        // Convert the saved entity to DTO for response
        TeacherDTO responseDTO = convertToTeacherDTO(savedTeacher);
        return ResponseEntity.ok(responseDTO);
    }

    // delete teacher
    @DeleteMapping("/{teacherId}")
    public ResponseEntity<?> deleteTeacher(@PathVariable int teacherId)
    {
        if(!teacherService.teacherExits(teacherId))
        {
            return ResponseEntity.badRequest().body("Teacher not exites wiht id = " + teacherId);
        }

        teacherService.deleteTeacherById(teacherId);

        return ResponseEntity.ok("Teacher deleted with id = " + teacherId);

    }

    // teacher and year CRUD

    // get all years
    @GetMapping("/years")
    public ResponseEntity<List<Year>> getAllYears() 
    {
        System.out.println(yearService);
        List<Year> theYears = yearService.getAllYears();
        return ResponseEntity.ok(theYears);
    }
    

    // add years
    @PostMapping("/years/{teacherId}")
    public ResponseEntity<String> addYears(@PathVariable int teacherId, @RequestBody List<Year> years) {
        //TODO: process POST request

        if(!teacherService.teacherExits(teacherId))
        {
            return ResponseEntity.badRequest().body("Teacher does not exits.");
        }
        
        // Retrieve the teacher entity
        Teacher teacher = teacherService.findTeacherById(teacherId);

        for(Year year : years)
        {
            Year theYear = yearService.findYearById(year.getId());
            if(theYear == null)
            {
                return ResponseEntity.badRequest().body("year with year id = " + year.getId() + " not found");
            }

            teacher.addYear(theYear);
        }

        teacherService.saveTeacher(teacher);

        
        return ResponseEntity.ok("Years added successfully.");
    }

    // delete year
    @DeleteMapping("/years/{teacherId}")
    public ResponseEntity<String> deleteYears(@PathVariable int teacherId, @RequestBody List<Year> years) {
        //TODO: process POST request

        if(!teacherService.teacherExits(teacherId))
        {
            return ResponseEntity.badRequest().body("Teacher does not exits.");
        }
        
        // Retrieve the teacher entity
        Teacher teacher = teacherService.findTeacherById(teacherId);

        for(Year year : years)
        {
            Year theYear = yearService.findYearById(year.getId());
            if(theYear == null)
            {
                return ResponseEntity.badRequest().body("year with year id = " + year.getId() + " not found");
            }

            teacher.removeYear(theYear);
        }

        teacherService.saveTeacher(teacher);

        
        return ResponseEntity.ok("Year/years deleted successfully.");
    }

    // add subjects
    @PostMapping("/add-subjects/{teacherId}")
    public ResponseEntity<?> addSubjects(@PathVariable int teacherId, @RequestBody List<SubjectNamesDTO> subjectNamesDTOs) 
    {
        Set<Subject> theSubjects = new HashSet();
        Teacher theTeacher = teacherService.findTeacherById(teacherId);
        if(theTeacher == null)
        {
            return ResponseEntity.badRequest().body("Teacher not found with id = " + teacherId);
        }
        for(SubjectNamesDTO sn : subjectNamesDTOs)
        {
            int subjectId = sn.getId();
            Subject subject = subjectService.getSubjectById(subjectId);
            theSubjects.add(subject);
        }

        theTeacher.setSubjects(theSubjects);
        teacherService.saveTeacher(theTeacher);
        
        return ResponseEntity.ok("Subjects added Successfully");
    }

    
    



    

    
}
