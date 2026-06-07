package com.orion.mdd.services;

//import com.orion.mdd.mapper.TopicMapper;
import com.orion.mdd.models.Topic;
import com.orion.mdd.models.Users;
import com.orion.mdd.repository.TopicRepository;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository){
        this.topicRepository = topicRepository;
    }

    public List<Topic> findAll() {
        return this.topicRepository.findAll();
    }

}