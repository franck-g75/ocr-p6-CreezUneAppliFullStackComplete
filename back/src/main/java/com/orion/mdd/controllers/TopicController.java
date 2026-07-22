package com.orion.mdd.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.dto.MyResponseDto;
import com.orion.mdd.dto.ResponseCode;
import com.orion.mdd.dto.TopicDto;
import com.orion.mdd.exception.CustomException;
import com.orion.mdd.exception.ErrorCode;
import com.orion.mdd.exception.ErrorManagement;
import com.orion.mdd.models.UserInfo;
import com.orion.mdd.services.TopicService;
import com.orion.mdd.services.UserInfoService;

import lombok.extern.log4j.Log4j2;

/**
 * TopicController is used to handle topic input and output 
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/topic")
@Log4j2
public class TopicController {
    
    private final TopicService topicService;
    private final UserInfoService userInfoService;
    

    /**
     * constructor
     * @param topicService gives services for topic objects
     * @param userInfoService gives services for userinfo objects
     */
    public TopicController( 
            TopicService topicService, 
            UserInfoService userInfoService
        ) {
        this.topicService = topicService;
        this.userInfoService = userInfoService;
    }

    /**
     * find all topics and set a read boolean to true for each topic subscribed by the user connected
     * @return all topics
     */
    @GetMapping("/user")
    public ResponseEntity<MyResponseDto> findAll() {
        log.info("findAll()  ...  ");

        try{
            List<TopicDto> topics = null;
            
            if ( SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {

                Optional<UserInfo> user = userInfoService.findByUsername( SecurityContextHolder.getContext().getAuthentication().getName() );
                
                if(user.isPresent()){

                    topics = this.topicService.findAll(user.get());
                    
                    MyResponseDto response = new MyResponseDto();
                    response.setCode(ResponseCode.DATA_FOUND.getCode());
                    response.setMessage(ResponseCode.DATA_FOUND.getMessage());
                    Map<String, Object> infos = new HashMap<>();
                    infos.put("topics", topics);
                    response.setData(infos);
                    
                    return ResponseEntity.ok().body(response);

                } else {

                    log.error("findAll({}) custom exception : username not found.  ", SecurityContextHolder.getContext().getAuthentication().getName() );
                    return ErrorManagement.responseError(new CustomException(ErrorCode.DATA_NOT_FOUND));
                    
                }

            } else { //user not present
                //user connected is not authenticated
                return ErrorManagement.responseError(new CustomException(ErrorCode.NOT_AUTHORIZED));
            }

        } catch ( Exception e ) {
            log.error("findAll({}) error : ", SecurityContextHolder.getContext().getAuthentication().getName() + "   " + e.toString());
            return ErrorManagement.responseError(e);
        }
    }

    /**
     * find all topics (used to fill the combo)
     * @return all topics with the read boolean set to false for every topics
     */
    @GetMapping()
    public ResponseEntity<MyResponseDto> findAllTopics() {
        log.info("findAllTopics() ...");
        try{
            List<TopicDto> topics = this.topicService.findAll();

            MyResponseDto response = new MyResponseDto();
            response.setCode(ResponseCode.DATA_FOUND.getCode());
            response.setMessage(ResponseCode.DATA_FOUND.getMessage());
            Map<String, Object> infos = new HashMap<>();
            infos.put("topics", topics);
            response.setData(infos);

            return ResponseEntity.ok().body(response);
            
        } catch (Exception e) {
            log.error("findAllTopics() exception : ", e.toString());
            return ErrorManagement.responseError(e);
        }
    }

    
}
