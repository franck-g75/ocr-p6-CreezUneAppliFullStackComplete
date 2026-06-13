package com.orion.mdd.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.orion.mdd.exception.NotFoundException;
import com.orion.mdd.models.Topic;
import com.orion.mdd.models.Users;
import com.orion.mdd.repository.UsersRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UsersService {


    private final TopicService topicService;
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository, TopicService topicService){
        this.usersRepository = usersRepository;
        this.topicService = topicService;
    }

    public Users findByEmail(String email){
        return usersRepository.findByEmail(email);
    }
    
    public Users findByUsername(String username){
        return usersRepository.findByUsername(username);
    }

    public Optional<Users> findById(Long id){
        return usersRepository.findById(id);
    }

    public void addTopic(Long idUser, long idTopic){

        Optional<Users> user = this.findById(idUser);
        Optional<Topic> topic = topicService.findById(idTopic);
        Topic topicTmp = topic.get(); 

        if (user!=null && topic!=null){
            Set<Topic> topicSet = user.get().getTopics();
            topicSet.add(topicTmp);
            user.get().setTopics(topicSet);
            this.usersRepository.save(user.get());
        } else {
           throw new NotFoundException();
        }
        

    }

    public void delTopic(Long idUser, Long idTopic) {
        Optional<Users> user = this.findById(idUser);
        Optional<Topic> topic = topicService.findById(idTopic);
        Topic topicTmp = topic.get(); 

        if (user!=null && topic!=null){
            Set<Topic> topicSet = user.get().getTopics();
            topicSet.remove(topicTmp);
            user.get().setTopics(topicSet);
            this.usersRepository.save(user.get());
        } else {
           throw new NotFoundException();
        }
    }

}
