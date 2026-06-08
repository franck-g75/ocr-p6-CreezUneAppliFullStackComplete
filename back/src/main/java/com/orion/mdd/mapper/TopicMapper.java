package com.orion.mdd.mapper;

import org.mapstruct.Mapper;

//import org.springframework.stereotype.Component;

import com.orion.mdd.dto.TopicDto;
import com.orion.mdd.models.Topic;


//@Mapper(componentModel = "spring")
public interface TopicMapper {

    TopicDto convertToTopicDto(Topic topic);

}
/*
@Mapper
public interface TopicMapper {

    TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);

    @Mapping(source = "id", target= "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    TopicDto convertToTopicDto(Topic t);

}*/