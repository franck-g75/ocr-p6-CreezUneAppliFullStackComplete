package com.orion.mdd.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.dto.TopicDto;
import com.orion.mdd.exception.CustomException;
import com.orion.mdd.exception.ErrorCode;
import com.orion.mdd.exception.ErrorManagement;
import com.orion.mdd.models.UserInfo;
import com.orion.mdd.services.TopicService;
import com.orion.mdd.services.UserInfoService;

import lombok.extern.log4j.Log4j2;

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

    @GetMapping("/user/{userid}")
    public ResponseEntity<?> findAll(@PathVariable("userid") Long userId) throws CustomException {
        log.info("findAll({}) ...", userId);
        try{
            List<TopicDto> topics = null;
            Optional<UserInfo> user = this.userInfoService.findById(userId);
            if (user.isPresent()) {
                topics = this.topicService.findAll(user.get());
            } else {
                log.error("findAll({}) custom exception : username not found.  ", userId);
                return ErrorManagement.responseError(new CustomException(ErrorCode.DATA_NOT_FOUND));
            }
            if (topics == null){
                log.error("findAll({}) custom exception : topics not found.  ", userId);
                return ErrorManagement.responseError(new CustomException(ErrorCode.DATA_NOT_FOUND));
            } else {
                return ResponseEntity.ok().body(topics);
            }

        } catch ( Exception e ) {
            log.error("findAll({}) error : ", userId + e.toString());
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
