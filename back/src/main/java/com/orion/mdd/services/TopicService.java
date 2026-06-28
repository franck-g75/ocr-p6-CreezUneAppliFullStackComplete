package com.orion.mdd.services;

import com.orion.mdd.dto.TopicDto;
import com.orion.mdd.exception.CustomException;
import com.orion.mdd.exception.ErrorCode;
import com.orion.mdd.models.Topic;
import com.orion.mdd.models.UserInfo;
import com.orion.mdd.repository.TopicRepository;

import lombok.extern.log4j.Log4j2;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository){
        this.topicRepository = topicRepository;
    }

    /**
     * Find all topics 
     * @return a List<TopicDto> with read=false for every topics
     */
    public List<TopicDto> findAll() {
        log.info("findAll() ...");
        
        List<Topic> topicList = this.topicRepository.findAll();
        List<TopicDto> topicRetour = new ArrayList<TopicDto>();
        for(Topic topic : topicList){
            TopicDto topicDto = new TopicDto(topic.getId(),topic.getTitle(),topic.getContent(), false);
            topicRetour.add(topicDto);
        }
        return topicRetour;
    }

    /**
     * Find all topics for a username and mark them as subscribed (read=true) or not subscribed (read=false)
     * @param userName 
     * @return a List<TopicDto> 
     */
    public List<TopicDto> findAll(UserInfo user) throws CustomException{
        
        List<Topic> topicList = this.topicRepository.findAll();
        List<TopicDto> topicReturn = new ArrayList<TopicDto>();

        if (user==null){
            log.info("findAll(null) user not found" );
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        } else {
            log.info("findAll({}) ...", user.getUsername());
            String userName = user.getUsername();

            //Add the non subscribed first
            for (Topic topic : topicList) {

                Boolean userIsIn = false;
                for(UserInfo u : topic.getUserInfos()){
                    log.debug(topic.getTitle() + " ?????? " + userName + " " + u.getUsername());
                    if (userName.equals(u.getUsername())){
                        log.debug(topic.getTitle() + u.getUsername() +" true");
                        userIsIn = true;
                    }
                }
                if (!userIsIn){
                    log.debug(topic.getTitle() + " false");
                    topicReturn.add(new TopicDto(topic.getId(),topic.getTitle(),topic.getContent(),false ));
                }
            }
            //Add the subscribed at the end
            //compare and complete the retour by the topic list
            for (Topic topic : topicList) {
                Boolean topicIsIn = false;
                for(TopicDto topicDto : topicReturn){
                    if (topicDto.getId()==topic.getId()){
                        topicIsIn = true;
                    }
                }
                if (!topicIsIn){//add to true the topic which is not already in
                    topicReturn.add(new TopicDto(topic.getId(),topic.getTitle(),topic.getContent(),true ));
                }
            }
            return topicReturn;
       }
        
    }

    public Optional<Topic> findById(@NonNull Long idTopic) {
        log.info("findById({})", idTopic);
        return topicRepository.findById(idTopic);
    }

}