package com.orion.mdd.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.dto.MyRequestLoginDto;
import com.orion.mdd.dto.MyResponseDto;
import com.orion.mdd.dto.ResponseCode;
import com.orion.mdd.dto.MyRequestUserInfoDto;
import com.orion.mdd.exception.CustomException;
import com.orion.mdd.exception.ErrorCode;
import com.orion.mdd.exception.ErrorManagement;
import com.orion.mdd.models.UserInfo;
import com.orion.mdd.security.services.JwtService;
import com.orion.mdd.security.services.UserDetailsImpl;
import com.orion.mdd.services.UserInfoService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

/**
 * class AuthController
 * use this class to log in or / and to create an account
 * this class is under "permit all" access
 */
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class AuthController {

    private final UserInfoService userInfoService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController( 
        AuthenticationManager authenticationManager, 
        UserInfoService userInfoService, 
        PasswordEncoder passwordEncoder,
        JwtService jwtService ) {
        this.userInfoService = userInfoService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Login function
     * @param loginDto
     * @return MyResponseDto with data fill in with id, login, email and token if OK and an HttpErrorResponse INVALID_CREDENTIALS if not ok
     */
    @PostMapping("/api/auth/login")
    public ResponseEntity<MyResponseDto> loginUser(@Valid @RequestBody MyRequestLoginDto loginDto) {
      
        UsernamePasswordAuthenticationToken userToken = null;  //the authenticated user found

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
            
            final List<SimpleGrantedAuthority> grantedAuths = new ArrayList<>();                //empty role list
            //final String encodedPassword = this.passwordEncoder.encode( loginDto.getPwd() );    //password is encoded in db and in network
            final UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();  //
            userToken = new UsernamePasswordAuthenticationToken( principal , loginDto.getPwd() , grantedAuths ); //UsernamePasswordAuthenticationToken
            SecurityContextHolder.getContext().setAuthentication(userToken);    //authenticate the user
            String jwt = jwtService.generateToken(authentication);              //generate the jwt

            //return the MyResponseDto
            Map<String, Object> infos = new HashMap<>();
            infos.put("id",principal.getId().toString());
            infos.put("email",principal.getEmail());
            infos.put("username",principal.getUsername());
            infos.put("token",jwt);

            return ResponseEntity.ok().body(
                new MyResponseDto(ResponseCode.USER_CONNEXION_SUCCESS.getMessage(),ResponseCode.USER_CONNEXION_SUCCESS.getCode(),infos)
            );

        } catch (AuthenticationException ae) {
            log.info(ae.toString());
            log.error("login({}) custom exception : authentication failed.  ", loginDto.getStr());
            return ErrorManagement.responseError(new CustomException(ErrorCode.INVALID_CREDENTIALS));
        }
        
    }

    /**
     * Create user function
     * @param userInfoDto
     * @return a MyResponseBody 201 USER_CREATION_SUCCESS with email and username or a badRequest response INVALID_INPUT or INVALID_USERNAME or INVALID_EMAIL if exception found
     */
    @PostMapping("/api/auth/register")
    public ResponseEntity<MyResponseDto> createUser(@Valid @RequestBody MyRequestUserInfoDto userInfoDto){
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

          Map<String, Object> infos = new HashMap<>();
          infos.put("email", user.getEmail());
          infos.put("username", user.getUsername());
        
          MyResponseDto myResponse = new MyResponseDto(ResponseCode.USER_CREATION_SUCCESS.getCode(), ResponseCode.USER_CREATION_SUCCESS.getMessage(), infos);
          
          return ResponseEntity.status(ResponseCode.USER_CREATION_SUCCESS.getHttpStatus()).body(myResponse);
          
        } catch (Exception e){
          log.error("create user exception : " + e.getMessage());
          throw e;
        }
    }

}
