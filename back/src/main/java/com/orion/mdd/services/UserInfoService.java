package com.orion.mdd.services;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.orion.mdd.exception.NotFoundException;
import com.orion.mdd.exception.UserFoundException;
import com.orion.mdd.models.Topic;
import com.orion.mdd.models.UserInfo;
import com.orion.mdd.repository.UserInfoRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserInfoService {


    private final TopicService topicService;
    private final UserInfoRepository userInfoRepository;

    public UserInfoService(UserInfoRepository userInfoRepository, TopicService topicService){
        this.userInfoRepository = userInfoRepository;
        this.topicService = topicService;
    }

    public UserInfo[] findByEmail(String email){
        return userInfoRepository.findByEmail(email);
    }
    
    public UserInfo[] findByUsername(String username){
        return userInfoRepository.findByUsername(username);
    }

    public Optional<UserInfo> findById(Long id){
        return userInfoRepository.findById(id);
    }

    public UserInfo create(UserInfo user) throws UserFoundException {
        UserInfo[] userByUsername = findByUsername(user.getUsername());
        UserInfo[] userByMail = findByEmail(user.getEmail());
        if (userByMail.length>0) {             //existing User (same email)
            throw new UserFoundException("user with same email already exist in DB");
        } else if (userByUsername.length>0) {  //existing user (same username)
            throw new UserFoundException("user with same username already exist in DB");
        } else { //non existing user : save it
            return userInfoRepository.save(user);
        }
    }

    public UserInfo update(UserInfo user) throws UserFoundException {
        UserInfo[] userByUsername = findByUsername(user.getUsername());
        UserInfo[] userByMail = findByEmail(user.getEmail());

        log.info( "UserInfoService.update userByUsername.length=" + userByUsername.length + " userByMail.length=" + userByMail.length );

        //boolean errFound = false;
        if (userByMail.length>0 && user.getId()!=userByMail[0].getId() && userByUsername.length>0 && user.getId()!=userByUsername[0].getId()) { 
            throw new UserFoundException("other user with same email and same username already exist in DB ()");
        } else if (userByMail.length>0 && user.getId()!=userByMail[0].getId()) {          //existing User (same email and not same id)
            throw new UserFoundException("other user with same email already exist in DB ()");
        } else if (userByUsername.length>0 && user.getId()!=userByUsername[0].getId()) {  //existing user (same username and not same id)
            throw new UserFoundException("other user with same username already exist in DB.");
        } else {
            return userInfoRepository.save(user);
        }
        
    }

    public void addTopic(Long idUser, long idTopic){

        Optional<UserInfo> user = this.findById(idUser);
        Optional<Topic> topic = topicService.findById(idTopic);

        Topic topicTmp = topic.get(); 

        if (user!=null && topic!=null){
            boolean found = false;
            Set<Topic> topicSet = user.get().getTopics();
            for (Topic topicofuser : topicSet) {
                if  (topicofuser.getId() == topicTmp.getId()){
                    found = true;
                }
            }
            if (!found){
                log.info("adding user - topic in db ...");
                this.userInfoRepository.addUserTopic(idUser, idTopic);
            } else {
                log.info("user- topic already in db : nothing done.");
            }
        } else {
           throw new NotFoundException();
        }
        
    }

    public void delTopic(Long idUser, Long idTopic) {

        Optional<UserInfo> user = this.findById(idUser);
        Optional<Topic> topic = topicService.findById(idTopic);
        
        if (user!=null && topic!=null){
            this.userInfoRepository.delUserTopic(idUser, idTopic);
        } else {
           throw new NotFoundException();
        }
    }

}
