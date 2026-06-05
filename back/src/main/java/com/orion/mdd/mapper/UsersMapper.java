package com.orion.mdd.mapper;

import org.mapstruct.Mapper;

import com.orion.mdd.dto.UsersDto;
import com.orion.mdd.models.Users;

@Mapper(componentModel = "spring")
public interface UsersMapper {

    UsersDto convertToUsersDto(Users users);

}
