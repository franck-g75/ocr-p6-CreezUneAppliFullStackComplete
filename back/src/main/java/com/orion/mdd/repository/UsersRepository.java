package com.orion.mdd.repository;

import com.orion.mdd.models.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
//import java.util.List;
import java.util.List;
import java.util.Optional;



@Repository
public interface UsersRepository  extends JpaRepository<Users, Long> {

    //List<Users> findByTopics(Long id);
    Users findByEmail(String email);
    Users findByUsername(String username);
    Optional<Users> findById(Long id);
}
