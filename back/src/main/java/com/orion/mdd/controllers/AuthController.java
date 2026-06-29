package com.orion.mdd.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.dto.UserInfoDto;
import com.orion.mdd.exception.CustomException;
import com.orion.mdd.exception.ErrorCode;
import com.orion.mdd.models.UserInfo;
import com.orion.mdd.services.UserInfoService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Log4j2
public class AuthController {

    private final UserInfoService userInfoService;
    
    public AuthController(  UserInfoService userInfoService ) {
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
