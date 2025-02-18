package com.project.lms.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.lms.DTO.ContentDTO;
import com.project.lms.DTO.ContentSubjectDTO;
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

   


    // convert content to contentSubjectDTO
    private ContentSubjectDTO convertToContentSubjectDTO(Content content)
    {

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
    public ResponseEntity<ContentSubjectDTO> getContentBySubjectAndTeacher(@PathVariable int teacherId,
                                                                @PathVariable int subjectId, @PathVariable int unitId) {
        Teacher teacher = teacherService.findTeacherById(teacherId);
        Subject subject = subjectService.getSubjectById(subjectId);
        Unit unit = unitService.getUnit(unitId);
        Content contents = contentService.getContentByTeacherSubjectAndUnit(teacher, subject, unit);
        ContentSubjectDTO contentSubjectDTO = convertToContentSubjectDTO(contents);

        return ResponseEntity.ok(contentSubjectDTO);

    }

    

}
