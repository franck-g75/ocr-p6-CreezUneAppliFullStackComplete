package com.orion.mdd.controllers;

import com.orion.mdd.dto.TopicDto;

import com.orion.mdd.services.TopicService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/topic")
@Log4j2
public class TopicController {
    
    private final TopicService topicService;

    public TopicController( TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> findAll(@PathVariable("username") String username) {
        log.info("findAll({}) ...", username);
        try{
            List<TopicDto> topics = this.topicService.findAll(username);
            return ResponseEntity.ok().body(topics);
        } catch ( Exception e ) {
            log.error("findAll({}) error : ", username + e.toString());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<?> findAllTopics() {
        log.info("findAllTopics() ...");
        try{
            List<TopicDto> topics = this.topicService.findAll();
            return ResponseEntity.ok().body(topics);
        } catch (Exception e) {
            log.error("findAll({}) error : ", e.toString());
            return ResponseEntity.badRequest().build();
        }
    }
}
