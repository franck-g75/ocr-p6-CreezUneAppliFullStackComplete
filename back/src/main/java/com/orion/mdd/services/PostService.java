package com.orion.mdd.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.orion.mdd.dto.CommentDto;
import com.orion.mdd.dto.PostDto;
import com.orion.mdd.exception.CustomException;
import com.orion.mdd.exception.ErrorCode;
import com.orion.mdd.models.Comment;
import com.orion.mdd.models.Post;
import com.orion.mdd.models.Topic;
import com.orion.mdd.models.UserInfo;
import com.orion.mdd.repository.CommentRepository;
import com.orion.mdd.repository.PostRepository;
import com.orion.mdd.repository.UserInfoRepository;

import lombok.extern.log4j.Log4j2;

/**
 * class that give services for post class
 * PostService
 */
@Log4j2
@Service
public class PostService {

    private TopicService topicService;
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private UserInfoRepository userInfoRepository;

    /**
     * constructor
     * @param postRepository to access data base post table
     * @param commentRepository to access data base comment table
     * @param userInfoRepository to access data base user info table 
     * @param topicService to check the existence of the topic in addPost
     */
    public PostService(
        PostRepository postRepository, 
        CommentRepository commentRepository,
        UserInfoRepository userInfoRepository,
        TopicService topicService
        ){
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userInfoRepository = userInfoRepository;
        this.topicService = topicService;
    }

    /**
     * findPostsByUserInfo 
     * @param id id of the user
     * @return all the posts related to the topic the user had subscribed 
     * @throws CustomException if user not found
     */
    public Set<PostDto> findPostsByUserInfo(@NonNull Long id) throws CustomException{

        Optional<UserInfo> user = this.userInfoRepository.findById(id);

        if (user.isEmpty()){
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        } else {
            return this.postRepository.findPostList(user.get().getId());
        }
        
    }

    /**
     * findPostById
     * @param id id of the post 
     * @return a postDto if exist
     * @throws CustomException if post n° id is not found
     */
    public PostDto findPostById(@NonNull Long id) throws CustomException{

        Optional<Post> post = this.postRepository.findById(id);

        if (post.isEmpty()){
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        } else {
            PostDto postReturn = new PostDto(
                post.get().getId(),
                post.get().getTitle(),
                post.get().getContent(), 
                post.get().getCreated_at(), 
                post.get().getUserInfo().getUsername(), 
                post.get().getTopic().getId(),
                post.get().getTopic().getTitle()
            );

            return postReturn;
        }
        
    }

    /**
     * findCommentsByPostId
     * @param id of the post
     * @return a list of comment DTO if exist
     * @throws CustomException if post not found
     */
    public List<CommentDto> findCommentsByPostId(@NonNull Long id) throws CustomException{

        Optional<Post> post = this.postRepository.findById(id);

        if (post.isEmpty()){
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        } else {

            Optional<Iterable<Comment>> comments = this.commentRepository.findByPost(post.get());
            if (post.isEmpty()){
                return new ArrayList<CommentDto>();
            } else {
                List<CommentDto> retour = new ArrayList<CommentDto>(); 
                for(Comment c : comments.get()){
                    CommentDto commentDto = new CommentDto();
                    commentDto.setId(c.getId());
                    commentDto.setContent(c.getContent());
                    commentDto.setUsername(c.getUserInfo().getUsername());
                    retour.add(commentDto);
                }
                return retour;
            }
        }
    }



    /**
     * addComment 
     * @param idPost the post to add the comment
     * @param commentDto the comment to add
     * @throws CustomException if the post or the user is not found
     */
    public void addComment(@NonNull Long idPost, CommentDto commentDto) throws CustomException {

        Optional<UserInfo> user = this.userInfoRepository.findByUsername(commentDto.getUsername());
        Optional<Post> post = this.postRepository.findById(idPost);

        if (post.isEmpty()){
            log.error("adding comment : post not found");
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        } else {
            if (user.isEmpty()) {
                log.error("adding comment : user not found");
                throw new CustomException(ErrorCode.DATA_NOT_FOUND);
            } else {
                this.postRepository.addComment(post.get().getId(),commentDto.getContent(),user.get().getId(),new Date());
            }
        }
        
    }


    /**
     * addPost 
     * @param postDto the post informations to add
     * @throws CustomException if the user is not found
     */
    public void addPost( PostDto postDto ) throws CustomException {

        Optional<UserInfo> user = this.userInfoRepository.findByUsername(postDto.getUsername());
        Optional<Topic> topic = this.topicService.findById(postDto.getId_topic());

        if (user.isEmpty()) {
            log.error("adding post : user not found");
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        } else if (topic.isEmpty()) {
            log.error("adding post : topic not found");
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        } else {
            this.postRepository.addPost(postDto.getTitle(), postDto.getContent(), postDto.getId_topic(), user.get().getId(), new Date());
        }
        
    }

}
