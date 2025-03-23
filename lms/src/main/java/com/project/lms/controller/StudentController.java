package com.project.lms.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.lms.DTO.AnswerDTO;
import com.project.lms.DTO.AnswerTrackingDTO;
import com.project.lms.DTO.QuestionDTO;
import com.project.lms.DTO.StudentDTO;
import com.project.lms.DTO.SubjectDTO;
import com.project.lms.DTO.TeacherNameDTO;
import com.project.lms.DTO.UnitDTO;
import com.project.lms.DTO.YearDTO;
import com.project.lms.entity.Answer;
import com.project.lms.entity.AnswerTracking;
import com.project.lms.entity.Question;
import com.project.lms.entity.Student;
import com.project.lms.entity.Subject;
import com.project.lms.entity.Unit;
import com.project.lms.entity.Year;
import com.project.lms.service.AnswerService;
import com.project.lms.service.AnswerTrackingService;
import com.project.lms.service.QuestionService;
import com.project.lms.service.StudentService;
import com.project.lms.service.SubjectService;
import com.project.lms.service.UnitService;
import com.project.lms.service.YearService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;






@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/students")
@SecurityRequirement(name = "bearerAuth")
public class StudentController {

    private SubjectService subjectService;
    private YearService yearService;
    private StudentService studentService; 
    private UnitService unitService;
    private QuestionService questionService;
    private AnswerService answerService;
    private AnswerTrackingService answerTrackingService;

    public StudentController(SubjectService subjectService,
            YearService yearService, UnitService unitService, StudentService studentService, QuestionService questionService2,
            AnswerService answerService, AnswerTrackingService answerTrackingService) {
        this.subjectService = subjectService;
        this.yearService = yearService;
        this.unitService = unitService;
        this.questionService = questionService2;
        this.studentService = studentService;
        this.answerService = answerService;
        this.answerTrackingService = answerTrackingService;
    }

    @GetMapping("/student-hello")
    public String getMethodName() {
        return new String("Hello from student controller");
    }


    // get student year
    @GetMapping("/year/{studentId}")
    public ResponseEntity<?> getStudentYeaResponseEntity(@PathVariable int studentId)
    {
        Student std = studentService.getStudentById(studentId);
        Year year = yearService.findYearById(std.getId());
        YearDTO yearDTO = new YearDTO();

        yearDTO.setId(year.getId());
        yearDTO.setYear(year.getName());
        return ResponseEntity.ok(yearDTO);
    }

    // get student subjects 
    @GetMapping("/subjects/{studentId}")
    public ResponseEntity<?> getStudentSubjectsEntity(@PathVariable int studentId) 
    {
        Student student = studentService.getStudentById(studentId);
        Set<Subject> subjects = student.getSubjects();
        List<SubjectDTO> subjectDTOs = convertSubjectToDTOList(subjects);
        return ResponseEntity.ok(subjectDTOs);
    }
    
    public List<SubjectDTO> convertSubjectToDTOList(Set<Subject> subjects) {
        return subjects.stream()
                       .map(subject -> new SubjectDTO(subject.getId(), subject.getName()))
                       .collect(Collectors.toList());
    }

    // add student subjects
    @PostMapping("/subjects/add/{studentId}")
    public ResponseEntity<?> addSubject(@PathVariable int studentId, @RequestBody List<SubjectDTO> subjectDTOs) 
    {
        Student student = studentService.getStudentById(studentId);
        Set<Subject> studentSubjects = student.getSubjects();
        for(SubjectDTO s : subjectDTOs)
        {
            Subject subject = subjectService.getSubjectById(s.getSubjectId());
            studentSubjects.add(subject);
        }
        studentService.saveStudent(student);

        return ResponseEntity.ok("Subjects added successfully");
    }

    // delete subject by student
    @DeleteMapping("/subject/{studentId}/{subjectId}")
    public ResponseEntity<?> deleteSubject(@PathVariable int studentId, @PathVariable int subjectId)
    {
        Student student = studentService.getStudentById(studentId);
        Subject subject = subjectService.getSubjectById(subjectId);
        student.getSubjects().remove(subject);
        return ResponseEntity.ok("Subject remove successfully -> " + subject.getName());
    }

    
    
    


    // read subject content from subject controller

    // get all questions ask by student
    @GetMapping("/questions/{studentId}")
    public ResponseEntity<?> getAllQuestions(@PathVariable int studentId) 
    {
        Student student = studentService.getStudentById(studentId);
        List<Question> questions = questionService.getQuestionByStudent(student);
        if(questions==null)
        {
            return ResponseEntity.noContent().build();
        }

        QuestionDTO questionDTO = new QuestionDTO();

        List<QuestionDTO> questionDTOs = convertToQuestionDTO(questions);
        return ResponseEntity.ok(questionDTOs);
    }

    public List<QuestionDTO> convertToQuestionDTO(List<Question> questions) {
        return questions.stream()
                .map(q -> new QuestionDTO(
                        q.getId(),
                        q.getText(),
                        new StudentDTO(q.getStudent().getId(), q.getStudent().getUser().getName()),
                        new SubjectDTO(q.getSubject().getId(), q.getSubject().getName()),
                        new UnitDTO(q.getUnit().getUnitNo())))
                .collect(Collectors.toList());
    }
    

