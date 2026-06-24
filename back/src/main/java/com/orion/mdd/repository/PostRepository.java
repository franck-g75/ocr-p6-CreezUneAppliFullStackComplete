package com.orion.mdd.repository;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import com.orion.mdd.dto.PostDto;
import com.orion.mdd.models.Post;

@Transactional
public interface PostRepository extends JpaRepository<Post, Long>{

    String qSelect = " SELECT POST.id, POST.title as 'title', POST.content as 'content', POST.created_at as 'created_at', fait.username as 'username', POST.topic_id as 'id_topic' ";
    String qFromInner1 = " FROM POST INNER JOIN TOPIC ON POST.topic_id=TOPIC.id ";
    String qInner2 = " INNER JOIN TOPIC_USER as abo1 ON abo1.topic_id = TOPIC.id  ";
    String qInner3 = " INNER JOIN USER_INFO as abo2 ON abo1.user_info_id = abo2.id  ";
    String qInner4 = " INNER JOIN USER_INFO as fait ON POST.user_info_id = fait.id  ";
    String qWhere = " WHERE abo2.id=:idUser ORDER BY POST.id desc  ";

    @Query(value=qSelect + qFromInner1 + qInner2 + qInner3 + qInner4 + qWhere, nativeQuery=true)
    public Set<PostDto> findPostList(@Param("idUser") Long idUser);

    @SuppressWarnings("null")
    public Optional<Post> findById(@NonNull Long id);

    @Modifying
    @Transactional
    @Query(value="INSERT INTO COMMENT(post_id, content, user_info_id, created_at) VALUES(?1,?2,?3,?4)", nativeQuery=true)
    public void addComment(Long idPost, String content, Long idUser, Date creatDate);

    @Modifying
    @Transactional
    @Query(value="INSERT INTO POST( title, content, topic_id, user_info_id, created_at ) VALUES(?1,?2,?3,?4, ?5)", nativeQuery=true)
    public void addPost(String Title, String content, Long idTopic, Long idUser, Date creatDate);

}
