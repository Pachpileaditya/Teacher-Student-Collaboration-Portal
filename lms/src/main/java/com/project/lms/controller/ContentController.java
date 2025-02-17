package com.project.lms.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.lms.DTO.ContentDTO;
import com.project.lms.DTO.SubjectNamesDTO;
import com.project.lms.DTO.TeacherContentDTO;
import com.project.lms.DTO.UnitDTO;
import com.project.lms.entity.Content;
import com.project.lms.entity.Subject;
import com.project.lms.entity.Teacher;
import com.project.lms.entity.Unit;
import com.project.lms.entity.User;
import com.project.lms.service.ContentService;
import com.project.lms.service.SubjectService;
import com.project.lms.service.TeacherService;
import com.project.lms.service.UnitService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private ContentService contentService;
    private TeacherService teacherService;
    private SubjectService subjectService;
    private UnitService unitService;

    @Autowired
    public ContentController(ContentService contentService, TeacherService teacherService,
            SubjectService subjectService, UnitService unitService) {
        this.contentService = contentService;
        this.teacherService = teacherService;
        this.subjectService = subjectService;
        this.unitService = unitService;
    }

    // teacher add content
    @PostMapping("/add")
    public ResponseEntity<ContentDTO> addContent(@RequestBody Map<String, Object> requestBody) {
        int teacherId = (Integer) requestBody.get("teacherId");
        int subjectId = (Integer) requestBody.get("subjectId");
        int unitId = (Integer) requestBody.get("unit");

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

        ContentDTO contentDTO = convertToContentDTO(savedContent);
        return ResponseEntity.ok(contentDTO);
    }

    private ContentDTO convertToContentDTO(Content content) {
        // Convert teacher to TeacherContentDTO using the associated user's name and
        // email
        Teacher teacher = content.getTeacher();
        User user = teacher.getUser();
        TeacherContentDTO teacherDTO = new TeacherContentDTO(user.getName(), user.getEmail());

        // Convert subject to SubjectNamesDTO
        Subject subject = content.getSubject();
        SubjectNamesDTO subjectDTO = new SubjectNamesDTO(subject.getId(), subject.getName());

        // unit ot unitDTO
        Unit unit = content.getUnit();
        UnitDTO unitDTO = new UnitDTO(unit.getUnitNo());

        // Create and return the ContentDTO
        return new ContentDTO(
                content.getId(),
                content.getTitle(),
                content.getDescription(),
                content.getFileUrl(),
                teacherDTO,
                subjectDTO,
                unitDTO);
    }

    // teacher and student can read content
    @GetMapping("/{teacherId}/{subjectId}/{unitId}")
    public ResponseEntity<List<ContentDTO>> getContentBySubjectAndTeacher(@PathVariable int teacherId,
                                                                @PathVariable int subjectId, @PathVariable int unitId) {
        Teacher teacher = teacherService.findTeacherById(teacherId);
        Subject subject = subjectService.getSubjectById(subjectId);
        Unit unit = unitService.getUnit(unitId);
        Content contents = contentService.getContentByTeacherSubjectAndUnit(teacher, subject, unit);
        List<ContentDTO> theContentDTOs = new ArrayList<>();
        

        return ResponseEntity.ok(theContentDTOs);

    }

    // teacher update content
    @PutMapping("/update/{contentId}")
    public ResponseEntity<ContentDTO> updateContent(@PathVariable int contentId,
            @RequestBody Map<String, Object> requestBody) {

        Content c = contentService.getContentById(contentId);
        Subject subject = c.getSubject();
        User user = c.getTeacher().getUser();
        String contentTitle = (String) requestBody.get("contentTitle");
        String contentDesc = (String) requestBody.get("contentDesc");
        String fileURL = (String) requestBody.get("fileURL");

        c.setTitle(contentTitle);
        c.setDescription(contentDesc);
        c.setFileUrl(fileURL);

        contentService.addContent(c);

        ContentDTO contentDTO = new ContentDTO();
        contentDTO.setId(c.getId());
        contentDTO.setTitle(c.getTitle());
        contentDTO.setDescription(c.getDescription());
        contentDTO.setFileURL(c.getFileUrl());
        SubjectNamesDTO subjectNamesDTO = new SubjectNamesDTO();
        subjectNamesDTO.setId(subject.getId());
        subjectNamesDTO.setName(subject.getName());
        contentDTO.setSubject(subjectNamesDTO);
        TeacherContentDTO teacherContentDTO = new TeacherContentDTO();
        teacherContentDTO.setName(user.getName());
        teacherContentDTO.setEmail(user.getEmail());
        contentDTO.setSubject(subjectNamesDTO);
        contentDTO.setTeacher(teacherContentDTO);

        return ResponseEntity.ok(contentDTO);

    }

    // teacher delete content
    @DeleteMapping("/{contentId}")
    public String deleteContent(@PathVariable int contentId)
    {
        if(!contentService.contentExits(contentId))
        {
            return "Content does not exits with id = " + contentId;
        }
        Content content = contentService.getContentById(contentId);
        contentService.deleteContent(content);

        return "content deleted successfully.";
    }

}
