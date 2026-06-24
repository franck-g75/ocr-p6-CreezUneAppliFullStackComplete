package com.orion.mdd.services;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.orion.mdd.dto.UserInfoDto;
import com.orion.mdd.exception.CustomException;
import com.orion.mdd.exception.ErrorCode;
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

    public UserInfo findByEmail(String email) throws CustomException {
        log.info("findByEmail({})...",email);
        Optional<UserInfo> user = userInfoRepository.findByEmail(email);
        if (user.isEmpty()){
            log.error("findByEmail({}) ==> user not found", email);
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        } else {
            return user.get();
        }

    }
    
    public UserInfo findByUsername(String username) throws CustomException{
        log.info("findByUsername({})...",username);
        Optional<UserInfo> user = userInfoRepository.findByUsername(username);
        if (user.isEmpty()){
            log.error("findByUsername({}) ==> user not found",username);
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        } else {
            return user.get();
        }
    }

    public Optional<UserInfo> findById(Long id){

        log.info("findById({})...",id);
        Optional<UserInfo> user = userInfoRepository.findById(id);
        if (user.isEmpty()){
            log.error("findById({}) ==> user not found",id);
        } 
        return user;
        
    }

    public UserInfo create(UserInfo user) throws CustomException {

        log.info("create({})...",user);

        Optional<UserInfo> userByUsername = userInfoRepository.findByUsername(user.getUsername());
        Optional<UserInfo> userByMail = userInfoRepository.findByEmail(user.getEmail());
        
        if (userByMail.isPresent()) {             //existing User (same email)
            log.error("create({}) mail is found in db",user);
            throw new CustomException(ErrorCode.INVALID_INPUT);
        } else if (userByUsername.isPresent()) {  //existing user (same username)
            log.error( "create({}) mail is found in db", user);
            throw new CustomException(ErrorCode.INVALID_INPUT);
        } else { //non existing user : save it
            log.info("saving user ...");
            return userInfoRepository.save(user);
        }

    }

    /**
     * 
     * @param userFoundById user found by the id
     * @param userDto data given in the request
     * @return the User modified
     * @throws CustomException
     */
    public UserInfo update(UserInfo userFoundById, UserInfoDto userDto) throws CustomException {

        Optional<UserInfo> userByUsername = userInfoRepository.findByUsername(userDto.getUsername());
        Optional<UserInfo> userByMail = userInfoRepository.findByEmail(userDto.getEmail());
        
        log.info("Updating user ...  :  userByMail:" + userByMail.isPresent() + "   userByUsername:" + userByUsername.isPresent() + "  userId WS:" + userDto.getId());

        if ( userByMail.isPresent() && userByUsername.isPresent() && userDto.getId()!=userByMail.get().getId() && userDto.getId()!=userByUsername.get().getId() && userByMail.get().getId() == userByUsername.get().getId()) { 
            log.error("Email et username trouvés dans le meme tuple de base.");
            throw new CustomException(ErrorCode.INVALID_INPUT);
        } else if ( userByMail.isPresent() && userByUsername.isPresent() && userDto.getId()!=userByMail.get().getId() && userDto.getId()!=userByUsername.get().getId() && userByMail.get().getId() != userByUsername.get().getId()) { 
            log.error("Email et username trouvés mais pas dans les meme tuples de base.");
            throw new CustomException(ErrorCode.INVALID_INPUT);
        } else if (userByMail.isPresent() && userDto.getId()!=userByMail.get().getId()) {          //existing User (same email and not same id)
            log.error("Email trouvé dans la base et id differents");
            throw new CustomException(ErrorCode.INVALID_INPUT);
        } else if (userByUsername.isPresent() && userDto.getId()!=userByUsername.get().getId()) {  //existing user (same username and not same id)
            log.error("Username trouvé dans la base et id différents.");
            throw new CustomException(ErrorCode.INVALID_INPUT);
        } else {
            log.info("Updating user...");
            userFoundById.setEmail(userDto.getEmail());
            userFoundById.setUsername(userDto.getUsername());
            userFoundById.setPwd(userDto.getPwd());
            return userInfoRepository.save(userFoundById);
        }
        
    }

    public void addTopic(Long idUser, long idTopic){

        log.info("adding topic {} to the user {} in USER_TOPIC table", idTopic, idUser);

        Optional<UserInfo> user = this.findById(idUser);
        Optional<Topic> topic = topicService.findById(idTopic);

        if (user.isEmpty()){
            log.error("user {} not found", idUser);
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        } else if (topic.isEmpty()) {
            log.error("topic {} not found",idTopic);
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        } else {
            Topic topicTmp = topic.get(); 
            boolean found = false;
            Set<Topic> topicSet = user.get().getTopics();
            for (Topic topic_of_user : topicSet) {
                if  (topic_of_user.getId() == topicTmp.getId()){
                    found = true;
                }
            }
            if (!found){
                log.info("adding user - topic in db ...");
                this.userInfoRepository.addUserTopic(idUser, idTopic);
            } else {
                log.info("user - topic already in db : nothing done.");
            }
        }
        
    }

    public void delTopic(Long idUser, Long idTopic) {

        log.info("delete topic {} to the user {} in USER_TOPIC table", idTopic, idUser);

        Optional<UserInfo> user = this.findById(idUser);
        Optional<Topic> topic = topicService.findById(idTopic);
        
        if (user.isEmpty()){
            log.error("user {} not found", idUser);
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        } else if (topic.isEmpty()) {
            log.error("topic {} not found",idTopic);
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        } else {
            log.info("deleting user - topic in db ...");
            this.userInfoRepository.delUserTopic(idUser, idTopic);
        }
      
    }

}
