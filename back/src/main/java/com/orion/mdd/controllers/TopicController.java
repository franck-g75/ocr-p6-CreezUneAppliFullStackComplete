package com.orion.mdd.controllers;

import com.orion.mdd.dto.TopicDto;
import com.orion.mdd.mapper.TopicMapper;
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
    private final TopicMapper topicMapper;
    private final TopicService topicService;


    public TopicController( TopicService topicService,
                            TopicMapper topicMapper) {
        this.topicMapper = topicMapper;
        this.topicService = topicService;
    }

    @GetMapping()
    public ResponseEntity<?> findAll() {
        List<Topic> topics = this.topicService.findAll();
        List<TopicDto> topicDtos = new ArrayList<TopicDto>();
        
        for (Topic topic : topics) {
            topicDtos.add(this.topicMapper.convertToTopicDto(topic));
         }
        
        return ResponseEntity.ok().body(topicDtos);
    }
    
}
