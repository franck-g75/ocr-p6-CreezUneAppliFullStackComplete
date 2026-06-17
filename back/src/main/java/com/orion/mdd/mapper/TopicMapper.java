package com.orion.mdd.mapper;

import org.mapstruct.Mapper;

import com.orion.mdd.dto.TopicDto;
import com.orion.mdd.models.Topic;


//@Mapper(componentModel = "spring")
public interface TopicMapper {

    TopicDto convertToTopicDto(Topic topic);

}
