package com.orion.mdd.repository;

import com.orion.mdd.models.Topic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * interface used to manage data access for topic table
 * TopicRepository
 */
@Repository
public interface TopicRepository  extends JpaRepository<Topic, Long> {

}
