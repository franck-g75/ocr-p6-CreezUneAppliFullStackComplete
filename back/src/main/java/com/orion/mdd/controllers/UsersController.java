package com.orion.mdd.controllers;

import com.orion.mdd.dto.UsersDto;
import com.orion.mdd.exception.NotFoundException;

import com.orion.mdd.mapper.UsersMapper;
import com.orion.mdd.models.Topic;
import com.orion.mdd.models.Users;

import com.orion.mdd.services.TopicService;
import com.orion.mdd.services.UsersService;

import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
@Log4j2
public class UsersController {

    
    private final UsersMapper usersMapper;
    private final UsersService usersService;
    private final TopicService topicService;
    

    public UsersController(  UsersService usersService, UsersMapper usersMapper, TopicService topicService) {
        this.usersMapper = usersMapper;
        this.topicService = topicService;
        this.usersService = usersService;
        
    }

    @GetMapping("/{string}")
    public ResponseEntity<?> findbyString(@PathVariable("string") String string) {
        
        Users users = null;
        UsersDto usersDto = null;

        users = usersService.findByUsername(string);
        if (users!=null){
            usersDto = this.usersMapper.convertToUsersDto(users);
        } else {
            users = usersService.findByEmail(string);
            if (users!=null){
                usersDto = this.usersMapper.convertToUsersDto(users);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        
        return ResponseEntity.ok().body(usersDto);

    }



    @PostMapping("/idUser/{idUser}/idTopic/{idTopic}")
    public ResponseEntity<?> addSubsiption(@PathVariable("idTopic") Long idTopic, @PathVariable("idUser") Long idUser) {

        try{

          this.usersService.addTopic(idUser,idTopic);
          log.info(idUser + " - " + idTopic);

          return ResponseEntity.ok().build();
        } catch (Exception e){
          log.info("api/user/{:idTopic-:idUser} exception " + e.toString());
          return ResponseEntity.badRequest().build();
        }
    }

}
