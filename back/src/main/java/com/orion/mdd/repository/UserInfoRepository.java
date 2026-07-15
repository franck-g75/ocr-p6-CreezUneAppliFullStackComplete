package com.orion.mdd.repository;

import com.orion.mdd.models.UserInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * interface UserInfoRepository used to read create delete data in the data base
 */
@Transactional()
public interface UserInfoRepository  extends JpaRepository<UserInfo, Long> {

    /**
     * find a user by email
     * @param email the email to search
     * @return a Optional<UserInfo>
     */
    @NonNull Optional<UserInfo> findByEmail(String email);

    /**
     * find a user by username
     * @param username the username to search
     * @return a Optional<UserInfo>
     */
    @NonNull Optional<UserInfo> findByUsername(String username);

    /**
     * find a user by id
     * @param id the id to search
     * @return a Optional<UserInfo>
     */
    @NonNull Optional<UserInfo> findById(@NonNull Long id);
    
    /**
     * save a UserInfo in DB
     */
    <U extends UserInfo> U save(@NonNull U entity);

    /**
     * add a user-topic relationShip in DB
     * @param idUser the user id to add
     * @param idTopic the topic id to add
     */
    @Modifying
    @Transactional
    @Query(value="INSERT INTO TOPIC_USER (user_info_id,topic_id) VALUES (?1, ?2)", nativeQuery=true)
    public void addUserTopic(Long idUser, Long idTopic);

    /**
     * delete a user-topic relationShip in DB
     * @param idUser the user id to delete
     * @param idTopic the topic id to delete
     */
    @Modifying
    @Transactional
    @Query(value="DELETE FROM TOPIC_USER WHERE user_info_id=?1 AND topic_id=?2", nativeQuery=true)
    public void delUserTopic(Long idUser, Long idTopic);

}
