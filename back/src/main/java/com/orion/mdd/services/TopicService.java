package com.orion.mdd.services;

import com.orion.mdd.dto.TopicDto;
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

    
    public List<TopicDto> findAll(String userName) {
        log.info("findAll({}) ...", userName);

        List<Topic> topicList = this.topicRepository.findAll();
        List<TopicDto> retour = new ArrayList<TopicDto>();

        //Add the non subscribed first
        for (Topic topic : topicList) {

            Boolean userIsIn = false;
            for(UserInfo u : topic.getUserInfos()){
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


    public Optional<Topic> findById(@NonNull Long idTopic) {
        log.info("findById({})", idTopic);
        return topicRepository.findById(idTopic);
    }

}