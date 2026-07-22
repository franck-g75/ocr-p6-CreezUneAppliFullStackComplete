package com.orion.mdd.controllers;

import com.orion.mdd.dto.MyResponseDto;
import com.orion.mdd.dto.ResponseCode;
import com.orion.mdd.dto.MyRequestUserInfoDto;
import com.orion.mdd.exception.CustomException;
import com.orion.mdd.exception.ErrorCode;
import com.orion.mdd.exception.ErrorManagement;
import com.orion.mdd.models.UserInfo;
import com.orion.mdd.services.UserInfoService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


/**
 * 
 * The class that handles user input and output ( add or delete subscription ) and update user
 * User must be identified in the app to use these routes
 * 
 * UserInfoController
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
@Log4j2
public class UserInfoController {
    
    private final UserInfoService userInfoService;
    
    /**
     * constructor
     * @param userInfoService a service class to manage users
     */
    public UserInfoController(  UserInfoService userInfoService ) {
        this.userInfoService = userInfoService;
    }

    /**
     * update a user
     * @param userInfoDto the request payload
     * @return a 200 ok response if ok or notFoundException if user not found or a badRequest response if exception found
     */
    @PutMapping()
    public ResponseEntity<MyResponseDto> update(@Valid @RequestBody MyRequestUserInfoDto userInfoDto){

        log.info("entering update user  {} :", userInfoDto.getId() + "  :  " + userInfoDto.getUsername() + " userToken=" + SecurityContextHolder.getContext().getAuthentication().getName());
        
        try{
          
          Optional<UserInfo> userRequest = userInfoService.findById(userInfoDto.getId());
          Optional<UserInfo> userToken = userInfoService.findByUsername( SecurityContextHolder.getContext().getAuthentication().getName() );

          if ( userToken.isPresent() && userRequest.isPresent() ){

            log.info("userToken.id=" +userToken.get().getId() + " userToken.userName=" + userToken.get().getUsername() + " userRequest.id=" + userRequest.get().getId() + " userRequest.username=" + userRequest.get().getUsername());

                this.userInfoService.update( userToken.get(), userInfoDto );

                Map<String, Object> infos = new HashMap<>();
                infos.put("email",userRequest.get().getEmail());
                infos.put("username",userRequest.get().getUsername());
                
                MyResponseDto response = new MyResponseDto(
                  ResponseCode.USER_UPDATE_SUCCESS.getCode(),
                  ResponseCode.USER_UPDATE_SUCCESS.getMessage(),
                  infos);

                return ResponseEntity.ok().body(response);
          
          } else {
            log.error("userToken or user Request or both are not found in db.");
            return ErrorManagement.responseError(new CustomException(ErrorCode.DATA_NOT_FOUND));
          }

        } catch (Exception e){

          log.info("update user exception : " + e.toString());
          return ErrorManagement.responseError(e);

        }
    }

    /**
     * add subscription to the user logged in
     * @param idTopic the topic id the user want to subscribe
     * @return the appropriate response (error or entity)
     */
    @PostMapping("/subscribe/topic/{idTopic}")
    public ResponseEntity<MyResponseDto> addSubscription(@PathVariable("idTopic") Long idTopic) {
        log.info("addSubscription - " + idTopic + " ...");
        try{

          Optional<UserInfo> userToken = userInfoService.findByUsername( SecurityContextHolder.getContext().getAuthentication().getName() );

          if (userToken.isPresent()){
            this.userInfoService.addTopic(userToken.get().getId(),idTopic);
            MyResponseDto response = new MyResponseDto();
            response.setCode(ResponseCode.OPERATION_SUCCESS.getCode());
            response.setMessage(ResponseCode.OPERATION_SUCCESS.getMessage());
            return ResponseEntity.ok().body(response);
          } else {
            return ErrorManagement.responseError(new CustomException(ErrorCode.DATA_NOT_FOUND));
          }

        } catch (Exception e) {

          log.info("/subscribe/topic/{} exception \n ",idTopic  + e.toString());
          return ErrorManagement.responseError(e);

        }
    }

    /**
     * delete subscription to the user logged in
     * @param idTopic the topic id the user want to unsubscribe
     * @return the appropriate response (error or entity)
     */
    @PostMapping("/unsubscribe/topic/{idTopic}")
    public ResponseEntity<MyResponseDto> delSubscription(@PathVariable("idTopic") Long idTopic) {
        log.info("delSubscription - " + idTopic + " ...");
        try{

          Optional<UserInfo> userToken = userInfoService.findByUsername( SecurityContextHolder.getContext().getAuthentication().getName() );
          
          if (userToken.isPresent()){
            this.userInfoService.delTopic(userToken.get().getId(),idTopic);
            MyResponseDto response = new MyResponseDto();
            response.setCode(ResponseCode.OPERATION_SUCCESS.getCode());
            response.setMessage(ResponseCode.OPERATION_SUCCESS.getMessage());
            return ResponseEntity.ok().body(response);
          } else {
            return ErrorManagement.responseError(new CustomException(ErrorCode.DATA_NOT_FOUND));
          }

        } catch (Exception e){

          log.error("/unsubscribe/topic/{} exception \n ",idTopic  + e.toString());
          return ErrorManagement.responseError(e);

        }
    }

}


