package com.orion.mdd.repository;

import com.orion.mdd.models.UserInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional()
public interface UserInfoRepository  extends JpaRepository<UserInfo, Long> {

    //List<Users> findByTopics(Long id);
    UserInfo[] findByEmail(String email);
    UserInfo[] findByUsername(String username);
    Optional<UserInfo> findById(Long id);
    
    <U extends UserInfo> U save(U entity);

    @Modifying
    @Transactional
    @Query(value="INSERT INTO TOPIC_USER (user_id,topic_id) VALUES (?1, ?2)", nativeQuery=true)
    public void addUserTopic(Long idUser, Long idTopic);

    @Modifying
    @Transactional
    @Query(value="DELETE FROM TOPIC_USER WHERE user_id=?1 AND topic_id=?2", nativeQuery=true)
    public void delUserTopic(Long idUser, Long idTopic);

}
