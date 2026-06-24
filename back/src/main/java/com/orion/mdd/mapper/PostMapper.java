package com.orion.mdd.mapper;

import org.mapstruct.Mapper;

import com.orion.mdd.dto.PostDto;
import com.orion.mdd.models.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {

    //PostDto convertToPostDto(Post post);
    
}
