package com.orion.mdd.services;

import com.orion.mdd.mapper.TopicMapper;
import com.orion.mdd.models.Topic;
import com.orion.mdd.repository.TopicRepository;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TopicService {
    private final TopicMapper topicMapper;
    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository, TopicMapper topicMapper) {
        this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    public List<Topic> findAll() {
        return this.topicRepository.findAll();
    }
   
}