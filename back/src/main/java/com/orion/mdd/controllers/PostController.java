package com.orion.mdd.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.dto.CommentDto;
import com.orion.mdd.dto.MyResponseDto;
import com.orion.mdd.dto.PostDto;
import com.orion.mdd.dto.ResponseCode;
import com.orion.mdd.exception.CustomException;
import com.orion.mdd.exception.ErrorCode;
import com.orion.mdd.exception.ErrorManagement;
import com.orion.mdd.models.Topic;
import com.orion.mdd.models.UserInfo;
import com.orion.mdd.services.PostService;
import com.orion.mdd.services.TopicService;
import com.orion.mdd.services.UserInfoService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

/**
 * PostController is a controller class to manage the posts ans comment operation 
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/post")
@Log4j2
public class PostController {

    private final PostService postService;
    private final TopicService topicService;
    private final UserInfoService userInfoService;

    public PostController( 
        PostService postService, 
        TopicService topicService,
        UserInfoService userInfoService
       ) {
        this.postService = postService;
        this.topicService = topicService;
        this.userInfoService = userInfoService;
    }

    /**
     * find all the posts for the user connected
     * @return MyResponseDto containing the post list or an error
     */
    @GetMapping("/user")
    public ResponseEntity<MyResponseDto> findAll() {
        log.info("findAll() ...");

        try{
           
            Optional<UserInfo> userToken = userInfoService.findByUsername( SecurityContextHolder.getContext().getAuthentication().getName() );

            if (userToken.isPresent()){

                Set<PostDto> posts = this.postService.findPostsByUserInfo(userToken.get().getId());

                MyResponseDto response = new MyResponseDto();
                response.setCode(ResponseCode.DATA_FOUND.getCode());
                response.setMessage(ResponseCode.DATA_FOUND.getMessage());
                Map<String, Object> infos = new HashMap<>();
                infos.put("posts", posts);
                response.setData(infos);
                    
                return ResponseEntity.ok().body(response);

            } else {

                log.info("/api/post/user error token username not found in db.");
                return ErrorManagement.responseError(new CustomException(ErrorCode.DATA_NOT_FOUND));

            }
            
        } catch (Exception e){

            log.info("/api/post/user exception \n" + e.toString());
            return ErrorManagement.responseError(e);
           
        }
    }

    /**
     * Find the post identified by id parameter
     * @param idPost the id of the post searched
     * @return MyResponseDto containing the post or an errror
     */
    @GetMapping("/{id}")
    public ResponseEntity<MyResponseDto> findPost(@PathVariable("id") @NonNull Long idPost) {
        log.info("findPost(idpost={}) ...", idPost);
        try{
            
            Optional<UserInfo> userToken = userInfoService.findByUsername( SecurityContextHolder.getContext().getAuthentication().getName() );

            if (userToken.isPresent()){

                PostDto post = this.postService.findPostById(idPost);
            
                MyResponseDto response = new MyResponseDto();
                response.setCode(ResponseCode.DATA_FOUND.getCode());
                response.setMessage(ResponseCode.DATA_FOUND.getMessage());
                Map<String, Object> infos = new HashMap<>();
                infos.put("post", post);
                response.setData(infos);
                
                return ResponseEntity.ok().body(response);
            } else {
                log.info("/api/post/{}' error token username not found in db.", idPost);
                return ErrorManagement.responseError(new CustomException(ErrorCode.DATA_NOT_FOUND));
            }     

        } catch (Exception e){

            log.error("findPost(idpost) exception : " + e.toString());
            return ErrorManagement.responseError(e);
            
        }
    }

    /**
     * Add a post 
     * @param postDto the post to add
     * @return a MyResponseDto in case of success or error
     */
    @PostMapping("")
    public ResponseEntity<MyResponseDto> addPost(@Valid @RequestBody PostDto postDto) {
        
        log.info("addPost post=" + postDto.toString());
        try{

            Optional<Topic> topic = this.topicService.findById(postDto.getId_topic());
            Optional<UserInfo> userToken = userInfoService.findByUsername( SecurityContextHolder.getContext().getAuthentication().getName() );

            if (topic.isPresent() && userToken.isPresent()){
            
                postDto.setUsername(userToken.get().getUsername()); //a user X cannot add a post for a user Y
                this.postService.addPost(postDto);

                MyResponseDto response = new MyResponseDto(ResponseCode.OPERATION_SUCCESS.getMessage(), ResponseCode.OPERATION_SUCCESS.getCode(), null);

                return ResponseEntity.ok().body(response);

            } else {

                log.error("addPost : Topic or userToken non trouvé.");
                return ErrorManagement.responseError( new CustomException(ErrorCode.DATA_NOT_FOUND) );

            }
        } catch (Exception e) {
            log.error("add post exception : " + e.getMessage());
            return ErrorManagement.responseError(e);
        }

    }

    /**
     * findAllComments finds all comments of the post identified by postId in parameter 
     * @param postId the id of of the comment's post wanted
     * @return MyResponseDto if success (200) or a MyResponseDto if there is an error
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<MyResponseDto> findAllComments(@PathVariable("id") @NonNull Long postId) {
        log.info("findAllComments(iduser={}) ...", postId);
        try{
            Optional<UserInfo> userToken = userInfoService.findByUsername( SecurityContextHolder.getContext().getAuthentication().getName() );
            List<CommentDto> comments = this.postService.findCommentsByPostId(postId);

            if ( userToken.isPresent() && comments != null ){

                MyResponseDto response = new MyResponseDto();
                response.setCode(ResponseCode.DATA_FOUND.getCode());
                response.setMessage(ResponseCode.DATA_FOUND.getMessage());
                Map<String, Object> infos = new HashMap<>();
                infos.put("comments", comments);
                response.setData(infos);
                return ResponseEntity.ok().body(response);

            } else {

                log.error("findAllComments : userToken not found in DB or comments list null.");
                return ErrorManagement.responseError( new CustomException(ErrorCode.DATA_NOT_FOUND) );
                
            }
        } catch(Exception e) {
            log.error("findAllComments exception : " + e.getMessage());
            return ErrorManagement.responseError(e);
        }
    }

    /**
     * addComment add the comment CommentDto to the post identified by idPost 
     * The username in CommentDto is useless to identify the author of the comment : the token user is choosen instead
     * @param idPost the id of the post 
     * @param commentDto the comment to add
     * @return a MyResponseDto in case of success or error
     */
    @PostMapping("/{id}/comment")
    public ResponseEntity<MyResponseDto> addComment(@PathVariable("id") @NonNull Long idPost, @Valid @RequestBody CommentDto commentDto) {

        log.info("addComment(idpost={}) commentDto= {}  ...", idPost, commentDto);
        try{

            Optional<UserInfo> userToken = userInfoService.findByUsername( SecurityContextHolder.getContext().getAuthentication().getName() );
            if (userToken.isPresent()){
                commentDto.setUsername(userToken.get().getUsername()); //User X can't post a comment for user Y
                this.postService.addComment(idPost, commentDto);
                return ResponseEntity.ok().body(
                    new MyResponseDto(ResponseCode.OPERATION_SUCCESS.getMessage(), ResponseCode.OPERATION_SUCCESS.getCode(), null)
                );
            } else {
                log.error("addComments : userToken not found in DB or comments list null.");
                return ErrorManagement.responseError( new CustomException(ErrorCode.DATA_NOT_FOUND) );
            }
            
        } catch (Exception e) {
            log.error("addComment exception : " + e.getMessage());
            return ErrorManagement.responseError(e);
        }

    }


    
}
