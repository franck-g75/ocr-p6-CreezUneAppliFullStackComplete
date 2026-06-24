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

import com.orion.mdd.dto.PostDto;
import com.orion.mdd.models.Topic;
import com.orion.mdd.dto.CommentDto;
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


    public PostController( PostService postService, TopicService topicService) {
        this.postService = postService;
        this.topicService = topicService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findAll(@PathVariable("id") Long idUser) {
        log.info("findAll(iduser={}) ...", idUser);
        try{
            Set<PostDto> posts = this.postService.findPostsByUserInfo(idUser);
            return ResponseEntity.ok().body(posts);
        } catch (Exception e){
            log.error("findAll(iduser) exception : " + e.toString());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> findAllComments(@PathVariable("id") Long postId) {
        log.info("findAllComments(iduser={}) ...", postId);
        try{
            List<CommentDto> comments = this.postService.findCommentsByPostId(postId);
            return ResponseEntity.ok().body(comments);
        } catch(Exception e) {
            log.error("findAllComments exception : " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<?> addComment(@PathVariable("id") Long idPost, @Valid @RequestBody CommentDto commentDto) {

        log.info("addComment(idpost={}) commentDto= {}  ...", idPost, commentDto);
        try{
            this.postService.addComment(idPost, commentDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("add comment exception : " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }


    @PostMapping("")
    public ResponseEntity<?> addPost(@Valid @RequestBody PostDto postDto) {
        
        log.info("addPost post=" + postDto.toString());

        Optional<Topic> topic = this.topicService.findById(postDto.getId_topic());

        if (topic.isPresent()){
            try{
                this.postService.addPost(postDto);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                log.error("add post exception : " + e.getMessage());
                return ResponseEntity.badRequest().build();
            }
        } else {
            log.error("addPost : Topic non trouvé.");
            return ResponseEntity.badRequest().build();
        }

    }
}
