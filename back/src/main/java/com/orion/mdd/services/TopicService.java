package com.orion.mdd.services;

import com.orion.mdd.MddApplication;
import com.orion.mdd.dto.TopicDto;
//import com.orion.mdd.mapper.TopicMapper;
import com.orion.mdd.models.Topic;
import com.orion.mdd.models.Users;
import com.orion.mdd.repository.TopicRepository;

import lombok.extern.log4j.Log4j2;

import org.springframework.stereotype.Service;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class TopicService {

    
    //private final MddApplication mddApplication;
    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository){//, MddApplication mddApplication){
        this.topicRepository = topicRepository;
        //this.mddApplication = mddApplication;
    }

    public List<Topic> findAll() {
        return this.topicRepository.findAll();
    }

    public List<TopicDto> findAll(String userName) {
        List<Topic> topicList = this.topicRepository.findAll();
        List<TopicDto> retour = new ArrayList<TopicDto>();

        //Add the non subscribed first
        for (Topic topic : topicList) {

            Boolean userIsIn = false;
            for(Users u : topic.getUsers()){
                log.info(topic.getTitle() + " ?????? " + userName + " " + u.getUsername());
                if (userName.equals(u.getUsername())){
                    log.info(topic.getTitle() + u.getUsername() +" true");
                    userIsIn = true;
                }
            }
            if (!userIsIn){
                log.info(topic.getTitle() + " false");
                retour.add(new TopicDto(topic.getId(),topic.getTitle(),topic.getContent(),false ));
            }
        }
        //Add the subscribed at the end
        //compare and complete the retour by the topic list
        for (Topic topic : topicList) {
            Boolean topicIsIn = false;
            for(TopicDto topicDto : retour){
                if (topicDto.getId()==topic.getId()){
                    topicIsIn = true;
                }
            }
            if (!topicIsIn){//add to true the topic which is not already in
                retour.add(new TopicDto(topic.getId(),topic.getTitle(),topic.getContent(),true ));
            }
        }
        return retour;
    }

}