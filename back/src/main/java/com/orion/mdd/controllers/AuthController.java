package com.orion.mdd.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.dto.LoginDto;
import com.orion.mdd.dto.MyResponseDto;
import com.orion.mdd.dto.ResponseCode;
import com.orion.mdd.dto.UserInfoDto;
import com.orion.mdd.exception.CustomException;
import com.orion.mdd.exception.ErrorCode;
import com.orion.mdd.exception.ErrorManagement;
import com.orion.mdd.models.UserInfo;
import com.orion.mdd.security.JwtUtils;
import com.orion.mdd.security.services.UserDetailsImpl;
import com.orion.mdd.services.UserInfoService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Log4j2
public class AuthController {

    private final UserInfoService userInfoService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthController( 
        AuthenticationManager authenticationManager, 
        UserInfoService userInfoService, 
        PasswordEncoder passwordEncoder,
        JwtUtils jwtUtils ) {
        this.userInfoService = userInfoService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDto loginDto) {
      
        log.info("récupérer le username & l'authentication...");
        try{
            //find the username with str
            Optional<UserInfo> user = null;
            Authentication authentication = null;
            user = userInfoService.findByUsername(loginDto.getStr());
            if (user.isPresent()){
                //username found in DB
                authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(loginDto.getStr(), loginDto.getPwd()) );
            } else {
                user = userInfoService.findByEmail(loginDto.getStr());
                if (user.isPresent()){
                    //email found in DB
                    authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(user.get().getUsername(), loginDto.getPwd()) );
                } 

            }

            if (authentication == null) {
                log.error("login({}) custom exception : authentication str not found.  ", loginDto.getStr());
                return ErrorManagement.responseError(new CustomException(ErrorCode.INVALID_CREDENTIALS));
            }
            
            log.info("régler l'authentification..." + authentication.getName());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("générer le token...");
            String jwt = jwtUtils.generateJwtToken(authentication);
            log.info("récupérer le userDetails...");
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            log.info("retourner la Response...");

            Map<String, String> infos = new HashMap<>();
            infos.put("id",userDetails.getId().toString());
            infos.put("email",userDetails.getEmail());
            infos.put("username",userDetails.getUsername());
            infos.put("token",jwt);

            return ResponseEntity.ok().body(
                new MyResponseDto(ResponseCode.USER_CONNEXION_SUCCESS.getMessage(),ResponseCode.USER_CONNEXION_SUCCESS.getCode(),infos)
            );

        } catch (AuthenticationException ae) {
            log.info(ae.toString());
            log.error("login({}) custom exception : authentication failed.  ", loginDto.getStr());
            return ErrorManagement.responseError(new CustomException(ErrorCode.INVALID_CREDENTIALS));
        }
        
        
        /*   
        UserInfo user = null;
        UserInfoDto usersDto = null;

        user = userInfoService.findByUsername(loginDto.getStr());
        if (user!=null){
            usersDto = this.userToDto(user);
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(usersDto);
        */


                
        

    }

    /**
     * Create a user
     * @param userInfoDto
     * @return a 200 ok response if ok or a badRequest response if exception found
     */
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserInfoDto userInfoDto){
        log.info("creating user..." + userInfoDto);
        try{
          UserInfo user = new UserInfo();

          user.setEmail(userInfoDto.getEmail());
          user.setUsername(userInfoDto.getUsername());
          user.setPwd(passwordEncoder.encode(userInfoDto.getPwd()));
          user.setComments(null);
          user.setTopics(null);
          user.setPosts(null);

          this.userInfoService.create(user);

          Map<String, String> infos = new HashMap<>();
          infos.put("email", user.getEmail());
          infos.put("username", user.getUsername());
        
          MyResponseDto myResponse = new MyResponseDto(ResponseCode.USER_CREATION_SUCCESS.getCode(), ResponseCode.USER_CREATION_SUCCESS.getMessage(), infos);
          
          return ResponseEntity.status(ResponseCode.USER_CREATION_SUCCESS.getHttpStatus()).body(myResponse);
          
        } catch (Exception e){
          log.error("create user exception : " + e.getMessage());
          throw e;
        }
    }

    /*
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
    */
}
