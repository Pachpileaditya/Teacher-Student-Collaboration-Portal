package com.project.lms.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.lms.DTO.AnswerDTO;
import com.project.lms.DTO.AnswerTrackingDTO;
import com.project.lms.DTO.ContentSubjectDTO;
import com.project.lms.DTO.QuestionDTO;
import com.project.lms.DTO.StudentDTO;
import com.project.lms.DTO.SubjectDTO;
import com.project.lms.DTO.SubjectNamesDTO;
import com.project.lms.DTO.TeacherDTO;
import com.project.lms.DTO.TeacherNameDTO;
import com.project.lms.DTO.UnitDTO;
import com.project.lms.DTO.UserDTO;
import com.project.lms.entity.Answer;
import com.project.lms.entity.AnswerTracking;
import com.project.lms.entity.Content;
import com.project.lms.entity.Question;
import com.project.lms.entity.Subject;
import com.project.lms.entity.Teacher;
import com.project.lms.entity.Unit;
import com.project.lms.entity.User;
import com.project.lms.entity.Year;
import com.project.lms.service.AnswerService;
import com.project.lms.service.AnswerTrackingService;
import com.project.lms.service.ContentService;
import com.project.lms.service.QuestionService;
import com.project.lms.service.SubjectService;
import com.project.lms.service.TeacherService;
import com.project.lms.service.UnitService;
import com.project.lms.service.UserService;
import com.project.lms.service.YearService;

import java.util.Arrays;
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
public class TeacherController {

    private YearService yearService;
    private TeacherService teacherService;
    private UserService userService;
    private SubjectService subjectService;
    private UnitService unitService;
    private ContentService contentService;
    private QuestionService questionService;
    private AnswerService answerService;
    private AnswerTrackingService answerTrackingService;

    @Autowired
    public TeacherController(YearService yearService,
            TeacherService teacherService,
            UserService userService,
            SubjectService subjectService,
            UnitService unitService,
            ContentService contentService,
            QuestionService questionService,
            AnswerService answerService, 
            AnswerTrackingService answerTrackingService) {
        this.yearService = yearService;
        this.teacherService = teacherService;
        this.userService = userService;
        this.subjectService = subjectService;
        this.unitService = unitService;
        this.contentService = contentService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.answerTrackingService = answerTrackingService;
    }

