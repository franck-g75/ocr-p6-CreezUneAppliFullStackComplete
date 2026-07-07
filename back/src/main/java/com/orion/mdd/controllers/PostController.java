package com.orion.mdd.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.dto.CommentDto;
import com.orion.mdd.dto.PostDto;
import com.orion.mdd.exception.CustomException;
import com.orion.mdd.exception.ErrorCode;
import com.orion.mdd.exception.ErrorManagement;
import com.orion.mdd.models.Topic;
import com.orion.mdd.services.PostService;
import com.orion.mdd.services.TopicService;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/post")
@Log4j2
public class PostController {

    private final PostService postService;
    private final TopicService topicService;
    
    public PostController( 
        PostService postService, 
        TopicService topicService
       ) {
        this.postService = postService;
        this.topicService = topicService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> findAll(@PathVariable("id") Long idUser) {
        log.info("findAll(iduser={}) ...", idUser);
        try{
            Set<PostDto> posts = this.postService.findPostsByUserInfo(idUser);
            return ResponseEntity.ok().body(posts);
        } catch(CustomException ce){
            log.error("findAll(iduser) customException : " + ce.toString());
            switch (ce.getErrorCode()){
                case INVALID_INPUT : 
                    return ResponseEntity.badRequest().build();
                case DATA_NOT_FOUND :
                    return ResponseEntity.notFound().build();
                case SERVER_ERROR : 
                    return ResponseEntity.internalServerError().build();
                default :
                    return ResponseEntity.notFound().build();
             }
        } catch (Exception e){
            log.error("findAll(iduser) exception : " + e.toString());
            if (e instanceof RuntimeException){
                return ResponseEntity.internalServerError().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findPost(@PathVariable("id") Long idPost) {
        log.info("findPost(idpost={}) ...", idPost);
        try{
            PostDto post = this.postService.findPostById(idPost);
            return ResponseEntity.ok().body(post);
        } catch (CustomException ce) {
            log.error("findPost(idpost) customException : " + ce.toString());
            throw ce;
        } catch (Exception e){
            log.error("findPost(idpost) exception : " + e.toString());
            if (e instanceof RuntimeException){
                return ResponseEntity.internalServerError().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> findAllComments(@PathVariable("id") Long postId) {
        log.info("findAllComments(iduser={}) ...", postId);
        try{
            List<CommentDto> comments = this.postService.findCommentsByPostId(postId);
            return ResponseEntity.ok().body(comments);
        } catch (CustomException ce) {
            log.error("findAllComments(postId) customException : " + ce.toString());
            throw ce;
        } catch(Exception e) {
            log.error("findAllComments exception : " + e.getMessage());
            if (e instanceof RuntimeException){
                return ResponseEntity.internalServerError().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<?> addComment(@PathVariable("id") Long idPost, @Valid @RequestBody CommentDto commentDto) {

        log.info("addComment(idpost={}) commentDto= {}  ...", idPost, commentDto);
        try{
            this.postService.addComment(idPost, commentDto);
            return ResponseEntity.ok().build();
        } catch (CustomException ce) {
            log.error("addComment(idPost,commentDto) customException : " + ce.toString());
            throw ce;
        } catch (Exception e) {
            log.error("addComment exception : " + e.getMessage());
            if (e instanceof RuntimeException){
                return ResponseEntity.internalServerError().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }

    }


    /**
     * 
     * @param postDto
     * @return
     * @throws CustomException
     */
    @PostMapping("")
    public ResponseEntity<?> addPost(@Valid @RequestBody PostDto postDto) throws CustomException {
        
        log.info("addPost post=" + postDto.toString());

        Optional<Topic> topic = this.topicService.findById(postDto.getId_topic());

        if (topic.isPresent()){
           
            try{
                this.postService.addPost(postDto);
                return ResponseEntity.ok().build();
            } catch (CustomException ce) {
                log.error("add post customexception : " + ce.getMessage());
                return ErrorManagement.responseError(ce);
            } catch (Exception e) {
                log.error("add post exception : " + e.getMessage());
                return ErrorManagement.responseError(e);
            }

        } else { //topic not present

            log.error("addPost : Topic non trouvé.");
            return ErrorManagement.responseError( new CustomException(ErrorCode.DATA_NOT_FOUND) );

        }

    }
}
