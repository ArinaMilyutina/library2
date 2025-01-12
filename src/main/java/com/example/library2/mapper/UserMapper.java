package com.example.library2.mapper;

import com.example.library2.dto.RequestAuthUser;
import com.example.library2.dto.RequestRegUser;
import com.example.library2.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User regUserDtoToUser(RequestRegUser regUserDto);

    User loginUserDtoToUser(RequestAuthUser authUserDto);
}
