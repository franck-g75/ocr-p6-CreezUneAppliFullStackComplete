package com.orion.mdd.mapper;

import org.mapstruct.Mapper;

import com.orion.mdd.dto.UserInfoDto;
import com.orion.mdd.models.UserInfo;

@Mapper(componentModel = "spring")
public interface UserInfoMapper {

    UserInfoDto toDto(UserInfo users);

}
