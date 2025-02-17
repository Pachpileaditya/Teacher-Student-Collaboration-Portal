package com.project.lms.service;

import java.lang.foreign.Linker.Option;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.lms.entity.Content;
import com.project.lms.entity.Subject;
import com.project.lms.entity.Teacher;
import com.project.lms.entity.Unit;
import com.project.lms.repo.ContentRepository;

import jakarta.transaction.Transactional;

@Service
public class ContentService 
{

    private ContentRepository contentRepository;

    public ContentService(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }


    @Transactional
    public Content addContent(Content content)
    {
        return contentRepository.save(content);
    }


    public Content getContentByTeacherSubjectAndUnit(Teacher teacher, Subject subject, Unit unit) {
        return contentRepository.findByTeacherAndSubjectAndUnit(teacher, subject, unit);
    }


    public Content getContentById(int contentId) {
       return contentRepository.findById(contentId)
                            .orElseThrow(()->new RuntimeException("content not found with id = " + contentId));
    }


    public boolean contentExits(int contentId) {
        Optional<Content> content = contentRepository.findById(contentId);

        if(!content.isPresent()){
            return false;
        }
        return true;
    }


    @Transactional
    public void deleteContent(Content content) {
        contentRepository.delete(content);
    }
    
}