    // get question ask by student unit wise for subject
    @GetMapping("/question/{studentId}/{subjectId}/{unitId}")
    public ResponseEntity<?> getMethodName(@PathVariable int studentId,
                                            @PathVariable int subjectId,
                                            @PathVariable int unitId) 
    {
        Student student = studentService.getStudentById(studentId);
        Subject subject = subjectService.getSubjectById(subjectId);
        Unit unit = unitService.getUnit(unitId);

        List<Question> questions = questionService.getQuestionByStudentAndSubjectAndUnit(student, subject, unit);
        if(questions==null)
        {
            return ResponseEntity.badRequest().body("Questions not found");
        }

        QuestionDTO questionDTO = new QuestionDTO();

        List<QuestionDTO> questionDTOs = convertToQuestionDTO(questions);
        return ResponseEntity.ok(questionDTOs);
    }

    // get questions ask by students unit wise for subject
    @GetMapping("/question/{subjectId}/{unitId}")
    public ResponseEntity<?> getCommanUnitWiseQuestions(
                                            @PathVariable int subjectId,
                                            @PathVariable int unitId) 
    {
        Subject subject = subjectService.getSubjectById(subjectId);
        Unit unit = unitService.getUnit(unitId);

        List<Question> questions = questionService.getQuestionBySubjectAndUnit(subject, unit);
        if(questions==null)
        {
            return ResponseEntity.badRequest().body("Questions not found");
        }
        QuestionDTO questionDTO = new QuestionDTO();

        List<QuestionDTO> questionDTOs = convertToQuestionDTO(questions);
        return ResponseEntity.ok(questionDTOs);
    }
     
    

    // ask question 
    @PostMapping("/question/{studentId}/{subjectId}/{unitId}")
    public ResponseEntity<QuestionDTO> askQuestion(@RequestBody String text,
                                            @PathVariable int studentId,
                                            @PathVariable int subjectId,
                                            @PathVariable int unitId) {

        Question question = new Question();
        Student student = studentService.getStudentById(studentId);
        Subject subject = subjectService.getSubjectById(subjectId);
        Unit unit = unitService.getUnit(unitId);
        question.setText(text);

        question.setSubject(subject);
        question.setStudent(student);
        question.setUnit(unit);


        question = questionService.saveQuestion(question);
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(question.getId());
        questionDTO.setText(question.getText());
        questionDTO.setStudentDTO(new StudentDTO(question.getStudent().getId(), question.getStudent().getUser().getName()));
        questionDTO.setSubjectDTO(new SubjectDTO(question.getSubject().getId(), question.getSubject().getName()));
        questionDTO.setUnitDTO(new UnitDTO(question.getUnit().getUnitNo()));

        return ResponseEntity.ok(questionDTO);
        
    }

    // update question
    @PutMapping("question/{questionId}")
    public String updateQuestioString(@PathVariable int questionId, @RequestBody String questionText) {

        Question question = questionService.getQuestionById(questionId);
        question.setText(questionText);
        questionService.saveQuestion(question);

        return "Question updated successfully.";
    }

    // delete question
    @DeleteMapping("/question/{questionId}")
    public String deleteQuestion(@PathVariable int questionId)
    {
        Question question = questionService.getQuestionById(questionId);
        questionService.deleteQuestion(question);
        return "Question delete with id = " + questionId;
    }


    // read answer for questions
    @GetMapping("/answer/{questionId}")
    public ResponseEntity<AnswerDTO> getAnswer(@PathVariable int questionId) {
        
        Question question = questionService.getQuestionById(questionId);
        Answer answer = answerService.getAnswerByQuestion(question);
        AnswerDTO answerDTO = convertTOAnswerDTO(Arrays.asList(answer)).get(0);
        return ResponseEntity.ok(answerDTO);
    }

    public List<AnswerDTO> convertTOAnswerDTO(List<Answer> answers)
    {
        return answers.stream()
                        .map(answer -> new AnswerDTO(
                            answer.getId(),
                            answer.getText(),
                            new TeacherNameDTO(
                                answer.getTeacher().getId(),
                                answer.getTeacher().getUser().getName()),
                            new QuestionDTO(
                                answer.getQuestion().getId(),
                                answer.getQuestion().getText(),
                                new StudentDTO(answer.getQuestion().getStudent().getId(), answer.getQuestion().getStudent().getUser().getName()),
                                new SubjectDTO(answer.getQuestion().getSubject().getId(), answer.getQuestion().getSubject().getName()),
                                new UnitDTO(answer.getQuestion().getUnit().getUnitNo())),
                            new AnswerTrackingDTO(
                                answer.getTracking().getId(),
                                answer.getTracking().getLikes(),
                                answer.getTracking().getViews()),
                            answer.getCreatedAt()
        ))
        .collect(Collectors.toList());
    }

    @PostMapping("/like-answer/{answerId}")
    public String likeAnswer(@PathVariable int answerId) 
    {

        Answer answer = answerService.getAnswerById(answerId);
        AnswerTracking answerTracking = answer.getTracking();
        int likes = answerTracking.getLikes();
        likes++;
        answerTracking.setLikes(likes);
        answerTrackingService.saveAnswerTracking(answerTracking);

        return "Answer liked";
    }

}
