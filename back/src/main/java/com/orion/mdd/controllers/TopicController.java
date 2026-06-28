package com.orion.mdd.controllers;

import com.orion.mdd.dto.TopicDto;
import com.orion.mdd.exception.CustomException;
import com.orion.mdd.exception.ErrorManagement;
import com.orion.mdd.models.UserInfo;
import com.orion.mdd.services.TopicService;
import com.orion.mdd.services.UserInfoService;

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
    private final UserInfoService userInfoService;


    public TopicController( TopicService topicService, UserInfoService userInfoService) {
        this.topicService = topicService;
        this.userInfoService = userInfoService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> findAll(@PathVariable("username") String username) throws CustomException {
        log.info("findAll({}) ...", username);
        try{
            UserInfo user = this.userInfoService.findByUsername(username);
            List<TopicDto> topics = this.topicService.findAll(user);
            return ResponseEntity.ok().body(topics);
        } catch ( CustomException ce ) {
            log.error("findAll({}) custom exception : ", username + ce.toString());
            return ErrorManagement.responseError(ce);
        } catch ( Exception e ) {
            log.error("findAll({}) error : ", username + e.toString());
            return ErrorManagement.responseError(e);
        }
    }

    @GetMapping()
    public ResponseEntity<?> findAllTopics() {
        log.info("findAllTopics() ...");
        try{
            List<TopicDto> topics = this.topicService.findAll();
            return ResponseEntity.ok().body(topics);
        } catch (CustomException ce) {
            log.error("findAllTopics() customException : ", ce.toString());
            return ErrorManagement.responseError(ce);            
        } catch (Exception e) {
            log.error("findAllTopics() exception : ", e.toString());
            return ErrorManagement.responseError(e);
        }
    }

    
}