    @GetMapping("/teacher-hello")
    public String getMethodName() {
        return new String("Hello from teacher controller");
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

    // update teacher profile

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

    // delete teacher account
    @DeleteMapping("/{teacherId}")
    public ResponseEntity<?> deleteTeacher(@PathVariable int teacherId) {
        if (!teacherService.teacherExits(teacherId)) {
            return ResponseEntity.badRequest().body("Teacher not exites wiht id = " + teacherId);
        }

        teacherService.deleteTeacherById(teacherId);

        return ResponseEntity.ok("Teacher deleted with id = " + teacherId);

    }

    // teacher and year CRUD

    // get all years teacher added
    @GetMapping("/years/{teacherId}")
    public ResponseEntity<List<Year>> getAllYears(@PathVariable int teacherId) {
        Teacher teacher = teacherService.findTeacherById(teacherId);
        // create yearDTO
        List<Year> theYears = yearService.getAllYears();
        return ResponseEntity.ok(theYears);
    }

    // add years
    @PostMapping("/years/{teacherId}")
    public ResponseEntity<String> addYears(@PathVariable int teacherId, @RequestBody List<Year> years) {
        // TODO: process POST request

        if (!teacherService.teacherExits(teacherId)) {
            return ResponseEntity.badRequest().body("Teacher does not exits.");
        }

        // Retrieve the teacher entity
        Teacher teacher = teacherService.findTeacherById(teacherId);

        for (Year year : years) {
            Year theYear = yearService.findYearById(year.getId());
            if (theYear == null) {
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
        // TODO: process POST request

        if (!teacherService.teacherExits(teacherId)) {
            return ResponseEntity.badRequest().body("Teacher does not exits.");
        }

        // Retrieve the teacher entity
        Teacher teacher = teacherService.findTeacherById(teacherId);

        for (Year year : years) {
            Year theYear = yearService.findYearById(year.getId());
            if (theYear == null) {
                return ResponseEntity.badRequest().body("year with year id = " + year.getId() + " not found");
            }

            teacher.removeYear(theYear);
        }

        teacherService.saveTeacher(teacher);

        return ResponseEntity.ok("Year/years deleted successfully.");
    }

    // get subject for teacher yearwise
    @GetMapping("/{teacherId}/{yearId}")
    public ResponseEntity<?> getSubjectsByTeachersAndYears(@PathVariable int teacherId, @PathVariable int yearId) {

        Teacher teacher = teacherService.findTeacherById(teacherId);
        Year year = yearService.findYearById(yearId);
        if (teacher == null || year == null) {
            return ResponseEntity.badRequest().body("Either teacher or Year does not match");
        }

        List<Subject> subjects = subjectService.getAllSubjectsByTeacherAndYear(teacher, year);

        List<SubjectNamesDTO> subjectNamesDTOs = toSubjectNamesDTOList(subjects);

        return ResponseEntity.ok(subjectNamesDTOs);
    }

    public static List<SubjectNamesDTO> toSubjectNamesDTOList(List<Subject> subjects) {
        return subjects.stream()
                .map(subject -> new SubjectNamesDTO(subject.getId(), subject.getName()))
                .collect(Collectors.toList());
    }

    // add subjects
    @PostMapping("/add-subjects/{teacherId}")
    public ResponseEntity<?> addSubjects(@PathVariable int teacherId,
            @RequestBody List<SubjectNamesDTO> subjectNamesDTOs) {
        Set<Subject> theSubjects = new HashSet();
        Teacher theTeacher = teacherService.findTeacherById(teacherId);
        if (theTeacher == null) {
            return ResponseEntity.badRequest().body("Teacher not found with id = " + teacherId);
        }
        for (SubjectNamesDTO sn : subjectNamesDTOs) {
            int subjectId = sn.getId();
            Subject subject = subjectService.getSubjectById(subjectId);
            theSubjects.add(subject);
        }

        theTeacher.setSubjects(theSubjects);
        teacherService.saveTeacher(theTeacher);

        return ResponseEntity.ok("Subjects added Successfully");
    }

    // delete subject
    @DeleteMapping("/delete/{teacherId}")
    public String deleteSubjects(@PathVariable int teacherId, @RequestBody List<SubjectNamesDTO> subjectNamesDTO) {
        Teacher teacher = teacherService.findTeacherById(teacherId);
        Set<Subject> subjects = teacher.getSubjects();
        Set<Subject> removeSubjects = new HashSet();

        for (SubjectNamesDTO snd : subjectNamesDTO) {
            int id = snd.getId();
            Subject subject = subjectService.getSubjectById(id);
            removeSubjects.add(subject);
        }

        subjects.remove(removeSubjects);

        teacher.setSubjects(subjects);

        teacherService.saveTeacher(teacher);

        return "Subjects deleted successfully.";

    }

    // teacher add content
    @PostMapping("/add/{teacherId}/{subjectId}/{unitId}")
    public ResponseEntity<ContentSubjectDTO> addContent(
            @PathVariable int teacherId,
            @PathVariable int subjectId,
            @PathVariable int unitId,
            @RequestBody Map<String, Object> requestBody) {

        Teacher teacher = teacherService.findTeacherById(teacherId);
        Subject subject = subjectService.getSubjectById(subjectId);
        Unit unit = unitService.getUnit(unitId);

        if (teacher == null || subject == null || unit == null) {
            return ResponseEntity.badRequest().build();
        }

        String contentTitle = (String) requestBody.get("contentTitle");
        String contentDesc = (String) requestBody.get("contentDesc");
        String fileURL = (String) requestBody.get("fileURL");

        Content content = new Content();
        content.setTitle(contentTitle);
        content.setDescription(contentDesc);
        content.setFileUrl(fileURL);
        content.setTeacher(teacher);
        content.setSubject(subject);
        content.setUnit(unit);

        Content savedContent = contentService.addContent(content);

        ContentSubjectDTO contentSubjectDTO = convertToContentSubjectDTO(savedContent);
        return ResponseEntity.ok(contentSubjectDTO);
    }

    // convert content to contentSubjectDTO
    private ContentSubjectDTO convertToContentSubjectDTO(Content content) {

        // get unitDTO
        Unit unit = content.getUnit();
        UnitDTO unitDTO = new UnitDTO(unit.getUnitNo());

        // create contentSubjectDTO
        ContentSubjectDTO contentSubjectDTO = new ContentSubjectDTO();
        contentSubjectDTO.setUnitNo(unitDTO);
        contentSubjectDTO.setTitle(content.getTitle());
        contentSubjectDTO.setDescription(content.getDescription());
        contentSubjectDTO.setFileURL(content.getFileUrl());

        return contentSubjectDTO;

    }

    // teacher update content
    // teacher can update its own content - teacherId
    // to updated content need subjectId, unitId
    @PutMapping("/update/{teacherId}/{subjectId}/{unitId}")
    public ResponseEntity<ContentSubjectDTO> updateContent(@PathVariable int teacherId,
            @PathVariable int subjectId, @PathVariable int unitId,
            @RequestBody Map<String, Object> requestBody) {

        Teacher teacher = teacherService.findTeacherById(teacherId);
        Subject subject = subjectService.getSubjectById(subjectId);
        Unit unit = unitService.getUnit(unitId);

        Content c = contentService.getContentByTeacherSubjectAndUnit(teacher, subject, unit);

        c.setTitle((String) requestBody.get("title"));
        c.setDescription((String) requestBody.get(("description")));
        c.setFileUrl((String) requestBody.get("fileURL"));

        contentService.addContent(c);

        ContentSubjectDTO contentSubjectDTO = convertToContentSubjectDTO(c);

        return ResponseEntity.ok(contentSubjectDTO);

        // Subject subject = c.getSubject();
        // User user = c.getTeacher().getUser();
        // String contentTitle = (String) requestBody.get("contentTitle");
        // String contentDesc = (String) requestBody.get("contentDesc");
        // String fileURL = (String) requestBody.get("fileURL");

        // c.setTitle(contentTitle);
        // c.setDescription(contentDesc);
        // c.setFileUrl(fileURL);

        // contentService.addContent(c);

        // ContentDTO contentDTO = new ContentDTO();
        // contentDTO.setId(c.getId());
        // contentDTO.setTitle(c.getTitle());
        // contentDTO.setDescription(c.getDescription());
        // contentDTO.setFileURL(c.getFileUrl());
        // SubjectNamesDTO subjectNamesDTO = new SubjectNamesDTO();
        // subjectNamesDTO.setId(subject.getId());
        // subjectNamesDTO.setName(subject.getName());
        // contentDTO.setSubject(subjectNamesDTO);
        // TeacherContentDTO teacherContentDTO = new TeacherContentDTO();
        // teacherContentDTO.setName(user.getName());
        // teacherContentDTO.setEmail(user.getEmail());
        // contentDTO.setSubject(subjectNamesDTO);
        // contentDTO.setTeacher(teacherContentDTO);

        // return ResponseEntity.ok(contentDTO);

    }

    // teacher delete content
    @DeleteMapping("/delete/{teacherId}/{subjectId}/{unitId}")
    public String deleteContent(@PathVariable int teacherId,
            @PathVariable int subjectId,
            @PathVariable int unitId) {

        Teacher teacher = teacherService.findTeacherById(teacherId);
        Subject subject = subjectService.getSubjectById(subjectId);
        Unit unit = unitService.getUnit(unitId);

        Content content = contentService.getContentByTeacherSubjectAndUnit(teacher, subject, unit);

        contentService.deleteContent(content);

        return "content deleted successfully.";
    }

    // read subject questions unit wise
    @GetMapping("/questions/{subjectId}/{unitId}")
    public ResponseEntity<?> getQuestionsSubjectWise(@PathVariable int subjectId,
            @PathVariable int unitId) {
        Subject subject = subjectService.getSubjectById(subjectId);
        Unit unit = unitService.getUnit(unitId);

        List<Question> questions = questionService.getQuestionBySubjectAndUnit(subject, unit);

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

    // teacher answer the questions
    @PostMapping("/answer/{teacherId}/{questionId}")
    public ResponseEntity<AnswerDTO> answerQuestions(@PathVariable int teacherId,
            @PathVariable int questionId,
            @RequestBody Map<String, Object> requestBody) {

        Teacher teacher = teacherService.findTeacherById(teacherId);
        Question question = questionService.getQuestionById(questionId);

        String text = (String) requestBody.get("text");
        int points = 2;
        Integer teacherPoints = teacher.getTotalPoints();
        if(teacherPoints == null)
        {
            teacherPoints = 0;
        }
        int updatedPoints = teacherPoints + points;
        teacher.setTotalPoints(updatedPoints);
        teacherService.saveTeacher(teacher);

        Answer answer = new Answer();
        answer.setText(text);
        answer.setQuestion(question);
        answer.setTeacher(teacher);
        answer.setPoints(points);
        Answer savedAnswer = answerService.saveAnswer(answer);
        
        AnswerTracking answerTracking = new AnswerTracking();
        answerTracking.setAnswer(savedAnswer);
        answerTracking.setLikes(0);
        answerTracking.setViews(0); 
        
        answerTrackingService.saveAnswerTracking(answerTracking);
        savedAnswer.setTracking(answerTracking);

        
        

        AnswerDTO answerDTO = convertTOAnswerDTO(Arrays.asList(savedAnswer)).get(0);

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

    // update answer
    @PutMapping("/update-answer/{answerId}")
    public ResponseEntity<AnswerDTO> updateAnswerEntity(@PathVariable int answerId, @RequestBody String answerText) {

        Answer answer = answerService.getAnswerById(answerId);
        answer.setText(answerText);
        Answer updatedAnswer = answerService.saveAnswer(answer);

        AnswerDTO answerDTO = convertTOAnswerDTO(Arrays.asList(updatedAnswer)).get(0);

        return ResponseEntity.ok(answerDTO);
    }

    // delete answer
    @DeleteMapping("/delete-answer/{answerId}")
    public String deleteAnswer(@PathVariable int answerId)
    {
        Answer answer = answerService.getAnswerById(answerId);
        answerService.deleteAnswer(answer);
        return "Answer deleted successfully with id = " + answerId;
    }

}
