package com.orion.mdd.controllers;

import com.orion.mdd.dto.TopicDto;
//import com.orion.mdd.mapper.TopicMapper;
import com.orion.mdd.models.Topic;
import com.orion.mdd.services.TopicService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/topic")
@Log4j2
public class TopicController {
    //private final TopicMapper topicMapper;
    private final TopicService topicService;


    public TopicController( TopicService topicService) {
        //this.topicMapper = topicMapper;
        this.topicService = topicService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> findAll(@PathVariable("username") String username) {
        List<TopicDto> topics = this.topicService.findAll(username);
                  
        return ResponseEntity.ok().body(topics);
    }
    
}
