package com.orion.mdd.controllers;

import com.orion.mdd.dto.UserInfoDto;
import com.orion.mdd.exception.CustomException;
import com.orion.mdd.exception.ErrorCode;
import com.orion.mdd.exception.ErrorManagement;
import com.orion.mdd.models.UserInfo;
import com.orion.mdd.services.UserInfoService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
@Log4j2
public class UserInfoController {
    
   
    private final UserInfoService userInfoService;
    
    public UserInfoController(  UserInfoService userInfoService ) {
        this.userInfoService = userInfoService;
    }

    @GetMapping("/{string}")
    public ResponseEntity<?> findbyString(@PathVariable("string") String string) {
        
        UserInfo user = null;
        UserInfoDto usersDto = null;

        user = userInfoService.findByUsername(string);
        if (user!=null){
            usersDto = this.userToDto(user);
        } else {
            user = userInfoService.findByEmail(string);
            if (user!=null){
                usersDto = this.userToDto(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        
        return ResponseEntity.ok().body(usersDto);

    }

    /**
     * Create a user
     * @param userInfoDto
     * @return a 200 ok response if ok or a badRequest response if exception found
     */
    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody UserInfoDto userInfoDto){
        log.info("creating user..." + userInfoDto);
        try{
          UserInfo user = new UserInfo();

          user.setEmail(userInfoDto.getEmail());
          user.setUsername(userInfoDto.getUsername());
          user.setPwd(userInfoDto.getPwd());
          user.setComments(null);
          user.setTopics(null);
          user.setPosts(null);

          this.userInfoService.create(user);
          return ResponseEntity.ok().body(this.userToDto(user));
        } catch (Exception e){
          log.error("create user exception : " + e.getMessage());
          throw new CustomException(ErrorCode.INVALID_INPUT);
        }
    }

     /**
     * update a user
     * @param userInfoDto
     * @return a 200 ok response if ok or notFoundException if user not found or a badRequest response if exception found
     * @throws notFoundException if user not found
     * @throws badRequest response if exception found
     */
    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody UserInfoDto userInfoDto){
        log.info("updating user  {} ...", userInfoDto.getId() + "  :  " + userInfoDto.getUsername());
        try{
                  
          Optional<UserInfo> user = userInfoService.findById(userInfoDto.getId());

          if (user.isPresent()){
            this.userInfoService.update( user.get(), userInfoDto );
            return ResponseEntity.ok().body(this.userToDto(user.get()));
          } else {
            log.error("user {} not found in db.", userInfoDto.getId());
            return ErrorManagement.responseError(new CustomException(ErrorCode.DATA_NOT_FOUND));
          }

        } catch (Exception e){

          log.info("update user exception : " + e.toString());
          return ErrorManagement.responseError(e);

        }
    }

    @PostMapping("/subscribe/idUser/{idUser}/idTopic/{idTopic}")
    public ResponseEntity<?> addSubscription(@PathVariable("idTopic") Long idTopic, @PathVariable("idUser") Long idUser) {
        log.info("addSubscription " + idUser + " - " + idTopic + "...");
        try{

          this.userInfoService.addTopic(idUser,idTopic);
          return ResponseEntity.ok().build();

        } catch (Exception e){

          log.info("/subscribe/idUser/{}/idTopic/{} exception \n ",idUser,idTopic  + e.toString());
          return ErrorManagement.responseError(e);


        }
    }

    @PostMapping("/unsubscribe/idUser/{idUser}/idTopic/{idTopic}")
    public ResponseEntity<?> delSubscription(@PathVariable("idTopic") Long idTopic, @PathVariable("idUser") Long idUser) {
        log.info("delSubscription " + idUser + " - " + idTopic + "...");
        try{

          this.userInfoService.delTopic(idUser,idTopic);
          return ResponseEntity.ok().build();

        } catch (Exception e){

          log.error("/unsubscribe/idUser/{}/idTopic/{} exception \n ",idUser,idTopic  + e.toString());
          return ErrorManagement.responseError(e);


        }
    }

    private UserInfoDto userToDto(UserInfo user) throws CustomException {
      UserInfoDto usrReturn = new UserInfoDto();

      if (user==null){
        log.error("userToDto(null) user==null");
        throw new CustomException(ErrorCode.DATA_NOT_FOUND);
      } else {
        usrReturn.setId(user.getId());
        usrReturn.setEmail(user.getEmail());
        usrReturn.setUsername(user.getUsername());
        usrReturn.setPwd(user.getPwd());
      }
      
      return usrReturn;
    }

}
