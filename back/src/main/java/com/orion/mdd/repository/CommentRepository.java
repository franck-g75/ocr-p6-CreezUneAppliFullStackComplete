package com.orion.mdd.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.orion.mdd.models.Comment;
import com.orion.mdd.models.Post;

@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {

    public Optional<Iterable<Comment>> findByPost(Post post);

}
