package com.orion.mdd.services;

import java.util.Optional;
import java.util.Set;

import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.orion.mdd.dto.MyRequestUserInfoDto;
import com.orion.mdd.exception.CustomException;
import com.orion.mdd.exception.ErrorCode;
import com.orion.mdd.models.Topic;
import com.orion.mdd.models.UserInfo;
import com.orion.mdd.repository.UserInfoRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserInfoService {

    private final PasswordEncoder passwordEncoder;
    private final TopicService topicService;
    private final UserInfoRepository userInfoRepository;

    public UserInfoService(
            UserInfoRepository userInfoRepository, 
            TopicService topicService,
            PasswordEncoder passwordEncoder
        ){
        this.userInfoRepository = userInfoRepository;
        this.topicService = topicService;
        this.passwordEncoder = passwordEncoder; 
    }

    /**
     * find a user by email (String)
     * @param email to search
     * @return an Optionnal<UserInfo>
     */
    public Optional<UserInfo> findByEmail(String email)  {
        log.info("findByEmail({})...",email);
        return userInfoRepository.findByEmail(email);
    }
    
    /**
     * find a user by username (String)
     * @param username to search
     * @return an Optionnal<UserInfo>
     */
    public Optional<UserInfo> findByUsername(String username) {
        log.info("findByUsername({})...",username);
        return userInfoRepository.findByUsername(username);
    }

    /**
     * find a user by id (Long)
     * @param id to search
     * @return an Optionnal<UserInfo>
     */
    public Optional<UserInfo> findById(@NonNull Long id){

        log.info("findById({})...",id);
        Optional<UserInfo> user = userInfoRepository.findById(id);
        if (user.isEmpty()){
            log.error("findById({}) ==> user not found",id);
        }
        return user;
        
    }

    /**
     * create user if email AND username not found in DB
     * @param user the user to create in DB
     * @return a UserInfo if ok
     * @throws CustomException if email or username found in DB
     */
    public UserInfo create(UserInfo user) throws CustomException {

        log.info("create({})...",user);

        Optional<UserInfo> userByUsername = userInfoRepository.findByUsername(user.getUsername());
        Optional<UserInfo> userByMail = userInfoRepository.findByEmail(user.getEmail());
        
        if (userByMail.isPresent()) {             //existing User (same email)
            log.error("create({}) mail is found in db",user);
            throw new CustomException(ErrorCode.INVALID_EMAIL);
        } else if (userByUsername.isPresent()) {  //existing user (same username)
            log.error( "create({}) username is found in db", user);
            throw new CustomException(ErrorCode.INVALID_USERNAME);
        } else { //non existing user : save it
            log.info("saving user ...");
            return userInfoRepository.save(user);
        }

    }

    /**
     * update user function
     * @param userToChange user found by the id
     * @param userDto data given in the request
     * @return the User modified
     * @throws CustomException
     */
    public UserInfo update(UserInfo userToChange, MyRequestUserInfoDto userDto) throws CustomException {

        Optional<UserInfo> userByUsername = userInfoRepository.findByUsername(userDto.getUsername());
        Optional<UserInfo> userByMail = userInfoRepository.findByEmail(userDto.getEmail());
        
        log.info("Entering update user : userByMail:" + userByMail.isPresent() + "   userByUsername:" + userByUsername.isPresent() + "   userDto:" + userDto.getId());

        if ((userToChange.getId()==userDto.getId()) && (userDto.getId()!=0)){
            //
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
                userToChange.setEmail(userDto.getEmail());
                userToChange.setUsername(userDto.getUsername());
                userToChange.setPwd(passwordEncoder.encode(userDto.getPwd()));
                return userInfoRepository.save(userToChange);
            }

        } else {
            log.error("Les identifiants sont differents.");
            throw new CustomException(ErrorCode.NOT_AUTHORIZED);
        }
        
    }

    /**
     * addTopic add a topic-user relationship if user and topic found in db
     * If relationship exist in DB, nothing is done except log.
     * @param idUser user id to add
     * @param idTopic topic id to add
     * @throws CustomException if user or topic not found
     */
    public void addTopic(@NonNull Long idUser, long idTopic) throws CustomException {

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

    /**
     * delete a topic-user relationship if topic and user found in db
     * @param idUser the id of the user
     * @param idTopic the id of the topic
     * throws CustomException if topic or user not found
     */
    public void delTopic(@NonNull Long idUser, @NonNull Long idTopic) throws CustomException {

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
