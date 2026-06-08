package com.orion.mdd.controllers;

import com.orion.mdd.dto.TopicDto;
import com.orion.mdd.dto.UsersDto;
//import com.orion.mdd.mapper.TopicMapper;
import com.orion.mdd.mapper.UsersMapper;
import com.orion.mdd.models.Topic;
import com.orion.mdd.models.Users;
import com.orion.mdd.services.TopicService;
import com.orion.mdd.services.UsersService;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
@Log4j2
public class UsersController {

    private final UsersMapper usersMapper;
    private final UsersService usersService;

    public UsersController( UsersService usersService, UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
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

}
