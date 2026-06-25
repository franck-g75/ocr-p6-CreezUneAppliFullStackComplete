package com.orion.mdd.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.orion.mdd.dto.CommentDto;
import com.orion.mdd.dto.PostDto;
import com.orion.mdd.exception.CustomException;
import com.orion.mdd.exception.ErrorCode;
import com.orion.mdd.models.Comment;
import com.orion.mdd.models.Post;
import com.orion.mdd.models.UserInfo;
import com.orion.mdd.repository.CommentRepository;
import com.orion.mdd.repository.PostRepository;
import com.orion.mdd.repository.UserInfoRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PostService {

    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private UserInfoService userInfoService;
    private UserInfoRepository userInfoRepository;

    public PostService(
        PostRepository postRepository, 
        CommentRepository commentRepository,
        UserInfoRepository userInfoRepository,
        UserInfoService userInfoService){
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userInfoService = userInfoService;
        this.userInfoRepository = userInfoRepository;
    }

    public Set<PostDto> findPostsByUserInfo(Long id){

        Optional<UserInfo> user = this.userInfoService.findById(id);

        if (user.isEmpty()){
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        } else {
            return this.postRepository.findPostList(user.get().getId());
        }
        
    }

    public PostDto findPostById(Long id){

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
                post.get().getTopic().getId());
            return postReturn;
        }
        
    }

    public List<CommentDto> findCommentsByPostId(Long id){

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
/*
    public List<CommentDto> addCommentsByPostId(Long id){

        Optional<Post> post = this.postRepository.findById(id);

        if (post.isEmpty()){
            throw new NotFoundException("post not found");
        } else {
            Comment comment;

            post.get().getComments().add(comment);
            
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
*/
    public void addComment(Long idPost, CommentDto commentDto){

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


    public void addPost( PostDto postDto ) throws CustomException {

        Optional<UserInfo> user = this.userInfoRepository.findByUsername(postDto.getUsername());
        
        if (user.isEmpty()) {
            log.error("adding post : user not found");
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        } else {
            this.postRepository.addPost(postDto.getTitle(), postDto.getContent(), postDto.getId_topic(), user.get().getId(), new Date());
        }
        
    }


}
