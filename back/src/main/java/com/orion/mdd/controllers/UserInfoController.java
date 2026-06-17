package com.orion.mdd.controllers;

import com.orion.mdd.dto.UserInfoDto;
import com.orion.mdd.mapper.UserInfoMapper;
import com.orion.mdd.models.UserInfo;
import com.orion.mdd.services.UserInfoService;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
@Log4j2
public class UserInfoController {
    
    private final UserInfoMapper userInfoMapper;
    private final UserInfoService userInfoService;
    
    public UserInfoController(  UserInfoService userInfoService, UserInfoMapper userInfoMapper ) {
        this.userInfoMapper = userInfoMapper;
        this.userInfoService = userInfoService;
    }

    @GetMapping("/{string}")
    public ResponseEntity<?> findbyString(@PathVariable("string") String string) {
        
        UserInfo[] user = null;
        UserInfoDto usersDto = null;

        user = userInfoService.findByUsername(string);
        if (user.length>0){
            usersDto = this.userInfoMapper.toDto(user[0]);
        } else {
            user = userInfoService.findByEmail(string);
            if (user.length>0){
                usersDto = this.userInfoMapper.toDto(user[0]);
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
    public ResponseEntity<?> create(@RequestBody UserInfoDto userInfoDto){
        log.info("creating user...");
        try{
          log.info(userInfoDto);
          
          UserInfo user = new UserInfo();

          user.setEmail(userInfoDto.getEmail());
          user.setUsername(userInfoDto.getUsername());
          user.setPwd(userInfoDto.getPwd());
          user.setComments(null);
          user.setTopics(null);
          user.setPosts(null);

          this.userInfoService.create(user);
          return ResponseEntity.ok().body(this.userInfoMapper.toDto(user));
        } catch (Exception e){
          log.info("create user exception : " + e.toString().split(": ")[1]);
          return ResponseEntity.badRequest().body(e.toString().split(": ")[1]);
        }
    }

     /**
     * Create a user
     * @param userInfoDto
     * @return a 200 ok response if ok or a badRequest response if exception found
     */
    @PutMapping()
    public ResponseEntity<?> update(@RequestBody UserInfoDto userInfoDto){
        log.info("updating user...");
        try{
          log.info(userInfoDto);
          
          Optional<UserInfo> user = userInfoService.findById(userInfoDto.getId());

          if (user != null){
            user.get().setEmail(userInfoDto.getEmail());
            user.get().setUsername(userInfoDto.getUsername());
            user.get().setPwd(userInfoDto.getPwd());
          } else {
            return ResponseEntity.notFound().build();
          }

          log.info(user.get());

          this.userInfoService.update(user.get());
          return ResponseEntity.ok().body(this.userInfoMapper.toDto(user.get()));
        } catch (Exception e){
          log.info("update user exception : " + e.toString().split(": ")[1]);
          return ResponseEntity.badRequest().body(e.toString().split(": ")[1]);
        }
    }

    @PostMapping("/subscribe/idUser/{idUser}/idTopic/{idTopic}")
    public ResponseEntity<?> addSubscription(@PathVariable("idTopic") Long idTopic, @PathVariable("idUser") Long idUser) {
        log.info("addSubscription " + idUser + " - " + idTopic + "...");
        try{

          this.userInfoService.addTopic(idUser,idTopic);
          return ResponseEntity.ok().build();

        } catch (Exception e){
          log.info("subscribe/api/user/{:idTopic-:idUser} exception " + e.toString());
          return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/unsubscribe/idUser/{idUser}/idTopic/{idTopic}")
    public ResponseEntity<?> delSubscription(@PathVariable("idTopic") Long idTopic, @PathVariable("idUser") Long idUser) {
        log.info("delSubscription " + idUser + " - " + idTopic + "...");
        try{

          this.userInfoService.delTopic(idUser,idTopic);
          return ResponseEntity.ok().build();

        } catch (Exception e){
          log.info("/unsubscribe/api/user/{:idTopic-:idUser} exception " + e.toString());
          return ResponseEntity.badRequest().build();
        }
    }
}
